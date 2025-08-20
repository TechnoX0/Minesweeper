package Main.Components;

import Main.Panels.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static Main.GameFrame.gamePanel;

public class Tile extends JButton {
    public int ix, iy;
    public boolean isBomb, revealed, isFlagged;
    public int nearbyBombs;

    Image hidden = new ImageIcon("src/Main/Images/Tile - Hidden.png").getImage();
    public Image image = hidden;
    Image revealedState;

    public Tile(int x, int y, int ix, int iy, int width, int height) {
        this.setBounds(x, y, width, height);
        this.ix = ix;
        this.iy = iy;

        this.setBorder(null);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gamePanel.isGameOver()) return;

                if (!gamePanel.isGameStart()) {
                    gamePanel.startGame();
                }

                if (SwingUtilities.isRightMouseButton(e)) {
                    flag();
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    GamePanel.revealGrid(gamePanel.getTiles(), iy, ix, true);
                }

                gamePanel.update();
                gamePanel.checkWin();
            }
        });
    }

    public void reveal() {
        this.revealed = true;
        this.image = new ImageIcon("src/Main/Images/Tile - " + nearbyBombs + ".png").getImage();
    }

    public static void explode() {
        gamePanel.setGameLost(true);
        gamePanel.endGame();
    }

    public void flag() {
        if (this.revealed || gamePanel.getFlagged() >= gamePanel.getBombCount() && !this.isFlagged) return;

        boolean flagged = !isFlagged;
        isFlagged = !isFlagged;

        if (flagged) {
            this.setBackground(Color.RED);
            this.image = new ImageIcon("src/Main/Images/Tile - Flag.png").getImage();
        } else {
            this.image = hidden;
        }
    }

    // Getters and Setters
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setRevealedState(Image revealedState) {
        this.revealedState = revealedState;
    }

    public int getIx() {
        return ix;
    }

    public int getIy() {
        return iy;
    }
}
