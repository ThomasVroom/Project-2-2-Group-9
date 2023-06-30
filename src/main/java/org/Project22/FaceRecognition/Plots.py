import matplotlib.pyplot as plt

output_data_running_time = {
    'CNN': 0.504211926460266,
    'HOG': 0.131909699440002
}

output_data_percentage = {
    'CNN': 96.0546,
    'HOG': 95.6505
}

# Extract the method names and corresponding values from the dictionaries
methods_running_time = list(output_data_running_time.keys())
values_running_time = list(output_data_running_time.values())

methods_percentage = list(output_data_percentage.keys())
values_percentage = list(output_data_percentage.values())

# Plotting running time data
plt.figure(figsize=(8, 4))
plt.bar(methods_running_time, values_running_time, width=0.5, color=['blue', 'purple'])  # Specify colors
plt.xlabel('Method', fontweight='bold')
plt.ylabel('Average Running Time Per Iteration (Second)', fontweight='bold')
plt.title('How do the methods differ in terms of running time?', fontweight='bold')
plt.show()

# Plotting percentage data
plt.figure(figsize=(8, 4))
plt.bar(methods_percentage, values_percentage, width=0.5, color=['blue', 'purple'])  # Specify colors
plt.xlabel('Method', fontweight='bold')
plt.ylabel('Mean Confidence Percentage', fontweight='bold')
plt.title('How do the methods differ in terms of accuracy?', fontweight='bold')
plt.show()

