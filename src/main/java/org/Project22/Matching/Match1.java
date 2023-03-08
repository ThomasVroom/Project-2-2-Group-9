package org.Project22.Matching;

import org.Project22.Question;

import java.util.regex.Pattern;

public class Match1 implements MatchingInterface{
    @Override
    public float Matching(String userQuestion, Question question) {
        if (Pattern.matches(question.patternQuestion,userQuestion))
            return 1.0f;
        return 0.0f;
    }
}
