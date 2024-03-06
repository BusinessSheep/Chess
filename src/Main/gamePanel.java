package Main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Inputs.MouseInput;


public class gamePanel extends JPanel{
    private Chess chess;
    public static final int TILE_SIZE = 100;
    public static final int WIDTH_IN_TILES = 8;
    private static int WINDOW_SIZE = TILE_SIZE * WIDTH_IN_TILES;
    
    public gamePanel(Chess chess) {
        this.chess = chess;
        setFocusable(true);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        addMouseListener(new MouseInput(this));
    }

    public Chess getChess() {
        return chess;
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

    public int getWidthInTiles() {
        return WIDTH_IN_TILES;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        chess.draw(g);
    }

}
