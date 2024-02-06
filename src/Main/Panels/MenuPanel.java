package Main.Panels;

import Main.Components.DifficultyButton;
import Main.Components.NumberInput;
import Main.Components.StartButton;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import static Main.GameFrame.*;

public class MenuPanel extends JPanel {
    public ButtonGroup difficultyButtons;
    NumberInput gridWidth, gridHeight, bombDensity;

    public MenuPanel() {
        // Set panel dimension and background color
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setBackground(new Color(0x212529));
        this.setLayout(null);

        // Start button
        JButton startButton = new StartButton("Start Game");

        // Difficulty buttons
        JRadioButton easyButton = new DifficultyButton("Easy", FRAME_WIDTH_CENTER - 80, FRAME_HEIGHT_CENTER - 30);
        JRadioButton mediumButton = new DifficultyButton("Medium", FRAME_WIDTH_CENTER - 80, FRAME_HEIGHT_CENTER);
        JRadioButton hardButton = new DifficultyButton("Hard", FRAME_WIDTH_CENTER - 80, FRAME_HEIGHT_CENTER + 30);
        JRadioButton customButton = new DifficultyButton("Custom", FRAME_WIDTH_CENTER - 80, FRAME_HEIGHT_CENTER + 60);

        // Difficulty actions
        easyButton.setActionCommand("easy");
        mediumButton.setActionCommand("medium");
        hardButton.setActionCommand("hard");
        customButton.setActionCommand("custom");

        // Set default difficulty
        mediumButton.setSelected(true);

        // Custom difficulty
        gridWidth = new NumberInput("Grid Length:" , FRAME_WIDTH_CENTER - 85, FRAME_HEIGHT_CENTER + 90);
        gridHeight = new NumberInput("Grid Height:" , FRAME_WIDTH_CENTER - 85, FRAME_HEIGHT_CENTER + 144);
        bombDensity = new NumberInput("Bomb Density:", FRAME_WIDTH_CENTER - 85, FRAME_HEIGHT_CENTER + 198);

        // Min and max inputs
        gridWidth.setInputMin(4);
        gridWidth.setInputMax(60);
        gridHeight.setInputMin(3);
        gridHeight.setInputMax(35);
        bombDensity.setInputMin(15);
        bombDensity.setInputMax(30);

        // Show number inputs if custom button is selected
        customButton.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                gridWidth.toggle(true);
                gridHeight.toggle(true);
                bombDensity.toggle(true);
            } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                gridWidth.toggle(false);
                gridHeight.toggle(false);
                bombDensity.toggle(false);
            }
        });

        // Radio button group
        difficultyButtons = new ButtonGroup();
        difficultyButtons.add(easyButton);
        difficultyButtons.add(mediumButton);
        difficultyButtons.add(hardButton);
        difficultyButtons.add(customButton);

        // Add components
        this.add(startButton);
        this.add(easyButton);
        this.add(mediumButton);
        this.add(hardButton);
        this.add(customButton);

        this.add(gridWidth);
        this.add(gridHeight);
        this.add(bombDensity);
    }

    public int[] getDifficulty() {
        int width = 6, height = 5, density = 5;
        ButtonModel selectedModel = difficultyButtons.getSelection();
        String selectedText = selectedModel.getActionCommand();

        switch (selectedText) {
            case "easy" -> {
                width = 12;
                height = 8;
                density = 8;
            }
            case "medium" -> {
                width = 20;
                height = 15;
                density = 13;
            }
            case "hard" -> {
                width = 30;
                height = 25;
                density = 15;
            }
            case "custom" -> {
                width = gridWidth.getInput();
                height = gridHeight.getInput();
                density = bombDensity.getInput();
            }
        }

        return new int[] {width, height, density};
    }
}
