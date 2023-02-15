package org.Project22.GUI;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class UI extends JFrame {
    
    public UI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Chatbot");
        setResizable(false);
        setPreferredSize(new Dimension(600, 700));

        // components
        add(new JScrollPane(new ChatWindow()));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
