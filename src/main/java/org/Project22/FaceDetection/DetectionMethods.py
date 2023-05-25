import numpy as np
import cv2
import dlib
import tensorflow as tf
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
    
class FaceDetector():
    def __init__(self,
                 threshold=0.3,
                 model_path='models/ssd/frozen_inference_graph_face.pb'):
        """
        Initializes the TensorFlow MobileNet SSD face detector.

        Args:
            threshold (float): Minimum confidence threshold for detected faces.
            model_path (str): Path to the frozen inference graph of the MobileNet SSD face detection model.
        """

        self.threshold = threshold
        self.graph = tf.Graph()
        with self.graph.as_default():
            graph_def = tf.GraphDef()
            with tf.gfile.GFile(model_path, 'rb') as file:
                serialized_graph = file.read()
                graph_def.ParseFromString(serialized_graph)
                tf.import_graph_def(graph_def, name='')

        with self.graph.as_default():
            config = tf.ConfigProto()
            config.gpu_options.allow_growth = True
            self.session = tf.Session(graph=self.graph, config=config)

    def detect_faces(self, image):
        """
        Detects faces in the given image using the TensorFlow MobileNet SSD face detector.

        Args:
            image (numpy.ndarray): Input image to detect faces in.

        Returns:
            numpy.ndarray: Array of detected face bounding boxes in the format [x1, y1, x2, y2].
        """

        height, width, channels = image.shape

        image_rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        image_expanded = np.expand_dims(image_rgb, axis=0)
        image_tensor = self.graph.get_tensor_by_name('image_tensor:0')

        detection_boxes = self.graph.get_tensor_by_name('detection_boxes:0')

        detection_scores = self.graph.get_tensor_by_name('detection_scores:0')
        detection_classes = self.graph.get_tensor_by_name('detection_classes:0')
        num_detections = self.graph.get_tensor_by_name('num_detections:0')

        (detection_boxes, detection_scores, detection_classes, num_detections) = self.session.run(
            [detection_boxes, detection_scores, detection_classes, num_detections],
            feed_dict={image_tensor: image_expanded})

        detection_boxes = np.squeeze(detection_boxes)
        detection_scores = np.squeeze(detection_scores)

        filtered_score_index = np.argwhere(detection_scores >= self.threshold).flatten()
        selected_boxes = detection_boxes[filtered_score_index]

        detected_faces = np.array([[
            int(x1 * width),
            int(y1 * height),
            int(x2 * width),
            int(y2 * height),
        ] for y1, x1, y2, x2 in selected_boxes])

        return detected_faces
