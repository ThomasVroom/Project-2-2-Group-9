import itertools
import logging
import os
import time

import nltk
import numpy as np
logging.getLogger().setLevel(logging.FATAL)
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
from keras.optimizers import Adam
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from keras.models import Sequential
from keras.layers import Dense, Embedding, GlobalAveragePooling1D
from keras.layers import LSTM, Bidirectional, Dropout
from keras.callbacks import EarlyStopping, ReduceLROnPlateau
from string import punctuation
from nltk.stem import SnowballStemmer
from nltk.corpus import stopwords
import string
from nltk.tokenize import word_tokenize

# Define constants
DATA_DIR = 'Data/'
MAX_WORDS = 10000
MAX_LEN = 100
EMBEDDING_DIM = 100
EPOCHS = 200
BATCH_SIZE = 128

import matplotlib.pyplot as plt

def plot_results(histories, configs, sizes):
    fig, axs = plt.subplots(2, figsize=(12,12))

    for history, config, size in zip(histories, configs, sizes):
        axs[0].plot(history.history['val_accuracy'], label=f'size = {size} skills = {config}')

    axs[0].set_title('Model Accuracy')
    axs[0].set_ylabel('Accuracy')
    axs[0].set_xlabel('Epoch')
    axs[0].legend()

    axs[1].plot(sizes, [max(h.history['val_accuracy']) for h in histories], 'o-')
    axs[1].set_title('Model Size vs. Max Validation Accuracy')
    axs[1].set_ylabel('Max Validation Accuracy')
    axs[1].set_xlabel('Model Size')

    plt.tight_layout()
    plt.show()

def train(stop, stem, sentences, labels, size=(16, 16, 8), learningrate=0.001, batchsize =128,reduce_lr_factor=0.2, reduce_lr_patience=3,monitor='val_accuracy',patience=85):
    tokenizer = Tokenizer(num_words=MAX_WORDS, oov_token='<OOV>')
    #preprocess sentences
    
    sequences = preprocess(sentences, stop=stop, stem=stem)
    #turn list of list back into one list.
    untokenized_list=[]
    for sentence in sequences:
        untokenize =' '.join(sentence)
        untokenized_list.append(untokenize)
    print(untokenized_list)

    tokenizer.fit_on_texts(untokenized_list)
    word_index = tokenizer.word_index
    word2num = tokenizer.texts_to_sequences(untokenized_list)

    padded_sequences = pad_sequences(word2num, maxlen=MAX_LEN)



    # Encode labels
    label_encoder = LabelEncoder()
    encoded_labels = label_encoder.fit_transform(labels)
    num_classes = len(label_encoder.classes_)

    # Save the label encoder classes
    np.save('label_encoder_classes.npy', label_encoder.classes_)

    # Split data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(
        padded_sequences, encoded_labels, test_size=0.2, random_state=42, stratify=encoded_labels
    )
    print("Total data points:", len(padded_sequences))
    print("Total labels:", len(encoded_labels))
    print("Train data points:", len(X_train))
    print("Train labels:", len(y_train))
    print("Test data points:", len(X_test))
    print("Test labels:", len(y_test))

    # Define the EarlyStopping callback
    if monitor == 'val_loss':
        mode = 'min'
    elif monitor == 'val_accuracy':
        mode = 'max'
    early_stopping = EarlyStopping(monitor=monitor, patience=patience, mode=mode , verbose=1, restore_best_weights=True)



    # Build neural network model
    model = Sequential([
        Embedding(MAX_WORDS, EMBEDDING_DIM, input_length=MAX_LEN),
        Bidirectional(LSTM(size[0], return_sequences=True, dropout=0.3)),
        GlobalAveragePooling1D(),
        Dense(size[1], activation='relu'),
        Dense(size[2], activation='relu'),
        Dropout(0.3),
        Dense(num_classes, activation='softmax')
    ])

    optimizer = Adam(learning_rate=learningrate)
    reduce_lr = ReduceLROnPlateau(monitor='val_accuracy', factor=reduce_lr_factor, patience=reduce_lr_patience, min_lr=0.001, )
    model.compile(
        loss='sparse_categorical_crossentropy',
        optimizer=optimizer,
        metrics=['accuracy']
    )

    start_time = time.time()

    # Train the model with early stopping
    history = model.fit(
        X_train, y_train, epochs=EPOCHS, batch_size=batchsize,
        validation_data=(X_test, y_test), callbacks=[early_stopping, reduce_lr]
    )

    end_time = time.time()
    training_time = end_time - start_time

    # Save the model and tokenizer for later use
    model.save('sentence_classification_model.h5')
    import pickle

    # Save the tokenizer
    tokenizer_path = 'tokenizer.pkl'
    with open(tokenizer_path, 'wb') as f:
        pickle.dump(tokenizer, f)
    print(f"Tokenizer saved to {tokenizer_path}")


    print("Model and tokenizer saved.")
    return history, training_time
    
def preprocess(sentences, stop=True, stem=True):
    stop_words = set(stopwords.words('english'))

    for sentence in sentences:
        tokenized_sentence = word_tokenize(sentence.lower())
        if stop:
            tokenized_sentence = [token for token in tokenized_sentence if token not in stop_words  ]
        if stem:
            stemmer = SnowballStemmer('english') 

            tokenized_sentence = [stemmer.stem(token) for token in tokenized_sentence]
        yield tokenized_sentence

def compute_model_size(sentences, factor=0.03):
    """
    Compute the size of the model based on the number of training samples and data complexity.
    The factor parameter controls how quickly the model size increases.
    """

    # Compute data complexity
    avg_sentence_len = np.mean([len(s.split()) for s in sentences])
    num_unique_words = len(set(' '.join(sentences).split()))

    # Compute base model size based on data volume (number of sentences)
    base_model_size = int(len(sentences) * factor)

    # Average model size based on data complexity and volume
    size = (
        int((avg_sentence_len * 1.5 + base_model_size) / 2),
        int((num_unique_words * 0.02 + base_model_size) / 2),
        int((avg_sentence_len + base_model_size) / 2)
    )
    print(size)
    return size



# In your training function...
def incremental_train(stop=True, stem=True, learningrate=0.001):
    histories = []
    sizes = []
    all_files = os.listdir(DATA_DIR)
    all_files.sort()

    for i in range(2, len(all_files) + 1):
        sentences = []
        labels = []

        # Only load the first `i` files
        for file_name in all_files[:i]:
            label = file_name.split('.')[0]
            with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
                lines = f.readlines()
                for line in lines:
                    sentences.append(line.strip())
                    labels.append(label)

        print(f"\nTraining with {i} file(s) and stop={stop} and stem={stem}")

        size = compute_model_size(sentences)

        history = train(stop, stem, sentences, labels, size, learningrate)
        histories.append(history)
        sizes.append(size)

    plot_results(histories, [f'{i} file(s)' for i in range(1, len(all_files) + 1)],sizes )

def batch_size_experiment(batch_sizes, stop=True, stem=True, learningrate=0.001):
        histories = []
        training_times = []
        all_files = os.listdir(DATA_DIR)
        all_files.sort()

        # Load all data
        sentences = []
        labels = []

        for file_name in all_files:
            label = file_name.split('.')[0]
            with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
                lines = f.readlines()
                for line in lines:
                    sentences.append(line.strip())
                    labels.append(label)

        size = compute_model_size(sentences)

        # Train a model with each batch size
        for batch_size in batch_sizes:
            print(f"\nTraining with batch size = {batch_size} and stop = {stop} and stem = {stem}")
            history, training_time = train(stop, stem, sentences, labels, size, learningrate, batchsize=batch_size)
            histories.append(history)
            training_times.append(training_time)

        # Plot the results
        fig, axs = plt.subplots(2, figsize=(12, 12))

        for i, history in enumerate(histories):
            axs[0].plot(history.history['val_accuracy'], label=f'Batch size = {batch_sizes[i]}')

        axs[0].set_title('Model validation accuracy over time for different batch sizes')
        axs[0].set_ylabel('Validation Accuracy')
        axs[0].set_xlabel('Epoch')
        axs[0].legend()

        axs[1].bar([str(bs) for bs in batch_sizes], training_times)
        axs[1].set_title('Training time for different batch sizes')
        axs[1].set_ylabel('Training Time (seconds)')
        axs[1].set_xlabel('Batch Size')

        plt.tight_layout()
        plt.show()


def learning_rate_experiment(learning_rates, reduce_lr_factors, reduce_lr_patiences, stop=True, stem=True,
                             batch_size=BATCH_SIZE):
    histories = []
    training_times = []
    sentences = []
    labels = []
    experiment_params = []
    all_files = os.listdir(DATA_DIR)
    all_files.sort()

    for file_name in all_files:
        label = file_name.split('.')[0]
        with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                sentences.append(line.strip())
                labels.append(label)

    size = compute_model_size(sentences)
    # For each combination of learning rate and ReduceLROnPlateau parameters
    for learning_rate, reduce_lr_factor, reduce_lr_patience in itertools.product(learning_rates, reduce_lr_factors,
                                                                                 reduce_lr_patiences):
        print(
            f"\nTraining with learning_rate={learning_rate}, reduce_lr_factor={reduce_lr_factor}, reduce_lr_patience={reduce_lr_patience}")

        history, training_time = train(stop, stem, sentences, labels, size, learning_rate, batch_size, reduce_lr_factor,
                                       reduce_lr_patience)
        histories.append(history)
        training_times.append(training_time)
        experiment_params.append(
            (learning_rate, reduce_lr_factor, reduce_lr_patience))  # Add current parameters to list

    # Plot the results
    fig, axs = plt.subplots(2, figsize=(12, 12))

    for i, history in enumerate(histories):
        lr, factor, patience = experiment_params[i]  # Retrieve parameters for current experiment
        axs[0].plot(history.history['val_accuracy'], label=f'LR = {lr}, Factor = {factor}, Patience = {patience}')

    axs[0].set_title(
        'Model validation accuracy over time for different learning rates and ReduceLROnPlateau parameters')
    axs[0].set_ylabel('Validation Accuracy')
    axs[0].set_xlabel('Epoch')
    axs[0].legend()

    axs[1].bar([str(i) for i in range(len(training_times))], training_times)
    axs[1].set_title('Training time for different learning rates and ReduceLROnPlateau parameters')
    axs[1].set_ylabel('Training Time (seconds)')
    axs[1].set_xlabel('Experiment Index')

    plt.tight_layout()
    plt.show()


from itertools import product
import time


def early_stopping_experiment(patience_values, monitor_values=['val_loss', 'val_accuracy'], stop=True, stem=True,
                              learning_rate=0.001, batch_size=BATCH_SIZE, reduce_lr_factor=0.2, reduce_lr_patience=3):
    histories = []
    training_times = []
    experiment_params = []
    sentences = []
    labels = []
    all_files = os.listdir(DATA_DIR)
    all_files.sort()

    for file_name in all_files:
        label = file_name.split('.')[0]
        with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                sentences.append(line.strip())
                labels.append(label)

    size = compute_model_size(sentences)
    # For each combination of patience and monitor parameters for EarlyStopping
    for patience, monitor in product(patience_values, monitor_values):
        print(f"\nTraining with patience={patience}, monitor={monitor}")

        start_time = time.time()

        history, _ = train(stop, stem, sentences, labels, size, learning_rate, batch_size, reduce_lr_factor,
                        reduce_lr_patience, patience=patience, monitor=monitor)

        end_time = time.time()
        training_time = end_time - start_time

        histories.append(history)
        training_times.append(training_time)
        experiment_params.append((patience, monitor))

    # Plot the results
    fig, axs = plt.subplots(2, figsize=(12, 12))

    for i, history in enumerate(histories):
        patience, monitor = experiment_params[i]  # Retrieve parameters for current experiment
        axs[0].plot(history.history[monitor], label=f'Patience = {patience}, Monitor = {monitor}')

    axs[0].set_title('Model metric over time for different EarlyStopping parameters')
    axs[0].set_ylabel('Monitored Metric')
    axs[0].set_xlabel('Epoch')
    axs[0].legend()

    axs[1].bar([str(i) for i in range(len(training_times))], training_times)
    axs[1].set_title('Training time for different EarlyStopping parameters')
    axs[1].set_ylabel('Training Time (seconds)')
    axs[1].set_xlabel('Experiment Index')

    plt.tight_layout()
    plt.show()


if __name__ == '__main__':
    early_stopping_experiment(patience_values=[5, 10, 25, 50], monitor_values=['val_loss', 'val_accuracy'])


