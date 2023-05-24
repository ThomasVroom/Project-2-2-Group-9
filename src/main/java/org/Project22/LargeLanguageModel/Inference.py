import time

import numpy as np
import tensorflow as tf
from tensorflow.keras.preprocessing.sequence import pad_sequences
from tensorflow.keras.models import load_model
from sklearn.preprocessing import LabelEncoder
from keras.preprocessing.text import Tokenizer
# Constants
MAX_WORDS = 10000
MAX_LEN = 100


import pickle

def load_tokenizer(tokenizer_path):
    with open(tokenizer_path, 'rb') as f:
        tokenizer = pickle.load(f)
    return tokenizer



def preprocess_text(text, tokenizer):
    sequences = tokenizer.texts_to_sequences([text])
    padded_sequences = pad_sequences(sequences, maxlen=MAX_LEN)
    print(padded_sequences)
    return padded_sequences


def load_saved_model(model_path):
    model = load_model(model_path)
    return model


def predict_label(text, model, tokenizer):
    preprocessed_text = preprocess_text(text, tokenizer)
    predictions = model.predict(preprocessed_text)
    print(predictions)
    predicted_label_id = np.argmax(predictions[0])
    predicted_label = label_encoder.inverse_transform([predicted_label_id])[0]
    return predicted_label


# Load the model and tokenizer
model_path = 'sentence_classification_model.h5'
model = load_saved_model(model_path)

tokenizer_path = 'tokenizer.pkl'
tokenizer = load_tokenizer(tokenizer_path)

# Load the label encoder
label_encoder = LabelEncoder()
label_encoder.classes_ = np.load('label_encoder_classes.npy')  # Load the saved label encoder classes


def inferclass(sentence):
    start_time = time.time()
    predicted_label = predict_label(sentence, model, tokenizer)
    end_time = time.time()
    elapsed_time = end_time - start_time
    print(f"Inference time: {elapsed_time} seconds")
    print(f"Predicted Label: {predicted_label}")
    return predicted_label
