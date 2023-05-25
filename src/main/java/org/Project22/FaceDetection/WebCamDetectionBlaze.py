import math
import time
import cv2
import numpy as np
import mediapipe as mp
from mediapipe.tasks import python
from mediapipe.tasks.python import vision
from typing import Tuple, Union

face_detection_mp = mp.solutions.face_detection
detector_mp = face_detection_mp.FaceDetection(min_detection_confidence = 0.3)

start_time = time.time()

capture = cv2.VideoCapture(0)

def detectFaceandKeyPoints():

    detected = False

    while time.time() - start_time <= 10:

        ret, frame = capture.read()

        results = detector_mp.process(frame)

        width = frame.shape[1]
        height = frame.shape[0]

        if results.detections != None:

            for face in results.detections:
                if face.score[0] > 0.80:

                    detected = True

                    rectangle = face.location_data.relative_bounding_box

                    x = int(rectangle.xmin * width)
                    w = int(rectangle.width * width)

                    y = int(rectangle.ymin * height)
                    h = int(rectangle.height * height)

                    cv2.rectangle(frame, (x,y), (x+w, y+h), color=(255,255,255), thickness=2)

            cv2.imshow('frame', frame)

            # terminates the frame if user clicks "a"
            if cv2.waitKey(1) == ord('a'):
                break

    capture.release()
    cv2.destroyAllWindows()

    if results.detections == None:
        detected = False

    return detected

def isDetected():
    if detectFaceandKeyPoints():
        print("A human was detected")
    else:
        print("No human was detected around.")


isDetected()

