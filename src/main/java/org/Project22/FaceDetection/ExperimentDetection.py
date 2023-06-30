import pandas as pd
import os
import cv2
from DetectionMethods import OpenCVHaarFaceDetector, BlazeDetector, DlibCNNFaceDetector, TensorFlowMobileNetSSDFaceDetector, DlibHOGFaceDetector
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

def test_detection_rtime(webcam, face_detector, method_name, num_iterations):
    with open("src/main/java/org/Project22/FaceDetection/experiment_hog.txt", "a") as f:
        for _ in range(num_iterations):
            detected, elapsed_time = detect_and_display_faces(webcam, face_detector)
            f.write(f"method: {method_name}, Elapsed time: {elapsed_time:.2f} milliseconds\n")
            f.flush()  # Flush the buffer to ensure immediate write to the file

def convert_txt_to_excel(txt_files, excel_file):
    with pd.ExcelWriter(excel_file) as writer:
        for txt_file in txt_files:
            # Read the text file
            with open(txt_file, 'r') as file:
                lines = file.readlines()

            # Extract method and elapsed time data from each line
            methods = []
            elapsed_times = []
            for line in lines:
                method, elapsed_time = line.strip().split(', ')
                method = method.split(': ')[-1]
                elapsed_time = float(elapsed_time.split(': ')[-1].split(' ')[0])
                methods.append(method)
                elapsed_times.append(elapsed_time)

            # Create a DataFrame with the extracted data
            df = pd.DataFrame({'Method': methods, 'Elapsed time (milliseconds)': elapsed_times})

            # Extract the filename (without extension) as the sheet name
            sheet_name = txt_file.split('/')[-1]

            # Write the DataFrame to the Excel sheet
            df.to_excel(writer, sheet_name=sheet_name, index=False)

def detect_or_not(webcam, face_detector):
    detected = 0
    not_detected = 0
    start_time = time.time()

    while time.time() - start_time <= 5:
        ret, frame = webcam.read()
        faces = face_detector.detect_face(frame)

    if len(faces) > 0:
        detected += 1
    if len(faces) == 0:
        not_detected += 1

    return detected, not_detected

def test_detection_performance(webcam, face_detector, method_name, num_iterations):
    print('Started...')
    with open("src/main/java/org/Project22/FaceDetection/experiment_performance_hog.txt", "a") as f:
        for i in range(num_iterations):
            print(i, '. iteration...')
            detected, notdetected  = detect_or_not(webcam, face_detector)
            f.write(f"method: {method_name}, Detected: {detected} faces, Not Detected: {notdetected}\n")
            f.flush()  # Flush the buffer to ensure immediate write to the file
    print('Completed.')

def export_to_excel(txt_files, excel_file_path):
    # Create an Excel writer in append mode
    writer = pd.ExcelWriter(excel_file_path, engine='openpyxl', mode='a')
    wb = writer.book

    # Iterate over the text files
    for txt_file in txt_files:
        # Extract the filename from the path
        file_name = os.path.splitext(os.path.basename(txt_file))[0]

        # Read the text file and extract the data
        with open(txt_file, 'r') as f:
            lines = f.readlines()

        data = []
        for line in lines:
            # Split the line and handle any potential errors
            try:
                method = line.split(',')[0].split(':')[1].strip()
                detected = int(line.split(':')[2].split(' ')[1])
                not_detected = int(line.split(':')[3].split(' ')[1])
                data.append({'method': method, 'detected': detected, 'not_detected': not_detected})
            except (IndexError, ValueError):
                # Skip the line if there are any errors
                continue

        # Create a DataFrame from the extracted data
        df = pd.DataFrame(data)

        # Write the DataFrame to a new sheet in the Excel file with the file name as the sheet name
        df.to_excel(writer, sheet_name=file_name, index=False)

    # Save and close the Excel writer
    writer.close()

# # Initialize webcam
# webcam = cv2.VideoCapture(0)

# # Create instance of BlazeDetector
# face_detector = DlibHOGFaceDetector()

# # ## Test detection run time performance for 100 iterations
# # test_detection_rtime(webcam, face_detector, "DlibHOGFaceDetector", 100)

# # # Test detection performance for 100 iterations
# test_detection_performance(webcam, face_detector, "DlibHOGFaceDetector", 100)

# # Close the webcam
# webcam.release()

# Specify the list of text files and the output Excel file
txt_files = ['src/main/java/org/Project22/FaceDetection/experiment_performance_hog.txt']
excel_file = 'src/main/java/org/Project22/FaceDetection/hog_perf.xlsx'

convert_txt_to_excel(txt_files, excel_file)

# txt_files = ['src/main/java/org/Project22/FaceDetection/experiment_performance_opencv.txt', 
#              'src/main/java/org/Project22/FaceDetection/experiment_performance_blaze.txt', 
#              'src/main/java/org/Project22/FaceDetection/experiment_performance_dlib.txt',
#              'src/main/java/org/Project22/FaceDetection/experiment_performance_tf.txt']

# excel_file_path = 'src/main/java/org/Project22/FaceDetection/combined_experiment.xlsx'
# export_to_excel(txt_files, excel_file_path)



