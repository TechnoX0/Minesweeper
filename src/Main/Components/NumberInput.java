package Main.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumberInput extends JPanel {
    int inputMax = 80, inputMin = 5;

    JLabel label = new JLabel();
    JTextField field = new JTextField();

    public NumberInput(String text, int x, int y) {
        this.setBackground(new Color(0x212529));
        Font font = new Font("Arial", Font.BOLD, 14);

        field.setLayout(null);
        field.setForeground(Color.GRAY);
        label.setLayout(null);

        label.setText(text);
        label.setBounds(0, 0, 100, 30);
        label.setFont(font);
        label.setForeground(Color.WHITE);

        field.setBounds(10, 30, 100, 24);
        field.setFont(font);
        field.addKeyListener(validation);
        field.addFocusListener(placeHolder);

        this.add(label);
        this.add(field);

        this.setBounds(x, y, 200, 54);
        this.setLayout(null);
        this.setVisible(false);
    }

    KeyListener validation = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            JTextField source = (JTextField) e.getSource();
            int c = e.getKeyCode();
            System.out.println(c);
            source.setEditable(Character.isDigit(c) || c == 8);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            JTextField source = (JTextField) e.getSource();

            long input = 0;
            if (!source.getText().equals("")) {
                input = Integer.parseInt(source.getText());
            }

            if (input >= inputMax) {
                source.setText("");
                source.setText(String.valueOf(inputMax));
            }
        }
    };

    FocusListener placeHolder = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(getPlaceholderText())) {
                field.setText("");
                field.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (field.getText().isEmpty()) {
                field.setText(getPlaceholderText());
                field.setForeground(Color.GRAY);
            }
        }
    };

    public void toggle(boolean toggle) {
        this.setVisible(toggle);
        this.setEnabled(toggle);
    }

    // Getters and Setters
    public String getPlaceholderText() {
        return getInputMin() + " - " + getInputMax();
    }

    public int getInput() {
        int input = 0;
        try {
            if (!field.getText().equals("")) {
                input = Integer.parseInt(field.getText());
            }
        } catch (NumberFormatException e) {
            input = inputMin;
        }
        return input;
    }

    public int getInputMax() {
        return inputMax;
    }

    public void setInputMax(int inputMax) {
        this.inputMax = inputMax;
        field.setText(getPlaceholderText());
    }

    public int getInputMin() {
        return inputMin;
    }

    public void setInputMin(int inputMin) {
        this.inputMin = inputMin;
        field.setText(getPlaceholderText());
    }
}
