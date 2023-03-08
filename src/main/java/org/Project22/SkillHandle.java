package org.Project22;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SkillHandle {
    List<Tuple<String,List<String>>> answers = new ArrayList<>();
    // Tuple<String, List<String>> question;

    public SkillHandle(String filename) throws FileNotFoundException{
        //TODO read file for answers - question & placeholders
        FileInputStream fstream = new FileInputStream("textfile.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        
        // Select answers and place them in a list first
        
        


    }
    public String AskQuestion(Tuple<String,List<String>> question){
        for (Tuple<String,List<String>> answer: answers) {
            if(answer.y().equals(question.y())){
                return answer.x();
            }
        }
        return "no answer found";
    }

}
