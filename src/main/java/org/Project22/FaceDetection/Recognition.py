import face_recognition
import os, sys
import cv2
import math
import time
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

# if you want to try opencv's recognition function then uncomment the 18th line (recognizer = cv2.face.LBPHFaceRecognizer_create()),
# uncomment 35th line (# self.encode_faces_openCV()) and 252nd line (face_rec.run_recognition_openCV()). 

# pre=trained data of HAAR is used for detection of faces:

face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

# Create an instance of the LBPHFRecognizer
# recognizer = cv2.face.LBPHFaceRecognizer_create()

THRESHOLD = 0.2 # 0.2 is ideal for cosine_similarity distance function.
                # Haven't find the optimal thresholds for the other functions yet.  

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
         # self.encode_faces_openCV()
        self.encode_faces_dlib()

    def encode_faces_dlib(self):
        """
            This method loads images from the 'dataset_of_people' directory, detects faces in each image,
            and encodes the faces into 128-dimensional feature vectors using the face_recognition library.
            The method then appends each encoding to the 'known_face_encodings' list and the corresponding
            image name to the 'known_face_names_dlib' list.
        """
        for image in os.listdir('src/main/java/org/Project22/FaceDetection/dataset_of_people'):
            name, ext = os.path.splitext(image)
            face_image = face_recognition.load_image_file(f'src/main/java/org/Project22/FaceDetection/dataset_of_people/{image}')
            face_encoding = face_recognition.face_encodings(face_image)[0]

            self.known_face_encodings.append(face_encoding)
            self.known_face_names_dlib.append(name)

        print(self.known_face_names_dlib)

    def get_similarity(self, encoding1, encoding2, method):
        """
        This method calculates the similarity score between two 128-dimensional face encodings using the specified
        methods.The cosine similarity measures the similarity between two vectors by taking the cosine of the
        exact dissimilarity.

        :param encoding1: The first 128-dimensional face encoding.
        :param encoding2: The second 128-dimensional face encoding.

        :param method: The name of the method which is employed to calculate distance.
        :return: If the method is cosine_similarity then returns cosine similarity between the two encodings as a
        float value between -1 and 1.
                If the method is euclidean_distance then returns euclidean distance between the two encodings.
        """
        if method == 'cosine_similarity': # Cosine distance measures the cosine of the angle between two vectors. Good alternative to Euclidean distance for high-dimensional data like face encodings.
            return cosine_similarity([encoding1], [encoding2])[0][0]
        if method == 'euclidean_distance':
            return np.sqrt(np.sum(np.square(np.subtract(encoding1, encoding2))))
        if method == 'canberra_distance':
            return np.sum(np.abs(encoding1 - encoding2) / (np.abs(encoding1) + np.abs(encoding2)))


    def run_recognition_dlib(self):
        capture = cv2.VideoCapture(0)

        if not capture.isOpened():
            sys.exit('Video source not found. Check your privacy settings.')
        time.sleep(0.1)
        start_time = time.time()

        while time.time() - start_time <= 20:
            ret, frame = capture.read()

            if self.process_current_frame:
                resized_frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)
                RGB_resized = resized_frame

                # The 'hog' model is faster but less accurate compared to the 'cnn' model.
                # It is based on Histogram of Oriented Gradients (HOG) features and a linear SVM classifier.
                # The 'hog' model can work well if the faces are well-lit and frontal, but it may fail in low-light
                # conditions or if the faces are in profile or at an angle.

                # The 'cnn' model is a Convolutional Neural Network (CNN) that is trained to detect faces in images.
                # It is slower but more accurate compared to the 'hog' model. The 'cnn' model can handle faces in
                # different orientations and lighting conditions, and can detect faces at smaller scales compared to
                # the 'hog' model.

                self.face_locations = face_recognition.face_locations(RGB_resized, model='cnn') # to try hog model -> model='hog'
                self.face_encodings = face_recognition.face_encodings(RGB_resized, self.face_locations)
                self.face_names = []

                for face_encoding in self.face_encodings:
                    matches = face_recognition.compare_faces(self.known_face_encodings, face_encoding)
                    name = "Unknown face"
                    confidence_str = "Unknown"

                    similarities = [self.get_similarity(face_encoding, known_encoding, 'cosine_similarity') for
                                    known_encoding in self.known_face_encodings]
                    best_match_index = np.argmax(similarities)
                    best_similarity = similarities[best_match_index]

                    if best_similarity > THRESHOLD:
                        name = self.known_face_names_dlib[best_match_index]
                        confidence_str = str(round(best_similarity * 100, 2)) + '%'

                    self.face_names.append(f'{name} ({confidence_str})')

            self.process_current_frame = not self.process_current_frame

            # Displaying
            for (top, right, bottom, left), name in zip(self.face_locations, self.face_names):
                top *= 4
                right *= 4
                bottom *= 4
                left *= 4

                # Calculate the size of the face rectangle
                width = right - left
                height = bottom - top
                x_padding = int(width * 0.2)
                y_padding = int(height * 0.2)

                # Calculate the new coordinates of the face rectangle
                left -= x_padding
                right += x_padding
                top -= y_padding
                bottom += y_padding

                # Draw the new rectangle on the frame
                cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
                cv2.rectangle(frame, (left, bottom - 35), (right, bottom), (0, 0, 255), -1)
                cv2.putText(frame, name, (left + 6, bottom - 6), cv2.FONT_HERSHEY_DUPLEX, 0.8, (255, 255, 255), 1)

            cv2.imshow("Face Recognition", frame)

            if cv2.waitKey(1) == ord('q'):
                break

        if face_encoding is not None:
                print("A human was detected")
        else:
                 print("No human was detected around.")

        capture.release()
        cv2.destroyAllWindows()

    # LBPHFaceRecognizer_create is tried to see difference between dlib and that one but dlib gives clearly better
    # results than that. So we can improve the function which uses LBPHFaceRecognizer_create function for recognition.
    def encode_faces_openCV(self):
        face_folder = 'dataset_of_people'
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

        while time.time() - start_time <= 20:

            ret, frame = capture.read()

            # the algorithm requires gray_scaled_frame scale image to perform classification.
            gray_scaled_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # detects all the faces in the frame. Scale Factor: This parameter controls how much the image size is
            # reduced at each image scale. Increasing the scale factor can result in better detection of smaller
            # faces, but it can also increase the chances of false positives. Minimum Neighbors: This parameter
            # specifies how many neighbors each potential detection rectangle must have in order to be considered a
            # valid detection. Increasing the number of neighbors can reduce false positives but can also decrease
            # the sensitivity of the detector and increase the likelihood of false negatives. Higher value = fewer
            # detections but higher quality. Value between 3-6 is good
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
    # face_rec.run_recognition_openCV()
    face_rec.run_recognition_dlib()
