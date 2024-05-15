package org.example.view;

import io.reactivex.rxjava3.core.Observable;
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;

public class TerminalView extends JPanel {
    private JTextPane textPane = new JTextPane();
    private JTextField inputField = new JTextField();
    private StyledDocument doc;
    private Style style;
    private Observable<String> terminalInputFieldObservable;
    public TerminalView(){
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(900,500));
        this.setupTextPane();
        this.setupInputObservable();
    }
    public Observable<String> getTerminalInputFieldObservable(){
        return this.terminalInputFieldObservable;
    }
    private void setupInputObservable(){
        this.terminalInputFieldObservable = Observable.create(emitter -> {
            this.inputField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        emitter.onNext(inputField.getText());
                    }
                }
            });
        });
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
        this.add(this.inputField, BorderLayout.SOUTH);
    }

    public void addLine(TerminalMessage tm) {
        StyleConstants.setForeground(style, tm.getColor());
        try {
            this.doc.insertString(doc.getLength(), "ISZ › " + tm.getMessage()+"\n", style);
            this.textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
