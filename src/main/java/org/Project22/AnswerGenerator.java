package org.Project22;

import org.Project22.Matching.Match1;
import org.Project22.Matching.Match2;
import org.Project22.Matching.Match3;
import org.Project22.Matching.Match4;
import org.Project22.Matching.MatchingInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnswerGenerator{

    // possible matching algorithms
    public static MatchingInterface matchingAlgorithms[] = {new Match1(),new Match2(),new Match3(),new Match4()};

    //threshold
    public float ConfidenceCutoff = 0.3f;

    //list of questions
    List<Question> questions;

    /**
     * @param questions list of question objects
     * @param algorithmChoice optional parameter, specify the matching algorithm type if needed
     */
    public AnswerGenerator(List<Question> questions, int... algorithmChoice){
        this.questions = questions;
    }

    /**
     * @param userString question of the user
     * @return the appropriate answer for the user question
     */
    public String getAnswer(String userString){
        Triple<Integer, String, Float> question = matchQuestion(userString,questions).get(0);

        Question question2 = questions.get(question.x());
        List<Tuple<String, String>> variables = question2.getVariable2(userString);
        
        Main.ui.setConfidence(question.z());
        Main.ui.setDebugText(question2.cleanQuestion, variables);

        if (question.z()<ConfidenceCutoff)
            return "too low confidence to make a decision";
        return question2.getAnswer(variables);
    }

    /**
     * Method used for debugging and testing returns the corresponding skill for a certain user question
     * @param userString
     * @param algorithm algorithm choice
     * @return null for invalid algorithm choice and for a valid one the question
     */
    public Tuple<Question,Float> getQuestion(String userString, int algorithm) {
        Triple<Integer, String, Float> question = matchQuestion(userString,questions, algorithm).get(0);
        if (question.equals(null))
            return null;
        if (question.z()<ConfidenceCutoff)
            return null;
        Question question2 = questions.get(question.x());
        return new Tuple<Question,Float>(question2,question.z()-matchQuestion(userString,questions, algorithm).get(1).z());
    }

    /**
     * @param userString question of the user
     * @param Questions list of question aka skills of the chatbot
     * @return an ordered list with (index of question,algorithm used + name of question, matches percentage)
     */
    public static List<Triple<Integer,String,Float>> matchQuestion(String userString, List<Question> Questions, int... algorithm){
        List<Triple<Integer,String,Float>> result = new ArrayList<>();
        int choice = 2;
        if (algorithm.length >= 1)
            choice = algorithm[0];
        if (choice >= matchingAlgorithms.length) {
            System.out.println("Invalid algorithm choice");
            return null;
        }
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
