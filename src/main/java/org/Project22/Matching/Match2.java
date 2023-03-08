package org.Project22.Matching;

import org.Project22.Question;

import java.util.ArrayList;
import java.util.Arrays;

public class Match2 implements MatchingInterface{
    @Override
    public float Matching(String userQuestion, Question question) {
        float result = 0;
        String[] userWords = userQuestion.toLowerCase().split(" ");
        ArrayList<String> skillWords = new ArrayList<>(Arrays.asList(question.cleanQuestion.toLowerCase().split(" ")));
        int lengthOfSkillWords = skillWords.size();
        for (String word : userWords) {
            if (skillWords.contains(word)) {
                skillWords.remove(skillWords.indexOf(word));
                result += 1f/lengthOfSkillWords;
            }
        }
        return result;
    }
}
