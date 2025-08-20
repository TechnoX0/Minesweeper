package Main.Panels;

import Main.Components.Tile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import static Main.GameFrame.*;
import static Main.Main.gameFrame;
import static Main.Main.random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    // Game states
    private boolean gameStart = false, gameOver = false, gameLost;

    // Tiles
    private final ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
    private final ArrayList<Tile> bombTiles = new ArrayList<>();

    // Board
    private int gridWidth, gridHeight;
    private int bombDensity = 20, bombCount;
    private int tileSize, tileCount;

    Graphics2D g2D;
    Timer timer;

    public GamePanel() {
        // Set panel dimension and background color
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setBackground(new Color(0x212529));

        setLayout(null);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g2D = (Graphics2D) g;

        // Draws all tile sprites
        for (ArrayList<Tile> tile2D : tiles) {
            for (Tile tile1D : tile2D) {
                Image image = tile1D.getImage();
                g2D.drawImage(image, tile1D.getX(), tile1D.getY(), tileSize, tileSize, null);
            }
        }
    }

    public void defaultValues() {
        gameStart = false;
        gameOver = false;
        gameLost = false;

        statPanel.setFlags(bombCount);
        statPanel.setFlagDisplay(bombCount);

        statPanel.resetTimer();
        statPanel.resetScore();
    }

    public void initGame(int gridWidth, int gridHeight, int bombDensity) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;

        this.tileSize = FRAME_HEIGHT / gridHeight;
        this.tileCount = gridWidth * gridHeight;

        this.bombDensity = bombDensity;
        bombCount = (int) (tileCount * ((double) bombDensity / 100));

        // Initialize the board
        initGrid(gridWidth, gridHeight);
        initBomb(gridWidth, gridHeight);
        initBombCount(gridWidth, gridHeight);

        bonusTile();
        defaultValues();
    }

    public void bonusTile() {
        boolean bonus = true;
        while (bonus) {
            int randX = random(gridWidth - 1);
            int randY = random(gridHeight - 1);
            Tile tile = tiles.get(randY).get(randX);

            if (!tile.isBomb) {
                bonus = false;
                tile.revealed = true;
                tile.image = new ImageIcon("src/Main/Images/Tile - Bonus.png").getImage();
            }
        }
    }

    private void initGrid(int width, int height) {
        // Set minimum tile size
        if (tileSize < 25) {
            tileSize = 25;
        }

        // Change game width and height
        this.setPreferredSize(new Dimension(tileSize * width, tileSize * height));

        // Center game window to screen
        int newPosX = (gameFrame.SCREEN_WIDTH - tileSize * width) / 2;
        int newPosY = ((gameFrame.SCREEN_HEIGHT - tileSize * height) / 2) - 50;
        gameFrame.setLocation(newPosX, newPosY);

        // Loop through every tile in the gird
        for (int y = 0; y < height; y++) {
            ArrayList<Tile> row = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                Tile tile = new Tile(x * tileSize, y * tileSize, x, y, tileSize, tileSize);
                row.add(tile);
                this.add(tile);
            }
            tiles.add(row);
        }
    }

    private void initBomb(int gridWidth, int gridHeight) {
        int bombs = 0;

        while (bombs < bombCount) {
            // Get random bomb position
            int rx = random(gridWidth);
            int ry = random(gridHeight);
            Tile tile = tiles.get(ry).get(rx);

            // Make sure tile isn't already a bomb
            if (!tile.isBomb) {
                tile.isBomb = true;
                bombTiles.add(tile);
                tile.setRevealedState(new ImageIcon("src/Main/Images/Tile - Bomb.png").getImage());
                bombs++;
            }
        }
    }

    private void initBombCount(int gridWidth, int gridHeight) {
        for (Tile bombTile : bombTiles) {
            int row = bombTile.getIy();
            int col = bombTile.getIx();

            // Check surrounding bombs
            for (int y = row - 1; y <= row + 1; y++) {
                for (int x = col - 1; x <= col + 1; x++) {
                    if (y >= 0 && y < gridHeight && x >= 0 && x < gridWidth) {
                        Tile tile = tiles.get(y).get(x);

                        if (!tile.isBomb) {
                            tile.nearbyBombs++;
                        }
                    }
                }
            }
        }
    }

    public void restart() {
        if (!gameOver) return;

        // Resets all the values back to default
        defaultValues();

        // Shows the timer
        statPanel.showTimer();

        // Resets all tiles
        clearBoard();
        this.removeAll();
        initGame(gridWidth, gridHeight, bombDensity);

        this.repaint();
    }

    public void update() {
        int totalFlags = statPanel.getFlag();
        statPanel.setFlagDisplay(totalFlags - getFlagged());
    }

    public void checkWin() {
        if (gameOver) return;
        int revealed = getRevealed();
        int flagged = getFlagged();

        if (revealed + flagged == tileCount) {
            this.gameOver = true;
            endGame();
        }
    }

    public void startGame() {
        this.gameStart = true;
        statPanel.startTimer();
    }

    public void endGame() {
        gamePanel.gameOver = true;
        statPanel.stopTimer();

        int elapsed = statPanel.getTimeElapsed();
        int score = calculateScore(elapsed);

        if (gameLost) {
            statPanel.showScore(0);
        } else {
            statPanel.showScore(score);
        }

        revealBombs();
    }

    private int calculateScore(long elapsedTimeInSeconds) {
        int maxScore = tileCount * 5;
        long deductedScore = elapsedTimeInSeconds * 5;
        int score = (int) (maxScore - deductedScore);
        return Math.max(score, 0); // Ensure the score is non-negative
    }

    public static void revealGrid(ArrayList<ArrayList<Tile>> tiles, int row, int col, boolean origin) {
        Tile tile = tiles.get(row).get(col);

        // Reveal the current cell
        tile.reveal();

        if (tile.isBomb) {
            Tile.explode();
            return;
        }

        ArrayList<Tile> neighbors = getNeighbors(tiles, row, col);
        int flagged = getNeighborsFlagged(neighbors);

        for (Tile t : neighbors) {
            if (t.revealed || t.isFlagged) {
                continue;
            }

            if (tile.nearbyBombs == 0) {
                revealGrid(tiles, t.iy, t.ix, false);
                System.out.println("hello");
            } else if (flagged >= tile.nearbyBombs && origin) {
                System.out.println("hi");
                revealGrid(tiles, t.iy, t.ix, false);
            }
        }
    }

    public static ArrayList<Tile> getNeighbors(ArrayList<ArrayList<Tile>> grid, int row, int col) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        for (int y = -1; y <= 1; y++) {
            int rows = row + y;
            for (int x = -1; x <= 1; x++) {
                int cols = col + x;
                if ((cols >= 0 && cols < gamePanel.gridWidth) && (rows >= 0 && rows < gamePanel.gridHeight) && (cols != x || rows != y)) {
                    Tile tile = grid.get(rows).get(cols);

                    neighbors.add(tile);
                }
            }
        }

        return neighbors;
    }

    public static int getNeighborsFlagged(ArrayList<Tile> neighbors) {
        int flagged = 0;

        for (Tile tile : neighbors) {
            if (tile.isFlagged) {
                flagged++;
            }
        }

        return flagged;
    }

    public void revealBombs() {
        for (Tile bombTile : bombTiles) {
            if (bombTile.isFlagged) {
                bombTile.setImage(new ImageIcon("src/Main/Images/Tile - Bomb Flagged.png").getImage());
            } else {
                bombTile.setImage(new ImageIcon("src/Main/Images/Tile - Bomb Explode.png").getImage());
            }
        }
    }

    public void clearBoard() {
        tiles.clear();
        bombTiles.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // Getters and Setters
    public boolean isGameStart() {
        return gameStart;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    public ArrayList<ArrayList<Tile>> getTiles() {
        return tiles;
    }

    public int getBombCount() {
        return bombCount;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getRevealed() {
        int revealed = 0;
        for (ArrayList<Tile> tiles : tiles) {
            for (Tile tile : tiles) {
                if (tile.revealed) {
                    revealed++;
                }
            }
        }

        return revealed;
    }

    public int getFlagged() {
        int flagged = 0;
        for (ArrayList<Tile> tiles : tiles) {
            for (Tile tile : tiles) {
                if (tile.isFlagged) {
                    flagged++;
                }
            }
        }

        return flagged;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
