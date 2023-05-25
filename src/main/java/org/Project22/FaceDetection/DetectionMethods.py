import numpy as np
import cv2
import dlib
import tensorflow as tf
import mediapipe as mp


class OpenCVHaarFaceDetector():
    def __init__(self,
                 scaleFactor=1.3,
                 minNeighbors=5,
                 model_path='models/haarcascade_frontalface_default.xml'):

        self.face_cascade = cv2.CascadeClassifier(model_path)
        self.scaleFactor = scaleFactor
        self.minNeighbors = minNeighbors

    def detect_face(self, image):
        gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)

        faces = self.face_cascade.detectMultiScale(gray, self.scaleFactor,
                                                   self.minNeighbors)

        faces = [[x, y, x + w, y + h] for x, y, w, h in faces]

        return np.array(faces)


class DlibCNNFaceDetector():
    def __init__(self,
                 nrof_upsample=0,
                 model_path='models/mmod_human_face_detector.dat'):

        self.cnn_detector = dlib.cnn_face_detection_model_v1(model_path)
        self.nrof_upsample = nrof_upsample

    def detect_face(self, image):

        dets = self.cnn_detector(image, self.nrof_upsample)

        faces = []
        for i, d in enumerate(dets):
            x1 = int(d.rect.left())
            y1 = int(d.rect.top())
            x2 = int(d.rect.right())
            y2 = int(d.rect.bottom())
            score = float(d.confidence)

            faces.append(np.array([x1, y1, x2, y2]))

        return np.array(faces)
    
class BlazeDetector():
    def __init__(self, min_detection_confidence=0.3):

        self.min_detection_confidence = min_detection_confidence
        self.face_detection_mp = mp.solutions.face_detection
        self.detector_mp = self.face_detection_mp.FaceDetection(self.min_detection_confidence)
        
    def detect_face(self, image):
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