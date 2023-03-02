package org.Project22.GUI;

import java.util.ArrayList;
import java.util.List;

import org.Project22.Question;
import org.Project22.Tuple;

public class InputProcessor {
    
    public static String process(String input) {
        List<Tuple<String,String>> places = new ArrayList<>();
        places.add(new Tuple<String,String>("DAY","monday"));
        places.add(new Tuple<String,String>("TIME","13:00"));
        places.add(new Tuple<String,String>("DAY","saturday"));
        places.add(new Tuple<String,String>("DAY","friday"));
        places.add(new Tuple<String,String>("DAY","tuesday"));
        places.add(new Tuple<String,String>("TIME","8:00"));
        places.add(new Tuple<String,String>("TIME","12:00"));
        List<Tuple<String,List<Tuple<String,String>>>> answers = new ArrayList<>();
        answers.add(new Tuple<>("on tuesday there is ICT on 8:00",places.subList(4,6)));
        answers.add(new Tuple<>("on monday there is SSA on 13:00",places.subList(0,2)));
        answers.add(new Tuple<>("on monday there is another class",places.subList(0,1)));
        Question k = new Question("Which lectures are there on <DAY> at <TIME>", places, answers);

        return k.getAnswer(k.getVariable2(input));
    }
}
