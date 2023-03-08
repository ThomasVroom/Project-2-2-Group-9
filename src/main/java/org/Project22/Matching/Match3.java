package org.Project22.Matching;

import org.Project22.Question;

public class Match3 implements MatchingInterface {


    @Override
    public float Matching(String userQuestion, Question question) {
        float result = new Match2().Matching(userQuestion,question);
        result += (float) question.getVariable2(userQuestion).size()/question.variablesWithAddedLocation.size();
        return result/2;
    }
}
