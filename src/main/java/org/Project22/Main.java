package org.Project22;

public class Main {
    public static void main(String[] args) {
        Question k = new Question("hey what is <NAMFORME> hehe <number2> ", "heheheh");
        System.out.println(k.Matches("hey what is .* hehe .* ")+", "+k.Matches("hey what is dddw hehe hddw ")+", "+k.Matches("hey what is dsgfsd hehe hell")+", "+k.Matches("hey whddgat is dsgfsd hehe hello hjhvd") + ", "+ k.getVariable("hey what is bye hehe heyyy "));

    }
}