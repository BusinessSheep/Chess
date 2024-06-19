package Inputs;

import static Main.gamePanel.TILE_SIZE;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import Game.Piece;
import Main.gamePanel;

public class MouseInput implements MouseInputListener {

    private gamePanel panel;
    private boolean pieceIsSelected = false;
    private int selectedPiece = -1;

    public MouseInput(gamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!pieceIsSelected) {
            for (Piece p : panel.getChess().getPieces()) {
                if (p.getHitbox().contains(e.getX(), e.getY()) && p.getPieceColour() == panel.getChess().getTurn()) {
                    p.setSelected(true);
                    pieceIsSelected = true;
                    selectedPiece = panel.getChess().getPieces().indexOf(p);
                }
            }
        } else {
            Piece pieceToMove = panel.getChess().getPieces().get(selectedPiece);
            // Where the piece is trying to move
            int moveX = (int) Math.ceil(e.getX() / TILE_SIZE) * TILE_SIZE;
            int moveY = (int) Math.ceil(e.getY() / TILE_SIZE) * TILE_SIZE;
            boolean validMove = false; // Is it a valid move
            switch (pieceToMove.getPieceType()) {
                case Piece.PAWN: // Move a pawn
                    validMove = isValidPawnMove(pieceToMove, moveX, moveY);
                    break;
                case Piece.ROOK:
                    validMove = isValidRookMove(pieceToMove, moveX, moveY);
                    break;
                case Piece.BISHOP:
                    validMove = isValidBishopMove(pieceToMove, moveX, moveY);
                    break;
            }
            pieceIsSelected = false;
            pieceToMove.setSelected(pieceIsSelected);
            if (validMove) { // Move the piece
                pieceToMove.x = moveX;
                pieceToMove.y = moveY;
                pieceToMove.fixHitbox(pieceToMove.x, pieceToMove.y);
                panel.getChess().setTurn((panel.getChess().getTurn() + 1) % 2); // set next turn
            }
        }
    }

    private boolean isValidPawnMove(Piece pieceToMove, int moveX, int moveY) {
        boolean validMove = false;
        boolean overLapping = false;
        int direction = pieceToMove.getPieceColour() == Piece.BLACK ? 1 : -1; // Black moves down, White moves up
    
        // Check for regular movement
        if (moveX == pieceToMove.x && moveY == pieceToMove.y + direction * TILE_SIZE ||
            (pieceToMove.getFirstMovePawn() && moveY == pieceToMove.y + 2 * direction * TILE_SIZE && moveX == pieceToMove.x)) {
            
            for (Piece p : panel.getChess().getPieces()) { // Checking if the move overlaps any other pieces
                if (p.x == moveX && p.y == moveY && p != pieceToMove) {
                    overLapping = true;
                    break;
                }
            }
            if (!overLapping) {
                validMove = true;
            }
        } else if ((moveX == pieceToMove.x - TILE_SIZE || moveX == pieceToMove.x + TILE_SIZE)
                   && moveY == pieceToMove.y + direction * TILE_SIZE) { // Check for capture movement
            for (Piece p : panel.getChess().getPieces()) { // Checking if it actually would capture a piece
                if (p.getPieceColour() != pieceToMove.getPieceColour() && p.x == moveX && p.y == moveY) {
                    validMove = true;
                    panel.getChess().getPieces().remove(p);
                    break;
                }
            }
        }
    
        return validMove;
    }

    private boolean isValidRookMove(Piece pieceToMove, int moveX, int moveY) {
        boolean validMove = false;
        int direction; // Up or Down/Left or Right

        if(moveX == pieceToMove.x && moveY != pieceToMove.y) { // Vertical move
            direction = moveY < pieceToMove.y ? -1 : 1; // Moving up or down
            validMove = true;
            for(Piece p : panel.getChess().getPieces()) {
                if(p != pieceToMove && p.x == pieceToMove.x) { // Check for capture
                    if((direction == -1 && p.y >= moveY) || (direction == 1 && p.y <= moveY)) { // Check if the move would intersect another piece in the column
                        if(p.getPieceColour() != pieceToMove.getPieceColour() && p.y == moveY) { // Check capture 
                            panel.getChess().getPieces().remove(p);
                            break;
                        }
                        validMove = false;
                        break;
                    }                     
                }
            }
        } else if(moveX != pieceToMove.x && moveY == pieceToMove.y) { // Horizontal move
            direction = moveX < pieceToMove.x ? -1 : 1; // Moving left or right
            validMove = true;
            for(Piece p : panel.getChess().getPieces()) {
                if(p != pieceToMove && p.y == pieceToMove.y) {
                    if((direction == -1 && p.x >= moveX) || (direction == 1 && p.x <= moveX)) { // Check if the move would intersect another piece in the row
                        if(p.getPieceColour() != pieceToMove.getPieceColour() && p.x == moveX) {
                            panel.getChess().getPieces().remove(p);
                            break;
                        } 
                        validMove = false;
                        break;
                    }
                }
            }
        }
        return validMove;
    }

    private boolean isValidBishopMove(Piece pieceToMove, int moveX, int moveY) {
        boolean validMove = false;
        int xDir = moveX < pieceToMove.x ? -1 : 1; // Left or Right?
        int yDir = moveY < pieceToMove.y ? -1 : 1; // Up or Down?
        
        if(Math.abs(moveX - pieceToMove.x) == Math.abs(moveY - pieceToMove.y)) {
            for(Piece p : panel.getChess().getPieces()) {
                // do something similar to the regular movement, but probably have to incorporate direction
            }
            validMove = true;
        }

        return validMove;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}
