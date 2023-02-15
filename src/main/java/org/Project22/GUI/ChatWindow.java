package org.Project22.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatWindow extends JTextPane {
    
    /**
     * Prefix used for when the user is typing.
     */
    public static final String HUMAN_PREFIX = "  " + new String(Character.toChars(0x1F4AC)) + "  ";

    /**
     * Prefix used for when the bot is talking.
     */
    public static final String BOT_PREFIX = "  " + new String(Character.toChars(0x1F916)) + "  ";

    /**
     * Attribute for left text alignment.
     */
    public static final SimpleAttributeSet LEFT = new SimpleAttributeSet();

    /**
     * Attribute for right test alignment.
     */
    public static final SimpleAttributeSet RIGHT = new SimpleAttributeSet();

    /**
     * Color used to fill the background with once the scrollpane activates.
     */
    public static final Color background_color = new Color(227, 227, 227);

    /**
     * Background image for the UI.
     */
    private Image background;

    /**
     * The document used to store the chat.
     */
    private StyledDocument doc;

    /**
     * Keeps track of where the user is allowed to type.
     */
    private int allowed_offset;

    public ChatWindow() {
        // load background
        Image i = null;
        try {
            i = ImageIO.read(new File("resources/background.png"));
        } catch (IOException e) {e.printStackTrace();}
        this.background = i;

        // set alignments
        StyleConstants.setAlignment(LEFT, StyleConstants.ALIGN_LEFT);
        StyleConstants.setAlignment(RIGHT, StyleConstants.ALIGN_RIGHT);
        
        // initialize component
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));

        // initialize document
        this.doc = (StyledDocument)this.getDocument();
        ((DefaultStyledDocument)this.doc).setDocumentFilter(new ChatDocument(this));
        this.addText(HUMAN_PREFIX);

        // update chat
        this.resetCursor();
        this.updateOffset();
    }

    @Override
    public void paintComponent(Graphics g) {
        // draw background
        g.setColor(background_color);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(this.background, 0, 0, this);
        super.paintComponent(g);
    }

    /**
     * Add text to the text pane.
     * @param s the text to add
     */
    public void addText(String s) {
        try {
            this.doc.insertString(this.doc.getLength(), s, null);
        } catch (BadLocationException e) {e.printStackTrace();}
    }

    /**
     * Update the allowed_offset field that determines where the user can type.
     */
    public void updateOffset() {
        this.allowed_offset = this.doc.getLength();
    }

    /**
     * Sets the cursor to after the last character in the document.
     */
    public void resetCursor() {
        this.setCaretPosition(this.doc.getLength());
    }

    /**
     * Change to current text alignment.
     * @param left true if left, else right
     */
    public void changeAlignment(boolean align_left) {
        this.doc.setParagraphAttributes(this.doc.getLength() + 1, 1, align_left ? LEFT : RIGHT, false);
    }

    /**
     * Custom document filter that allows the document to act like a chat.
     */
    private class ChatDocument extends DocumentFilter {

        /**
         * Reference to the chat component.
         */
        private ChatWindow comp;

        public ChatDocument(ChatWindow comp) {
            // set component
            this.comp = comp;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            // don't allow removing before the offset
            if (offset < this.comp.allowed_offset) {
                return;
            }

            super.remove(fb, offset, length);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            // if enter is pressed, process the input
            if (text == "\n") {
                try {
                    // get input string
                    String input = this.comp.doc.getText(allowed_offset, offset - allowed_offset);

                    // process input
                    this.comp.addText("\n\n" + BOT_PREFIX);
                    this.comp.addText(InputProcessor.process(input));
                    this.comp.changeAlignment(false);
                    this.comp.addText("\n\n" + HUMAN_PREFIX);
                    this.comp.changeAlignment(true);

                    // update document
                    this.comp.resetCursor();
                    this.comp.updateOffset();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                return;
            }

            // don't allow typing before prompt
            if (offset < this.comp.allowed_offset) {
                return;
            }

            super.replace(fb, offset, length, text, attrs);
        }
    }
}
