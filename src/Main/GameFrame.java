package Main;

import Main.Panels.GamePanel;
import Main.Panels.MenuPanel;
import Main.Panels.StatPanel;
import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    // Frame Dimensions
    public static final int FRAME_WIDTH = 800, FRAME_HEIGHT = 800;
    public static final int FRAME_WIDTH_CENTER = FRAME_WIDTH / 2;
    public static final int FRAME_HEIGHT_CENTER = FRAME_HEIGHT / 2;

    // Screen Dimensions
    final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public final int SCREEN_WIDTH = SCREEN_SIZE.width;
    public final int SCREEN_HEIGHT = SCREEN_SIZE.height;

    // Panels
    public static MenuPanel menuPanel = new MenuPanel();
    public static GamePanel gamePanel = new GamePanel();
    public static StatPanel statPanel = new StatPanel();

    public GameFrame() {
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Stops program when program is closed

        // Layout manager and Layer
        this.setResizable(false); // Prevent screen from being resized
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        int x = (SCREEN_WIDTH - FRAME_WIDTH) / 2;
        this.setLocation(x, 80);

        showMenu();

        this.setVisible(true); // Make the program visible
    }

    public void showGame() {
        this.getContentPane().removeAll();
        this.add(statPanel, BorderLayout.NORTH);
        this.add(gamePanel, BorderLayout.CENTER);

        this.pack();
        this.revalidate();
        this.repaint();
    }

    public void showMenu() {
        this.getContentPane().removeAll();
        this.add(menuPanel, BorderLayout.CENTER);

        this.pack();
        this.revalidate();
        this.repaint();
        this.setLocation((SCREEN_WIDTH - menuPanel.getWidth()) / 2, (SCREEN_HEIGHT - menuPanel.getHeight()) / 2);
    }
}
