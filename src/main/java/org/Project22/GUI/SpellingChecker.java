package org.Project22.GUI;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.Project22.Main;

public class SpellingChecker {
    
    /**
     * Check the spelling of a sentence.
     * @param input the sentence to check
     * @return an array containing the start- and end-index of misspelled words.
     */
    public static int[][] checkSpelling(String input) {
        // create lists
        List<int[]> indeces = new ArrayList<int[]>();
        List<String> misspelledWords = new ArrayList<String>();

        // split input into words
        String[] words = input.split(" ");

        // load the dictionary into memory (370k lines!!)
        // source: https://github.com/dwyl/english-words
        String[] dictionary = null;
        try {
            List<String> dict_list = Files.readAllLines(Paths.get("resources/dictionary.txt"), StandardCharsets.UTF_8);
            dictionary = dict_list.toArray(new String[dict_list.size()]);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // check the spelling of each word
        for (String word : words) {
            if (Arrays.binarySearch(dictionary, word) < 0) {
                if (!Main.ui.getVariables().contains(word)) {
                    misspelledWords.add(word);
                }
            }
        }

        // find indeces
        for (String word : misspelledWords) {
            int lastIndex = 0;
            while (lastIndex != -1) { // find all occurences
                lastIndex = input.indexOf(word, lastIndex);

                // if an index was found
                if (lastIndex != -1) {

                    // if the index is not a substring, add it to the list
                    if ((lastIndex == 0 || input.charAt(lastIndex - 1) == ' ') && 
                    (lastIndex == input.length() - word.length() || input.charAt(lastIndex + word.length()) == ' ')) {
                        indeces.add(new int[] {lastIndex, lastIndex + word.length()});
                    }
                    lastIndex += word.length();
                }
            }
        }

        // convert to array
        int[][] output = new int[indeces.size()][2];
        return indeces.toArray(output);
    }
}
