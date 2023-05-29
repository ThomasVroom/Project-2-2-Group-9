package org.Project22.Experiments.Phase2Experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.Project22.CFG_Handler.BobTheBuilder;
import org.Project22.CFG_Handler.CFGtoCNFConverter;
import org.Project22.CFG_Handler.CockeYoungerKasami.Rule;
import org.Project22.GUI.CFGSkillEditor;

public class CYKExperiments {
    
    public static void main(String[] args) {
        // cfg to load
        CFGSkillEditor.default_cfg_file_cyk = "resources/CFG/CYK/large_cfg.txt";
        allQuestions();
    }

    public static void loadCFG() {
        final int N = 1000;
        CFGtoCNFConverter converter = new CFGtoCNFConverter();

        try (FileWriter fw = new FileWriter("src/main/java/org/Project22/Experiments/Phase2Experiments/cyk_load_cfg_small.txt")) {
            long startTime, time;
            
            for (int i = 0; i < N; i++) {
                // compute time
                startTime = System.nanoTime();
                converter.getRulesCNF();
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
        CFGtoCNFConverter converter = new CFGtoCNFConverter();
        List<Rule> rules = converter.getRulesCNF();

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

        try (FileWriter fw = new FileWriter("src/main/java/org/Project22/Experiments/Phase2Experiments/cyk_questions_large.txt")) {
            long startTime, time;
            for (int i = 0; i < N; i++) {
                startTime = System.nanoTime();
                for (String question : questions) {
                    BobTheBuilder.iterateRules(question.split(" "), rules);
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
