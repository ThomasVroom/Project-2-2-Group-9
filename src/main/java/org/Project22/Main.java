package org.Project22;

import org.Project22.GUI.MenuWindow;
import org.Project22.GUI.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
}
