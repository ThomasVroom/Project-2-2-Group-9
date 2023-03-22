package org.Project22.GUI;

import org.Project22.Main;

public class InputProcessor {
    
    /**
     * @param input string from the user
     * @return the correct answer to display based on the input
     */
    public static String process(String input) {
        return Main.answerGenerator.getAnswer(input.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase());
    }
}
