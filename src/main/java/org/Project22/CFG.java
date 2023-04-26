package org.Project22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CFG {

    // a list of: answer to question List([<DAY>,monday],[<TIME>,9])
    public List<Tuple<String,List<Tuple<String,String>>>> actions;

    // tree that stores the entire CFG
    public Tree CFG;

    public CFG() {
        createTree();
    }

    /**
     * Create the tree from the CFG.txt file.
     */
    private void createTree() {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/NEW_CFG.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // read all the rules
                if (line.startsWith("Rule")) {
                    // convert line
                    line = line.toLowerCase();

                    // root
                    if (CFG == null) {
                        CFG = new Tree(new Tuple<String,String>(line.substring(4, line.indexOf('>') + 1).trim(), line.substring(line.indexOf('>') + 1).trim()));
                    }
                    else {
                        // non-terminal
                        String nonterminal = line.substring(4, line.indexOf('>') + 1).trim();

                        // terminals
                        String[] terminals = line.substring(line.indexOf('>') + 1).split("\\|");
                        for (int i = 0; i < terminals.length; i++) {
                            // edit string
                            terminals[i] = terminals[i].replace(">", "> &");
                            terminals[i] = terminals[i].trim();

                            // add to the tree
                            addChildrenToTree(CFG.getRoot(), nonterminal, terminals[i]);                            
                        }
                    }
                }

                // load all actions
                if (line.startsWith("Action")) {
                    if (actions == null) {
                        actions = new ArrayList<Tuple<String,List<Tuple<String,String>>>>();
                    }

                    // convert line
                    line = line.substring(7);
                    String[] splitLine = line.split("\\-");
                    splitLine[0] = splitLine[0].toLowerCase();

                    // values
                    List<Tuple<String,String>> values = new ArrayList<Tuple<String,String>>();
                    int indexLeft = splitLine[0].lastIndexOf('<');
                    int indexRight = splitLine[0].lastIndexOf('>');
                    while (indexLeft != -1) {
                        values.add(new Tuple<String,String>(splitLine[0].substring(indexLeft, indexRight + 1), splitLine[0].substring(indexRight + 1).trim()));
                        splitLine[0] = splitLine[0].substring(0, indexLeft);
                        indexLeft = splitLine[0].lastIndexOf('<');
                        indexRight = splitLine[0].lastIndexOf('>');
                    }

                    // add to actions
                    actions.add(new Tuple<String,List<Tuple<String,String>>>(splitLine[1].trim(), values));
                }
            }

            // close stream
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // delete all the nonterminal references
        deleteNonTerminalReferences(CFG.getRoot());
    }

    /**
     * Add children to relevant nodes of the CFG tree.
     * @param node the node it is currently visiting
     * @param nonterminal the nonterminal we are searching for in the tree
     * @param terminal the value we are assigning to the children
     */
    private void addChildrenToTree(Tree.Node node, String nonterminal, String terminal) {
        // check if the node contains a reference to the nonterminal
        if (node.getValue().y().contains(nonterminal)) {
            // split terminal in case of multiple nonterminals
            String[] splitTerminals = terminal.split("\\&");

            // add node to tree
            Tree.Node newNode = new Tree.Node(new Tuple<String,String>(nonterminal, splitTerminals[0].trim()));
            node.addChild(newNode);

            // add suffixes
            Tree.Node previous = newNode;
            for (int i = 1; i < splitTerminals.length; i++) {
                Tree.Node suffix = new Tree.Node(new Tuple<String,String>(nonterminal, splitTerminals[i].trim()));
                previous.addSuffix(suffix);
                previous = suffix;
            }
        }

        // dfs iteration over children
        for (Tree.Node child : node.getChildren()) {
            addChildrenToTree(child, nonterminal, terminal);
        }
        if (node.getSuffix() != null) addChildrenToTree(node.getSuffix(), nonterminal, terminal);
    }

    /**
     * Delete all references to nonterminals from the CFG tree
     * (This is necessary for the traversal to work!)
     * @param node the node we are currently visiting
     */
    private void deleteNonTerminalReferences(Tree.Node node) {
        // get values
        Tuple<String,String> v = node.getValue();

        // check for arrow brackets
        if (v.y().contains("<")) {
            // check for oddities
            if (node.getChildren().isEmpty()) throw new RuntimeException("Incorrect CFG format (check row order).");

            // delete values
            node.setValue(new Tuple<String,String>(v.x(), v.y().replaceAll("<.*>", "").trim()));
        }

        // dfs iteration over children
        for (Tree.Node child : node.getChildren()) {
            deleteNonTerminalReferences(child);
        }
        if (node.getSuffix() != null) deleteNonTerminalReferences(node.getSuffix());
    }

    /**
     * Match a string with the CFG tree.
     * @param input the string to match
     * @return a list of all non-terminal values and their terminal values found in the sentence.
     * If the input sentence is not a part of the CFG, it returns null instead.
     */
    public List<Tuple<String,String>> matchString(final String input) {
        Tuple<Boolean,List<Tuple<String,String>>> output = dfs(CFG.getRoot(), input, new ArrayList<Tuple<String,String>>());
        return output.x().booleanValue() ? output.y() : new ArrayList<Tuple<String,String>>();
    }

    /**
     * Get the string output corresponding to the values of the input sentence.
     * @param values a list of all variables and their values
     * @return the corresponding string
     */
    public String getAnswer(List<Tuple<String,String>> values) {
        for (Tuple<String,List<Tuple<String,String>>> answer : actions) { // loop over all actions
            boolean answerFound = true;
            boolean[] usedValues = new boolean[values.size()];
            for (Tuple<String, String> answerValue : answer.y()) { // for every value in the action
                boolean found = false;
                for (int i = 0; i < values.size(); i++) { // for every value in the parameter
                    if (usedValues[i]) continue;
                    if (answerValue.x().equals(values.get(i).x())) {
                        if (answerValue.y().equals(values.get(i).y()) || answerValue.y().equals("*")) {
                            usedValues[i] = true;
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    answerFound = false;
                    break;
                }
            }
            if (answerFound) {
                String output = answer.x();
                if (output.startsWith("\\py")) {
                    // replace placeholders
                    output = output.replace("\\py", "python");
                    for (Tuple<String,String> value : values) {
                        output = output.replaceFirst(value.x().toUpperCase(), value.y());
                    }

                    // split string
                    String[] code = output.split(" ");
                    code[1] = "resources/CFG/python_answers/" + code[1];
                    
                    // execute python code
                    try {
                        Process process = Runtime.getRuntime().exec(code);
                        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        return br.readLine();
                    } catch (IOException e) {e.printStackTrace();}
                }
                else return output;
            }
        }
        throw new RuntimeException("error selecting answer (probably python).");
    }

    /**
     * Recursive method that computes a depth-first search of the CFG tree.
     * @param node the node it is currently visiting
     * @param remainder the remainder of the input string (that hasn't been matched yet)
     * @param values the values already found
     * @return if the string belongs to the CFG and the list of values
     */
    private Tuple<Boolean,List<Tuple<String,String>>> dfs(Tree.Node node, String remainder, List<Tuple<String,String>> values) {
        // if the remainder contains the value of the node
        String value = node.getValue().y();
        Pattern pattern = value.startsWith("regex") ? Pattern.compile(value.substring(6)) : Pattern.compile(value, Pattern.LITERAL);
        Matcher matcher = pattern.matcher(remainder);
        if (matcher.find()) {
            // remove the value from the remainder
            values.add(new Tuple<String,String> (node.getValue().x(), matcher.group()));
            remainder = remainder.replaceFirst(matcher.group(), "").trim();

            // if node is a leaf
            if (node.getChildren().isEmpty()) {
                // check for suffixes
                return suffixSearch(node, remainder, values);
            }

            // apply dfs to children
            for (Tree.Node child : node.getChildren()) {
                if (dfs(child, remainder, values).x().booleanValue()) {
                    return new Tuple<Boolean,List<Tuple<String,String>>>(true, values);
                }
            }
            return new Tuple<Boolean,List<Tuple<String,String>>>(false, values);
        }
        else {
            return new Tuple<Boolean,List<Tuple<String,String>>>(false, values);
        }
    }

    /**
     * Recursive method that moves up the CFG tree and searches for nodes with suffixes.
     * @param node the node it is currently visiting
     * @param remainder the remainder of the input string (that hasn't been matched yet)
     * @param values the values already found
     * @return if the string belongs to the CFG and the list of values
     */
    private Tuple<Boolean,List<Tuple<String,String>>> suffixSearch(Tree.Node node, String remainder, List<Tuple<String,String>> values) {
        // if we reach the root
        if (node.getParent() == null) {
            return new Tuple<Boolean,List<Tuple<String,String>>>(remainder.isBlank(), values);
        }

        // if we find a suffix, explore that suffix
        if (node.getSuffix() != null) {
            return dfs(node.getSuffix(), remainder, values);
        }

        // else check the parent
        return suffixSearch(node.getParent(), remainder, values);
    }
}
