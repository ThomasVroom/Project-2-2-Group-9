package org.Project22.CFG_Handler.CockeYoungerKasami;

import java.util.List;

public class Rule {
    public String lhs; // Left-hand side non-terminal symbol
    public List<List<String>> rhs; // Right-hand side symbols (terminals or non-terminals)

    public Rule(String lhs, List<List<String>> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public String getLhs() {
        return lhs;
    }

    public void setLhs(String lhs) {
        this.lhs = lhs;
    }

    public List<List<String>> getRhs() {
        return rhs;
    }

    public void setRhs(List<List<String>> rhs) {
        this.rhs = rhs;
    }

    // equals, hashCode, and toString methods
}
