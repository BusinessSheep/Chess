package Game;

import static Main.gamePanel.TILE_SIZE;
import static Main.gamePanel.WIDTH_IN_TILES;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Piece {
    private int pieceType, pieceColour;
    private boolean firstMovePawn = true;
    public int x, y;
    private Rectangle hitbox;
    private boolean selected = false;
    private boolean[][] validMoves = new boolean[WIDTH_IN_TILES][WIDTH_IN_TILES];

    public static int drawOffset = TILE_SIZE/20;
    public static final int PIECE_SIZE = TILE_SIZE - drawOffset*2;

    public static final int WHITE = 0;
    public static final int BLACK = 1;

    public static final int PAWN = 0;
    public static final int ROOK = 1;
    public static final int BISHOP = 2;
    public static final int KNIGHT = 3;
    public static final int QUEEN = 4;
    public static final int KING = 5;


    public Piece(int x, int y, int pieceType, int pieceColour) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x*TILE_SIZE+drawOffset, y*TILE_SIZE+drawOffset, PIECE_SIZE, PIECE_SIZE);
        this.pieceType = pieceType;
        this.pieceColour = pieceColour;
    }

    public void draw(Graphics g) {

        if(!selected){
            g.setColor(pieceColour == WHITE ? Color.white : Color.black);
        }
        else {
            //drawValidMoves(g);
            g.setColor(Color.blue);
        } 
            
        g.fillRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        g.setColor(Color.gray);
        g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

        
        switch(pieceType) {
            case PAWN:
            g.drawString("PAWN", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
            case ROOK:
            g.drawString("ROOK", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
            case BISHOP:
            g.drawString("BISHOP", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
            case KNIGHT:
            g.drawString("KNIGHT", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
            case QUEEN:
            g.drawString("QUEEN", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
            case KING:
            g.drawString("KING", hitbox.x + TILE_SIZE/4, hitbox.y + TILE_SIZE/2);
            break;
        }

        
    
        
    }

    public void drawValidMoves(Graphics g, Piece[][] pieces) {
        for(int i = 0; i < validMoves.length; i++) {
            for(int j = 0; j < validMoves[i].length; j++) {
                if(validMoves[i][j]) {
                    g.setColor(Color.yellow);
                    if(pieces[i][j] != null) {
                        g.setColor(Color.red);
                    }
                    g.fillRect(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setValidMoves(boolean[][] validMoves) {
        this.validMoves = validMoves;
    }

    public boolean[][] getValidMoves() {
        return validMoves;
    }

    public boolean getFirstMovePawn() {
        return firstMovePawn;
    }

    public void setFirstMovePawn(boolean firstMovePawn) {
        this.firstMovePawn = firstMovePawn;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getPieceType() {
        return pieceType;
    }
    
    public int getPieceColour() {
       return pieceColour;
    }

    public void fixHitbox(int x, int y) {
        hitbox.x = x*TILE_SIZE+drawOffset;
        hitbox.y = y*TILE_SIZE+drawOffset;
    }

    

}
