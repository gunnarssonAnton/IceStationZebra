package org.example;

import com.apple.eawt.ApplicationAdapter;
import io.reactivex.rxjava3.core.Observable;
import org.example.Utility.TerminalMessage;
import org.example.controller.CompilationViewController;
import org.example.controller.ExecutionViewController;
import org.example.controller.TerminalViewController;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Date;

public class Gui extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cards = new JPanel(cardLayout);
    private final TerminalViewController terminalViewController = new TerminalViewController();
    private final CompilationViewController compilationViewController = new CompilationViewController(this.terminalViewController.getSubject());
    private final ExecutionViewController executionViewController = new ExecutionViewController(this.terminalViewController.getSubject());
    public Gui(){
        // Icon
        System.out.println(Main.class.getResource("ISZ_icon.png").getPath());
        ImageIcon imgIcon = new ImageIcon(Main.class.getResource("ISZ_icon.png"));
        //loading an image from a file
        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final URL imageResource = Main.class.getClassLoader().getResource("org/example/ISZ_icon.png");
        final Image image = defaultToolkit.getImage(imageResource);

        //this is new since JDK 9
        final Taskbar taskbar = Taskbar.getTaskbar();

        try {
            //set icon for mac os (and other systems which do support this method)
            taskbar.setIconImage(image);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }
        this.setIconImage(imgIcon.getImage());
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("org/example/ISZ_icon.png")));
        this.pack();
        this.setSize(1000,1000);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setTitle("Ice Station Zebra");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        //this.setLocation(-80,-1200);

        this.cards.add(compilationViewController.getView());
        this.cards.add(executionViewController.getView());
        this.add(this.cards);

        this.add(this.terminalViewController.getView(),BorderLayout.SOUTH);
        this.terminalViewController.getSubject().onNext(new TerminalMessage(new Date().toString(),Color.PINK));
        Observable<String> terminalInput = this.terminalViewController.getView().getTerminalInputFieldObservable();
        compilationViewController.setTerminalInput(terminalInput);
        this.compilationViewController
                .getView()
                .setOnClick(e-> this.slideOut());
        this.executionViewController
                .getView()
                .setOnClick(e -> this.slideIn());
    }

    private void slideIn() {
        Rectangle bounds = cards.getBounds();
        bounds.x = 0;
        cards.setBounds(bounds);
        animateSlide(1000,false, 100);

    }

    private void slideOut() {
        Rectangle bounds = cards.getBounds();
        bounds.x = 0;
        cards.setBounds(bounds);
        animateSlide(-1000,true, -100);
    }

    private void animateSlide(int targetX, boolean isSlideOut, int dx) {
        Timer timer = new Timer(2, e -> {
            Rectangle bounds = cards.getBounds();
            if (bounds.x == targetX) {
                if(isSlideOut){
                    cardLayout.next(cards);
                }
                else{
                    cardLayout.previous(cards);
                }
                ((Timer) e.getSource()).stop();
            } else {
                bounds.x += dx;
                cards.setBounds(bounds);
            }
        });
        timer.start();
    }
}
