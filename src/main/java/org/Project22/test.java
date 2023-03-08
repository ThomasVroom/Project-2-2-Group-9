package org.Project22;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    List<Tuple<String,List<String>>> answers = new ArrayList<>();
    Tuple<String, List<String>> question;

    public static boolean DEBUG = true;

    public void SkillHandle(String filename) throws IOException{
        //TODO read file for answers

        BufferedReader br = new BufferedReader(new FileReader(filename));

        // retrieve placeholders name

        ArrayList<String> placeholders = new ArrayList<>();

        String first_line = br.readLine();

        ArrayList<Integer> start = new ArrayList<>();
        ArrayList<Integer> end = new ArrayList<>();


        for (int target_index = first_line.indexOf("<"); target_index >= 0; target_index = first_line.indexOf("<", target_index+1)){
            start.add( (Integer) target_index);
        }

        for (int target_index = first_line.indexOf(">"); target_index >= 0; target_index = first_line.indexOf(">", target_index+1)){
            end.add( (Integer) target_index);
        }

        for(int index = 0; index < start.size(); index++){
            String placeholder = first_line.substring(start.get(index)+1, end.get(index));

            if (DEBUG)
                System.out.println(placeholder);

            placeholders.add(placeholder);
        }

        // Retrieve question

        question = new Tuple(first_line, (List) placeholders);

        // Retrieve placeholders data
        ArrayList<String> all_data = new ArrayList<>();

        for(int i = 0; i< placeholders.size(); i++){

            String line;

            int counter = 0;

            while ((line = br.readLine()) != null)   {

                if(counter == 2){
                    break;
                }

                int start2 = line.indexOf("<"); // what does it return if not found
                int end2 = line.indexOf(">"); // what does it return if not found

                if (start2 > 0 || end2 > 0){

                    String placeholder = line.substring(start2+1,end2);
         
                    if (placeholders.get(i) == placeholder){
                        String data = line.substring(end2+2,line.length()-1);
                        all_data.add(data);
                    }
                    else{
                        all_data.add("s");
                    }
                }
                else{
                    counter++;
                } 

                if (DEBUG)
                    System.out.println(all_data.toString());

                // retrieve question

                // retrieve answers
            
            }
        }
        br.close();
    }

    public static void main (String[] args) throws IOException{

        if(DEBUG)
            System.out.println(new File("resources/SkillFiles/drinks.txt").getAbsolutePath()); // returns path of drinks.txt

        test test = new test();

        test.SkillHandle("resources/SkillFiles/drinks.txt");

    }

}
