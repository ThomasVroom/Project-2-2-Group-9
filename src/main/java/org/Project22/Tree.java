package org.Project22;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    public static class Node {
        private Tuple<String,String> value;
        private List<Node> children;
        private Node suffix;
        public Node(Tuple<String,String> value) {
            this.value = value;
            this.children = new ArrayList<>();
        }

        public Tuple<String,String> getValue() {
            return value;
        }

        public void setValue(Tuple<String,String> value) {
            this.value = value;
        }

        public List<Node> getChildren() {
            return children;
        }
        public Node getSuffix() {
            return suffix;
        }

        public void addChild(Node child) {
            children.add(child);
        }
        public void addSuffix(Node child){
            if (suffix != null)
                throw new RuntimeException("node already has suffix");
            suffix = child;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", children=" + children +
                    ", suffix=" + suffix +
                    '}';
        }
    }

    private Node root;

    public Tree(Tuple<String,String> rootValue) {
        root = new Node(rootValue);
    }

    public Node getRoot() {
        return root;
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                '}';
    }

    public static void main(String[] args) {

    }
}
