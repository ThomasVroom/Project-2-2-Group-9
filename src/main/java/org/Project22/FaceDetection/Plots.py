import matplotlib.pyplot as plt

output_data_on_dataset = {'OpenCV': {'average_iou': 0.21356696150864374, 'mean_average_precision': 0.16094956345924968, 'average_inference_time': 49.498165828872745},
        'BlazePose': {'average_iou': 0.062434269915389064, 'mean_average_precision': 0.04580270421869926, 'average_inference_time': 2.759250438664171},
        'DlibCNNFaceDetector': {'average_iou': 0.2827014003360415, 'mean_average_precision': 0.14485922089945655, 'average_inference_time': 1060.5117386346712}}
# 'TensorFlowMobileNetSSDFaceDetector': {'average_iou': 0.5840790930792693, 'mean_average_precision': 0.52721169854748, 'average_inference_time': 214.1369269342062}

output_data_running_time = {
    'OpenCV': {33.788},
    'BlazePose': {32.8557},
    'DlibCNN': {752.6145},
}
    # 'TensorFlowMobileNetSSD': {156.8958}
output_data_detected_1 = {
    'OpenCV': {46},
    'BlazePose': {55},
    'DlibCNN': {98},
    # 'TensorFlowMobileNetSSD': {99}
}
output_data_detected_2 = {
    'OpenCV': {100},
    'BlazePose': {100},
    'DlibCNN': {100},
    # 'TensorFlowMobileNetSSD': {100}
}

def plot_iou_vs_time_complexity(data):
    models = list(data.keys())
    avg_iou = [data[model]['average_iou'] for model in models]
    inference_time = [data[model]['average_inference_time'] for model in models]

    plt.figure(figsize=(8, 6))
    plt.scatter(inference_time, avg_iou)
    plt.xlabel('Average Time Elapsed to Detect Faces on a Single Image in Milliseconds (Inference Time)', fontsize=14)
    plt.ylabel('Average Intersection over Union (IOU)', fontsize=14)
    plt.title('Intersection over Union (IoU) Ratio with Respect to Time Complexity', fontsize=16)

    for i, model in enumerate(models):
        plt.annotate(model, (inference_time[i], avg_iou[i]), textcoords="offset points", xytext=(5, 5), ha='center')

    plt.show()


def plot_map_vs_time_complexity(data):
    models = list(data.keys())
    mAP = [data[model]['mean_average_precision'] for model in models]
    inference_time = [data[model]['average_inference_time'] for model in models]

    plt.figure(figsize=(8, 6))
    plt.scatter(inference_time, mAP)
    plt.xlabel('Average Time Elapsed to Detect Faces on a Single Image in Milliseconds (Inference Time)', fontsize=14)
    plt.ylabel('Mean Average Precision', fontsize=14)
    plt.title('Mean Average Precision with Respect to Time Complexity', fontsize=16)

    for i, model in enumerate(models):
        plt.annotate(model, (inference_time[i], mAP[i]), textcoords="offset points", xytext=(5, 5), ha='center')

    plt.show()

def plot_running_time(output_data_running_time):
    methods = list(output_data_running_time.keys())
    running_times = [list(values)[0] for values in output_data_running_time.values()]

    plt.bar(methods, running_times, width=0.5)
    plt.xlabel('Methods', fontweight='bold', fontsize=14)  
    plt.ylabel('Running Time (milliseconds)', fontweight='bold', fontsize=14)  
    plt.title('Average Elapsed Time to Detect Face', fontweight='bold', fontsize=16)
    plt.xticks(fontsize=12)
    plt.yticks(fontsize=12)
    plt.show()

# Plotting the data
# plot_iou_vs_time_complexity(output_data_on_dataset)
# plot_map_vs_time_complexity(output_data_on_dataset)
# plot_running_time(output_data_running_time)

def plot_webcam_experiment(detected_not_detected):
    methods = list(output_data_running_time.keys())
    detected_face_percent = [list(values)[0] for values in output_data_detected_1.values()]

    plt.bar(methods, detected_face_percent, width=0.5)
    plt.xlabel('Methods', fontweight='bold', fontsize=14)  
    plt.ylabel('Percentage of Detected Faces', fontweight='bold', fontsize=14)  
    plt.title('Detected Face Percentage over 100 Iteration (with restrictions)', fontweight='bold', fontsize=14)
    plt.xticks(fontsize=12)  
    plt.yticks(fontsize=12) 
    plt.show()

# plot_running_time(output_data_running_time)
plot_webcam_experiment(output_data_detected_2)

