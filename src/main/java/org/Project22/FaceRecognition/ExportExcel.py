import pandas as pd
import os
import cv2
from tensorflow.python.ops.numpy_ops import np_config
import time
np_config.enable_numpy_behavior()

def export_to_excel(txt_files, excel_file_path):
    # Create an Excel writer in append mode
    writer = pd.ExcelWriter(excel_file_path, engine='openpyxl', mode='a')
    wb = writer.book

    # Iterate over the text files
    for txt_file in txt_files:
        # Extract the filename from the path
        file_name = os.path.splitext(os.path.basename(txt_file))[0]

        # Determine the data type based on the file name
        if 'confidences' in txt_file:
            data_type = 'percentage'
        elif 'elapsed_time' in txt_file:
            data_type = 'time'
        else:
            # Skip the file if it doesn't match the expected formats
            continue

        # Read the text file and extract the data
        with open(txt_file, 'r') as f:
            lines = f.readlines()

        data = []
        for line in lines:
            line = line.strip()
            if data_type == 'percentage':
                try:
                    percentage = float(line.replace('%', ''))
                    data.append({'percentage': percentage})
                except ValueError:
                    # Skip the line if it's not a valid percentage
                    continue
            elif data_type == 'time':
                try:
                    elapsed_time = float(line)
                    data.append({'elapsed_time': elapsed_time})
                except ValueError:
                    # Skip the line if it's not a valid elapsed time
                    continue

        # Create a DataFrame from the extracted data
        df = pd.DataFrame(data)

        # Write the DataFrame to a new sheet in the Excel file with the file name as the sheet name
        df.to_excel(writer, sheet_name=file_name, index=False)

    # Save and close the Excel writer
    writer.save()
    writer.close()


txt_files = ['src/main/java/org/Project22/FaceRecognition/experiments/cnn_confidences.txt', 
             'src/main/java/org/Project22/FaceRecognition/experiments/cnn_elapsed_time.txt',
             'src/main/java/org/Project22/FaceRecognition/experiments/hog_confidences.txt',
             'src/main/java/org/Project22/FaceRecognition/experiments/hog_elapsed_times.txt'
             ]

excel_file_path = 'src/main/java/org/Project22/FaceRecognition/experiments/FaceRecognitionExperiments.xlsx'

export_to_excel(txt_files, excel_file_path)