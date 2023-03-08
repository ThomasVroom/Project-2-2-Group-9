package org.Project22.GUI;

import org.Project22.SkillHandle;

public class InputProcessor {
    
    public static String process(String input) {

        SkillHandle skill1 = new SkillHandle("test.txt");
        return skill1.getQuestion().getAnswer(skill1.getQuestion().getVariable2(input));
    }
}
