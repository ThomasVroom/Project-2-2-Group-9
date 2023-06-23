import face_recognition
import os, sys
import cv2
import time
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity


# pre=trained data of HAAR is used for detection of faces:

face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

THRESHOLD = 0.91 

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
        self.encode_faces_dlib()

    def encode_faces_dlib(self):
        """
            This method loads images from the 'dataset_of_people' directory, detects faces in each image,
            and encodes the faces into 128-dimensional feature vectors using the face_recognition library.
            The method then appends each encoding to the 'known_face_encodings' list and the corresponding
            image name to the 'known_face_names_dlib' list.
        """
        for image in os.listdir('src/main/java/org/Project22/FaceRecognition/dataset_of_people'):
            name, ext = os.path.splitext(image)
            face_image = face_recognition.load_image_file(f'src/main/java/org/Project22/FaceRecognition/dataset_of_people/{image}')
            face_encoding = face_recognition.face_encodings(face_image)[0]

            self.known_face_encodings.append(face_encoding)
            self.known_face_names_dlib.append(name)

        # print(self.known_face_names_dlib)

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
        hog_elapsed_times = []
        cnn_elapsed_times = []
        hog_confidences = []
        cnn_confidences = []

        for iteration in range(100):
            print(f"Iteration: {iteration + 1}")

            capture = cv2.VideoCapture(0)

            if not capture.isOpened():
                sys.exit('Video source not found. Check your privacy settings.')
            start_time = time.time()

            while time.time() - start_time <= 10:
                ret, frame = capture.read()

                if self.process_current_frame:
                    resized_frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)
                    RGB_resized = resized_frame

                    print("Running face recognition with HOG model...")

                    # Run face recognition with 'hog' model
                    self.face_locations = face_recognition.face_locations(RGB_resized, model='hog')
                    self.face_encodings = face_recognition.face_encodings(RGB_resized, self.face_locations)
                    self.face_names = []

                    face_detected = False

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

                            face_detected = True

                        self.face_names.append(f'{name} ({confidence_str})')

                    if face_detected:
                        elapsed_time = time.time() - start_time
                        hog_elapsed_times.append(elapsed_time)
                        hog_confidences.append(confidence_str)
                        print("Face recognized using HOG model.")
                        break

                self.process_current_frame = not self.process_current_frame

            if not face_detected:
                print("No human was detected around.")

            capture.release()
            cv2.destroyAllWindows()

        with open('hog_confidences.txt', 'w') as file:
            file.write('\n'.join(hog_confidences))

        # with open('cnn_confidences.txt', 'w') as file:
        #     file.write('\n'.join(cnn_confidences))

        with open('hog_elapsed_times.txt', 'w') as file:
            file.write('\n'.join(map(str, hog_elapsed_times)))

        # with open('cnn_elapsed_times.txt', 'w') as file:
        #     file.write('\n'.join(map(str, cnn_elapsed_times)))

        print("Experiment finished successfully.")


if __name__ == '__main__':
    face_rec = Recognition()
    face_rec.run_recognition_dlib()



