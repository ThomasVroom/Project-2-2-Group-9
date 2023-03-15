package org.Project22;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextHandler {
     
    public TextHandler(){} 

    /**
     * @param filePath string of the file's path : /resources/"filename"
     * @param content content that needs to be written inside the chosen .txt file
     * @throws IOException
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

        //The content can be changed in desired format
        bufferedWriter.write(content); 

        bufferedWriter.close();
    }

    /**
     * @param filePath string of the file's path : /resources/"filename"
     * @return a string obtained from reading the whole file
     * @throws IOException
     */
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

    /**
     * @param originalString initial string
     * @param substringToRemove substring to remove from the initial string
     * @return initial string without all the occurences of the substringtoRemove
     */
    public static String removeSubstring(String originalString, String substringToRemove) {
        // Replacing all occurrences of the substring with an empty string 
        String resultString = originalString.replaceAll(substringToRemove, "");
        return resultString;
    }
}
