import os
import cv2

cap = cv2.VideoCapture(0)
count = 0

save_dir = os.path.join(os.getcwd(), "dataset_of_people", "Lale")

if not os.path.exists(save_dir):
    os.makedirs(save_dir)

while True:
    ret, frame = cap.read()

    if ret:
        cv2.imshow('Capture', frame)
        save_path = os.path.join(save_dir, f"{count}.jpg")
        cv2.imwrite(save_path, frame)
        print(f"Image {count} saved to {save_path}")
        count += 1

    if count == 50:
        break

    if cv2.waitKey(1) == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()

