package Main;

import java.util.Random;

public class Main {
    public static GameFrame gameFrame;

    public static void main(String[] args) {
        gameFrame = new GameFrame();
    }

    public static int random(int r) {
        Random rand = new Random();
        return rand.nextInt(r);
    }
}