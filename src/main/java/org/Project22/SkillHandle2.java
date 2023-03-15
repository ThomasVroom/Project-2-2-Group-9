package org.Project22;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SkillHandle2 {

    List<Tuple<String,List<String>>> answers = new ArrayList<>();
    
    private Question question = null;

    String skillID = "";

    private String skillQuestion = new String();
    private List<Tuple<String,String>> slots = new ArrayList<>();
    private List<Tuple<String,List<Tuple<String,String>>>> skillAnswers = new ArrayList<>();

    /**
     * @param filename string name of the skill file to read
     */
    public SkillHandle2(String filename){
        this.skillID = filename.substring(0, filename.lastIndexOf('.'));

        try {
            ReadLines(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.question = new Question(skillQuestion, slots, skillAnswers,filename);
    }

    /**
     * @return getter function to return question object
     */
    public Question getQuestion(){
        return this.question;
    }

    /**
     * @param filename string name of the skill file to read
     * @throws FileNotFoundException
     * @throws IOException
     * parses every line in the skill file according to the ParseLine method
     */
    private void ReadLines(String filename) throws FileNotFoundException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ParseLine(line);
            }
            reader.close();
        }
    }

    /**
     * @param line string representing the current line of the file
     */
    private void ParseLine(String line){

        if (line.length() < 6) {
            System.out.println("Invalid line was ignored");
        }
        else

        if (line.substring(0, 8).equals("Question")) {
            this.skillQuestion = ParseQuestion(line);
        }

        else if (line.substring(0, 6).equals("Answer")) {
            this.skillAnswers.add(ParseAnswer(line));
        }

        else if (line.substring(0, 4).equals("Slot")) {
            this.slots.add(ParseSlot(line));
        }
        else{
            System.out.println("Invalid line was ignored");
        }

    }

    /**
     * @param line string representing the current line of the file
     * @return returns the typical question from the file
     */
    private String ParseQuestion(String line){
        String question = line.substring(9);
        return question;
    }

    /**
     * @param line string representing the current line of the file
     * @return the placeholder slot parsed with <Placeholder name, Placeholder value>
     */
    private Tuple<String,String> ParseSlot(String line){

        line = line.substring(5);
        String[] lineElements = line.split(">");
        String firstString = lineElements[0].substring(1).trim();
        String secondString = lineElements[1].trim();
        Tuple<String,String> result = new Tuple<String,String>(firstString,secondString);

        return result;
    }

    /**
     * @param line string representing the current line of the file
     * @return a tuple with <answer1, list(tuple<placeholder name, placeholder value>) : for each answer, the placeholders and the value that goes with them extracted from the file.
     */
    private Tuple<String,List<Tuple<String,String>>> ParseAnswer(String line){
        line = line.substring(7);

        String[] lineWords = line.split(" ");
        String resultString = "";
        List<Tuple<String,String>> parametersList = new  ArrayList<>();

        for(int i = 0; i < lineWords.length; i++){
            if(lineWords[i].startsWith("<")){
                Tuple<String,String> tuple = new Tuple<String,String>(lineWords[i].substring(1, lineWords[i].length() - 1), lineWords[i+1]);
                parametersList.add(tuple);
                i++;
            }
            else{
                resultString += (lineWords[i] + " ");
            }
        }
        return new Tuple<String,List<Tuple<String,String>>>(resultString,parametersList);
    }
}
