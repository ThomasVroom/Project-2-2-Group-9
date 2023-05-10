package org.Project22.Matching;

import org.Project22.Question;
import org.Project22.Tuple;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Match4 implements MatchingInterface{

    // ----- Weights -----
    private double LevenshteinDistance = 0.8; // minimum similarity between words
    private double relevantWordsWeight = 0.3; // weight of the number of relevant words
    private double consistentParametersWeight = 0.7; // weight of the number of consistent parameters
    // ----- Weights -----

    private Map<String, Boolean> stopWordsTable; // Hash table of stop words
    
    @Override
    public float Matching(String userQuestion, Question question) {
        StopWordsHashTable();
        double result;
        int matchingWords = 0;
        int totalWords = 0;
        userQuestion = removePunctuation(userQuestion);
        double consistentParametersRate = (double) getVariable(userQuestion, question).size()/question.variablesWithAddedLocation.size();

        userQuestion = userQuestion.toLowerCase();
        ArrayList<String> userWords = new ArrayList<>(Arrays.asList(userQuestion.toLowerCase().split(" ")));
        ArrayList<String> skillWords = new ArrayList<>(Arrays.asList(question.cleanQuestion.toLowerCase().split(" ")));

        for (String word : skillWords) {
            if (isStopWord(word) == false && !word.equals(" ") && !word.equals("")) {
                totalWords += 1;
                for (String userWord : userWords) {
                    if (StringSimilarity(word, userWord) >= LevenshteinDistance) {
                        matchingWords += 1;
                    }
                }
            }
        }
        result = ((double) matchingWords / totalWords) * relevantWordsWeight;
        result += (consistentParametersRate * consistentParametersWeight);
        return (float) result;
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
        float similarityRate = (float) (maxLen - distance[len1][len2]) / maxLen;

        return similarityRate;
        
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

    private List<Tuple<String,String>> getVariable(String userQuestion, Question question){
        List<String> userWords = Arrays.asList(userQuestion.split(" "));
        List<Tuple<String,String>> result = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        for (String word: userWords) {
            for (Tuple<String,String> placeholder: question.placeholders) {
                if(StringSimilarity(word, placeholder.y().toLowerCase()) >= LevenshteinDistance && !categories.contains(placeholder.x())){
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
}