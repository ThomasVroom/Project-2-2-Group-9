import os
import numpy as np
import tensorflow as tf
from keras.optimizers import Adam
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.preprocessing import LabelEncoder
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Embedding, GlobalAveragePooling1D
from tensorflow.keras.layers import LSTM, Bidirectional, Dropout
from keras.wrappers.scikit_learn import KerasClassifier
from keras.callbacks import EarlyStopping, ReduceLROnPlateau

# Define constants
DATA_DIR = 'Data/'
MAX_WORDS = 10000
MAX_LEN = 100
EMBEDDING_DIM = 100
EPOCHS = 30


def build_model(size=(64, 64, 32), learning_rate=0.01, batch_size=32):
    # Read data from files and create dataset
    sentences = []
    labels = []

    for file_name in os.listdir(DATA_DIR):
        label = file_name.split('.')[0]
        with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                sentences.append(line.strip())
                labels.append(label)

    # Preprocess sentences
    tokenizer = Tokenizer(num_words=MAX_WORDS, oov_token='<OOV>')
    tokenizer.fit_on_texts(sentences)
    word_index = tokenizer.word_index
    sequences = tokenizer.texts_to_sequences(sentences)
    padded_sequences = pad_sequences(sequences, maxlen=MAX_LEN)

    # Encode labels
    label_encoder = LabelEncoder()
    encoded_labels = label_encoder.fit_transform(labels)
    num_classes = len(label_encoder.classes_)

    # Split data into training and testing sets
    X_train, X_test, y_train, y_test = train_test_split(
        padded_sequences, encoded_labels, test_size=0.2, random_state=42
    )

    # Define the EarlyStopping callback
    early_stopping = EarlyStopping(
        monitor='val_accuracy', patience=15, mode='max', verbose=1, restore_best_weights=True
    )

    # Build neural network model
    model = Sequential([
        Embedding(MAX_WORDS, EMBEDDING_DIM, input_length=MAX_LEN),
        Bidirectional(LSTM(size[0], return_sequences=True, dropout=0.3)),
        GlobalAveragePooling1D(),
        Dense(size[1], activation='relu'),
        Dense(size[2], activation='relu'),
        Dense(32, activation='relu'),
        Dropout(0.3),
        Dense(num_classes, activation='softmax')
    ])

    optimizer = Adam(learning_rate=learning_rate)
    reduce_lr = ReduceLROnPlateau(monitor='val_accuracy', factor=0.2, patience=3, min_lr=0.001)
    model.compile(
        loss='sparse_categorical_crossentropy',
        optimizer=optimizer,
        metrics=['accuracy']
    )

    # Train the model with early stopping

    return model


def main():
    # Read data from files and create dataset
    sentences = []
    labels = []

    for file_name in os.listdir(DATA_DIR):
        label = file_name.split('.')[0]
        with open(os.path.join(DATA_DIR, file_name), 'r', encoding='utf-8') as f:
            lines = f.readlines()
            for line in lines:
                sentences.append(line.strip())
                labels.append(label)

    # Preprocess sentences
    tokenizer = Tokenizer(num_words=MAX_WORDS, oov_token='<OOV>')
    tokenizer.fit_on_texts(sentences)
    word_index = tokenizer.word_index
    sequences = tokenizer.texts_to_sequences(sentences)
    padded_sequences = pad_sequences(sequences, maxlen=MAX_LEN)

    # Encode labels
    label_encoder = LabelEncoder()
    encoded_labels = label_encoder.fit_transform(labels)

    # Create the GridSearchCV object
    param_grid = {
        'size': [(64, 64, 32), (128, 64, 32), (128, 128, 64), (256, 128, 64),(512, 256, 128),(1024, 256, 128)],
        'learning_rate': [0.01, 0.05, 0.1, 0.2, 0.5, 0.005,0.001],
        'batch_size': [128]
    }

    # Wrap the Keras model inside KerasClassifier
    keras_model = KerasClassifier(build_fn=build_model, epochs=EPOCHS, verbose=1)

    grid_search = GridSearchCV(
        estimator=keras_model, param_grid=param_grid, cv=3, verbose=1
    )

    # Run the grid search
    grid_search.fit(padded_sequences, encoded_labels)

    # Print the best parameters and results
    print("Best Parameters: ", grid_search.best_params_)
    print("Best Accuracy: ", grid_search.best_score_)


if __name__ == '__main__':
    main()
