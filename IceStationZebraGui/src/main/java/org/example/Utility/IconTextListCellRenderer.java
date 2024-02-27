package org.example.Utility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Class used when an icon is going to be displayed beside the label in a JList
 */
public class IconTextListCellRenderer extends JPanel implements ListCellRenderer<String> {
    private final JLabel textLabel;
    private final JLabel iconLabel;
    private final Icon icon;
    public IconTextListCellRenderer(Icon icon){
        this.icon = icon;
        this.setLayout(new BorderLayout());

        this.textLabel = new JLabel();
        this.textLabel.setBorder(new EmptyBorder(5,10,5,5));
        this.add(this.textLabel, BorderLayout.WEST);


        this.iconLabel = new JLabel();
        this.iconLabel.setBorder(new EmptyBorder(5,10,5,10));
        this.iconLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        this.add(this.iconLabel,BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        this.textLabel.setText(value);
        this.iconLabel.setIcon(this.icon);


            if(isSelected) {
                setBackground(list.getSelectionBackground());
                textLabel.setForeground(list.getSelectionForeground());
                iconLabel.setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                textLabel.setForeground(list.getForeground());
                iconLabel.setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            return this;
    }

}
