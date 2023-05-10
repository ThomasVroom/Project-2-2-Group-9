import matplotlib.pyplot as plt
import pandas as pd

data = pd.read_csv('src\main\java\org\Project22\Experiments\Experiment2\Results.csv', delimiter=';')

rgb_colors = [(0.2, 0.4, 0.6), (0.5, 0.2, 0.8)]

plt.bar(data['Confidence cutoff'], data['Score'], color=rgb_colors)
plt.xlabel('Threshold Value')
plt.ylabel('Score achieved')
plt.title('Score based on each threshold value')
plt.show()