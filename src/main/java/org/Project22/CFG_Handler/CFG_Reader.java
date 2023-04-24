package org.Project22.CFG_Handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CFG_Reader {


    public static void reader(){
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/CFG.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.contains("rule") || line.contains("Rule")){

                    // TODO create a function parseRULE

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void parseRULE(String input){


    }
}
