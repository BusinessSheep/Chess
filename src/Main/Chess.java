package Main;

import java.awt.Color;
import java.awt.Graphics;

import Game.Piece;

import static Main.gamePanel.TILE_SIZE;
import static Main.gamePanel.WIDTH_IN_TILES;

public class Chess implements Runnable {
    private gamePanel panel;
    private Thread gameThread;
    private final int FPS = 10;
    private Piece[][] pieces;
    private int turn = 0;
    private boolean check = false;
    private Piece blackKing = null;
    private Piece whiteKing = null;

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
        pieces = new Piece[8][8];

        for (int i = 0; i < 8; i++) { // Pawns
            pieces[1][i] = new Piece(i, 1, Piece.PAWN, Piece.BLACK);
            pieces[6][i] = new Piece(i, WIDTH_IN_TILES - 2, Piece.PAWN, Piece.WHITE);
        }

        for (int i = 0; i < 2; i++) { // Rooks
            pieces[0][7 * i] = new Piece(i * (WIDTH_IN_TILES - 1), 0, Piece.ROOK, Piece.BLACK);
            pieces[7][7 * i] = new Piece(i * (WIDTH_IN_TILES - 1), WIDTH_IN_TILES - 1, Piece.ROOK, Piece.WHITE);
        }

        // Bishops
        for (int i = 0; i < 2; i++) {
            pieces[i * (WIDTH_IN_TILES - 1)][2] = new Piece(2, i * (WIDTH_IN_TILES - 1), Piece.BISHOP, 1 - i);
            pieces[i * (WIDTH_IN_TILES - 1)][5] = new Piece(5, i * (WIDTH_IN_TILES - 1), Piece.BISHOP, 1 - i);
        }

        // Knights
        for (int i = 0; i < 2; i++) {
            pieces[i * (WIDTH_IN_TILES - 1)][1] = new Piece(1, i * (WIDTH_IN_TILES - 1), Piece.KNIGHT, 1 - i);
            pieces[i * (WIDTH_IN_TILES - 1)][6] = new Piece(6, i * (WIDTH_IN_TILES - 1), Piece.KNIGHT, 1 - i);
        }

        // Queens
        pieces[0][3] = new Piece(3, 0, Piece.QUEEN, Piece.BLACK);
        pieces[7][3] = new Piece(3, 7, Piece.QUEEN, Piece.WHITE);

        // Kings
        pieces[0][4] = new Piece(4, 0, Piece.KING, Piece.BLACK);
        pieces[7][4] = new Piece(4, 7, Piece.KING, Piece.WHITE);
        blackKing = pieces[0][4];
        whiteKing = pieces[7][4];

        setValidMoves(pieces);

        setValidKingMoves(pieces, blackKing);
        setValidKingMoves(pieces, whiteKing);
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void draw(Graphics g) {
        for (int i = 0; i < WIDTH_IN_TILES; i++) {
            for (int j = 0; j < WIDTH_IN_TILES; j++) {
                g.setColor(Color.white);
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.darkGray);
                }
                g.fillRect(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] != null && pieces[i][j].getSelected()) {
                    pieces[i][j].drawValidMoves(g, pieces);
                }
            }
        }
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] != null) {
                    pieces[i][j].draw(g);
                }
            }
        }
    }
    public void setValidMoves(Piece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece p = pieces[i][j];
                boolean[][] validMoves = new boolean[pieces.length][pieces.length];
                if (p == null) {
                    continue;
                }
                switch (p.getPieceType()) {
                    case Piece.PAWN:
                        int direction = p.getPieceColour() == Piece.BLACK ? 1 : -1; // Black moves down, White moves up
                        if (i + direction < 0 || i + direction >= pieces.length) {
                            break;
                        }
                        if (pieces[i + direction][j] == null) {
                            validMoves[i + direction][j] = true;
                            if (p.getFirstMovePawn() && pieces[i + 2 * direction][j] == null) {
                                validMoves[i + 2 * direction][j] = true;
                            }
                        }
                        if (j + 1 < pieces.length && pieces[i + direction][j + 1] != null
                                && pieces[i + direction][j + 1].getPieceColour() != p.getPieceColour()) {
                            validMoves[i + direction][j + 1] = true;
                        }
                        if (j - 1 >= 0 && pieces[i + direction][j - 1] != null
                                && pieces[i + direction][j - 1].getPieceColour() != p.getPieceColour()) {
                            validMoves[i + direction][j - 1] = true;
                        }
                        break;
                    case Piece.ROOK:
                        // Directions for the rook: up, down, left, right
                        int[][] directions = {
                                { 0, -1 }, // up
                                { 0, 1 }, // down
                                { -1, 0 }, // left
                                { 1, 0 } // right
                        };
                        for (int[] dir : directions) {
                            int dx = dir[0];
                            int dy = dir[1];
                            int dist = 1;
                            while (true) {
                                int newX = p.x + dx * dist; // X coord to be checked
                                int newY = p.y + dy * dist; // Y coord to be checked
                                if (newX < 0 || newX >= pieces[0].length || newY < 0 || newY >= pieces.length) { // Checking
                                                                                                                 // boundaries
                                    break;
                                }
                                if (pieces[newY][newX] == null) { // Is this a possible move?
                                    validMoves[newY][newX] = true;
                                } else {
                                    if (pieces[newY][newX].getPieceColour() != p.getPieceColour()) { // Is this a
                                                                                                     // capture?
                                        validMoves[newY][newX] = true;
                                    }
                                    break;
                                }
                                dist++;
                            }
                        }
                        break;
                    case Piece.BISHOP:
                        int[][] diagonals = {
                                { -1, -1 }, // up left
                                { 1, 1 }, // down right
                                { 1, -1 }, // up right
                                { -1, 1 } // down left
                        };
                        for (int[] dir : diagonals) {
                            int dx = dir[0];
                            int dy = dir[1];
                            int dist = 1;
                            while (true) {
                                int newX = p.x + dx * dist; // X coord to be checked
                                int newY = p.y + dy * dist; // Y coord to be checked

                                if (newX < 0 || newX >= pieces[0].length || newY < 0 || newY >= pieces.length) { // Checking boundaries
                                    break;
                                }
                                if (pieces[newY][newX] == null) { // Is this a possible move?
                                    validMoves[newY][newX] = true;
                                } else {
                                    if (pieces[newY][newX].getPieceColour() != p.getPieceColour()) { // Is this a
                                                                                                     // capture?
                                        validMoves[newY][newX] = true;
                                    }
                                    break;
                                }
                                dist++;
                            }
                        }
                        break;
                    case Piece.KNIGHT:
                        int[][] posChanges = {
                            {-2, -1},
                            {-2, 1},
                            {-1, 2},
                            {1, 2},
                            {2, -1},
                            {2, 1},
                            {-1, -2},
                            {1, -2}
                        };
                        for(int[] posC : posChanges) {
                            int dx = posC[0];
                            int dy = posC[1];

                            int newX = p.x + dx;
                            int newY = p.y + dy;
                            if (newX < 0 || newX >= pieces[0].length || newY < 0 || newY >= pieces.length) { // Checking boundaries
                                continue;
                            }
                            if(pieces[newY][newX] == null || pieces[newY][newX].getPieceColour() != p.getPieceColour()) {
                                validMoves[newY][newX] = true;
                            }
                        }
                        break;
                    case Piece.QUEEN:
                        int[][] allDirections = {
                                { 0, -1 }, // up
                                { 0, 1 }, // down
                                { -1, 0 }, // left
                                { 1, 0 }, // right
                                { -1, -1 }, // up left
                                { 1, 1 }, // down right
                                { 1, -1 }, // up right
                                { -1, 1 } // down left
                        };
                        for (int[] dir : allDirections) {
                            int dx = dir[0];
                            int dy = dir[1];
                            int dist = 1;
                            while (true) {
                                int newX = p.x + dx * dist; // X coord to be checked
                                int newY = p.y + dy * dist; // Y coord to be checked

                                if (newX < 0 || newX >= pieces[0].length || newY < 0 || newY >= pieces.length) { // Checking boundaries
                                    break;
                                }
                                if (pieces[newY][newX] == null) { // Is this a possible move?
                                    validMoves[newY][newX] = true;
                                } else {
                                    if (pieces[newY][newX].getPieceColour() != p.getPieceColour()) { // Is this a capture?
                                        validMoves[newY][newX] = true;
                                    }
                                    break;
                                }
                                dist++;
                            }
                        }
                        break;
                }
                if (p.getPieceType() != Piece.KING) {
                    validMoves[p.y][p.x] = false;
                    p.setValidMoves(validMoves);
                }
            }
        }
    }

    public void findKings(Piece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (pieces[i][j] == null || pieces[i][j].getPieceType() != Piece.KING) {
                    continue;
                } else {
                    if (pieces[i][j].getPieceColour() == Piece.BLACK) {
                        blackKing = pieces[i][j];
                    } else {
                        whiteKing = pieces[i][j];
                    }
                }
            }
        }
    }

    public void setValidKingMoves(Piece[][] pieces, Piece king) {
        boolean[][] validMoves = new boolean[pieces.length][pieces.length];
        int[][] dirMatrix = {
                { 0, -1 }, // up
                { 0, 1 }, // down
                { -1, 0 }, // left
                { 1, 0 }, // right
                { -1, -1 }, // up left
                { 1, 1 }, // down right
                { 1, -1 }, // up right
                { -1, 1 } // down left
        };
        for (int[] dir : dirMatrix) {
            int dx = dir[0];
            int dy = dir[1];

            int newX = king.x + dx; // X coord to be checked
            int newY = king.y + dy; // Y coord to be checked

            if (newX < 0 || newX >= pieces[0].length || newY < 0 || newY >= pieces.length) { // Checking boundaries
                continue;
            }

            Piece originalPiece = pieces[newY][newX];

            if (pieces[newY][newX] == null) { // Is this a possible move?
                // Temporarily move the king

                pieces[king.y][king.x] = null;
                pieces[newY][newX] = king;

                setValidMoves(pieces);

                validMoves[newY][newX] = true;
                for (int i = 0; i < pieces.length; i++) {
                    for (int j = 0; j < pieces[i].length; j++) {
                        Piece p = pieces[i][j];
                        if (p != null) {
                            if (p.getPieceColour() != king.getPieceColour() && p.getValidMoves()[newY][newX] == true) {
                                validMoves[newY][newX] = false;
                            }
                        }
                    }
                }

            } else {
                if (pieces[newY][newX].getPieceColour() != king.getPieceColour()) { // Is this a capture?
                    pieces[king.y][king.x] = null;
                    pieces[newY][newX] = king;

                    setValidMoves(pieces);
                    validMoves[newY][newX] = true;
                    for (int i = 0; i < pieces.length; i++) {
                        for (int j = 0; j < pieces[i].length; j++) {
                            Piece p = pieces[i][j];
                            if (p != null) {
                                if (p.getPieceColour() != king.getPieceColour()
                                        && p.getValidMoves()[newY][newX] == true) {
                                    validMoves[newY][newX] = false;
                                }
                            }
                        }
                    }
                }
            }
            // Restore the original board state
            pieces[newY][newX] = originalPiece;
            pieces[king.y][king.x] = king;
            setValidMoves(pieces);

        }
        validMoves[king.y][king.x] = false;
        boolean hasValidMove = false;
        for(int i = 0; i < pieces.length; i++) {
            for(int j = 0; j < pieces[i].length; j++) {
                if(validMoves[i][j]) {
                    hasValidMove = true;
                    break;
                }
                if(hasValidMove) break;
            }
        }
        if(checkForCheck(pieces) && !hasValidMove) {
            System.exit(0);
        }
        king.setValidMoves(validMoves);
    }

    public Piece getBlackKing() {
        return blackKing;
    }

    public Piece getWhiteKing() {
        return whiteKing;
    }

    public boolean checkForCheck(Piece[][] pieces) {
        Piece king = null;

        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece p = pieces[i][j];
                if (p != null && p.getPieceColour() == turn && p.getPieceType() == Piece.KING) {
                    king = p;
                    break;
                }
            }
            if (king != null) {
                break;
            }
        }
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece p = pieces[i][j];
                if (p != null && p.getPieceColour() != turn && p.getValidMoves()[king.y][king.x]) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean getCheck() {
        return check;
    }

    public void update() {

    }

    public void setPositions() {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece p = pieces[i][j];
                if (p != null) {
                    pieces[i][j].x = j;
                    pieces[i][j].y = i;
                }
            }
        }
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public void setPieces(Piece[][] pieces) {
        this.pieces = pieces;
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
