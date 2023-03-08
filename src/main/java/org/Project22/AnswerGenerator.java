package org.Project22;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnswerGenerator {
    List<Question> questions;
    public AnswerGenerator(List<Question> questions, int... algorithmChoice){
        this.questions = questions;
    }
    public String getAnswer(String userString){
        int indexQuestion = matchQuestion(userString,questions).get(0).x();
        return questions.get(indexQuestion).getAnswer(questions.get(indexQuestion).getVariable2(userString));
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
