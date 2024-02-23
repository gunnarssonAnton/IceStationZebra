package org.example.Utility;

import javax.swing.text.*;
import java.awt.*;

public class BashSyntaxHighlighting extends DefaultStyledDocument{

    private final String[] BASH_KEYWORDS = {
            "apt","cd","ls","pwd",
            "mkdir","rm","cp","mv","touch","cat","grep",
            "echo","chmod","chown","sudo","su","man","df",
            "du","tar","find","sort","wc","tee","head","tail"
    };

    public BashSyntaxHighlighting() {
        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setForeground(defaultStyle, Color.BLACK);
        setParagraphAttributes(0, getLength(), defaultStyle, true);
    }



    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offset, str, a);
        updateSyntaxHighlighting();
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        updateSyntaxHighlighting();
    }

    private void updateSyntaxHighlighting() throws BadLocationException {
        String text = getText(0, getLength());
        SimpleAttributeSet keywordAttr = new SimpleAttributeSet();
        StyleConstants.setForeground(keywordAttr, Color.BLUE);

        for (String keyword : BASH_KEYWORDS) {
            highlightKeyword(text, keyword, keywordAttr);
        }
    }

    private void highlightKeyword(String text, String keyword, AttributeSet attr) {
        int pos = 0;
        while ((pos = text.indexOf(keyword, pos)) >= 0) {
            setCharacterAttributes(pos, keyword.length(), attr, false);
            pos += keyword.length();
        }
    }

}
