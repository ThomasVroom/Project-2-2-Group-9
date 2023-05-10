import os
import cv2
import numpy as np
import face_recognition

cap = cv2.VideoCapture(0)

# Create an empty dictionary to store the face encodings
known_faces = {}

save_dir = os.path.join(os.getcwd(), "dataset_of_people", "lale")

if not os.path.exists(save_dir):
    os.makedirs(save_dir)

haar_face_recognition_ds = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')

count = 0

while True:
    ret, frame = cap.read()

    if ret:
        cv2.imshow('Capture', frame)
        save_path = os.path.join(save_dir, f"{count}.jpg")
        cv2.imwrite(save_path, frame)
        print(f"Image {count} saved to {save_path}")

        # Load the saved image and encode the face
        image = face_recognition.load_image_file(save_path)
        face_locations = face_recognition.face_locations(image)

        if len(face_locations) > 0:
            # Encode the face if at least one face is detected
            encoding = face_recognition.face_encodings(image, face_locations)[0]
            # Add the encoding to the dictionary with the person's name as the key
            known_faces['lale'] = encoding
            count += 1

    if count == 50:
        break

    if cv2.waitKey(1) == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

# Save the dictionary of known faces to a numpy file
known_faces_file = 'known_faces.npy'
np.save(known_faces_file, known_faces)

# import cv2
# import time
# import os
# import numpy as np
# import sys
# import face_recognition
#
# # retrieve the user's name from the command-line arguments
# user_name = "lale"
#
# # sys.argv[2]
#
# # pre=trained data of HAAR is used:
# face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
# eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')
#
# capture = cv2.VideoCapture(0)
#
# # ret indicates whether the capture is done successfully.
# # frame is the numpy array that represents the image.
# ret, frame = capture.read()
#
#
# def detectFaceAndEyes():
#     detected = False
#
#     # the algorithm requires gray scale image to perform classification.
#     gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
#
#     # detects all the faces in the frame.
#     # Scale Factor: This parameter controls how much the image size is reduced at each image scale.
#     #   Increasing the scale factor can result in better detection of smaller faces, but it can also increase the
#     #   chances of false positives.
#     # Minimum Neighbors: This parameter specifies how many neighbors each potential detection rectangle must have
#     #   in order to be considered a valid detection. Increasing the number of neighbors can reduce false positives
#     #   but can also decrease the sensitivity of the detector and increase the likelihood of false negatives.
#     #   Higher value = fewer detections but higher quality. Value between 3-6 is good
#     faces = face_detection.detectMultiScale(gray, scaleFactor=1.3, minNeighbors=5)
#
#     for (x, y, w, h) in faces:
#         # the rectangle draws rectangles which covers the faces in the image.
#         # the parameters are: frame which has the image, top left corner, the bottom right, color, thickness.
#         cv2.rectangle(frame, (x, y), (x + w, y + h), (50, 200, 0), 4)
#
#         # rectangleOfImage will be used to focus on specifically inside this rectangle to detect the eyes.
#         rectangleOfImage = gray[y:y + w, x:x + w]
#
#         # the copy of the frame to draw rectangles to cover the eyes however there is a difference between frame and
#         # rectangleOfImage_in_frame: rectangleOfImage_in_frame is the little part of the frame that has the sizes of
#         # rectangles that covers the faces
#         rectangleOfImage_in_frame = frame[y:y + h, x:x + w]
#
#         # the eyes in the rectangle which covers the detected face, will be detected and the rectangles will be
#         # drawn aon the rectangleOfImage_in_frame to specify eyes.
#         eyes = eye_detection.detectMultiScale(rectangleOfImage, scaleFactor=1.3, minNeighbors=5)
#         for (ex, ey, ew, eh) in eyes:
#             cv2.rectangle(rectangleOfImage_in_frame, (ex, ey), (ex + ew, ey + eh), (100, 0, 0), 4)
#
#     if len(faces) > 0:
#         detected = True
#     return detected
#
#
# def isDetected():
#     if detectFaceAndEyes():
#         print("A human was detected")
#     else:
#         print("No human was detected around.")
#
#
# def recognition(user_name):
#     # Encodings of the faces:
#     known_faces = {}
#     count = 0
#
#     save_dir = os.path.join(os.getcwd(), "dataset_of_people", user_name)
#
#     if not os.path.exists(save_dir):
#         os.makedirs(save_dir)
#
#     while True:
#         save_path = os.path.join(save_dir, f"{count}.jpg")
#         cv2.imwrite(save_path, frame)
#         print(f"Image {count} saved to {save_path}")
#
#         # Load the saved image and detect faces
#         image = face_recognition.load_image_file(save_path)
#         face_locations = face_recognition.face_locations(image)
#
#         if detectFaceAndEyes():
#             # Encode the face if at least one face is detected
#             encoding = face_recognition.face_encodings(image, face_locations)[0]
#             known_faces[user_name] = encoding
#             count += 1
#
#         if count == 50:
#             break
#
#
# start_time = time.time()
#
#
# while time.time() - start_time <= 15:
#     ret, frame = capture.read()  # read the next frame
#     recognition(user_name)
#     detectFaceAndEyes()
#
#     cv2.imshow('frame', frame)
#
#     # terminates the frame if user clicks "a"
#     if cv2.waitKey(1) == ord('a'):
#         break
#
# isDetected()
#
#
# capture.release()
# cv2.destroyAllWindows()
