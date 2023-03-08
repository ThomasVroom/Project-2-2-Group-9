package org.Project22.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UI extends JFrame {

    private JScrollPane chatScrollPane;
    private ChatWindow chatwindow;
    private JProgressBar confidenceBar;
    private JLabel confidenceLabel;
    private JCheckBox darkModeBox;
    private JLabel debugLabel;
    private JTextPane debugPane;
    private JScrollPane debugScrollPane;
    private JComboBox<String> languageBox;
    private JLabel languageLabel;
    private JButton skilleditorButton;
    private JButton webcamButton;
    
    public UI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Chatbot");
        setResizable(false);

        initComponents();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {

        skilleditorButton = new JButton();
        webcamButton = new JButton();
        languageBox = new JComboBox<>();
        languageLabel = new JLabel();
        darkModeBox = new JCheckBox();
        confidenceLabel = new JLabel();
        confidenceBar = new JProgressBar();
        debugLabel = new JLabel();
        chatScrollPane = new JScrollPane();
        chatwindow = new ChatWindow();
        debugScrollPane = new JScrollPane();
        debugPane = new JTextPane();

        skilleditorButton.setText("Skill Editor");
        skilleditorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                skilleditorButtonActionPerformed(evt);
            }
        });

        webcamButton.setText("Webcam");
        webcamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                webcamButtonActionPerformed(evt);
            }
        });

        languageBox.setModel(new DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        languageLabel.setText("Language Model");

        darkModeBox.setText("Dark Mode");
        darkModeBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                darkModeBoxActionPerformed(evt);
            }
        });

        confidenceLabel.setText("Confidence: XX");

        confidenceBar.setMinimum(0);
        confidenceBar.setMaximum(100);

        debugLabel.setText("Debug:");

        chatScrollPane.setViewportView(chatwindow);

        debugScrollPane.setViewportView(debugPane);

        debugPane.setEditable(false);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chatScrollPane, GroupLayout.PREFERRED_SIZE, 600, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(confidenceBar, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(languageLabel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(debugScrollPane, GroupLayout.Alignment.TRAILING)
                    .addComponent(confidenceLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(debugLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(skilleditorButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(webcamButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(darkModeBox, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                    .addComponent(languageBox, GroupLayout.Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(webcamButton)
                        .addGap(4, 4, 4)
                        .addComponent(skilleditorButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(darkModeBox)
                        .addGap(18, 18, 18)
                        .addComponent(languageLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(languageBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(debugLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(debugScrollPane, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(confidenceLabel)
                        .addGap(4, 4, 4)
                        .addComponent(confidenceBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addComponent(chatScrollPane, GroupLayout.PREFERRED_SIZE, 700, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    public void setConfidence(float percentage) {
        this.confidenceLabel.setText("Confidence: "+((int)(percentage * 100))/100f);
        this.confidenceBar.setValue((int)(percentage * 100));
    }

    private void skilleditorButtonActionPerformed(ActionEvent evt) {                                                  
        new SkillEditor().setVisible(true);
    }                                                 

    private void webcamButtonActionPerformed(ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void darkModeBoxActionPerformed(ActionEvent evt) {                                            
        // TODO add your handling code here:
    }
}
