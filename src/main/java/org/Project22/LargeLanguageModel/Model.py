import logging
import os
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

# Define constants
DATA_DIR = 'Data/'
MAX_WORDS = 10000
MAX_LEN = 100
EMBEDDING_DIM = 100
EPOCHS = 200
BATCH_SIZE = 256


def train(size=(128, 64, 32),learningrate = 0.001):

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

        # Print label and a few sentences for testing
        print(f"Label: {label}")
        print("Sentences:")
        for sentence in lines[:3]:  # Adjust the number of sentences to print
            print(f"  - {sentence.strip()}")

    # Preprocess sentences
    tokenizer = Tokenizer(num_words=MAX_WORDS, oov_token='<OOV>')
    tokenizer.fit_on_texts(sentences)
    word_index = tokenizer.word_index
    sequences = tokenizer.texts_to_sequences(sentences)
    padded_sequences = pad_sequences(sequences, maxlen=MAX_LEN)
    print(padded_sequences)
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
        monitor='val_accuracy', patience=35, mode='max', verbose=1, restore_best_weights=True
    )


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
    reduce_lr = ReduceLROnPlateau(monitor='val_accuracy', factor=0.2, patience=3, min_lr=0.001, )
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
