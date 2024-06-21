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

    

    // private boolean isValidPawnMove(Piece pieceToMove, Piece[][] pieces, int moveX, int moveY) {
    //     boolean validMove = false;
    //     boolean overLapping = false;
    //     int direction = pieceToMove.getPieceColour() == Piece.BLACK ? 1 : -1; // Black moves down, White moves up

    //     // Check for regular movement
    //     if (moveX == pieceToMove.x && moveY == pieceToMove.y + direction) {
    //         for (int i = 0; i < pieces.length; i++) {
    //             for (int j = 0; j < pieces[i].length; j++) {
    //                 Piece p = pieces[i][j];
    //                 if (p == null) {
    //                     continue;
    //                 } // Checking if the move overlaps any other pieces
    //                 else if (p != pieceToMove && (p.x == moveX && p.y == moveY)) {
    //                     overLapping = true;
    //                     break;
    //                 }
    //             }
    //         }
    //         if (!overLapping) {
    //             pieceToMove.setFirstMovePawn(false);
    //             validMove = true;
    //         }
    //     } else if((pieceToMove.getFirstMovePawn() && moveY == pieceToMove.y + 2 * direction && moveX == pieceToMove.x)) {
    //         if(pieces[moveY - direction][moveX] != null || pieces[moveY][moveX] != null) {
    //             overLapping = true;
    //         }
    //         if (!overLapping) {
    //             pieceToMove.setFirstMovePawn(false);
    //             validMove = true;
    //         }
    //     } 
        
    //     else if ((moveX == pieceToMove.x - 1 || moveX == pieceToMove.x + 1)
    //             && moveY == pieceToMove.y + direction) { // Check for capture movement
    //         for (int i = 0; i < pieces.length; i++) {
    //             for (int j = 0; j < pieces[i].length; j++) {
    //                 Piece p = pieces[i][j];
    //                 if (p == null) {
    //                     continue; // Checking if it actually would capture a piece
    //                 }
    //                 if (p.getPieceColour() != pieceToMove.getPieceColour() && j == moveX && i == moveY) {
    //                     validMove = true;
    //                     pieces[i][j] = null;
    //                     break;
    //                 }
    //             }
    //         }
    //     }

    //     return validMove;
    // }

    // private boolean isValidRookMove(Piece pieceToMove, Piece[][] pieces, int moveX, int moveY) {
    //     boolean validMove = false;
    //     int direction; // Up or Down/Left or Right

    //     if (moveX == pieceToMove.x && moveY != pieceToMove.y) { // Vertical move
    //         direction = moveY < pieceToMove.y ? -1 : 1; // Moving up or down
    //         validMove = true;
    //         for (int i = 0; i < pieces.length; i++) {
    //             for (int j = 0; j < pieces[i].length; j++) {
    //                 Piece p = pieces[i][j];
    //                 if (p == null) {
    //                     continue;
    //                 }
    //                 if (p != pieceToMove && p.x == pieceToMove.x) { // Check for capture
    //                     // Check if themove would intersect another piece in the column
    //                     if ((direction == -1 && pieceToMove.y > p.y && p.y >= moveY) // Check up
    //                     || (direction == 1 && pieceToMove.y < p.y && p.y <= moveY)) { // Check down
    //                         if (p.getPieceColour() != pieceToMove.getPieceColour() && p.y == moveY) { // Check capture
    //                             pieces[i][j] = null; // Delete piece then break out of loop
    //                             break;
    //                         }
    //                         validMove = false;
    //                         break;
    //                     }
    //                 }
    //             }
    //         }
    //     } else if (moveX != pieceToMove.x && moveY == pieceToMove.y) { // Horizontal move
    //         direction = moveX < pieceToMove.x ? -1 : 1; // Moving left or right
    //         validMove = true;
    //         for (int i = 0; i < pieces.length; i++) {
    //             for (int j = 0; j < pieces[i].length; j++) {
    //                 Piece p = pieces[i][j];
    //                 if (p == null) {
    //                     continue;
    //                 }
    //             if (p != pieceToMove && p.y == pieceToMove.y) { // Check capture
    //                 // Check if the move would intersect another piece in the row
    //                 if ((direction == -1 && pieceToMove.x > p.x && p.x >= moveX) // Check left
    //                 || (direction == 1 && pieceToMove.x < p.x && p.x <= moveX)) { // Check right
    //                     if (p.getPieceColour() != pieceToMove.getPieceColour() && p.x == moveX) {
    //                         pieces[i][j] = null;
    //                         break;
    //                     }
    //                     validMove = false;
    //                     break;
    //                 }
    //             }
    //         }
    //     }
    // }
    //     return validMove;
    // }

    // private boolean isValidBishopMove(Piece pieceToMove, Piece[][] pieces, int moveX, int moveY) {
    //     boolean validMove = false;
    //     int xDir = moveX < pieceToMove.x ? -1 : 1; // Left or Right?
    //     int yDir = moveY < pieceToMove.y ? -1 : 1; // Up or Down?

    //     if(Math.abs(moveX - pieceToMove.x) == 0) {
    //         return false;
    //     }

    //     if (Math.abs(moveX - pieceToMove.x) == Math.abs(moveY - pieceToMove.y)) {
    //         validMove = true;
    //         for(int i = 1; i <= Math.abs(moveX - pieceToMove.x); i++) {
    //             Piece p = pieces[pieceToMove.y + i * yDir][pieceToMove.x + i * xDir];
    //             if(p != null) {
    //                 if(p.getPieceColour() != pieceToMove.getPieceColour() && i == Math.abs(moveX - pieceToMove.x)) {
    //                     pieces[moveY][moveX] = null;
    //                     break;
    //                 }
    //                 validMove = false;
    //                 break;
    //             }
                
    //         }
    //     }

    //     return validMove;
    // }

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
