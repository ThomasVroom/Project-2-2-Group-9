import face_recognition
import os, sys
import cv2
import math
import time
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

THRESHOLD = 0.6

class Recognition:
    face_locations = []
    face_encodings = []
    face_names = []
    known_face_encodings = []
    known_face_names = []

    process_current_frame = True

    def __init__(self):
        self.encode_faces()

    def encode_faces(self):
        """
            This method loads images from the 'dataset_of_people' directory, detects faces in each image,
            and encodes the faces into 128-dimensional feature vectors using the face_recognition library.
            The method then appends each encoding to the 'known_face_encodings' list and the corresponding
            image name to the 'known_face_names' list.
        """
        for image in os.listdir('dataset_of_people'):
            name, ext = os.path.splitext(image)
            face_image = face_recognition.load_image_file(f'dataset_of_people/{image}')
            face_encoding = face_recognition.face_encodings(face_image)[0]

            self.known_face_encodings.append(face_encoding)
            self.known_face_names.append(name)

        print(self.known_face_names)

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
        if method == 'cosine_similarity':
            return cosine_similarity([encoding1], [encoding2])[0][0]
        if method == 'euclidean_distance':
            return np.sqrt(np.sum(np.square(np.subtract(encoding1, encoding2))))


    def run_recognition(self):
        capture = cv2.VideoCapture(0)

        if not capture.isOpened():
            sys.exit('Video source not found. Check your privacy settings.')
        time.sleep(0.1)
        start_time = time.time()

        while time.time() - start_time <= 10:
            ret, frame = capture.read()

            if self.process_current_frame:
                resized_frame = cv2.resize(frame, (0, 0), fx=0.25, fy=0.25)
                RGB_resized = resized_frame

                self.face_locations = face_recognition.face_locations(RGB_resized, model='cnn')
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
                        name = self.known_face_names[best_match_index]
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

        capture.release()
        cv2.destroyAllWindows()




if __name__ == '__main__':
    face_rec = Recognition()
    face_rec.run_recognition()
