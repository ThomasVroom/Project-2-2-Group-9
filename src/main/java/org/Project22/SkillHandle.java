package org.Project22;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillHandle {
    public String BigString;
    public String filename;
    public SkillHandle(String filename) throws IOException {
        this.filename = filename;
        BigString = TextHandler.readFileToString("resources/SkillFiles/"+filename);
        //System.out.println(BigString);
    }

    public Question returnQuestion(){
        List<String> slots = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        String question = null;
        String[] lines = BigString.split("\n");
        for (String line : lines) {
            if (line.startsWith("question")||line.startsWith("Question"))
                question = line.substring(9);
            else if (line.startsWith("slot")||line.startsWith("Slot"))
                slots.add(line.substring(4));
            else if (line.startsWith("action")||line.startsWith("Action"))
                answers.add(line.substring(7));
            else if (line.trim().length()>0)
                System.out.println("Unrecognised String:" + line);
        }
        List<Tuple<String,String>> slotsExtracted = new ArrayList<>();
        for (String slot:slots) {
            slotsExtracted.add(new Tuple<>(slot.substring(slot.indexOf("<")+1,slot.indexOf(">")).trim().toUpperCase(),slot.substring(slot.indexOf(">")+1).trim()));
        }
        List<Tuple<String,List<Tuple<String,String>>>> answersFinal = new ArrayList<>();
        for (String answer: answers){
            List<Tuple<String,String>> placeholders = getVariable(answer,slotsExtracted);
            while (answer.contains("<")){
                answer = answer.replaceAll(answer.substring(answer.indexOf("<"),answer.indexOf(">")+1), "");
            }
            answersFinal.add(new Tuple<>(answer,placeholders));
        }
        return new Question(question,slotsExtracted,answersFinal,filename.substring(0, filename.lastIndexOf('.')));
    }
    public List<Tuple<String,String>> getVariable(String answer,List<Tuple<String,String>> slotsExtracted){
        List<String> userWords = Arrays.asList(answer.split(" "));
        List<Tuple<String,String>> result = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (String word: userWords) {
            for (Tuple<String,String> slot: slotsExtracted) {
                if(word.equals(slot.y()) && !categories.contains(slot.x())){
                    result.add(slot);
                    categories.add(slot.x());
                    break;
                }
            }
        }
        return result;
    }
}
