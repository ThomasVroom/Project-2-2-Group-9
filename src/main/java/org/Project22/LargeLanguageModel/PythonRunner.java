package org.Project22.LargeLanguageModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class PythonRunner {
    public static void main(String[] args) {
        try {
            // Set the command and arguments to activate venv and execute the Python script
            String pythonScript = "/home/raaf/IdeaProjects/Project-2-2-Group-92/src/main/java/org/Project22/LargeLanguageModel/main.py";
            String venvActivateCommand = "source /home/raaf/IdeaProjects/Project-2-2-Group-92/src/main/java/org/Project22/LargeLanguageModel/venv/bin/activate &&";
            String workingDirectory = "/home/raaf/IdeaProjects/Project-2-2-Group-92/src/main/java/org/Project22/LargeLanguageModel";
            String[] pythonCommand = { "bash", "-c", venvActivateCommand + " python " + pythonScript + " inference \"where is deepspace\"" };
            System.out.println(Arrays.toString(pythonCommand));
            // Create a ProcessBuilder for running the Python script
            ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand);
            processBuilder.directory(new File(workingDirectory));
            Process process = processBuilder.start();

            // Read the output from the Python script
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Wait for the process to finish and get the exit code
            int exitCode = process.waitFor();
            System.out.println("Python script executed with exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
