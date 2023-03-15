package org.Project22;

import org.Project22.GUI.UI;
import org.Project22.Matching.Match2;
import org.Project22.Matching.Match1;
import org.Project22.Matching.MatchingInterface;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
   public static AnswerGenerator answerGenerator;

   public static UI ui;
    public static void main(String[] args) throws IOException {
        File masterFile = new File("resources/SkillFiles");
        List<Question> questions = new ArrayList<>();
        String[] fileNames = masterFile.list();
        for (String filename: fileNames) {
            questions.add(new SkillHandle(filename).returnQuestion());
        }
        answerGenerator = new AnswerGenerator(questions);
        ui = new UI();
    }
}
