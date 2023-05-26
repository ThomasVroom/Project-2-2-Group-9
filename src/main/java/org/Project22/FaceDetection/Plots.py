import matplotlib.pyplot as plt

output_data = {'OpenCV': {'average_iou': 0.21356696150864374, 'mean_average_precision': 0.16094956345924968, 'average_inference_time': 0.049498165828872745},
        'BlazePose': {'average_iou': 0.062434269915389064, 'mean_average_precision': 0.04580270421869926, 'average_inference_time': 0.002759250438664171},
        'DlibCNNFaceDetector': {'average_iou': 0.2827014003360415, 'mean_average_precision': 0.14485922089945655, 'average_inference_time': 1.0605117386346712},
        'TensorFlowMobileNetSSDFaceDetector': {'average_iou': 0.5840790930792693, 'mean_average_precision': 0.52721169854748, 'average_inference_time': 0.2141369269342062}}


def plot_iou_vs_time_complexity(data):
    models = list(data.keys())
    avg_iou = [data[model]['average_iou'] for model in models]
    inference_time = [data[model]['average_inference_time'] for model in models]

    plt.figure(figsize=(8, 6))
    plt.scatter(inference_time, avg_iou)
    plt.xlabel('Average Time Elapsed to Detect Faces on a Single Image in Seconds (Inference Time)', fontsize=12)
    plt.ylabel('Average Intersection over Union (IOU)', fontsize=12)
    plt.title('Intersection over Union (IoU) Ratio with Respect to Time Complexity', fontsize=14)

    for i, model in enumerate(models):
        plt.annotate(model, (inference_time[i], avg_iou[i]), textcoords="offset points", xytext=(5, 5), ha='center')

    plt.show()


def plot_map_vs_time_complexity(data):
    models = list(data.keys())
    mAP = [data[model]['mean_average_precision'] for model in models]
    inference_time = [data[model]['average_inference_time'] for model in models]

    plt.figure(figsize=(8, 6))
    plt.scatter(inference_time, mAP)
    plt.xlabel('Average Time Elapsed to Detect Faces on a Single Image in Seconds (Inference Time)', fontsize=12)
    plt.ylabel('Mean Average Precision', fontsize=12)
    plt.title('Mean Average Precision with Respect to Time Complexity', fontsize=14)

    for i, model in enumerate(models):
        plt.annotate(model, (inference_time[i], mAP[i]), textcoords="offset points", xytext=(5, 5), ha='center')

    plt.show()

# Plotting the data
plot_iou_vs_time_complexity(output_data)
plot_map_vs_time_complexity(output_data)

