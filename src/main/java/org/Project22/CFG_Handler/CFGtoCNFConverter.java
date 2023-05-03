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

    private final static boolean DEBUG = true; // Debug variable
    
    public static void main(String[] args) {
        readRulesFromFile();
        convertRulesToCNF();
    }

    //////////////////////////
    // Analyzing Skill File //
    //////////////////////////

    // Adds rules to rulesList if it is a rule
    private static void readRulesFromFile(){
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/CFG.txt"))) {

            String line;

            while ((line = br.readLine()) != null) {
                if (line.contains("rule") || line.contains("Rule")) {

                    // Parse the line to create a Rule object
                    Rule rule = parseRule(line);

                    // Add the Rule object to the rulesList
                    rulesList.add(rule);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Parses the rule in appropriate format
    private static Rule parseRule(String line) {

        // Split the line into parts separated by whitespace
        String[] parts = line.split("\\s+"); // Split on whitespace : " "

        // The first part is the lhs
        String lhs = parts[1];

        /*if (DEBUG){
            System.out.println("parts: " + Arrays.toString(parts));
            System.out.println("lhs: " + lhs);
        }*/

        // The rest of the parts are the rhs
        List<List<String>> rhs = new ArrayList<>();
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
        
        
        if (DEBUG){
            System.out.println("lhs: " + lhs + " rhs: " + rhs);
        }
        
        return new Rule(lhs, rhs);
    }


    /////////////////////////////
    // Main Conversion Program //
    /////////////////////////////

    private static void convertRulesToCNF() {
        // Step 2
        // removeEmptyStringRules(); // Rules of the form A -> ""

        // Step 3 (Can also be named : remove all unit productions rules)
        // replaceSingleNonterminalRules(); // Rules of the form A -> B

        //Step 4
        //convertTerminals()
    }

    // Step 4

    
}
