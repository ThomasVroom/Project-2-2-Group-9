package org.Project22;

import java.util.ArrayList;
import java.util.List;

public class SkillHandle {
    List<Tuple<String, List <Tuple<Integer,String>>>> answers = new ArrayList<>();
    //Tuple<String, List<String>> question;

    public SkillHandle(String filename){
        //TODO read file for answers
    }
    public String AskQuestion(Tuple<String,List<String>> question){
        for (Tuple<String,List<Tuple<Integer,String>>> answer: answers) {
            if(answer.y().equals(question.y())){
                return answer.x();
            }
        }
        return "no answer found";
    }

}
