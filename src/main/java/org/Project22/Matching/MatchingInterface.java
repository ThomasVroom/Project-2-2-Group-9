package org.Project22.Matching;

import org.Project22.Question;

public interface MatchingInterface {

        /**
        * @param userQuestion string representing the user question
        * @param question object from question class, typical question
        * @return float number : confidence of similarity between userQuestion and question
        */
        float Matching(String userQuestion, Question question);
}
