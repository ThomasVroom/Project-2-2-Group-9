import matplotlib.pyplot as plt

def plot_accuracy_vs_time_complexity(data):
    models = list(data.keys())
    avg_iou = [data[model]['average_iou'] for model in models]
    inference_time = [data[model]['average_inference_time'] for model in models]

    plt.figure(figsize=(8, 6))
    plt.scatter(inference_time, avg_iou)
    plt.xlabel('Total Time for Prediction on a Single Image (Inference Time)')
    plt.ylabel('Average Intersection over Union (IOU)')
    plt.title('Accuracy with Respect to Time Complexity')

    for i, model in enumerate(models):
        plt.annotate(model, (inference_time[i], avg_iou[i]), textcoords="offset points", xytext=(5, 5), ha='center')

    plt.show()

# Data for OpenCV and BlazePose
data = {
    'OpenCV': {'average_iou': 0.21356696150864374, 'average_inference_time': 0.049498165828872745},
    'BlazePose': {'average_iou': 0.062434269915389064, 'average_inference_time': 0.002759250438664171}
}

# Data for OpenCV and BlazePose
data = {
    'OpenCV': {'average_iou': 0.21356696150864374, 'average_inference_time': 0.049498165828872745},
    'BlazePose': {'average_iou': 0.062434269915389064, 'average_inference_time': 0.002759250438664171},
    'DlibCNNFaceDetector': {'average_iou': 0.2827014003360415, 'average_inference_time': 1.0605117386346712}
}

# Plotting the data
plot_accuracy_vs_time_complexity(data)

