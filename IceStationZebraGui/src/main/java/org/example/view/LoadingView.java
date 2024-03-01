package org.example.view;

import org.example.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoadingView extends JPanel {
    private final JButton changeBtn = new JButton("Click");
    private JProgressBar progressBar = new JProgressBar();
    public LoadingView(){
        ImageIcon imgIcon = new ImageIcon(Main.class.getResource("ISZ_icon.png"));
        JPanel container = new JPanel();
        JPanel barContainer = new JPanel();
        container.add(new JLabel(imgIcon));
        container.setPreferredSize(new Dimension(500,500));
        progressBar.setBounds(0,0,420,50);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setBackground(Color.RED);
        barContainer.add(progressBar);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.lightGray);
        this.add(container, BorderLayout.CENTER);
        this.add(barContainer, BorderLayout.SOUTH);

    }

    public void whenIsFilledBar(Runnable runnable){
        SwingUtilities.invokeLater(()->{
            new Thread(()->{
                int i = 0;
                while (i <= 100) {
                    this.progressBar.setValue(i);
                    this.progressBar.revalidate();
                    this.progressBar.repaint();
                    try {
                        Thread.sleep(500);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    i += 20;

                }
                runnable.run();
                System.out.println("DONE");
            }).start();



        });


    }

    public void setClickListener(ActionListener l){
        System.out.println(l.toString());
    }
}
