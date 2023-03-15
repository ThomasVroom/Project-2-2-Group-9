package org.Project22.Matching;

import org.Project22.Question;

public class Match3 implements MatchingInterface {

    /**
     * @param userQuestion string representing the user question
     * @param question object from question class, typical question
     * @return float number : confidence of similarity between userQuestion and question
     */
    @Override
    public float Matching(String userQuestion, Question question) {
        float result = new Match2().Matching(userQuestion,question);
        result += (float) question.getVariable2(userQuestion).size()/question.variablesWithAddedLocation.size();
        return result/2;
    }
}
