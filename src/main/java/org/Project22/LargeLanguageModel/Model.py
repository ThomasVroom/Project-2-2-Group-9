import logging
import os

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
nltk.download('stopwords')
nltk.download('punkt')

# Define constants
DATA_DIR = 'Data/'
MAX_WORDS = 10000
MAX_LEN = 100
EMBEDDING_DIM = 100
EPOCHS = 200
BATCH_SIZE = 128

import matplotlib.pyplot as plt

def plot_results(histories, configs):
    plt.figure(figsize=(12,6))

    for history, config in zip(histories, configs):
        plt.plot(history.history['val_accuracy'], label=f'stop={config[0]}, stem={config[1]}')

    plt.title('Model Accuracy')
    plt.ylabel('Accuracy')
    plt.xlabel('Epoch')
    plt.legend()
    plt.show()

def train(stop=True, stem=True, size=(128, 128, 64),learningrate = 0.01):


    sentences = []
    labels = []

    for file_name in os.listdir(DATA_DIR):
        label = file_name.split('.')[0]
        with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                sentences.append(line.strip())
                labels.append(label)

    tokenizer = Tokenizer(num_words=MAX_WORDS, oov_token='<OOV>')
    #preprocess sentences
    size = compute_model_size(sentences, 0.04)
    print(f"modelsize = {size}")
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
        padded_sequences, encoded_labels, test_size=0.2, random_state=42
    )


    # Define the EarlyStopping callback
    early_stopping = EarlyStopping(
        monitor='val_loss', patience=50, mode='min', verbose=1, restore_best_weights=True
    )


    # Build neural network model
    model = Sequential([
        Embedding(MAX_WORDS, EMBEDDING_DIM, input_length=MAX_LEN),
        Bidirectional(LSTM(size[0], return_sequences=True, dropout=0.2)),
        GlobalAveragePooling1D(),
        Dense(size[1], activation='relu'),
        Dense(size[2], activation='relu'),
        Dropout(0.2),
        Dense(num_classes, activation='softmax')
    ])

    optimizer = Adam(learning_rate=learningrate)
    reduce_lr = ReduceLROnPlateau(monitor='val_accuracy', factor=0.5, patience=7, min_lr=0.001, )
    model.compile(
        loss='sparse_categorical_crossentropy',
        optimizer=optimizer,
        metrics=['accuracy']
    )

    # Train the model with early stopping
    history = model.fit(
        X_train, y_train, epochs=EPOCHS, batch_size=BATCH_SIZE,
        validation_data=(X_test, y_test), callbacks=[early_stopping, reduce_lr]
    )

    # Save the model and tokenizer for later use
    model.save('sentence_classification_model.h5')
    import pickle

    # Save the tokenizer
    tokenizer_path = 'tokenizer.pkl'
    with open(tokenizer_path, 'wb') as f:
        pickle.dump(tokenizer, f)
    print(f"Tokenizer saved to {tokenizer_path}")


    print("Model and tokenizer saved.")
    return history
    
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



if __name__ == '__main__':
    options = [(True, True), (True, False), (False, True), (False, False)]
    histories = []
    for stop, stem in options:
        print(f"\nTraining with stop={stop} and stem={stem}")
        history = train(stop, stem)
        histories.append(history)

    plot_results(histories, options)