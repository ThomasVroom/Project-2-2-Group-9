import cv2
import time
#import face_recognition

start_time = time.time()

capture = cv2.VideoCapture(0)

# pre=trained data of HAAR is used:
face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

def detectFaceAndEyes():
    detected = False

    while time.time() - start_time <= 10:

        # ret indicates whether the capture is done successfully.
        # frame is the numpy array that represents the image.
        ret, frame = capture.read()

        # the algorithm requires gray scale image to perform classification.
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # detects all the faces in the frame.
        # Scale Factor: This parameter controls how much the image size is reduced at each image scale.
        #   Increasing the scale factor can result in better detection of smaller faces, but it can also increase the
        #   chances of false positives.
        # Minimum Neighbors: This parameter specifies how many neighbors each potential detection rectangle must have
        #   in order to be considered a valid detection. Increasing the number of neighbors can reduce false positives
        #   but can also decrease the sensitivity of the detector and increase the likelihood of false negatives.
        #   Higher value = fewer detections but higher quality. Value between 3-6 is good.
        faces = face_detection.detectMultiScale(gray, scaleFactor=1.3, minNeighbors=5)

        for (x, y, w, h) in faces:
            # the rectangle draws rectangles which covers the faces in the image.
            # the parameters are: frame which has the image, top left corner, the bottom right, color, thickness.
            cv2.rectangle(frame, (x, y), (x + w, y + h), (50, 200, 0), 4)

            # rectangleOfImage will be used to focus on specifically inside this rectangle to detect the eyes.
            rectangleOfImage = gray[y:y + w, x:x + w]

            # the copy of the frame to draw rectangles to cover the eyes however there is a difference between frame and
            # rectangleOfImage_in_frame: rectangleOfImage_in_frame is the little part of the frame that has the sizes of
            # rectangles that covers the faces
            rectangleOfImage_in_frame = frame[y:y + h, x:x + w]

            # the eyes in the rectangle which covers the detected face, will be detected and the rectangles will be
            # drawn aon the rectangleOfImage_in_frame to specify eyes.
            eyes = eye_detection.detectMultiScale(rectangleOfImage, scaleFactor=1.3, minNeighbors=5)
            for (ex, ey, ew, eh) in eyes:
                cv2.rectangle(rectangleOfImage_in_frame, (ex, ey), (ex + ew, ey + eh), (100, 0, 0), 4)

        cv2.imshow('frame', frame)

        # terminates the frame if user clicks "a"
        if cv2.waitKey(1) == ord('a'):
            break
        if len(faces) > 0:
            detected = True

    capture.release()
    cv2.destroyAllWindows()

    return detected


def isDetected():
    if detectFaceAndEyes():
        print("A human was detected")
    else:
        print("No human was detected around.")


isDetected()