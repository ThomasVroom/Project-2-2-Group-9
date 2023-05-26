import cv2
import numpy as np
import time
from tqdm import tqdm
import dlib
import mediapipe as mp

from DetectionMethods import OpenCVHaarFaceDetector, BlazeDetector, DlibCNNFaceDetector, TensorFlowMobileNetSSDFaceDetector


def extract_and_filter_data(annotation_file):
    # Extract bounding box ground truth from dataset annotations and maintain all information in one dictionary
    bb_gt_collection = dict()

    with open(annotation_file, 'r') as f:
        lines = f.readlines()

    for line in lines:
        line = line.strip()
        if line.endswith('.jpg'):
            image_path = line
            bb_gt_collection[image_path] = []
        else:
            line_components = line.split(' ')
            if len(line_components) > 1:
                # Discard annotation with invalid image information
                if int(line_components[7]) != 1:
                    x1 = int(line_components[0])
                    y1 = int(line_components[1])
                    w = int(line_components[2])
                    h = int(line_components[3])

                    # Filter faces with width or height less than 15 pixels
                    if w > 15 and h > 15:
                        bb_gt_collection[image_path].append([x1, y1, x1 + w, y1 + h])

    return bb_gt_collection

# print(extract_and_filter_data(annotation_file=annotation_file))

def evaluate(face_detector, bb_gt_collection, iou_threshold):

    total_images = len(bb_gt_collection.keys())
    total_iou_sum = 0
    total_precision_sum = 0
    total_inference_time = 0

    for i, image_path in tqdm(enumerate(bb_gt_collection), total=total_images):

        image_data = cv2.imread("src/main/java/org/Project22/FaceDetection/WIDER_val/images/{}".format(image_path))

        # if image_data is None:
        #     print(f"Error reading image: {image_path}")
        #     continue
        # print(f"Processing image: {image_path}")
    
        ground_truth_bboxes = np.array(bb_gt_collection[image_path])
        total_gt_faces = len(ground_truth_bboxes)

        start_time = time.time()
        predicted_bboxes = face_detector.detect_face(image_data)
        inference_time = time.time() - start_time
        total_inference_time += inference_time

        total_iou = 0
        true_positives = 0
        prediction_dict = dict()
        for gt_bbox in ground_truth_bboxes:
            max_iou_per_gt = 0
            cv2.rectangle(image_data, (gt_bbox[0], gt_bbox[1]), (gt_bbox[2], gt_bbox[3]), (255, 0, 0), 2)
            for i, pred_bbox in enumerate(predicted_bboxes):
                if i not in prediction_dict.keys():
                    prediction_dict[i] = 0
                cv2.rectangle(image_data, (pred_bbox[0], pred_bbox[1]), (pred_bbox[2], pred_bbox[3]), (0, 0, 255), 2)
                iou = calculate_iou(gt_bbox, pred_bbox)
                if iou > max_iou_per_gt:
                    max_iou_per_gt = iou
                if iou > prediction_dict[i]:
                    prediction_dict[i] = iou
            total_iou += max_iou_per_gt

        if total_gt_faces != 0:
            if len(prediction_dict.keys()) > 0:
                for i in prediction_dict:
                    if prediction_dict[i] >= iou_threshold:
                        true_positives += 1
                precision = float(true_positives) / float(total_gt_faces)
            else:
                precision = 0

            image_average_iou = total_iou / total_gt_faces
            image_average_precision = precision

            total_iou_sum += image_average_iou
            total_precision_sum += image_average_precision

    evaluation_results = dict()
    evaluation_results['average_iou'] = float(total_iou_sum) / float(total_images)
    evaluation_results['mean_average_precision'] = float(total_precision_sum) / float(total_images)
    evaluation_results['average_inference_time'] = float(total_inference_time) / float(total_images)

    return evaluation_results

def calculate_iou(boxA, boxB):
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
        IoU value in the range [0, 1]
    """

    bb1 = {
        'xmin': boxA[0],
        'ymin': boxA[1],
        'xmax': boxA[2],
        'ymax': boxA[3]
    }

    bb2 = {
        'xmin': boxB[0],
        'ymin': boxB[1],
        'xmax': boxB[2],
        'ymax': boxB[3]
    }

    # Determine the coordinates of the intersection rectangle
    x_left = max(bb1['xmin'], bb2['xmin'])
    y_top = max(bb1['ymin'], bb2['ymin'])
    x_right = min(bb1['xmax'], bb2['xmax'])
    y_bottom = min(bb1['ymax'], bb2['ymax'])

    if x_right < x_left or y_bottom < y_top:
        return 0.0

    # Compute the intersection area
    intersection_area = (x_right - x_left) * (y_bottom - y_top)

    # Compute the areas of both bounding boxes
    bb1_area = (bb1['xmax'] - bb1['xmin']) * (bb1['ymax'] - bb1['ymin'])
    bb2_area = (bb2['xmax'] - bb2['xmin']) * (bb2['ymax'] - bb2['ymin'])

    # Compute the intersection over union
    iou = intersection_area / float(bb1_area + bb2_area - intersection_area)

    assert 0.0 <= iou <= 1.0

    return iou

annotation_file = "src/main/java/org/Project22/FaceDetection/dataset/wider_face_split/wider_face_val_bbx_gt.txt"

face_detector = TensorFlowMobileNetSSDFaceDetector()
iou_threshold = 0.7
bb_gt_collection = extract_and_filter_data(annotation_file=annotation_file)
evaluation_results = evaluate(face_detector, bb_gt_collection, iou_threshold)
print(evaluation_results)

