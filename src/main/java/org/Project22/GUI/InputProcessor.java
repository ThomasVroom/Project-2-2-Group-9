package org.Project22.GUI;

import java.util.ArrayList;
import java.util.List;

import org.Project22.Main;
import org.Project22.Question;
import org.Project22.Tuple;

public class InputProcessor {
    
    public static String process(String input) {
        return Main.answerGenerator.getAnswer(input);
    }
}
