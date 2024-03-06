package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import Game.Piece;

import static Main.gamePanel.TILE_SIZE;
import static Main.gamePanel.WIDTH_IN_TILES;

public class Chess implements Runnable{
    private gamePanel panel;
    private Thread gameThread;
    private final int FPS = 10;
    private ArrayList<Piece> pieces;
    private int turn = 0;

    public Chess() {
        panel = new gamePanel(this);
        new gameFrame(panel);
        initialize();
        panel.setFocusable(true);
        panel.requestFocus();
        panel.setBackground(Color.white);
        
        startGame();
    }
    
public void initialize() {
    pieces = new ArrayList<Piece>();
    for(int i = 0; i < 8; i++) {
        pieces.add (new Piece(i*TILE_SIZE, TILE_SIZE, Piece.PAWN, Piece.BLACK));
        pieces.add(new Piece(i * TILE_SIZE, (WIDTH_IN_TILES-2)*TILE_SIZE, Piece.PAWN, Piece.WHITE));
    }

}

public void startGame() {
    gameThread = new Thread(this);
    gameThread.start();
}


public void draw(Graphics g) {
    for(int i = 0; i < WIDTH_IN_TILES; i++) {
        for(int j = 0; j < WIDTH_IN_TILES; j++) {
            g.setColor(Color.white);
            if((i+j) % 2 == 0) {
                g.setColor(Color.darkGray);
            }
            g.fillRect(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
    for(Piece p : pieces) {
        p.draw(g);
    }
}

public void update() {

}

public ArrayList<Piece> getPieces() {
    return pieces;
}

public int getTurn() {
    return turn;
}

public void setTurn(int turn) {
    this.turn = turn;
}

@Override
	public void run() {
		double frameTime = 1000000000.0 / FPS;

        long lastTime = System.nanoTime();

        double timeSinceLastFrame = 0;

        while (true) {
            long currentTime = System.nanoTime();

            timeSinceLastFrame += (currentTime - lastTime) / frameTime;
            lastTime = currentTime;
            if (timeSinceLastFrame >= 1) { 
            	update();
                panel.repaint();
                timeSinceLastFrame--; // Don't set to 0 as a means of catching up if frames are lost
            }
        }
	}


}
