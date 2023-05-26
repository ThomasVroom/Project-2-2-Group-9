import cv2
from DetectionMethods import OpenCVHaarFaceDetector, BlazeDetector, DlibCNNFaceDetector, TensorFlowMobileNetSSDFaceDetector
from tensorflow.python.ops.numpy_ops import np_config
import time
np_config.enable_numpy_behavior()

def detect_and_display_faces(webcam, face_detector):

    detected = False
    
    while time.time() - start_time <= 5:
               
        # ret indicates whether the capture is done successfully.
        # frame is the numpy array that represents the image.
        ret, frame = webcam.read()

        # Detect faces using the face detector
        faces = face_detector.detect_face(frame)

        # Draw rectangles around the detected faces
        for (x1, y1, x2, y2) in faces:
            cv2.rectangle(frame, (x1, y1), (x2, y2), (50, 200, 0), 4)

        # Display the frame with detected faces
        cv2.imshow('Face Detection', frame)

        # Exit loop when 'q' is pressed
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    # Release the webcam and close the OpenCV windows
    webcam.release()
    cv2.destroyAllWindows()

    if len(faces) > 0:
            detected = True
    
    return detected

# Initialize webcam
webcam = cv2.VideoCapture(0)

# Create instance of OpenCVHaarFaceDetector
face_detector = BlazeDetector()

start_time = time.time()

def isDetected():
    if detect_and_display_faces(webcam, face_detector):
        print("A human was detected")
    else:
        print("No human was detected around.")

isDetected()
