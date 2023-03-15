package org.Project22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebcamRecognition {

    public static void main(String[] args) throws IOException {
        String[] cmd = {"python3", "src/main/java/org/Project22/WebCamRecognition/WebCamRec.py"};
        Process process = Runtime.getRuntime().exec(cmd);

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output;
        while ((output = reader.readLine()) != null) {
            System.out.println(output);
        }
    }
}
