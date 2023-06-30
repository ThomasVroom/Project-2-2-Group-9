# Project-2-2-Group-9

This is the Multi-Modal Digital Assistant of Group 9.

This repository contains all code necessary to run the application,
as well as the full source code of the application.

This application uses [Java Swing](https://en.wikipedia.org/wiki/Swing_(Java)) as graphical engine.

### Group 9
- Shireen Aynetchi
- Rafael Diederen
- Aurelien Giuglaris-Michael
- Thierry Guiot
- Lale Kosucu Ozgen
- Gunes Ozmen Bakan
- Thomas Vroom

### Instructions

1. Open this repository. Make sure you have Java, Python, OpenCV and Gradle installed.
2. Launch the application by running the main class located in Main.java.

3. Press the `authentication` button to start the authentication process:
  - The assistant will access your webcam to see if there is a human in front of it,
  - If the assistant finds a human you will be granted access to the chat.
  - If you don't have a webcam press the `No access to webcam` button and enter the password `XXXXX`.

4. Talk to our digital assistant by typing in the chat box, then press the Enter key to send your message.
5. Note that after selecting a different language model, you need to press the `clear chat` button to reload the chat window.

### Notes

- Gradle instructions:
  - make sure you have [Gradle](https://gradle.org/) installed
  - open a terminal and navigate to this repository
  - to run the application, type `gradle run` in the terminal (starting up might take a while)

- Java Runtime Environment must be needed to launch the application.
  - https://www.java.com/nl/

- Python and the OpenCV library must be needed to use the webcam functionality:
  - https://www.python.org/
  - https://pypi.org/project/opencv-python/
  - Other packages include `numpy`, `mediapipe` and `face_detection`

- To use the neural network the following packages are needed:
  - `tensorflow`
  - `fastapi`
  - `sklearn`
  - `numpy`
  - `bert_score`
  - `transformers`
  - `nltk`

- To use the neural network first do the following steps before launching the main class:
  1. Open a new terminal.
  2. CD to `src\main\java\org\Project22\LargeLanguageModel`.
  3. Type `uvicorn main:app --reload`.
  4. Wait for the message `Application startup complete.` to appear.

- Citation of code is mentioned in comments throughout the source code.
