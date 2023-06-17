package org.Project22;

import org.Project22.GUI.MenuWindow;
import org.Project22.GUI.UI;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Main {
    
   // answer generator declaration
   public static AnswerGenerator answerGenerator;

   // UI interface
   public static UI ui;
   public static MenuWindow menu;

    public static void main(String[] args) {
        loadSkills();
        menu = new MenuWindow();
    }

    public static void loadSkills() {
        File masterFile = new File("resources/SkillFiles");
        List<Question> questions = new ArrayList<>();
        String[] fileNames = masterFile.list();
        try {
            for (String filename: fileNames) {
                questions.add(new SkillHandle(filename).returnQuestion());
            }
        } catch (IOException e) {e.printStackTrace(); System.exit(0);}
        answerGenerator = new AnswerGenerator(questions);
    }

    // HOW TO RUN PYTHON SERVER:
    // 1. Open a new terminal
    // 2. CD to the LargeLanguageModel folder
    // 3. Type "uvicorn main:app --reload"
    // 4. Wait for the message "Application startup complete." to appear
}
