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
            boolean overLapping = false; // Is the attempted move overlapping another piece?
            switch (pieceToMove.getPieceType()) {
                case Piece.PAWN: // Move a pawn
                    if (pieceToMove.getPieceColour() == Piece.BLACK) { // Move black pawn
                        // Checking for regular movement (since pawns move different to kill)
                        if (moveX == pieceToMove.x && moveY > pieceToMove.y && moveY <= pieceToMove.y
                                + (pieceToMove.getFirstMovePawn() ? 2 * TILE_SIZE : TILE_SIZE)) {
                            for (Piece p : panel.getChess().getPieces()) { // Checking if the move overlaps any other pieces
                                if (p.x == moveX && p.y == moveY && p != pieceToMove) {
                                    overLapping = true;
                                    break;
                                }
                            }
                            if (!overLapping) {
                                pieceToMove.setFirstMovePawn(false);
                                validMove = true;
                            }
                            // Checking for kill movement
                        } else if ((moveX == pieceToMove.x - TILE_SIZE || moveX == pieceToMove.x + TILE_SIZE)
                                && moveY >= pieceToMove.y && moveY <= pieceToMove.y + TILE_SIZE) {
                            for (Piece p : panel.getChess().getPieces()) { // Checking if it actual would kill a piece
                                if (p.getPieceColour() != pieceToMove.getPieceColour() && p.x == moveX
                                        && p.y == moveY) {
                                    validMove = true;
                                    panel.getChess().getPieces().remove(p);
                                    break;
                                }
                            }
                        }
                    } else { // Move black pawns
                        // Checking for regular movement (since pawns move different to kill)
                        if (moveX == pieceToMove.x && moveY < pieceToMove.y && moveY >= pieceToMove.y
                                - (pieceToMove.getFirstMovePawn() ? 2 * TILE_SIZE : TILE_SIZE)) {
                            for (Piece p : panel.getChess().getPieces()) { // Checking if the move overlaps any other pieces
                                if (p.x == moveX && p.y == moveY) {
                                    overLapping = true;
                                    break;
                                }
                            }
                            if (!overLapping) {
                                pieceToMove.setFirstMovePawn(false);
                                validMove = true;
                            }
                            // Checking for kill movement
                        } else if ((moveX == pieceToMove.x - TILE_SIZE || moveX == pieceToMove.x + TILE_SIZE)
                                && moveY <= pieceToMove.y && moveY >= pieceToMove.y - TILE_SIZE) {
                            for (Piece p : panel.getChess().getPieces()) { // Checking if it actual would kill a piece
                                if (p.getPieceColour() != pieceToMove.getPieceColour() && p.x == moveX
                                        && p.y == moveY) {
                                    validMove = true;
                                    panel.getChess().getPieces().remove(p);
                                    break;
                                }
                            }
                        }
                    }
                    break;
            }
            pieceIsSelected = false;
            pieceToMove.setSelected(pieceIsSelected);
            if (validMove) { // Move the piece
                pieceToMove.x = (int) Math.ceil(e.getX() / TILE_SIZE) * TILE_SIZE;
                pieceToMove.y = (int) Math.ceil(e.getY() / TILE_SIZE) * TILE_SIZE;
                pieceToMove.fixHitbox(pieceToMove.x, pieceToMove.y);
                panel.getChess().setTurn((panel.getChess().getTurn() + 1) % 2); // set next turn
            }
        }
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
