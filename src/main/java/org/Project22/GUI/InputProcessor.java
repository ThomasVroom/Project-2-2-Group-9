package org.Project22.GUI;

import java.util.ArrayList;
import java.util.List;

import org.Project22.Question;
import org.Project22.Tuple;

public class InputProcessor {
    
    public static String process(String input) {
        List<Tuple<String,String>> places = new ArrayList<>();
        places.add(new Tuple<String,String>("DAY","monday"));
        places.add(new Tuple<String,String>("DAY","saturday"));
        places.add(new Tuple<String,String>("DAY","friday"));
        places.add(new Tuple<String,String>("DAY","tuesday"));
        places.add(new Tuple<String,String>("TIME","8:00"));
        places.add(new Tuple<String,String>("TIME","12:00"));
        places.add(new Tuple<String,String>("TIME","13:00"));
        List<Tuple<String,List<Tuple<String,String>>>> answers = new ArrayList<>();
        List<Tuple<String,String>> placeholders0 = new ArrayList<>();
        List<Tuple<String,String>> placeholders1 = new ArrayList<>();
        List<Tuple<String,String>> placeholders2 = new ArrayList<>();
        placeholders0.add(places.get(3));
        placeholders0.add(places.get(4));
        placeholders1.add(places.get(0));
        placeholders1.add(places.get(6));
        placeholders2.add(places.get(0));
        answers.add(new Tuple<>("on tuesday there is ICT on 8:00",placeholders0));
        answers.add(new Tuple<>("on monday there is SSA on 13:00",placeholders1));
        answers.add(new Tuple<>("on monday there is another class",placeholders2));
        Question k = new Question("Which lectures are there on <DAY> at <TIME>", places, answers,"test");

        return k.getAnswer(k.getVariable2(input));
    }
}
