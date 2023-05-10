package org.Project22;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestMatchingAlgorithms {
    public static String testname = "match1-4new";
    public static void main(String[] args) throws IOException {
        File masterFile = new File("resources/SkillFiles");
        File masterTestFile = new File("resources/TestQuestions");
        if (masterFile.list().length != masterTestFile.listFiles().length)
            throw new RuntimeException("difference in file structure please recode or fix file structure");
        //question initialisation
        List<Question> questions = new ArrayList<>();
        String[] filenames = masterFile.list();
        for (String filename: filenames) {
            try {
                questions.add(new SkillHandle(filename).returnQuestion());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //end of initialisation start of testing init
        List<String[]> tests = new ArrayList<>();
        for (String filename:masterTestFile.list()) {
            String bigString = TextHandler.readFileToString(masterTestFile.getPath()+"/"+filename);
            tests.add(bigString.split("\n"));
        }
        if (tests.size() != questions.size())
            throw new RuntimeException("you fucked up the textfiles in some way");
        AnswerGenerator  answerGenerator = new AnswerGenerator(questions);
        List<String> resultString = new ArrayList<>();
        for (int i = 0; i < answerGenerator.matchingAlgorithms.length; i++) {
            for (int j = 0; j < questions.size(); j++) {
                float perCorrect = 0;
                float difCorrect = 0;
                String addedCategorie = "";
                boolean checkForAddingInBetween = false;
                for(String test : tests.get(j)) {
                    if (test.startsWith("<")){
                        if (checkForAddingInBetween){
                            resultString.add(perCorrect/tests.get(j).length+","+difCorrect/tests.get(j).length+","+answerGenerator.matchingAlgorithms[i].getClass().getName().substring(answerGenerator.matchingAlgorithms[i].getClass().getName().lastIndexOf(".")+1)+","+questions.get(j).name + ", "+addedCategorie);
                        }
                        addedCategorie = test.substring(1,test.length()-1);
                    }
                    else {
                        checkForAddingInBetween = true;
                        Tuple<Question, Float> skillChosen = answerGenerator.getQuestion(test, i);
                        if (skillChosen == null) {
                        } else if (skillChosen.x().name.equals(questions.get(j).name)) {
                            perCorrect += 1.0;
                            difCorrect += skillChosen.y();
                        } else {
                        }
                    }
                }
                resultString.add(perCorrect/tests.get(j).length+","+difCorrect/tests.get(j).length+","+answerGenerator.matchingAlgorithms[i].getClass().getName().substring(answerGenerator.matchingAlgorithms[i].getClass().getName().lastIndexOf(".")+1)+","+questions.get(j).name + ", "+addedCategorie);
            }
        }
        String BigString = "Percentage correct,Difference Correctly identified,algorithm used,Categories";
        for (String result:resultString) {
            BigString+= "\n"+result;
        }
        TextHandler.writeStringToFile("resources/TestOutput/"+testname,BigString);
    }


}
