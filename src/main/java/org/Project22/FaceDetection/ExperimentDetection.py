import cv2
from DetectionMethods import OpenCVHaarFaceDetector, BlazeDetector, DlibCNNFaceDetector, TensorFlowMobileNetSSDFaceDetector
from tensorflow.python.ops.numpy_ops import np_config
import time
np_config.enable_numpy_behavior()

def detect_and_display_faces(webcam, face_detector):
    detected = False
    start_time = time.time()
    
    while True:
        ret, frame = webcam.read()
        faces = face_detector.detect_face(frame)

        if len(faces) > 0:
            detected = True
            break
    
    elapsed_time = (time.time() - start_time) * 1000
    
    return detected, elapsed_time

def test_detection_performance(webcam, face_detector, method_name, num_iterations):
    with open("src/main/java/org/Project22/FaceDetection/experiment_tf.txt", "a") as f:
        for _ in range(num_iterations):
            detected, elapsed_time = detect_and_display_faces(webcam, face_detector)
            f.write(f"method: {method_name}, Elapsed time: {elapsed_time:.2f} milliseconds\n")
            f.flush()  # Flush the buffer to ensure immediate write to the file

# Initialize webcam
webcam = cv2.VideoCapture(0)

# Create instance of BlazeDetector
face_detector = TensorFlowMobileNetSSDFaceDetector()

# Test detection performance for 4 iterations
test_detection_performance(webcam, face_detector, "TensorFlowMobileNetSSDFaceDetector", 100)

# Close the webcam
webcam.release()
