package org.Project22.Experiments.Phase2Experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class LanguageModelExperiments {

    // RUN PYTHON SERVER FIRST!
    // See README.md or Main.java
    
    public static void main(String[] args) {
        // cfg to load
        // TODO
    }

    public static void loadCFG() {
        final int N = 1000;

        try (FileWriter fw = new FileWriter("src/main/java/org/Project22/Experiments/Phase2Experiments/nn_load_cfg_small.txt")) {
            long startTime, time;
            
            for (int i = 0; i < N; i++) {
                // compute time
                startTime = System.nanoTime();
                
                // load neural network
                // TODO

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

        // HTTP client for connecting to server
        HttpClient client = HttpClient.newHttpClient();

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
            HttpRequest request;
            for (int i = 0; i < N; i++) {
                startTime = System.nanoTime();
                for (String question : questions) {
                    // feed questions to algorithm
                    request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/infer"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"inference\":\"" + question + "\"}"))
                    .build();
                    client.send(request, HttpResponse.BodyHandlers.ofString());
                }
                time = System.nanoTime() - startTime;

                // write time to file
                fw.write(time + "\n");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
