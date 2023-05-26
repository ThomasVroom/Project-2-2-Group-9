import numpy as np
import cv2
import dlib
import tensorflow as tf
import tensorflow.compat.v1 as tf
import mediapipe as mp


class OpenCVHaarFaceDetector():
    def __init__(self,
                 scaleFactor=1.3,
                 minNeighbors=5,
                 model_path='src/main/java/org/Project22/FaceDetection/models/haarcascade_frontalface_default.xml'):
        """
        Initializes the OpenCV Haar Cascade face detector.

        Args:
            scale_factor (float): Parameter specifying how much the image size is reduced at each image scale.
            min_neighbors (int): Parameter specifying how many neighbors each candidate rectangle should have to retain it.
            model_path (str): Path to the Haar Cascade XML model file.
        """

        self.cascade_classifier = cv2.CascadeClassifier(model_path)
        self.scaleFactor = scaleFactor
        self.minNeighbors = minNeighbors

    def detect_face(self, image):
        """
        Detects faces in the given image using the OpenCV Haar Cascade face detector.

        Args:
            image (numpy.ndarray): Input image to detect faces in.

        Returns:
            numpy.ndarray: Array of detected face bounding boxes in the format [x1, y1, x2, y2].
        """
        # Requires gray scale image to perform classification.
        gray_scaled_image = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        detected_faces = self.cascade_classifier.detectMultiScale(gray_scaled_image, self.scaleFactor,
                                                   self.minNeighbors)

        detected_faces = [[x, y, x + w, y + h] for x, y, w, h in detected_faces]

        return np.array(detected_faces)


class DlibCNNFaceDetector():
    
    def __init__(self,
                 nrof_upsample=0,
                 model_path='src/main/java/org/Project22/FaceDetection/models/mmod_human_face_detector.dat'):
        """
        Initializes the Dlib CNN face detector.

        Args:
            num_upsamples (int): Number of upsampling times for the input image.
            model_path (str): Path to the Dlib CNN face detection model file.
        """

        self.cnn_detector = dlib.cnn_face_detection_model_v1(model_path)
        self.nrof_upsample = nrof_upsample

    def detect_face(self, image):
        """
        Detects faces in the given image using the Dlib CNN face detector.

        Args:
            image (numpy.ndarray): Input image to detect faces in.

        Returns:
            numpy.ndarray: Array of detected face bounding boxes in the format [x1, y1, x2, y2].
        """

        detected_faces = self.cnn_detector(image, self.nrof_upsample)

        faces = []
        for i, d in enumerate(detected_faces):
            x1 = int(d.rect.left())
            y1 = int(d.rect.top())
            x2 = int(d.rect.right())
            y2 = int(d.rect.bottom())
            confidence = float(d.confidence)

            faces.append(np.array([x1, y1, x2, y2]))

        return np.array(faces)
    
class BlazeDetector():
    def __init__(self, min_detection_confidence=0.3):
        """
        Initializes the BlazeFace detector.

        Args:
            min_detection_confidence (float): Minimum confidence threshold for detected faces.
        """

        self.min_detection_confidence = min_detection_confidence
        self.face_detection_mp = mp.solutions.face_detection
        self.detector_mp = self.face_detection_mp.FaceDetection(self.min_detection_confidence)
        
    def detect_face(self, image):
        """
        Detects faces in the given image using the BlazeFace detector.

        Args:
            image (numpy.ndarray): Input image to detect faces in.

        Returns:
            numpy.ndarray: Array of detected face bounding boxes in the format [x1, y1, x2, y2].
        """
        results = self.detector_mp.process(image)
        width = image.shape[1]
        height = image.shape[0]
        faces = []

        if results.detections != None:

            for face in results.detections:

                if face.score[0] > 0.80:

                    rectangle = face.location_data.relative_bounding_box

                    x = int(rectangle.xmin * width)
                    w = int(rectangle.width * width)

                    y = int(rectangle.ymin * height)
                    h = int(rectangle.height * height)

                    faces.append(np.array([x, y, x + w, y + h]))

        return np.array(faces)

class TensorFlowMobileNetSSDFaceDetector:

    def __init__(self, det_threshold=0.3, model_path='src/main/java/org/Project22/FaceDetection/models/ssd/frozen_inference_graph_face.pb'):
        self.det_threshold = det_threshold
        self.detection_graph = tf.Graph()
        with self.detection_graph.as_default():
            od_graph_def = tf.compat.v1.GraphDef()
            with tf.gfile.GFile(model_path, 'rb') as fid:
                serialized_graph = fid.read()
                od_graph_def.ParseFromString(serialized_graph)
                tf.import_graph_def(od_graph_def, name='')

        with self.detection_graph.as_default():
            config = tf.compat.v1.ConfigProto()
            config.gpu_options.allow_growth = True
            self.sess = tf.compat.v1.Session(graph=self.detection_graph, config=config)

    def detect_face(self, image):
        h, w, c = image.shape

        image_np = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        image_np_expanded = np.expand_dims(image_np, axis=0)
        image_tensor = self.detection_graph.get_tensor_by_name(
            'image_tensor:0')

        boxes = self.detection_graph.get_tensor_by_name('detection_boxes:0')

        scores = self.detection_graph.get_tensor_by_name('detection_scores:0')
        classes = self.detection_graph.get_tensor_by_name(
            'detection_classes:0')
        num_detections = self.detection_graph.get_tensor_by_name(
            'num_detections:0')

        (boxes, scores, classes, num_detections) = self.sess.run(
            [boxes, scores, classes, num_detections],
            feed_dict={image_tensor: image_np_expanded})

        boxes = np.squeeze(boxes)
        scores = np.squeeze(scores)

        filtered_score_index = np.argwhere(
            scores >= self.det_threshold).flatten()
        selected_boxes = boxes[filtered_score_index]

        faces = np.array([[
            int(x1 * w),
            int(y1 * h),
            int(x2 * w),
            int(y2 * h),
        ] for y1, x1, y2, x2 in selected_boxes])

        return faces
