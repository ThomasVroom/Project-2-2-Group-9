package org.Project22;

import java.util.ArrayList;
import java.util.List;

public class SkillHandle {
    List<Tuple<String, List<String>>> answers = new ArrayList<>();
    //Tuple<String, List<String>> question;

    public SkillHandle(){
        //TODO read file for answers
    }
    public String AskQuestion(Tuple<String,List<String>> question){
        for (Tuple<String,List<String>> answer: answers) {
            if(answer.equals(question)){
                return answer.x();
            }
        }
        return "no answer found";
    }

}
