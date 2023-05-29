package org.Project22.Experiments.Phase2Experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.Project22.CFGTree;
import org.Project22.GUI.CFGSkillEditor;

public class TreeTraversalExperiments {
    
    public static void main(String[] args) {
        // cfg to load
        CFGSkillEditor.default_cfg_file_tree_traversal = "resources/CFG/TreeTraversal/large_cfg.txt";
    }

    public static void loadCFG() {
        final int N = 1000;

        try (FileWriter fw = new FileWriter("src/main/java/org/Project22/Experiments/Phase2Experiments/tree_traversal_load_cfg_small.txt")) {
            long startTime, time;
            for (int i = 0; i < N; i++) {
                // compute time
                startTime = System.nanoTime();
                new CFGTree();
                time = System.nanoTime() - startTime;

                // write time to file
                fw.write(time + "\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void allQuestions() {
        final int N = 100;
        CFGTree tree = new CFGTree();

        // load all possible questions
        List<String> questions = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/CFG/UniqueSentenceslarge_cfg.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                questions.add(line.toLowerCase());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter fw = new FileWriter("src/main/java/org/Project22/Experiments/Phase2Experiments/tree_traversal_questions_large.txt")) {
            long startTime, time;
            for (int i = 0; i < N; i++) {
                startTime = System.nanoTime();
                for (String question : questions) {
                    tree.matchString(question);
                }
                time = System.nanoTime() - startTime;

                // write time to file
                fw.write(time + "\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
