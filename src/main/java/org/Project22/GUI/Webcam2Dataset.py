import sys
import cv2

def capture_images(username):
    cap = cv2.VideoCapture(0)

    for i in range(10):
        ret, frame = cap.read()

        cv2.imshow('Webcam Capture', frame)

        file_name = f"/Users/lalekosucuozgen/Documents/GitHub/Project-2-2-Group-9/src/main/java/org/Project22/FaceRecognition/dataset_of_people/{username}.jpg"
        cv2.imwrite(file_name, frame)

        cv2.waitKey(500)

    cap.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    username = sys.argv[1]
    capture_images(username)

