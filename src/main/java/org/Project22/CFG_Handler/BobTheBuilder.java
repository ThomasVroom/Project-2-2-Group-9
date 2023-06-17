package org.Project22.CFG_Handler;

import java.util.*;

import org.Project22.Tuple;
import org.Project22.CFG_Handler.CockeYoungerKasami.Rule;

public class BobTheBuilder {
    static List<Tuple<String, String>> placeholderList = new ArrayList<>();
    //static List<List<String>> globalList = new ArrayList<>();
    public static String topic = "";
    public static void main(String[] args) {

        CFGtoCNFConverter converter = new CFGtoCNFConverter();
        List<Rule> rules = converter.getRulesCNF();

        String input = "how do i get to spacebox";
        String[] inputWords = input.split(" ");

        boolean found = iterateRules(inputWords, rules);

        if(found){
            System.out.println("Found " + topic);
        }
        else{
            System.out.println("Not Found");
        }
        
    }

    public static List<Tuple<String, String>> getPlaceholders(List<Rule> rules, String input){
        placeholderList.clear();
        input = " " + input + " ";

        // For each rule
        for (Rule r : rules) {

            String lhs = r.getLhs();

            // Find the ones that don't start with <X
            if ( !lhs.startsWith("<X") && !lhs.startsWith("<LOCATION") && !lhs.startsWith("<SCHED") ) {

                List<List<String>> combinations = generateCombinations(r, rules);

                // For each combination
                for (List<String> combination : combinations) {

                    // Join the words into a single string
                    String combinationString = combination.get(0).toLowerCase();
                    //System.out.println(combinationString);

                    // Check if the combination is a substring of the sentence
                    if (input.toLowerCase().contains(" "+combinationString+" ")) {
                    
                        Tuple<String, String> tuple = new Tuple<String, String>(lhs.toLowerCase(), combinationString);
                        placeholderList.add(tuple);
                    }
                }
            }
        
        }
        System.out.println(placeholderList.toString());
        return placeholderList;

    }

    
    public static List<List<String>> generateCombinations(Rule rule, List<Rule> rules) {

        List<List<String>> combinations = new ArrayList<>();

        for (List<String> option : rule.rhs) {

            List<List<String>> optionCombinations = new ArrayList<>();
            optionCombinations.add(new ArrayList<>());

            for (String symbol : option) {

                if (symbol.charAt(0) == '<') {

                    Rule subRule = findRuleByLHS(symbol, rules);
                    List<List<String>> subCombinations = generateCombinations(subRule, rules);
                    optionCombinations = crossJoin(optionCombinations, subCombinations);

                }else {

                    for (List<String> combination : optionCombinations) {

                        combination.add(symbol);
                    }
                }
            }

            // Convert each combination list into a single string
            List<List<String>> stringCombinations = new ArrayList<>();

            for (List<String> combination : optionCombinations) {

                stringCombinations.add(Arrays.asList(String.join(" ", combination)));
            }

            combinations.addAll(stringCombinations);
        }

        return combinations;
    }

    // Cross join of two lists of lists
    private static List<List<String>> crossJoin(List<List<String>> list1, List<List<String>> list2) {

        List<List<String>> result = new ArrayList<>();

        for (List<String> list1Item : list1) {

            for (List<String> list2Item : list2) {

                List<String> joined = new ArrayList<>(list1Item);
                joined.addAll(list2Item);
                result.add(joined);
            }
        }

        return result;
    }

    // Find a rule by its lhs
    public static Rule findRuleByLHS(String lhs, List<Rule> rules) {

        for (Rule rule : rules) {

            if (rule.lhs.equals(lhs)) {

                return rule;
            }
        }

        return null;
    }

    public static boolean iterateRules(String[] inputWords, List<Rule> rules){

        boolean bobBool = true;

        //for each rule create a bobList
        for (Rule rule : rules) {

            if(!rule.getLhs().startsWith("<X")){

                List<List<List<String>>> bob = bob(rule, inputWords, rules);
                
                printBob(bob);
                System.out.println();

                if(!checkBob(bob, inputWords)){
                    bobBool = false;
                }
                
                if(bobBool){
                    topic = rule.getLhs();
                    return true;
                }
            }
            bobBool = true;

        }
        return false;
    }

    //given a rule build a 2D list that represents all the possible sentences of that rule
    public static List<List<List<String>>> bob(Rule rule, String[] inputWords, List<Rule> rules) {
        
        List<List<List<String>>> bobList = new ArrayList<List<List<String>>>();

        //for each list in the given rule
        for(List<String> list : rule.getRhs()){

            List<List<String>> possibleSentences = new ArrayList<List<String>>();
            possibleSentences.addAll(buildPossibleSentence(list, rules));    
            bobList.add(possibleSentences);
        }
        return bobList;    
    }

    
    public static List<List<String>> buildPossibleSentence(List<String> list, List<Rule> rules){
        
        List<List<String>> sentence = new ArrayList<List<String>>();

        //for every string in the list
        for (String str : list) {
        
            //iterate every rule
            for(Rule r : rules){

                //if you find the rule we are looking for
                if(r.getLhs().equalsIgnoreCase(str)){

                    //for every list on the right hand side     
                    List<String> word = getPossibleWords(str, rules);
                    sentence.add(word);

                }
            }
        }
        return sentence;
    }

    //lhs can not be values that start with <X
    private static List<String> getPossibleWords(String lhs, List<Rule> rules) {
        
        List<String> words = new ArrayList<>();

        //go thourhg every rule
        for(Rule r : rules){

            //find the one you are looking for
            if(r.getLhs().equalsIgnoreCase(lhs)){

                List<List<String>> rhs = r.getRhs();

                //if its a default placeholder like X11 = Where
                if(rhs.get(0).size() == 1 && rhs.size() == 1){

                    words.add(rhs.get(0).get(0));
                    return words;
                }

                //else if its a non-default placeholder like ROOM
                else if(rhs.get(0).size() == 1){

                    //for every possible value it can get, add it to words list
                    for(int i = 0; i < rhs.size(); i++){

                        words.add(rhs.get(i).get(0));
                        
                    }

                    return words;
                }

                //else if its a placeholder like X40 or X37
                else if(rhs.get(0).size() != 1){

                    words.addAll(specialGetPossibleWords(lhs, rules));
                    return words;
                }
            }
        }
        return null;
    }

    private static List<String> specialGetPossibleWords(String lhs, List<Rule> rules) {
        List<String> result = new ArrayList<>();
        specialGetPossibleWordsHelper(lhs, rules, result, new HashSet<>());
        return result;
    }
    
    private static void specialGetPossibleWordsHelper(String lhs, List<Rule> rules, List<String> result, Set<String> visited) {
        
        visited.add(lhs);
        
        for (Rule rule : rules) {

            if (rule.getLhs().equalsIgnoreCase(lhs)) {

                for (List<String> rhsOption : rule.getRhs()) {

                    for (String symbol : rhsOption) {

                        if (symbol.startsWith("<") && symbol.endsWith(">") && !visited.contains(symbol)) { 

                            // Non-terminal symbol detected
                            specialGetPossibleWordsHelper(symbol, rules, result, visited);

                        } else {
                            // Terminal symbol detected
                            result.add(symbol);
                        }
                    }
                }
            }
        }
    }

    private static boolean checkBob(List<List<List<String>>> bob, String[] inputWords) {
        
        for (List<List<String>> list1 : bob) {

            for (List<String> list2 : list1) {

                for (String value : list2) {

                    for (String inputWord : inputWords) {

                        if (value.equalsIgnoreCase(inputWord)) {

                            // Remove matched word from the inputWords array
                            inputWords = removeElement(inputWords, inputWord);
                            break;
                        }
                    }
                }
            }
        }

        // Check if all words were found in the 3D list
        return inputWords.length == 0;
    }

    private static String[] removeElement(String[] arr, String element) {

        String[] newArr = new String[arr.length - 1];
        int index = 0;

        for (String value : arr) {

            if (!value.equalsIgnoreCase(element)) {

                newArr[index++] = value;
            }
        }
        return newArr;
    }

    public static void printBob(List<List<List<String>>> list) {
        for (List<List<String>> list1 : list) {
            System.out.print("[");
            for (List<String> list2 : list1) {
                System.out.print("[");
                for (String value : list2) {
                    System.out.print(value + ", ");
                }
                System.out.print("], ");
            }
            System.out.println("]");
        }
    }

}
