import cv2
import numpy as np
import time
import os
from tqdm import tqdm
import time
import cv2
import dlib
import mediapipe as mp
import argparse

from DetectionMethods import OpenCVHaarFaceDetector, BlazeDetector, DlibCNNFaceDetector

# Instantiate face detection models
haar_detector = OpenCVHaarFaceDetector()
dlib_detector = DlibCNNFaceDetector()
blaze_detector = BlazeDetector()

# Initialize counts for true positives, false positives, and false negatives
tp = 0
fp = 0
fn = 0

def get_iou(boxA, boxB):
    """
    Calculate the Intersection over Union (IoU) of two bounding boxes.

    Parameters
    ----------
    boxA : np.array([xmin, ymin, xmax, ymax])
        First bounding box coordinates.
    boxB : np.array([xmin, ymin, xmax, ymax])
        Second bounding box coordinates.

    Returns
    -------
    float
        Intersection over Union (IoU) between the two bounding boxes in the range [0, 1].
    """
    x_left = max(boxA[0], boxB[0])
    y_top = max(boxA[1], boxB[1])
    x_right = min(boxA[2], boxB[2])
    y_bottom = min(boxA[3], boxB[3])

    if x_right < x_left or y_bottom < y_top:
        return 0.0

    intersection_area = (x_right - x_left) * (y_bottom - y_top)
    bb1_area = (boxA[2] - boxA[0]) * (boxA[3] - boxA[1])
    bb2_area = (boxB[2] - boxB[0]) * (boxB[3] - boxB[1])

    iou = intersection_area / float(bb1_area + bb2_area - intersection_area)
    iou = max(0.0, min(1.0, iou))

    return iou

iou_threshold = 0.7

import os
import numpy as np

# Function to parse the WIDER dataset annotation file
def parse_wider_annotation_file(anno_file_path):
    with open(anno_file_path, 'r') as file:
        lines = file.readlines()

    annotations = []
    image_path = None
    num_faces = None
    face_counter = 0
    bbox = []

    for line in lines:
        line = line.strip()

        if 'jpg' in line or 'png' in line:
            if image_path is not None:
                annotations.append((image_path, num_faces, bbox))
                bbox = []
            image_path = line
            num_faces = None
            face_counter = 0
        elif num_faces is None:
            num_faces = int(line)
        else:
            face_counter += 1
            if face_counter <= num_faces:
                face_data = line.split()
                bbox.append([int(face_data[0]), int(face_data[1]), int(face_data[2]), int(face_data[3])])

    annotations.append((image_path, num_faces, bbox))

    return annotations


# Path to the WIDER dataset annotation file
wider_annotation_file = 'src/main/java/org/Project22/FaceDetection/Experiments/dataset/wider_face_split/wider_face_train_bbx_gt.txt'

# Parse the WIDER dataset annotation file
wider_annotations = parse_wider_annotation_file(wider_annotation_file)

# Iterate over images in the dataset
for image_path, num_faces, ground_truth_bboxes in wider_annotations:
    # Load the image
    image = cv2.imread(image_path)
    
    # Detect faces using OpenCVHaarFaceDetector
    opencv_faces = haar_detector.detect_face(image)
    
    # Calculate IoU and update TP, FP, FN counts
    for opencv_face in opencv_faces:
        for ground_truth_bbox in ground_truth_bboxes:
            iou = get_iou(opencv_face, ground_truth_bbox)
            if iou >= iou_threshold:
                tp += 1
                break  # Break the inner loop once a match is found
        else:
            fp += 1
    
    # Detect faces using DlibCNNFaceDetector
    dlib_faces = dlib_detector.detect_face(image)
    
    # Calculate IoU and update TP, FP, FN counts
    for dlib_face in dlib_faces:
        for ground_truth_bbox in ground_truth_bboxes:
            iou = get_iou(dlib_face, ground_truth_bbox)
            if iou >= iou_threshold:
                tp += 1
                break  # Break the inner loop once a match is found
        else:
            fp += 1
    
    # Detect faces using BlazeDetector
    blaze_faces = blaze_detector.detect_face(image)
    
    # Calculate IoU and update TP, FP, FN counts
    for blaze_face in blaze_faces:
        for ground_truth_bbox in ground_truth_bboxes:
            iou = get_iou(blaze_face, ground_truth_bbox)
            if iou >= iou_threshold:
                tp += 1
                break  # Break the inner loop once a match is found
        else:
            fp += 1
    
    # Calculate false negatives
    fn += num_faces - tp

# Calculate accuracy metrics
precision = tp / (tp + fp)
recall = tp / (tp + fn)
f1_score = 2 * (precision * recall) / (precision + recall)
