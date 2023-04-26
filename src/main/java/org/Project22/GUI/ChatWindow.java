package org.Project22.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Action;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.LayeredHighlighter;
import javax.swing.text.Position;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.View;

public class ChatWindow extends JTextPane {

    /**
     * Name shown above the prompts typed by the human.
     */
    public static final String HUMAN_NAME = " " + new String(Character.toChars(0x1F4AC)) + " HUMAN";

    /**
     * Name shown above the prompt given by the bot.
     */
    public static final String BOT_NAME = " " + new String(Character.toChars(0x1F916)) + " BOT";
    
    /**
     * Prefix used for when the user is typing.
     */
    public static final String HUMAN_PREFIX = " ";

    /**
     * Prefix used for when the bot is talking.
     */
    public static final String BOT_PREFIX = " ";

    /**
     * Attribute for left text alignment.
     */
    public static final SimpleAttributeSet LEFT = new SimpleAttributeSet();

    /**
     * Attribute for right text alignment.
     */
    public static final SimpleAttributeSet RIGHT = new SimpleAttributeSet();

    /**
     * Attribute name header.
     */
    public static final SimpleAttributeSet NAME_HEADER = new SimpleAttributeSet();

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
     * Action called when the up-key is pressed.
     */
    private UpDownAction upAction;

    /**
     * Action called when the down-key is pressed.
     */
    private UpDownAction downAction;

    /**
     * Underliner that highlights misspelled words.
     */
    private Underliner underliner;

    /**
     * Keeps track of where the user is allowed to type.
     */
    private int allowed_offset;

    /**
     * List that keeps track of the prompts the user has typed.
     */
    private List<String> promptHistory;

    /**
     * Keeps track of how many times the user has pressed up.
     */
    private int upCount;

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

        // set name header
        StyleConstants.setFontSize(NAME_HEADER, 12);
        StyleConstants.setForeground(NAME_HEADER, Color.DARK_GRAY);
        
        // initialize component
        this.setOpaque(false);
        this.setBackground(new Color(0,0,0,0));
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        this.promptHistory = new ArrayList<String>();
        this.underliner = new Underliner();
        this.setHighlighter(this.underliner);

        // change keybindings
        this.upAction = new UpDownAction(this, true);
        this.downAction = new UpDownAction(this, false);
        this.getInputMap().put(KeyStroke.getKeyStroke("UP"), "up_key_press");
        this.getInputMap().put(KeyStroke.getKeyStroke("DOWN"), "down_key_press");
        this.getActionMap().put("up_key_press", this.upAction);
        this.getActionMap().put("down_key_press", this.downAction);

        // initialize document
        this.doc = (StyledDocument)this.getDocument();
        ((DefaultStyledDocument)this.doc).setDocumentFilter(new ChatDocument(this));
        this.addText(HUMAN_NAME, NAME_HEADER);
        this.addText("\n" + HUMAN_PREFIX, LEFT);

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
    public void addText(String s, AttributeSet attr) {
        try {
            this.doc.insertString(this.doc.getLength(), s, attr);
        } catch (BadLocationException e) {e.printStackTrace();}
    }

    /**
     * Delete the prompt the user is currently typing.
     */
    public void deletePrompt() {
        int promptSize = this.doc.getLength() - this.allowed_offset;
        try {
            for (int i = 0; i < promptSize; i++) {
                this.doc.remove(this.doc.getLength() - 1, 1);
            }
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
                    input = input.replaceAll("[^a-zA-Z0-9 :+-]", "").toLowerCase();

                    // add prompt to history
                    if (!input.isBlank()) this.comp.promptHistory.add(input);

                    // process input
                    this.comp.addText("\n\n" + BOT_NAME, NAME_HEADER);
                    this.comp.changeAlignment(false);
                    this.comp.addText("\n" + BOT_PREFIX, null);
                    this.comp.addText(InputProcessor.process(input), null);
                    this.comp.addText("\n\n" + HUMAN_NAME, NAME_HEADER);
                    this.comp.changeAlignment(true);
                    this.comp.addText("\n" + HUMAN_PREFIX, LEFT);

                    // spelling check
                    int[][] misspelledWords = SpellingChecker.checkSpelling(input);
                    for (int[] word : misspelledWords) {
                        this.comp.underliner.addUnderline(allowed_offset + word[0], allowed_offset + word[1]);
                    }

                    // update document
                    this.comp.resetCursor();
                    this.comp.updateOffset();
                    this.comp.upCount = 0;
                } catch (BadLocationException e) {
                    e.printStackTrace();
                    System.out.println("(ignore)");
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

    /**
     * Action that allows the user to view their prompt history by pressing up or down.
     */
    private class UpDownAction implements Action {

        /**
         * Reference to the chat component.
         */
        private ChatWindow comp;

        /**
         * true if this action should be used for the up key, false if down.
         */
        private final boolean up;

        public UpDownAction(ChatWindow comp, boolean up) {
            // set components
            this.comp = comp;
            this.up = up;
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            // hardcoded patch
            if (this.comp.promptHistory.size() == 0) return;

            // only activate if mouse position is not before prompt
            if (this.comp.getCaretPosition() >= this.comp.allowed_offset) {
                // delete current prompt
                this.comp.deletePrompt();

                String prompt;

                // up
                if (up) {
                    if (this.comp.upCount < this.comp.promptHistory.size()) {
                        prompt = this.comp.promptHistory.get(this.comp.promptHistory.size() - 1 - this.comp.upCount);
                        this.comp.upCount++;
                    }
                    else {
                        prompt = this.comp.promptHistory.get(0);
                    }
                }

                //down
                else {
                    if (this.comp.upCount > 0) {
                        this.comp.upCount--;
                    }
                    
                    if (this.comp.upCount == 0) {
                        prompt = "";
                    }
                    else {
                        prompt = this.comp.promptHistory.get(this.comp.promptHistory.size() - this.comp.upCount);
                    }
                }

                // add prompt
                this.comp.addText(prompt, LEFT);
            }
        }

        @Override
        public Object getValue(String key) {return null;}

        @Override
        public void putValue(String key, Object value) {}

        @Override
        public void setEnabled(boolean b) {}

        @Override
        public boolean isEnabled() {return true;}

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {}

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {}
    }

    /**
     * Highlighter used for underlining text.
     * Source: http://www.java2s.com/Code/Java/Swing-JFC/JTextPaneHighlightExample.ht
     */
    private class Underliner extends DefaultHighlighter {

        /**
         * Painter used for underlining.
         */
        protected Highlighter.HighlightPainter painter = new UnderlinePainter();

        /**
         * Add an underline to the text component.
         * @param p0 the starting index of the underline
         * @param p1 the ending index of the underline
         * @throws BadLocationException
         */
        public void addUnderline(int p0, int p1) throws BadLocationException {
            super.addHighlight(p0, p1, painter);
        }
      
        /**
         * Painter used for underlining.
         */
        private static class UnderlinePainter extends LayeredHighlighter.LayerPainter {

            /**
             * Underline color.
             */
            protected final Color color = Color.ORANGE;
      
            @Override
            public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {}
      
            @Override
            public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
                g.setColor(color);
      
                Rectangle alloc = null;
                if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
                    if (bounds instanceof Rectangle) {
                        alloc = (Rectangle) bounds;
                    } else {
                        alloc = bounds.getBounds();
                    }
                } 
                else {
                    try {
                        Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
                        alloc = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
                    } catch (BadLocationException e) {return null;}
                }
      
                // draw the underline
                FontMetrics fm = c.getFontMetrics(c.getFont());
                int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
                g.drawLine(alloc.x, baseline + 1, alloc.x + alloc.width, baseline + 1);
                g.drawLine(alloc.x, baseline + 2, alloc.x + alloc.width, baseline + 2);
      
                return alloc;
            }
        }
    }
}
