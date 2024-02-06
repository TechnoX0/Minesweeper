package Main.Components;

import javax.swing.*;
import java.awt.*;

import static Main.GameFrame.*;
import static Main.Main.gameFrame;

public class StartButton extends JButton {
    public StartButton(String str) {
        this.setBounds(FRAME_WIDTH_CENTER - 100, FRAME_HEIGHT_CENTER - 100, 200, 50);

        Font font = new Font("Arial", Font.BOLD, 24);
        this.setBackground(new Color(0x008D00));
        this.setForeground(Color.WHITE);
        this.setFont(font);
        this.setText(str);

        this.addActionListener(e -> {
            int[] difficulty = menuPanel.getDifficulty();

            gamePanel.defaultValues();
            gamePanel.clearBoard();
            gamePanel.removeAll();

            gamePanel.initGame(difficulty[0], difficulty[1], difficulty[2]);
            gamePanel.repaint();
            gameFrame.showGame();
        });

        this.setFocusPainted(false);
        this.setFocusable(false);
        this.setBorder(null);
    }
}
