package org.Project22.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

public class CFGSkillEditor extends JFrame {

    public static String default_cfg_file_tree_traversal = "resources/CFG/TreeTraversal/CFG.txt";
    public static String default_cfg_file_cyk = "resources/CFG/CYK/CFG.txt";

    private String currentFile;
    
    private JButton jButton1;
    private JButton jButton2;
    private JLabel jLabel1;
    private JLabel jLabel10;
    private JLabel jLabel11;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTextArea jTextArea2;
    private JTextArea jTextArea3;

    private int algorithm;

    public CFGSkillEditor(int algorithm) {
        // ui
        initComponents();
        setLocationRelativeTo(null);

        // set algorithm
        this.algorithm = algorithm;

        // load cfg file
        if (algorithm == 0)
            loadFile(default_cfg_file_tree_traversal);
        else if (algorithm == 1)
            loadFile(default_cfg_file_cyk);
    }

    private void initComponents() {

        jScrollPane2 = new JScrollPane();
        jTextArea2 = new JTextArea();
        jScrollPane3 = new JScrollPane();
        jTextArea3 = new JTextArea();
        jButton2 = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jLabel6 = new JLabel();
        jLabel7 = new JLabel();
        jLabel8 = new JLabel();
        jLabel9 = new JLabel();
        jLabel10 = new JLabel();
        jLabel11 = new JLabel();
        jButton1 = new JButton();

        setTitle("CFG Skill Editor");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane3.setViewportView(jTextArea3);

        jButton2.setText("Save");
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(jTextArea2, true);
                saveFile(jTextArea3, false);
            }
        });

        jButton1.setText("Switch File");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (algorithm == 0) {
                    JFileChooser fileChooser = new JFileChooser(default_cfg_file_tree_traversal);
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        default_cfg_file_tree_traversal = fileChooser.getSelectedFile().getAbsolutePath();
                        loadFile(default_cfg_file_tree_traversal);
                    }
                }
                else if (algorithm == 1) {
                    JFileChooser fileChooser = new JFileChooser(default_cfg_file_cyk);
                    int result = fileChooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        default_cfg_file_cyk = fileChooser.getSelectedFile().getAbsolutePath();
                        loadFile(default_cfg_file_cyk);
                    }
                }
            }
        });

        jLabel1.setText("Use <> to denote non-terminal expressions");

        jLabel2.setText("Use | to denote an OR-statement");

        jLabel3.setText("The response for an action comes after the - symbol");

        jLabel4.setText("Special characters are not allowed");

        jLabel5.setText("Special characters are not allowed before the - symbol");

        jLabel6.setText("To use a regex, start the rule with the word 'regex'");

        jLabel7.setText("Matching the rules works case-insensitive");

        jLabel8.setText("Matching the actions works case-insensitive");

        jLabel9.setText("Use '/py file.py <ARGUMENTS> to run a python file,");

        jLabel10.setText("Keep the order of rules in mind!");

        jLabel11.setText("(note all arguments must be upper case)");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel11))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton1, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addGap(158, 158, 158)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton1)))
                    .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    public void loadFile(String filename) {
        jTextArea2.setText("");
        jTextArea3.setText("");
        currentFile = filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Rule")) {
                    jTextArea2.append(line + "\n");
                }
                else if (line.startsWith("Action")) {
                    jTextArea3.append(line + "\n");
                }
            }
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public void saveFile(JTextArea area, boolean overwrite) {
        try {
            FileWriter fw = new FileWriter(currentFile, !overwrite);
            area.write(fw);
            fw.append("\n");
            fw.close();
        } catch(IOException e) {e.printStackTrace();}
        this.setVisible(false);
    }
}
