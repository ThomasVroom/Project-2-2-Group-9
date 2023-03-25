package org.Project22.GUI;

import org.Project22.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.atomic.AtomicReference;

public class MenuWindow {
    public JFrame frame;
    public JPanel menuPanel;
    private JButton authenticationButton;
    private JButton welcomeButton;
    private JButton detectedButton;
    private JButton welcomeLabel;
    private JButton noAccessButton;
    private JButton noWebCamButton;
    private JButton continueButton;
    private BufferedImage background, resized_b;
    private JLabel background_label;
    private JPasswordField passwordField;
    private ImageIcon icon;

    public MenuWindow() {initializeMenu();}

    public void initializeMenu(){

        //TAKES DIMENSION OF THE SCREEN. The dimensions are used to set the gameFrame and the image size.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = (int) (screenSize.getWidth());
        int h = (int) (screenSize.getHeight());

        menuPanel = new JPanel();
        menuPanel.setLayout(new BorderLayout());
        Color lightBlue = new Color(173, 216, 230); // RGB values for light blue
        //menuPanel.setBackground(lightBlue);

        welcomeButton = new JButton("Digital Assistant");
        menuPanel.add(welcomeButton);
        welcomeButton.setBounds(15,60,768,80);
        welcomeButton.setForeground(Color.red.darker());
        welcomeButton.setFont(new Font("Sans-serif",Font.BOLD,60));
        welcomeButton.setOpaque(false);
        welcomeButton.setContentAreaFilled(false);
        welcomeButton.setBorderPainted(false);
        welcomeButton.setFocusPainted(false);

        welcomeLabel = new JButton("Hello DACS Student!");
        menuPanel.add(welcomeLabel);
        welcomeLabel.setFont(new Font("Sans-serif",Font.BOLD,30));
        welcomeLabel.setBounds(15,150,768,100);
        welcomeLabel.setForeground(Color.black.darker());
        welcomeLabel.setOpaque(false);
        welcomeLabel.setContentAreaFilled(false);
        welcomeLabel.setBorderPainted(false);
        welcomeLabel.setFocusPainted(false);

        //MENU LABEL
        authenticationButton = new JButton("Authentication");
        authenticationButton.setBounds(200,500,368,50);
        authenticationButton.setForeground(Color.black.brighter());
        authenticationButton.setFont(new Font("Sans-serif",Font.BOLD,30));
        authenticationButton.setOpaque(true);
        authenticationButton.setContentAreaFilled(true);
        authenticationButton.setBackground(lightBlue);
        authenticationButton.setBorderPainted(true);
        authenticationButton.setFocusPainted(false);
        menuPanel.add(authenticationButton);

        noWebCamButton = new JButton("No access to webcam");
        noWebCamButton.setBounds(200,585,368,50);
        noWebCamButton.setFont(new Font("Sans-serif",Font.BOLD,20));
        Color darkBlue = new Color(53, 98, 189); // RGB values for light blue
        noWebCamButton.setForeground(darkBlue);
        Font font = noWebCamButton.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        noWebCamButton.setFont(font.deriveFont(attributes));
        noWebCamButton.setOpaque(false);
        noWebCamButton.setContentAreaFilled(false);
        noWebCamButton.setBorderPainted(false);
        noWebCamButton.setFocusPainted(false);
        menuPanel.add(noWebCamButton);

        passwordField = new JPasswordField();
        passwordField.setBounds(284,629,200,30);
        passwordField.setVisible(false);
        menuPanel.add(passwordField);

        continueButton = new JButton("Continue");
        continueButton.setBounds(330,665,100,30);
        continueButton.setFont(new Font("Sans-serif",Font.BOLD,10));
        continueButton.setVisible(false);
        menuPanel.add(continueButton);


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

        noAccessButton = new JButton("Try again");
        noAccessButton.setVisible(false);
        menuPanel.add(noAccessButton);
        noAccessButton.setForeground(Color.black.brighter());
        noAccessButton.setBounds(200,550,368,50);
        noAccessButton.setFont(new Font("Sans-serif",Font.BOLD,20));
        noAccessButton.setOpaque(true);
        noAccessButton.setContentAreaFilled(true);
        noAccessButton.setBorderPainted(false);
        noAccessButton.setFocusPainted(false);

        authenticationButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent event) {

                webcamButtonActionPerformed(event);

                noAccessButton.setVisible(false);
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

        noWebCamButton.addActionListener(e -> {
            noAccessButton.setVisible(false);
            noWebCamButton.setText("Enter your password:");
            noWebCamButton.setForeground(Color.BLACK);
            passwordField.setVisible(true);
            continueButton.setVisible(true);

        });
        continueButton.addActionListener(e -> {
            try {
                Thread.sleep(1000); // delay for 1000 milliseconds (1 second)
            } catch (InterruptedException et) {
                // handle the exception
            }
            frame.setVisible(false);
            Main.ui = new UI();
        });


        try {
            File imageFile = new File("resources/image.png");
            BufferedImage originalImage = ImageIO.read(imageFile);

            int scaledWidth = 200; // set the width to which you want to resize the image
            int scaledHeight = 200; // set the height to which you want to resize the image
            Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D graphics2D = resizedImage.createGraphics();
            graphics2D.drawImage(scaledImage, 0, 0, null);
            graphics2D.dispose();

            background_label = new JLabel(new ImageIcon(resizedImage));
            menuPanel.add(background_label, BorderLayout.CENTER);

            // use the resized image as needed
        } catch (IOException ex) {
            System.err.println("Error reading image file: " + ex.getMessage());
            ex.printStackTrace();
        }

        frame = new JFrame();
        frame.setVisible(true);
        frame.setBounds((w -768)/2,(h -767)/2,768,740);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(menuPanel);

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

                while ((output = reader.readLine()) != null) {
                    System.out.println(output);
                    if(output.equals("A human was detected")){

                        noAccessButton.setVisible(false);
    
                        menuPanel.remove(authenticationButton);
                        menuPanel.add(detectedButton);
    
                        try {
                            Thread.sleep(1000); // delay for 1000 milliseconds (1 second)
                        } catch (InterruptedException et) {
                            // handle the exception
                        }
                        
                        frame.setVisible(false);
                        Main.ui = new UI();
    
                    }else{
                        noAccessButton.setVisible(true);
                    }
                }
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
