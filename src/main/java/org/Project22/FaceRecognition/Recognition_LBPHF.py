import os, sys
import cv2
import time
import numpy as np

# pre=trained data of HAAR is used for detection of faces:

face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

# Create an instance of the LBPHFRecognizer
recognizer = cv2.face.LBPHFaceRecognizer_create()

class Recognition:
    face_locations = []
    face_encodings = []
    face_names = []
    known_face_encodings = []
    known_face_names_dlib = []
    known_face_names_openCV = []
    known_face_recognizers = []

    process_current_frame = True

    def __init__(self):
        self.encode_faces_openCV()

    # LBPHFaceRecognizer_create is tried to see difference between dlib and that one but dlib gives clearly better
    # results than that. So we can improve the function which uses LBPHFaceRecognizer_create function for recognition.
    def encode_faces_openCV(self):
        face_folder = 'src/main/java/org/Project22/FaceRecognition/dataset_of_people'
        label_dict = {}  # create an empty dictionary to map names to labels
        current_label = 0  # initialize the current label

        # Create a list of face images for each person in the dataset
        face_images_dict = {}
        for image in os.listdir(face_folder):
            name, ext = os.path.splitext(image)
            if name not in label_dict:
                label_dict[name] = current_label  # map the name to a label
                current_label += 1  # increment the current label
                face_images_dict[name] = []
            face_image = cv2.imread(os.path.join(face_folder, image), cv2.IMREAD_GRAYSCALE)
            face_images_dict[name].append(face_image)

        # Train a recognizer for each person in the dataset
        for name, face_images in face_images_dict.items():
            labels = [label_dict[name]] * len(face_images)
            recognizer.train(face_images, np.array(labels))

            # Add the trained recognizer and the name to the respective lists
            self.known_face_recognizers.append(recognizer)
            self.known_face_names_openCV.append(name)

        print(self.known_face_names_openCV)

    def run_recognition_openCV(self):
        capture = cv2.VideoCapture(0)

        if not capture.isOpened():
            sys.exit('Video source not found. Check your privacy settings.')
        time.sleep(0.1)
        start_time = time.time()

        while time.time() - start_time <= 10:

            ret, frame = capture.read()

            # the algorithm requires gray_scaled_frame scale image to perform classification.
            gray_scaled_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            faces = face_detection.detectMultiScale(gray_scaled_frame, scaleFactor=1.3, minNeighbors=5)

            # For each detected face, try to recognize it
            for (x, y, w, h) in faces:
                face_image = gray_scaled_frame[y:y+h, x:x+w]

                # Train the recognizer with the face image and assign it an ID equal to the name of the file without
                # the extension
                recognizer.train([face_image], np.array([0]))

                # Initialize the minimum distance and recognized name variables
                min_distance = 25
                recognized_name = "Unknown"

                # Iterate over the known face recognizers and try to recognize the current face
                for i, known_recognizer in enumerate(self.known_face_recognizers):
                    label, confidence = known_recognizer.predict(face_image)
                    distance = abs(confidence)

                    # If the current recognizer has a smaller distance than the current minimum, update the minimum
                    # distance and recognized name
                    if distance < min_distance:
                        min_distance = distance
                        recognized_name = self.known_face_names_openCV[i]

                # Draw a rectangle around the recognized face and display the recognized name
                cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
                cv2.putText(frame, recognized_name, (x, y-10), cv2.FONT_HERSHEY_SIMPLEX, 0.9, (0, 255, 0), 2)

            cv2.imshow("Face Recognition", frame)

            if cv2.waitKey(1) == ord('q'):
                break

        capture.release()
        cv2.destroyAllWindows()


if __name__ == '__main__':
    face_rec = Recognition()
    face_rec.run_recognition_openCV()

