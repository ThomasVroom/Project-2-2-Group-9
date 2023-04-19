package org.Project22.GUI;

import org.Project22.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class MenuWindow {
    public JFrame frame;
    public JPanel menuPanel;
    private JButton authenticationButton;
    private JButton welcomeButton;
    private JButton detectedButton;
    private JButton welcomeLabel;
    private JButton nameLabel;
    private JTextField text_name;
    private ArrayList<String> name_lists = new ArrayList<>();
    private String userInput;
    private JButton noAccessButton;
    private JButton noWebCamButton;
    private JButton continueButton1,continueButton2;
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

        nameLabel = new JButton("What is your name?");
        nameLabel.setBounds(240,485,300,21);
        nameLabel.setFont(new Font("Sans-serif",Font.BOLD,20));
        nameLabel.setForeground(Color.black.darker());
        nameLabel.setOpaque(false);
        nameLabel.setContentAreaFilled(false);
        nameLabel.setBorderPainted(false);
        nameLabel.setFocusPainted(false);
        menuPanel.add(nameLabel);

        text_name = new JTextField();
        text_name.setBounds(280,520,150,30);
        text_name.setEnabled(true);
        menuPanel.add(text_name);

        continueButton2 = new JButton("Continue");
        continueButton2.setBounds(435,520,100,30);
        continueButton2.setFont(new Font("Sans-serif",Font.BOLD,10));
        menuPanel.add(continueButton2);

        continueButton2.addActionListener(e -> {
            userInput = text_name.getText().toLowerCase();
            System.out.println(userInput);
            if(!name_lists.contains(userInput)){
                name_lists.add(userInput);
            }
        });

        //MENU LABEL
        authenticationButton = new JButton("Authentication");
        authenticationButton.setBounds(240,570,300,45);
        authenticationButton.setForeground(Color.black.brighter());
        authenticationButton.setFont(new Font("Sans-serif",Font.BOLD,26));
        authenticationButton.setOpaque(true);
        authenticationButton.setContentAreaFilled(true);
        authenticationButton.setBackground(lightBlue);
        authenticationButton.setBorderPainted(true);
        authenticationButton.setFocusPainted(false);
        menuPanel.add(authenticationButton);

        noWebCamButton = new JButton("No access to webcam");
        noWebCamButton.setBounds(200,620,368,50);
        noWebCamButton.setFont(new Font("Sans-serif",Font.BOLD,15));
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
        passwordField.setBounds(284,662,200,30);
        passwordField.setVisible(false);
        menuPanel.add(passwordField);

        continueButton1 = new JButton("Continue");
        continueButton1.setBounds(330,695,100,30);
        continueButton1.setFont(new Font("Sans-serif",Font.BOLD,10));
        continueButton1.setVisible(false);
        menuPanel.add(continueButton1);

        detectedButton = new JButton("Hello human! I'd love to help you.");
        menuPanel.add(detectedButton);
        detectedButton.setVisible(false);
        detectedButton.setBounds(15,430,500,100);
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
        noAccessButton.setBounds(200,530,368,50);
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
            continueButton1.setVisible(true);

        });
        continueButton1.addActionListener(e -> {
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

            int scaledWidth = 180; // set the width to which you want to resize the image
            int scaledHeight = 180; // set the height to which you want to resize the image
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
        frame.setBounds((w -768)/2,(h -767)/2,768,765);
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
