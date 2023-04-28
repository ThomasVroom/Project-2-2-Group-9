//import java.util.*;
//
//public class CNFConverter {
//
//    // CNF grammar rules
//    private List<String> cnfRules;
//
//    public CNFConverter() {
//        this.cnfRules = new ArrayList<>();
//    }
//
//    public List<String> convertToCNF(List<String> cfgRules) {
//        // Step 1: Eliminate all rules with more than two non-terminals on the right-hand side.
//        List<String> newRules = eliminateLongRules(cfgRules);
//
//        // Step 2: Eliminate all rules with terminals on the left-hand side.
//        newRules = eliminateTerminals(newRules);
//
//        // Step 3: Introduce new non-terminals to represent all terminal symbols.
//        newRules = introduceNewNonTerminals(newRules);
//
//        // Step 4: Remove all epsilon productions.
//        newRules = removeEpsilonProductions(newRules);
//
//        // Step 5: Remove all unit productions.
//        newRules = removeUnitProductions(newRules);
//
//        return newRules;
//    }
//
//    // Helper method to eliminate all rules with more than two non-terminals on the right-hand side.
//    private List<String> eliminateLongRules(List<String> rules) {
//        List<String> newRules = new ArrayList<>();
//        for (String rule : rules) {
//            String[] parts = rule.split("\\s+");
//            if (parts.length > 3) {
//                String nonTerminal = parts[0];
//                for (int i = 2; i < parts.length; i++) {
//                    String newNonTerminal = generateNewNonTerminal();
//                    newRules.add(nonTerminal + " " + newNonTerminal);
//                    nonTerminal = newNonTerminal;
//                }
//                newRules.add(nonTerminal + " " + parts[parts.length - 2] + " " + parts[parts.length - 1]);
//            } else {
//                newRules.add(rule);
//            }
//        }
//        return newRules;
//    }
//
//    // Helper method to eliminate all rules with terminals on the left-hand side.
//    private List<String> eliminateTerminals(List<String> rules) {
//        List<String> newRules = new ArrayList<>();
//        for (String rule : rules) {
//            String[] parts = rule.split("\\s+");
//            if (!isNonTerminal(parts[0])) {
//                String newNonTerminal = generateNewNonTerminal();
//                cnfRules.add(newNonTerminal + " " + parts[0]);
//                newRules.add(newNonTerminal + " " + rule.substring(parts[0].length()).trim());
//            } else {
//                newRules.add(rule);
//            }
//        }
//        return newRules;
//    }
//
////    // Helper method to introduce new non-terminals to represent all terminal symbols.
////    private List<String> introduceNewNonTerminals(List<String> rules) {
////        List<String> newRules = new ArrayList<>(rules);
////        for (String rule : rules) {
////            String[] parts = rule.split("\\s+");
////            for (int i = 1; i < parts.length; i++) {
////                if (!isNonTerminal(parts[i])) {
////                    String newNonTerminal = generateNewNonTerminal();
////                    cnfRules.add(newNonTerminal + " " + parts[i]);
////                    parts[i] = newNonTerminal;
////                }
////            }
////            /* newRules.set(newRules.indexOf(rule), String.join("*/
////// Chat GPT stopped generating here
////
////// Chat GPT restarts to generate code
////
////        }
////    }
////    //return newRules;
////}
//
//// Helper method to remove all epsilon productions.
//private List<String> removeEpsilonProductions(List<String> rules) {
//    List<String> newRules = new ArrayList<>(rules);
//    Set<String> nullableNonTerminals = getNullableNonTerminals(rules);
//
//    for (String rule : rules) {
//        List<String> combinations = generateCombinations(rule.split("\\s+"), nullableNonTerminals);
//        newRules.addAll(combinations);
//    }
//
//    // Remove all epsilon productions
//    newRules.removeIf(String::isBlank);
//
//    return newRules;
//}
//
//// Helper method to remove all unit productions.
//private List<String> removeUnitProductions(List<String> rules) {
//    Map<String, Set<String>> nonTerminalMap = getNonTerminalMap(rules);
//    List<String> newRules = new ArrayList<>();
//
//    for (String rule : rules) {
//        String[] parts = rule.split("\\s+");
//        if (parts.length == 3 && isNonTerminal(parts[1])) {
//            Set<String> nonTerminals = nonTerminalMap.get(parts[1]);
//            if (nonTerminals != null) {
//                for (String nonTerminal : nonTerminals) {
//                    newRules.add(parts[0] + " " + nonTerminal);
//                }
//            }
//        } else {
//            newRules.add(rule);
//        }
//    }
//
//    return newRules;
//}
//
//// Helper method to check if a symbol is a non-terminal.
//private boolean isNonTerminal(String symbol) {
//    return symbol.startsWith("<") && symbol.endsWith(">");
//}
//
//// Helper method to generate a new non-terminal symbol.
//private String generateNewNonTerminal() {
//    return "<X" + cnfRules.size() + ">";
//}
//
//// Helper method to get the set of nullable non-terminals.
//private Set<String> getNullableNonTerminals(List<String> rules) {
//    Set<String> nullableNonTerminals = new HashSet<>();
//    boolean changed = true;
//
//    while (changed) {
//        changed = false;
//        for (String rule : rules) {
//            String[] parts = rule.split("\\s+");
//            if (parts.length == 2 && parts[1].equals("epsilon")) {
//                nullableNonTerminals.add(parts[0]);
//                changed = true;
//            } else if (allNullable(parts, nullableNonTerminals)) {
//                nullableNonTerminals.add(parts[0]);
//                changed = true;
//            }
//        }
//    }
//
//    return nullableNonTerminals;
//}
//
//// Helper method to check if all non-terminals in an array are nullable.
//private boolean allNullable(String[] symbols, Set<String> nullableNonTerminals) {
//    for (int i = 1; i < symbols.length; i++) {
//        if (!isNonTerminal(symbols[i]) || !nullableNonTerminals.contains(symbols[i])) {
//            return false;
//        }
//    }
//    return true;
//}
//
//// Helper method to generate all possible combinations of symbols, given a set of nullable non-terminals.
//private List<String> generateCombinations(String[] symbols, Set<String> nullableNonTerminals) {
//    List<String> combinations = new ArrayList<>();
//    combinations.add("");
//
//    for (int i = 1; i < symbols.length; i++) {
//        if (isNonTerminal(symbols[i])) {
//            int size = combinations.size();
//            Set<String> newCombinations = new HashSet<>();
//
//            for (int j = 0; j < size; j++) {
//                String combination = combinations.get(j);
//                if (nullableNonTerminals.contains(symbols[i])) {
//                    newCombinations.add(combination + symbols[i]);
//                }
//                newCombinations.add
//
//// Chat GPT stopped generating here
//
//// Chat GPT restarts to generate code
//
//
//                combination + symbols[i]);
//            }
//
//            combinations.addAll(newCombinations);
//        } else {
//            int size = combinations.size();
//            for (int j = 0; j < size; j++) {
//                String combination = combinations.get(j);
//                combinations.set(j, combination + symbols[i] + " ");
//            }
//        }
//    }
//
//    for (int i = 0; i < combinations.size(); i++) {
//        combinations.set(i, combinations.get(i).trim());
//    }
//
//    return combinations;
//}
//
//// Helper method to get a map of non-terminals to their possible expansions.
//private Map<String, Set<String>> getNonTerminalMap(List<String> rules) {
//    Map<String, Set<String>> nonTerminalMap = new HashMap<>();
//
//    for (String rule : rules) {
//        String[] parts = rule.split("\\s+");
//        if (parts.length == 3 && isNonTerminal(parts[1])) {
//            Set<String> expansions = nonTerminalMap.get(parts[1]);
//            if (expansions == null) {
//                expansions = new HashSet<>();
//                nonTerminalMap.put(parts[1], expansions);
//            }
//            expansions.add(parts[2]);
//        }
//    }
//
//    return nonTerminalMap;
//}
//
//// Helper method to print out the CNF rules.
//private void printCNFRules() {
//    for (String rule : cnfRules) {
//        System.out.println(rule);
//    }
//}





