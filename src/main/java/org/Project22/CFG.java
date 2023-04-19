package org.Project22;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CFG {

    // a list of: answer to question List([<DAY>,monday],[<TIME>,9])
    public List<Tuple<String,List<Tuple<String,String>>>> Action;

    // tree that stores the entire CFG
    public Tree CFG;

    public CFG() {
        createTree();
    }

    /**
     * Create the tree from the CFG.txt file.
     */
    private void createTree() {
        // create tree
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/CFG.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // convert line
                line = line.replaceAll("[^a-zA-Z0-9 <>\\|]", "").toLowerCase();

                // read all the rules
                if (line.startsWith("rule")) {
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
            }

            // close stream
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // delete all the nonterminal references
        deleteNonTerminalReferences(CFG.getRoot());

        // TODO load actions
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
        // remove all the text within arrow brackets
        Tuple<String,String> v = node.getValue();
        node.setValue(new Tuple<String,String>(v.x(), v.y().replaceAll("<.*>", "").trim()));

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
        return output.x().booleanValue() ? output.y() : null;

        // TODO get answer from actions
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
        if (remainder.contains(node.getValue().y())) {
            // remove the value from the remainder
            remainder = remainder.replaceFirst(node.getValue().y(), "");
            values.add(node.getValue());

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

    public static void main(String[] args) {
        // TESTING:
        CFG cfg = new CFG();

        String input = "which lectures are there on monday at 9";
        System.out.println(input + " : " + cfg.matchString(input));
    }
}
