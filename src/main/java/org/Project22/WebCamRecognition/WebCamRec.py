import cv2
import numpy as np

capture = cv2.VideoCapture(0)

# pre=trained data of HAAR is used:
face_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_detection = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

while True:
    # image is the numpy array that represents the image.
    # ret indicates whether the capture is done successfully.
    ret, frame = capture.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)  # the algo requires gray scale image to perform classification.

    # the second input (1.3) is the scale factor for the case of the pixels of the image are high then the size of
    #   the pixels are reduced by the scale factor -30% in this case-. Using small reduce size decreases the risk of
    #   missing the detection of faces however it makes algo slower. Using bigger scale factor has the reverse effect.
    # the third input - minNeighbors - (5) is the parameter specifying how many neighbors each candidate rectangle
    #   should have to retain it. Higher value = fewer detections but higher quality. Value between 3-6 is good.
    faces = face_detection.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        # the rectangle draws rectangles which covers the faces in the image.
        # image, top left corner, the bottom right, color, thickness
        cv2.rectangle(frame, (x, y), (x + w, y + h), (50, 200, 0), 4)

        roi_gray = gray[y:y+w, x:x+w]
        roi_color = frame[y:y + h, x:x + w]
        eyes = eye_detection.detectMultiScale(roi_gray, 1.3, 5)
        for(ex, ey, ew, eh) in eyes:
            cv2.rectangle(roi_color, (ex, ey), (ex + ew, ey + eh), (100, 0, 0), 4)

    cv2.imshow('frame', frame)

    # terminates the frame if user clicks "a"
    if cv2.waitKey(1) == ord('a'):
        break

capture.release()
cv2.destroyAllWindows()
