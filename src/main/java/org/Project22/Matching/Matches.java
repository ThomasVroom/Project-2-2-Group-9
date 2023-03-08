package org.Project22.Matching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Matches implements MatchInterface{
    @Override
    public float matchesBasic(String userQuestion, String cleanQuestion, String patternQuestion) {
        if (Pattern.matches(patternQuestion,userQuestion))
            return 1.0f;
        return 0.0f;
    }

    @Override
    public float matches(String userQuestion, String cleanQuestion, String patternQuestion) {
        float result = 0;
        String[] userWords = userQuestion.toLowerCase().split(" ");
        ArrayList<String> skillWords = new ArrayList<>(Arrays.asList(cleanQuestion.toLowerCase().split(" ")));
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
