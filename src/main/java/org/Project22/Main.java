package org.Project22;

import org.Project22.GUI.UI;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {

   // answer generator declaration
   public static AnswerGenerator answerGenerator;

   // UI interface
   public static UI ui;

    public static void main(String[] args) {
        loadSkills();
        ui = new UI();
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
}
