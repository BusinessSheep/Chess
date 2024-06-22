package Inputs;

import static Main.gamePanel.TILE_SIZE;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import Game.Piece;
import Main.gamePanel;

public class MouseInput implements MouseInputListener {

    private gamePanel panel;
    private boolean pieceIsSelected = false;
    private Piece selectedPiece = null;

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
        Piece[][] pieces = panel.getChess().getPieces();
        if (!pieceIsSelected) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    Piece p = pieces[i][j];
                    if (p == null) {
                        continue;
                    } else if (p.getHitbox().contains(e.getX(), e.getY())
                            && p.getPieceColour() == panel.getChess().getTurn()) {
                        p.setSelected(true);
                        pieceIsSelected = true;
                        selectedPiece = p;
                        break;
                    }
                }
            }
        } else {
            // Where the piece is trying to move
            int moveX = (int) Math.floor(e.getX() / TILE_SIZE);
            int moveY = (int) Math.floor(e.getY() / TILE_SIZE);
            boolean validMove = selectedPiece.getValidMoves()[moveY][moveX]; // Is it a valid move
            
            pieceIsSelected = false;
            selectedPiece.setSelected(pieceIsSelected);
            if (validMove) { // Move the piece
                pieces[moveY][moveX] = selectedPiece;
                pieces[selectedPiece.y][selectedPiece.x] = null;
                selectedPiece.setFirstMovePawn(false);
                selectedPiece = null;
                pieces[moveY][moveX].fixHitbox(moveX, moveY);
                panel.getChess().setPieces(pieces);
                panel.getChess().setPositions();
                panel.getChess().setTurn((panel.getChess().getTurn() + 1) % 2); // set next turn
                panel.getChess().setValidMoves(pieces);
                panel.getChess().findKings(pieces);
                panel.getChess().setValidKingMoves(pieces, panel.getChess().getBlackKing());
                panel.getChess().setValidKingMoves(pieces, panel.getChess().getWhiteKing());
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
