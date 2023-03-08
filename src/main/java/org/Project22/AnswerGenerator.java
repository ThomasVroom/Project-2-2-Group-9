package org.Project22;

import org.Project22.Matching.Match1;
import org.Project22.Matching.Match2;
import org.Project22.Matching.Match3;
import org.Project22.Matching.MatchingInterface;

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
        Triple<Integer, String, Float> question = matchQuestion(userString,questions).get(0);
        Question question2 = questions.get(question.x());
        List<Tuple<String, String>> variables = question2.getVariable2(userString);
        
        Main.ui.setConfidence(question.z());
        Main.ui.setDebugText(question2.cleanQuestion, variables);

        return question2.getAnswer(variables);
    }
    /**
     * @param userString question of the user
     * @param Questions list of question aka skills of the chatbot
     * @return an ordered list with (index of question,algorithm used + name of question, matches percentage)
     */
    public static List<Triple<Integer,String,Float>> matchQuestion(String userString, List<Question> Questions, int... algorithm){
        MatchingInterface matchingAlgorithms[] = {new Match1(),new Match2(),new Match3()};
        List<Triple<Integer,String,Float>> result = new ArrayList<>();
        int choice = 2;
        if (algorithm.length >= 1)
            choice = algorithm[0];
        for (Question question : Questions) {
            result.add(new Triple<>(Questions.indexOf(question),matchingAlgorithms[choice].getClass().getName()+" ,"+question.name,matchingAlgorithms[choice].Matching(userString,question)));
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
