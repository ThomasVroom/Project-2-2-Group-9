package org.Project22.CFG_Handler.CockeYoungerKasami;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private String startSymbol;
    private Set<String> terminalSymbols;
    private Set<String> nonTerminalSymbols;
    private List<Rule> productionRules;

    public Grammar(String startSymbol) {
        this.startSymbol = startSymbol;
        this.terminalSymbols = new HashSet<>();
        this.nonTerminalSymbols = new HashSet<>();
        this.productionRules = new ArrayList<>();
    }

    // Load grammar from a file or string
    public void loadGrammar(String input) {}

    // Get production rules for a specific non-terminal symbol
    public List<Rule> getRulesByNonTerminal(String nonTerminal) {
        return null;
    }

    // Convert the grammar to Chomsky Normal Form (if needed)
    public void convertToCNF() {}

    public String getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public Set<String> getTerminalSymbols() {
        return terminalSymbols;
    }

    public Set<String> getNonTerminalSymbols() {
        return nonTerminalSymbols;
    }

    public List<Rule> getProductionRules() {
        return productionRules;
    }

    // toString method
}
