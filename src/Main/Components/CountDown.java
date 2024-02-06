package Main.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountDown extends JLabel {
    private final int COUNTDOWN_SECONDS;
    private final int TIMER_DELAY = 1000;
    private final JLabel label = this;
    private final Timer timer;

    int count = 1, seconds;

    public CountDown(int seconds) {
        COUNTDOWN_SECONDS = seconds;

        if (seconds > 0) {
            count *= -1;
        }

        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.setText(formatTime(COUNTDOWN_SECONDS));
        this.setForeground(Color.WHITE);

        initSeconds();

        timer = new Timer(TIMER_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSeconds(getSeconds() + count);
                label.setText(formatTime(getSeconds()));
            }
        });
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public void reset() {
        label.setText(formatTime(0));
    }

    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    public void initSeconds() {
        seconds = COUNTDOWN_SECONDS;
    }

    // Getters and Setters
    public int getTimeElapsed() {
        if (COUNTDOWN_SECONDS == 0) {
            return seconds;
        } else {
            return COUNTDOWN_SECONDS - seconds;
        }
    }

    public String getTimer() {
        return formatTime(seconds);
    }

    public void setSeconds(int s) {
        seconds = s;
    }

    public int getSeconds() {
        return seconds;
    }
}
