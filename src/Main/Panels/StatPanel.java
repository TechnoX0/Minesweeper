package Main.Panels;

import Main.Components.CountDown;
import Main.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static Main.GameFrame.gamePanel;
import static Main.Main.gameFrame;

public class StatPanel extends JPanel implements ActionListener {
    Font font = new Font("Arial", Font.BOLD, 24), buttonFont = new Font("Ariel", Font.BOLD, 14);
    private int flags;

    CountDown countDown = new CountDown(0);

    JLabel flagLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JPanel buttonsPanel = new JPanel();

    JButton restartButton = new JButton();
    JButton menuButton = new JButton();

    Image restartButtonImage = new ImageIcon("src/Main/Images/Button - Restart.png").getImage();
    Image menuButtonImage = new ImageIcon("src/Main/Images/Button - Menu.png").getImage();

    Graphics2D g2D;
    Timer timer;

    public StatPanel() {
        // Set panel dimension and background color
        this.setBackground(Settings.primaryColor);
        this.setLayout(new BorderLayout(10, 0));

        flagLabel.setPreferredSize(new Dimension(80, 60));
        flagLabel.setHorizontalAlignment(SwingConstants.CENTER);
        flagLabel.setForeground(Color.WHITE);
        flagLabel.setFont(font);

        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(font);

        // Size
        restartButton.setPreferredSize(new Dimension(190, 60));
        restartButton.setContentAreaFilled(false);
        restartButton.setRolloverEnabled(false);

        menuButton.setPreferredSize(new Dimension(120, 60));
        menuButton.setContentAreaFilled(false);
        menuButton.setRolloverEnabled(false);

        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonsPanel.add(restartButton);
        buttonsPanel.add(menuButton);

        flagLabel.setText(String.valueOf(flags));

        restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gamePanel.restart();
            }
        });
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gameFrame.showMenu();
            }
        });

        // Adding to StatPanel
        this.add(flagLabel, BorderLayout.WEST);
        this.add(countDown, BorderLayout.CENTER);
        this.add(buttonsPanel, BorderLayout.EAST);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g2D = (Graphics2D) g;

        g2D.drawImage(menuButtonImage, this.getWidth() - menuButton.getWidth(), 0, menuButton.getWidth(), menuButton.getHeight(), null);
        g2D.drawImage(restartButtonImage, this.getWidth() - (restartButton.getWidth() + menuButton.getWidth()), 0, restartButton.getWidth(), restartButton.getHeight(), null);
    }

    public void startTimer() {
        countDown.startTimer();
    }

    public void stopTimer() {
        countDown.stopTimer();
    }

    public void resetTimer() {
        countDown.stopTimer();
        countDown.initSeconds();
        countDown.reset();
    }

    public void resetScore() {
        scoreLabel.setText("0");
    }

    public void showScore(int score) {
        scoreLabel.setText(countDown.getTimer() + " | Score: " + score);
        this.remove(countDown);
        this.add(scoreLabel, BorderLayout.CENTER);
        this.repaint();
    }

    public void showTimer() {
        this.remove(scoreLabel);
        this.add(countDown, BorderLayout.CENTER);
        this.repaint();
    }

    // Getters and Setters
    public int getFlag() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public void setFlagDisplay(int flags) {
        flagLabel.setText(String.valueOf(flags));
    }

    public int getTimeElapsed() {
        return countDown.getTimeElapsed();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
