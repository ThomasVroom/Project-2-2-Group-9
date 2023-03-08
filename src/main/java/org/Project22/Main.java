package org.Project22;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String[] fileNames = new String[]{"activities.txt","drinks.txt","food.txt","lectures.txt","services.txt","theater.txt"};
        List<SkillHandle> skills = new ArrayList<>();
        List<Question> questions = new ArrayList<>();
        for (String filename: fileNames) {
            skills.add(new SkillHandle(filename));
        }
        for (SkillHandle skill:skills) {
            questions.add(skill.returnQuestion());
        }
        System.out.println(matchQuestion("what things are available at the park while its monday",questions));
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
