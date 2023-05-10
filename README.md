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

1. Open this repository. Make sure you have java, python, opencv and gradle installed.
2. Launch the application by running the main class located in Main.java.

3. Press the `authentication` button to start the authentication process:
  - The assistant will access your webcam to see if there is a human in front of it,
  - If the assistant finds a human you will be granted access to the chat.
  - If you don't have a webcam press the `No access to webcam` button and enter the password `XXXXX`.

4. Talk to our digital assistant by typing in the chat box, then press the Enter key to send your message.
5. To define a new skill, open the skill editor and specify a skill based on the template-based format, for example:

```
Question How do I get from <Place> to <Place> at <Time>?

Slot <Place> Maastricht
Slot <Place> Heerlen
Slot <Place> Sittard

Slot <Time> 9am
Slot <Time> 11am

Action <Place> Maastricht <Place> Heerlen <Time> 9am By train.
Action <Place> Maastricht <Place> Heerlen <Time> 11am By car.
Action <Place> Sittard <Place> Maastricht <Time> 11am By bike.
```

Note that:
- Key-words in the skill template cannot have any special characters, and is not case-sensitive.
- After defining a new skill, press the 'Clear Chat' button to reload all existing skills.

### Notes

- Gradle instructions:
  - make sure you have [gradle](https://gradle.org/) installed
  - open a terminal and navigate to this repository
  - to run the application, type `gradle run` in the terminal (starting up might take a while)

- Java Runtime Environment must be needed to launch the application.
  - https://www.java.com/nl/

- Python and the OpenCV library must be needed to use the webcam functionality:
  - https://www.python.org/
  - https://pypi.org/project/opencv-python/

- Citation of code is mentioned in comments throughout the source code.
