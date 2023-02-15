package org.Project22;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
public class Question {
    String patternQuestion;
    String skill;
    List<Tuple<Integer,String>> variables;

    //example input: what weather is it on <Name1> at <Name2> thanks
    public Question(String rquestion, String skill){
        variables = new ArrayList<>();
        String fquestion = rquestion;
        while(rquestion.contains("<")){
            variables.add(new Tuple(rquestion.indexOf("<"),rquestion.substring(rquestion.indexOf("<")+1,rquestion.indexOf(">")-1)));
            rquestion = rquestion.replaceAll(rquestion.substring(rquestion.indexOf("<"),rquestion.indexOf(">")+1), "");
        }
        while (fquestion.contains("<")){
            fquestion = fquestion.replaceAll(fquestion.substring(fquestion.indexOf("<"),fquestion.indexOf(">")+1), ".*");
        }
        patternQuestion = fquestion;
        this.skill = skill;
    }

    public boolean Matches(String userQuestion) {
        return Pattern.matches(patternQuestion,userQuestion);
    }
    public List <Tuple<Integer,String>> getVariable(String userQuestion) {
        if (!Matches(userQuestion))
            throw new RuntimeException();
        List <Tuple<Integer,String>> returnList = new ArrayList<>();
        for (int i = 0; i < variables.size(); i++) {
            returnList.add(new Tuple<>(i,userQuestion.substring(variables.get(i).x(),userQuestion.indexOf(" " , variables.get(i).x()))));
            userQuestion = userQuestion.replaceAll(userQuestion.substring(variables.get(i).x(),userQuestion.indexOf(" " , variables.get(i).x())),"");
        }
        return returnList;
    }

}
