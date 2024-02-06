package Main.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DifficultyButton extends JRadioButton {
    Font font = new Font("Arial", Font.BOLD, 18);

    public DifficultyButton(String str, int x, int y) {
        this.setBounds(x, y, 200, 30);

        this.setFont(font);
        this.setText(str);
        this.setForeground(Color.WHITE);

        this.addItemListener(e -> {
            JRadioButton source = (JRadioButton) e.getSource();
            if (e.getStateChange() == ItemEvent.SELECTED) {
                source.setForeground(new Color(0x008D00));
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                source.setForeground(Color.WHITE);
            }
        });

        this.setOpaque(false);
        this.setFocusPainted(false);
        this.setFocusable(false);
    }
}
