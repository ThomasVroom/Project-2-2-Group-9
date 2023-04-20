package org.Project22.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import org.Project22.Main;
import org.Project22.Tuple;

public class UI extends JFrame{

    private JScrollPane chatScrollPane;
    private JProgressBar confidenceBar;
    private JLabel confidenceLabel;
    private JButton clearButton;
    private JLabel debugLabel;
    private JTextPane debugPane;
    private JScrollPane debugScrollPane;
    private JComboBox<String> languageBox;
    private JComboBox<String> skillBox;
    private JLabel languageLabel;
    private JLabel skillLabel;
    private JButton skilleditorButton;

    private static final String[] skill_types = new String[] {"Template", "CFG"};
    private static final String[] cfg_matching_algorithms = new String[] {"Tree Traversal"};
    private static final String[] template_matching_algorithms = new String[] {"Exact Match", "Split Variables", "Split Variables+", "Filter Match"};

    private List<String> variables;

    public UI() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Chatbot");
        this.setResizable(true);

        initComponents();

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void initComponents() {
        skilleditorButton = new JButton();
        languageBox = new JComboBox<>();
        skillBox = new JComboBox<>();
        languageLabel = new JLabel();
        skillLabel = new JLabel();
        clearButton = new JButton();
        confidenceLabel = new JLabel();
        confidenceBar = new JProgressBar();
        debugLabel = new JLabel();
        chatScrollPane = new JScrollPane();
        debugScrollPane = new JScrollPane();
        debugPane = new JTextPane();
        variables = new ArrayList<String>();

        skilleditorButton.setText("Skill Editor");
        skilleditorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                skilleditorButtonActionPerformed(evt);
            }
        });

        skillBox.setModel(new DefaultComboBoxModel<>(skill_types));
        skillBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                if (skillBox.getSelectedIndex() == 0) { // template
                    languageBox.setModel(new DefaultComboBoxModel<>(template_matching_algorithms));
                }
                else if (skillBox.getSelectedIndex() == 1) { // cfg
                    languageBox.setModel(new DefaultComboBoxModel<>(cfg_matching_algorithms));
                }
            }
        });

        languageBox.setModel(new DefaultComboBoxModel<>(template_matching_algorithms));

        languageLabel.setText("Language Model");
        skillLabel.setText("Skill Type");

        clearButton.setText("Clear Chat");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        confidenceLabel.setText("Confidence: XX");

        confidenceBar.setMinimum(0);
        confidenceBar.setMaximum(100);

        debugLabel.setText("Debug:");

        chatScrollPane.setViewportView(new ChatWindow());

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
                                        .addComponent(skillLabel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                        .addComponent(languageLabel, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                        .addComponent(debugScrollPane, GroupLayout.Alignment.TRAILING)
                                        .addComponent(confidenceLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(debugLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(skilleditorButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(clearButton, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                                        .addComponent(skillBox, GroupLayout.Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(languageBox, GroupLayout.Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(skilleditorButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(clearButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(skillLabel)
                                                .addComponent(skillBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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

        this.pack();
    }

    public void setConfidence(float percentage) {
        this.confidenceLabel.setText("Confidence: " + ((int) (percentage * 100)) / 100f);
        this.confidenceBar.setValue((int) (percentage * 100));
    }

    public void setDebugText(String question, List<Tuple<String, String>> variables) {
        String s = question + "\n\n";

        this.variables.clear();
        for (Tuple<String, String> variable : variables) {
            s += variable.x() + " " + variable.y() + "\n";
            this.variables.add(variable.y());
        }

        this.debugPane.setText(s);
    }

    public List<String> getVariables() {
        return this.variables;
    }

    public Tuple<Integer, Integer> getMatchingAlgorithm() {
        return new Tuple<Integer,Integer>(skillBox.getSelectedIndex(), languageBox.getSelectedIndex());
    }

    private void skilleditorButtonActionPerformed(ActionEvent evt) {
        new SkillEditor().setVisible(true);
    }

    private void clearButtonActionPerformed(ActionEvent evt) {
        Main.loadSkills();
        this.chatScrollPane.setViewportView(new ChatWindow());
    }
}