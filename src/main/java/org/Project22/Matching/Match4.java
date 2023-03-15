package org.Project22.Matching;

import org.Project22.Question;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Match4 implements MatchingInterface{

    private Map<String, Boolean> stopWordsTable;

    @Override
    public float Matching(String userQuestion, Question question) {
        StopWordsHashTable();
        float result;
        int matchingWords = 0;
        int totalWords = 0;
        float consistentParametersRate = (float) question.getVariable2(userQuestion).size()/question.variablesWithAddedLocation.size();

        userQuestion = removePunctuation(userQuestion.toLowerCase()); 
        ArrayList<String> userWords = new ArrayList<>(Arrays.asList(userQuestion.toLowerCase().split(" ")));
        ArrayList<String> skillWords = new ArrayList<>(Arrays.asList(question.cleanQuestion.toLowerCase().split(" ")));

        for (String word : skillWords) {
            if (isStopWord(word) == false && !word.equals(" ") && !word.equals("")) {
                totalWords += 1;
                for (String userWord : userWords) {
                    if (StringSimilarity(word, userWord) > 0.8) {
                        matchingWords += 1;
                    }
                }
            }
        }
        result = (float) (matchingWords/totalWords) * 0.5f;
        result += (float)(0.5f * consistentParametersRate);
        return result;
    }
    // Read stop words from the file and store them in a hash table
    private void StopWordsHashTable() {
        stopWordsTable = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/org/Project22/Matching/stopwords.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    stopWordsTable.put(word.toLowerCase(), true);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Check if a word is a stop word or not
    private boolean isStopWord(String word) {
        return stopWordsTable.containsKey(word.toLowerCase());
    }

    // Calculate the similarity between two strings (using Levenshtein distance)
    private float StringSimilarity(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
     
        int[][] distance = new int[len1 + 1][len2 + 1];
     
        for (int i = 0; i <= len1; i++) {
            distance[i][0] = i;
        }
        for (int j = 1; j <= len2; j++) {
            distance[0][j] = j;
        }
     
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                distance[i][j] = Math.min(Math.min(distance[i - 1][j] + 1, distance[i][j - 1] + 1), distance[i - 1][j - 1] + cost);
            }
        }
     
        int maxLen = Math.max(len1, len2);
        double similarityRate = (double) (maxLen - distance[len1][len2]) / maxLen;

        return (float) similarityRate;
        
    }

    // Remove punctuation from a string
    private String removePunctuation(String text) {
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetterOrDigit(c) || Character.isWhitespace(c)) {
                result += c;
            }
        }
        return result;
    }
}