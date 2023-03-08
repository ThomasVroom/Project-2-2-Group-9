package org.Project22;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextHandler {
     
    public TextHandler(){

    } 

    public static void writeStringToFile(String filePath, String content) throws IOException {
        
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

        //The content can be changed in desired format
        bufferedWriter.write(content); 

        bufferedWriter.close();
    }

    public static String readFileToString(String filePath) throws IOException {
        
        FileReader fileReader = new FileReader(filePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
    
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.lineSeparator());
        }
    
        bufferedReader.close();
        fileReader.close();
    
        return stringBuilder.toString();
    }

    public static String removeSubstring(String originalString, String substringToRemove) {
        // Replacing all occurrences of the substring with an empty string 
        String resultString = originalString.replaceAll(substringToRemove, "");
        return resultString;
    }
}
