package org.Project22;

import org.Project22.GUI.UI;

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

    /**
     * @param userString question of the user
     * @param Questions list of question aka skills of the chatbot
     * @return an ordered list with (index of question,algorithm used + name of question, matches percentage)
     */
    public static List<Triple<Integer,String,Float>> matchQuestion(String userString, List<Question> Questions, int... algorithm){
        List<Triple<Integer,String,Float>> result = new ArrayList<>();
        int choice = 2;
        if (algorithm.length >= 1){
             choice = algorithm[0];
        }

        for (Question question : Questions) {
            if (choice == 0)
                result.add(new Triple<>(Questions.indexOf(question),"matches1.0 ,"+question.name,question.Matches(userString)));
            else if (choice == 1)
                result.add(new Triple<>(Questions.indexOf(question),"matches2.0 ,"+question.name,question.Matches2(userString)));
            else if (choice == 2)
                result.add(new Triple<>(Questions.indexOf(question),"matches3.0 ,"+question.name,question.Matches3(userString)));
            else
                throw new IllegalArgumentException("choose a valid algorithm choice");
        }
        Comparator<Triple<Integer, String, Float>> comparator = new Comparator<Triple<Integer, String, Float>>() {
            @Override
            public int compare(Triple<Integer, String, Float> triple1, Triple<Integer, String, Float> triple2) {
                return Float.compare(triple2.z(),triple1.z());
            }
        };
        Collections.sort(result, comparator);
        return result;
    }

}
