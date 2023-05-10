import matplotlib.pyplot as plt
import pandas as pd

#data_1 = pd.read_csv('Sheet 1-match1-4 - services.csv', delimiter=';')

rgb_colors = [(0.2, 0.4, 0.6), (0.5, 0.2, 0.8), (0.8, 0.3, 0.2), (0.6, 0.9, 0.2)]

#plt.bar(data_1['Algorithm used'], data_1['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Service Category')
#plt.show()

#data_2 = pd.read_csv('Sheet 2-match1-4 - lectures.csv', delimiter=';')

#plt.bar(data_2['Algorithm used'], data_2['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Lecture Category')
#plt.show()

#data_3 = pd.read_csv('Sheet 3-match1-4 theater.csv', delimiter=';')

#plt.bar(data_3['Algorithm used'], data_3['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Theater Category')
#plt.show()

#data_4 = pd.read_csv('Sheet 4-match1-4 drinks.csv', delimiter=';')

#plt.bar(data_4['Algorithm used'], data_4['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Drink Category')
#plt.show()

#data_5 = pd.read_csv('Sheet 5-match1-4 food.csv', delimiter=';')

#plt.bar(data_5['Algorithm used'], data_5['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Food Category')
#plt.show()

#data_6 = pd.read_csv('Sheet 6-match1-4 activities.csv', delimiter=';')

#plt.bar(data_6['Algorithm used'], data_6['Percentage correct'], color=rgb_colors)
#plt.xlabel('Matching Algorithms')
#plt.ylabel('Percentage Correct')
#plt.title('The Performance Of Matching Algorithms For The Activity Category')
#plt.show()

data_8 = pd.read_csv('src\main\java\org\Project22\Experiments\Experiment1\Sheet 8-match1-4 avg.csv', delimiter=';')

plt.bar(data_8['Algorithm used'], data_8['Percentage correct'], color=rgb_colors)
plt.xlabel('Matching Algorithms')
plt.ylabel('Percentage Correct')
plt.title('The Average Performance Of Matching Algorithms For All The Categories')
plt.show()