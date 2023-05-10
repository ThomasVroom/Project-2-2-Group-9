package org.Project22.CFG_Handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.Project22.CFG_Handler.CockeYoungerKasami.Rule;

public class CFGtoCNFConverter {

    // Steps to convert a CFG to CNF form

    // CNF form : Either two terminal symbols on the rhs or one nonterminal symbol on the rhs.
    // If there are more than two nonterminal symbols on the rhs, then we need to create new rules
    // to break them down into smaller rules.

    // lhs : left-hand side non-terminal symbol
    // rhs : right-hand side symbols (terminals or non-terminals)

    private static List<Rule> rulesList = new ArrayList<>(); // List of all rules

    private static int newSymbolCounter = 0 ;

    private final static boolean DEBUG = true; // Debug variable

    private static List<Rule> cnf_rules = new ArrayList<>();
    
    public static void main(String[] args) {
        Rule testRule = new Rule("test", Arrays.asList(Arrays.asList(new String[]{"A", "B", "C", "D"}), Arrays.asList(new String[]{"E", "F", "G", "H"}), Arrays.asList(new String[]{"I", "J"}),  Arrays.asList(new String[]{"O", "M"})));
        generateNewRule(testRule);
        //readRulesFromFile();
        //convertRulesToCNF();
        //printRules(rulesList);
    }

    //////////////////////////
    // Analyzing Skill File //
    //////////////////////////

    // Adds rules to rulesList if it is a rule
    private static void readRulesFromFile(){
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/CFG.txt"))) {

            String line;
            List<String> lines= new ArrayList<>();
            while ((line = br.readLine()) != null) {
                
                if (line.contains("rule") || line.contains("Rule")) {
                    lines.add(line);
                }
            }
            parseRule(lines);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parses the rule in appropriate format
    private static List<Rule> parseRule(List<String> lines) {

        // Split the line into parts separated by whitespace
        String[] parts={};
        String newSymbol="";
        for(String line: lines){
            parts = line.split("\\s+"); // Split on whitespace : " "

            // The first part is the lhs
            String lhs = parts[1];

            /*if (DEBUG){
                System.out.println("parts: " + Arrays.toString(parts));
                System.out.println("lhs: " + lhs);
            }*/

            // The rest of the parts are the rhs
            List<List<String>> rhs = new ArrayList<>();
            List<List<String>> newRHS = new ArrayList<>();
            List<String> terminal = new ArrayList<>();
            List<String> element = new ArrayList<>();

            // Skip the arrow part in the string and add every element of the rhs as an element of the list
            for (int i = 3; i < parts.length + 1; i++) {

                if (i == parts.length){
                    rhs.add(element);
                    break;
                }

                if (!parts[i].equals("|")) { // Skip the "|" symbol , delete this line if you want to keep it
                    element.add(parts[i]);
                }

                if (parts[i].equals("|")){
                    rhs.add(element);
                    element = new ArrayList<>();
                }
            }

            for (List<String> sublist : rhs) {        
                    for (int j = 0; j < sublist.size(); j++) {
                        String elem = sublist.get(j);
                        if (!isNonterminalSymbol(elem)) {
                            newSymbol = getNextNewSymbol();
                            terminal=Arrays.asList(elem.split(" "));
                            newRHS.add(terminal);
                            rulesList.add(new Rule(newSymbol, newRHS));
                            sublist.set(j,newSymbol);
                            newRHS= new ArrayList<>();
        
                        } 

                    }

                }

                Rule rule= new Rule(lhs,rhs);
                rulesList.add(rule);
        }

        // if (DEBUG){
        //     System.out.println("lhs: " + lhs + " rhs: " + rhs);
        // }
        
        return rulesList;
                
    }

    public static void printRules(List<Rule> rules) {
        System.out.println();
        for (Rule rule : rules) {
            System.out.print(rule.lhs + " -> ");
            System.out.println((rule.rhs));
        }
        System.out.println();
        System.out.println();

    }
            
    /////////////////////////////
    // Main Conversion Program //
    /////////////////////////////

    private static void convertRulesToCNF(){

        // Step 1
        removeEpsilonRules(); // Delete rules of the form A -> ""

        // Step 2
        removeUnitProductions(); // Delete rules of the form A -> B

        // Step 3 
        //replaceLongProductions(); // Replace rules of the form A -> BCD into A -> BE and E -> CD

        //Step 4
        //convertTerminals()
    }
    private static boolean isNonterminalSymbol(String symbol) {
        return symbol.startsWith("<") && symbol.endsWith(">");
    }
    
    private static String getNextNewSymbol() {
        newSymbolCounter++;
        return "<X" + newSymbolCounter + ">";
    }
    

    // Step 1
    private static void removeEpsilonRules(){

        for (Rule rule : rulesList){ 

            for (List<String> element_list : rule.getRhs()) {

                if (element_list.contains("ε") && rule.getRhs().size() == 1){
                    rulesList.remove(rule);
                    break;
                }
            }
        }

        /*if (DEBUG){
            System.out.println("rulesList: " + rulesList);
        }*/
    }    

    // Step 2
    private static void removeUnitProductions(){
            
            for (Rule rule : rulesList){ 
    
                for (List<String> element_list : rule.getRhs()) {
    
                    if (rule.getRhs().size() == 1 && element_list.size() == 1){
                        rulesList.remove(rule);
                        break;
                    }
                }
            }
    
            /*if (DEBUG){
                System.out.println("rulesList: " + rulesList);
            }*/
    }

    // Step 4
    private static void convertTerminals(){
        for (Rule rule : rulesList){ 

            List<Rule> rule_list_tmp = generateNewRule(rule);

            for (Rule rule_tmp : rule_list_tmp){
                cnf_rules.add(rule_tmp);
            }
        }
        /*if (DEBUG){
            System.out.println("rulesList: " + rulesList);
        }*/
    }

    private static List<Rule> generateNewRule(Rule rule){
        // parameter List<String> element_list
        List<Rule> newRules = new ArrayList();

        generateNewSymbol(rule, newRules);

        List<List<String>> tmpLHS = new ArrayList<>();
        int size = newRules.size();

        List<Rule> finalRules = new ArrayList();

        for (int i = 0; i < size; i++) {
            if (newRules.get(i).lhs.equals(rule.lhs)){
                tmpLHS.add(newRules.get(i).rhs.get(0));
            }
            else{
                finalRules.add(newRules.get(i));
            }
        }
        Rule  tmpRule = new Rule (rule.lhs, tmpLHS);
        finalRules.add(0, tmpRule);

        // printRules(Arrays.asList(rule));
        return finalRules;
    }


    private static void generateNewSymbol(Rule rule, List<Rule> newRules) {
        List<List<String>> elements = rule.rhs;

        for (int i = 0; i < elements.size(); i++) {

            if (elements.get(i).size() > 2) {
                String newRuleName = generateNewX();

                Rule newRule = new Rule(rule.lhs, Arrays.asList(Arrays.asList(new String[]{elements.get(i).get(0), newRuleName})));
                newRules.add(newRule);

                List<String> rhs = elements.get(i);
                rhs = new ArrayList<>(rhs.subList(1, rhs.size()));

                List<List<String>> newRuleRHS = Arrays.asList(rhs);

                generateNewSymbol(new Rule(newRuleName, newRuleRHS), newRules);
            } else {
                Rule newRule = new Rule(rule.lhs, Arrays.asList(elements.get(i)));
                newRules.add(newRule);
            }
        }
    }

    private static String generateNewX(){
        newSymbolCounter ++;
        return "<X" + newSymbolCounter + ">";
    }
}
