package org.example.view;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.Utility.TerminalMessage;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Date;

public class TerminalView extends JPanel {
    private JTextPane textPane = new JTextPane();
    private StyledDocument doc;
    private Style style;
    public TerminalView(){
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(900,500));
        this.setupTextPane();

    }

    private void setupTextPane(){
        this.doc = textPane.getStyledDocument();
        this.style = textPane.addStyle("ColorStyle", null);
        StyleConstants.setForeground(style, Color.WHITE);

        this.textPane.setEditable(false);
        this.textPane.setBackground(Color.darkGray);
        this.textPane.setMargin(new Insets(10,10,10,10));
        JScrollPane scrollPane = new JScrollPane(this.textPane);
        this.add(scrollPane,BorderLayout.CENTER);
    }

    public void addLine(TerminalMessage tm) {
        StyleConstants.setForeground(style, tm.color);
        try {
            this.doc.insertString(doc.getLength(), "ISZ › " + tm.message+"\n", style);
            this.textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
