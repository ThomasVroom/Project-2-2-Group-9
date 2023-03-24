package org.Project22.GUI;

import org.Project22.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class MenuWindow {
    public JFrame frame = new JFrame();
    public JPanel menuPanel;
    private JButton authenticationButton;
    private JButton welcomeButton;
    private JButton detectedButton;
    private JButton welcomeLabel;
    private JButton noAccessButton;
    private BufferedImage background, resized_b;
    private JLabel background_label;
    private ImageIcon icon;
    public ArrayList<String> outputList;

    public MenuWindow() {initializeMenu();}

    public void initializeMenu(){

        //TAKES DIMENSION OF THE SCREEN. The dimensions are used to set the gameFrame and the image size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth());
        int h = (int) (screenSize.getHeight());

        frame.setVisible(true);
        frame.setBounds((w -768)/2,(h -767)/2,768,740);
        frame.setResizable(false);
        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        Color lightBlue = new Color(173, 216, 230); // RGB values for light blue
        menuPanel.setBackground(lightBlue);
        frame.add(menuPanel);

        welcomeButton = new JButton("Digital Assistant");
        menuPanel.add(welcomeButton);
        welcomeButton.setBounds(15,200,768,80);
        welcomeButton.setForeground(Color.red.darker());
        welcomeButton.setFont(new Font("Sans-serif",Font.BOLD,60));
        welcomeButton.setOpaque(false);
        welcomeButton.setContentAreaFilled(false);
        welcomeButton.setBorderPainted(false);
        welcomeButton.setFocusPainted(false);

        welcomeLabel = new JButton("Hello DACS Student!");
        menuPanel.add(welcomeLabel);
        welcomeLabel.setFont(new Font("Sans-serif",Font.BOLD,20));
        welcomeLabel.setBounds(15,320,768,100);
        welcomeLabel.setForeground(Color.black.darker());
        welcomeLabel.setOpaque(false);
        welcomeLabel.setContentAreaFilled(false);
        welcomeLabel.setBorderPainted(false);
        welcomeLabel.setFocusPainted(false);

        //MENU LABEL
        authenticationButton = new JButton("Authentication");
        menuPanel.add(authenticationButton);
        authenticationButton.setBounds(200,500,368,50);
        authenticationButton.setForeground(Color.black.brighter());
        authenticationButton.setFont(new Font("Sans-serif",Font.BOLD,30));
        authenticationButton.setOpaque(false);
        authenticationButton.setContentAreaFilled(false);
        authenticationButton.setBorderPainted(true);
        authenticationButton.setFocusPainted(false);

        detectedButton = new JButton("Hello human! I'd love to help you.");
        menuPanel.add(detectedButton);
        detectedButton.setVisible(false);
        detectedButton.setBounds(15,450,500,100);
        detectedButton.setForeground(Color.black.brighter());
        detectedButton.setFont(new Font("Sans-serif",Font.BOLD,20));
        detectedButton.setOpaque(false);
        detectedButton.setContentAreaFilled(false);
        detectedButton.setBorderPainted(true);
        detectedButton.setFocusPainted(false);

        noAccessButton = new JButton("Try again.");
        noAccessButton.setVisible(false);
        menuPanel.add(noAccessButton);
        noAccessButton.setForeground(Color.black.brighter());
        noAccessButton.setBounds(15,500,400,50);
        noAccessButton.setFont(new Font("Sans-serif",Font.BOLD,20));
        noAccessButton.setOpaque(false);
        noAccessButton.setContentAreaFilled(false);
        noAccessButton.setBorderPainted(true);
        noAccessButton.setFocusPainted(false);

        authenticationButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {

                webcamButtonActionPerformed(event);

                try {
                    Thread.sleep(7000); // delay for 1000 milliseconds (1 second)
                } catch (InterruptedException et) {
                    // handle the exception
                }

                if(!outputList.isEmpty() & outputList.contains("A human was detected")){

                    noAccessButton.setVisible(false);

                    menuPanel.remove(authenticationButton);
                    menuPanel.add(detectedButton);

                    try {
                        Thread.sleep(2000); // delay for 1000 milliseconds (1 second)
                    } catch (InterruptedException et) {
                        // handle the exception
                    }
                    frame.setVisible(false);
                    Main.ui = new UI();

                }else{
                    noAccessButton.setVisible(true);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
//
//
//        try {
//            File imageFile = new File("image.png");
//            background = ImageIO.read(imageFile);
//        } catch (IOException ex) {
//            System.err.println("Error reading image file: " + ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        background_label = new JLabel();
//        icon = new ImageIcon(background);
//        background_label.setIcon(icon);
//        menuPanel.add(background_label,BorderLayout.CENTER);

    }
    private void webcamButtonActionPerformed(MouseEvent evt) {
        new WebCamThread().start();
    }

    class WebCamThread extends Thread {

        private static final String[] source = {"python3", "src/main/java/org/Project22/FaceDetection/WebCamRec.py"};

        @Override
        public void run() {
            try {
                Process process = Runtime.getRuntime().exec(source);

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output;

                outputList = new ArrayList<>();

                while ((output = reader.readLine()) != null) {
                    outputList.add(output);
                    System.out.println(output);
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
