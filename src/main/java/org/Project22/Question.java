package org.Project22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
public class Question {
    //used for identifying the skill (Debug/convenience) purposes only
    String name;
    //patternQuestion is the string of the skill with .* at the places of the variable
    String patternQuestion;
    //cleanQuestion is string of the skill with nothing at the places of the variable
    String cleanQuestion;
    List<Tuple<Integer,String>> variablesWithAddedLocation;
    List<String> variables;
    //these are thing like (<DAY>, monday) or (<TIME>, 8:00)
    List<Tuple<String,String>> placeholders;
    // this looks like {(answer1,{(<DAY>, monday),(<TIME>, 9:00)}),(answer2,{(<DAY>, friday),(<TIME>, 11:00)})}
    List<Tuple<String,List<Tuple<String,String>>>> answers;
    //example input: what weather is it on <Name1> at <Name2> thanks
    public Question(String rquestion,List<Tuple<String,String>> placeholders, List<Tuple<String,List<Tuple<String,String>>>> answers,String name){
        variablesWithAddedLocation = new ArrayList<>();
        String fquestion = rquestion;
        while(rquestion.contains("<")){
            variablesWithAddedLocation.add(new Tuple(rquestion.indexOf("<"),rquestion.substring(rquestion.indexOf("<")+1,rquestion.indexOf(">"))));
            rquestion = rquestion.replaceAll(rquestion.substring(rquestion.indexOf("<"),rquestion.indexOf(">")+1), "");
        }
        cleanQuestion = fquestion;
        while (fquestion.contains("<")){
            fquestion = fquestion.replaceAll(fquestion.substring(fquestion.indexOf("<"),fquestion.indexOf(">")+1), ".*");
        }
        while (cleanQuestion.contains("<")){
            cleanQuestion = cleanQuestion.replaceAll(cleanQuestion.substring(cleanQuestion.indexOf("<"),cleanQuestion.indexOf(">")+1), "");
        }
        patternQuestion = fquestion;
        this.placeholders = placeholders;
        this.answers = answers;
        this.name = name;
    }

    public float Matches(String userQuestion) {
        if (Pattern.matches(patternQuestion,userQuestion))
            return 1.0f;
        return 0.0f;
    }
    public float Matches2(String userQuestion) {
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
    public float Matches3(String userQuestion){
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
        result += (float) getVariable2(userQuestion).size()/variablesWithAddedLocation.size();
        return result/2;
    }
    public List <Tuple<Integer,String>> getVariable(String userQuestion) {
        if (Matches(userQuestion) < 0.5) //float stuff don't worry about it
            throw new RuntimeException();
        List <Tuple<Integer,String>> returnList = new ArrayList<>();
        for (int i = 0; i < variablesWithAddedLocation.size(); i++) {
            returnList.add(new Tuple<>(i,userQuestion.substring(variablesWithAddedLocation.get(i).x(),userQuestion.indexOf(" " , variablesWithAddedLocation.get(i).x()))));
            userQuestion = userQuestion.replaceAll(userQuestion.substring(variablesWithAddedLocation.get(i).x(),userQuestion.indexOf(" " , variablesWithAddedLocation.get(i).x())),"");
        }
        return returnList;
    }

    // return all the variables in a string as list of tuples like : {(DAY,monday), (TIME,8:00)}
    public List<Tuple<String,String>> getVariable2(String userQuestion){
        List<String> userWords = Arrays.asList(userQuestion.split(" "));
        List<Tuple<String,String>> result = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (String word: userWords) {
            for (Tuple<String,String> placeholder: placeholders) {
                if(word.equals(placeholder.y().toLowerCase()) && !categories.contains(placeholder.x())){
                    result.add(placeholder);
                    categories.add(placeholder.x());
                    break;
                }
                else if (placeholder.y().contains(word) && !categories.contains(placeholder.x()) && !word.equals("")){
                    String addedwords = "";
                    for (int i = userWords.indexOf(word)+1; i < userWords.size(); i++) {
                        addedwords += " "+userWords.get(i);
                        if (placeholder.y().equals(word+addedwords)){
                            result.add(placeholder);
                            categories.add(placeholder.x());
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }
    public String getAnswer(List<Tuple<String,String>> variables){
        boolean breaked = false;
        for (Tuple<String,List<Tuple<String,String>>> answer : answers) {
            breaked = false;
            for (Tuple<String,String> variableOfAnswer: answer.y()) {
                if (!variables.contains(variableOfAnswer) || answer.y().size() != variables.size()){
                    breaked = true;
                    break;
                }
            }
            if (breaked == false){
                return answer.x();
            }
        }
        return "No answer found :(";
    }
}
