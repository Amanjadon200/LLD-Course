package lldinterview;

import java.util.Scanner;

public class ChessGameMain {
    public static void main(String[] args) {
        int row = 8;
        int col = 8;
        Scanner scanner = new Scanner(System.in);
        Cell[][] grid = new Cell[row][col];
        try {
            
            Board board = new Board(row, col, grid);
            board.initializeBoard();
            PlayerStrategy p1 = new HumanPlayer("Player 1", true);
            PlayerStrategy p2 = new HumanPlayer("Player 2", false);
            PlayerStrategy currentPlayer = p1;
            ChessGame chessGame = new ChessGame(board, p1, p2, currentPlayer);
            chessGame.play(scanner);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e+"aman*************************");
        }

    }
}

interface BoardGames {
    void play(Scanner scanner);// abstract and public
}

class ChessGame implements BoardGames {
    private Board board;
    private PlayerStrategy p1;
    private PlayerStrategy p2;
    private PlayerStrategy currentPlayer;

    public ChessGame(Board board, PlayerStrategy p1, PlayerStrategy p2, PlayerStrategy currentPlayer) {
        this.board = board;
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = currentPlayer;
    }

    public void play(Scanner scanner) {
        try {
            while (true) {

            Cell[] move = currentPlayer.makeMove(board, scanner);
            // if (!new Move(move[0], move[1]).isValidMove(board)) {
            //     System.out.println("Invalid move. Please try again.");
            //     continue;
            // }
            if (board.hasWon(move[1].getPiece())) {
                System.out.println(currentPlayer.getPlayerName() + " has won the game");
                break;
            }
            board.makeMove(move[0], move[1]);
            System.out.println("Board after move:");
            if (currentPlayer == p1) {
                currentPlayer = p2;
            } else {
                currentPlayer = p1;
            }
        }
        } catch (Exception e) {
            System.out.println(e+" in  ChessGame play method");
        }
        
    }
}

class Cell {
    private Piece piece;
    private int row;
    private int col;

    public Cell(int row, int col, Piece piece) {
        this.row = row;
        this.col = col;
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }
}

class Board {
    private int row;
    private int col;
    private Cell[][] grid;

    public Board(int row, int col, Cell grid[][]) {
        this.row = row;
        this.col = col;
        this.grid = grid;
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public void initializeBoard() {
        // Initialize the chess board with pieces in their starting positions
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0 && j == 0 || i == 0 && j == 7) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Rook", false));
                } else if (i == 0 && j == 1 || i == 0 && j == 6) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Knight", false));
                } else if (i == 0 && j == 2 || i == 0 && j == 5) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Bishop", false));
                } else if (i == 0 && j == 3) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Queen", false));
                } else if (i == 0 && j == 4) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("King", false));
                } else if (i == 1) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Pawn", false));
                } else if (i == 6) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Pawn", true));
                } else if (i == 7 && (j == 0 || j == 7)) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Rook", true));
                } else if (i == 7 && (j == 1 || j == 6)) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Knight", true));
                } else if (i == 7 && (j == 2 || j == 5)) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Bishop", true));
                } else if (i == 7 && (j == 3)) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("Queen", true));
                } else if (i == 7 && (j == 4)) {
                    grid[i][j] = new Cell(i, j, PeaceFactory.createPiece("King", true));
                }
            }
            
        }
        for (int i = 2; i < row - 2; i++) {
            for (int j = 0; j < col; j++) {
                grid[i][j] = new Cell(i, j, null);
            }
        }
    }

    public void makeMove(Cell x, Cell y) {
        System.out.println("Moving piece from (" + x.getPiece() + ") to (" + y.getPiece() + ")");
        y.setPiece(x.getPiece());
        x.setPiece(null);
    }
    public boolean hasWon(Piece piece) {
        // Check if the player has won the game
        // Implement the logic to check for winning conditions
        if(piece instanceof King){
            return true;
        }
        return false; // Placeholder, implement actual winning logic
    }
}

class Move {
    private Cell fromCell;
    private Cell toCell;

    public Move(Cell fromCell, Cell toCell) {
        this.fromCell = fromCell;
        this.toCell = toCell;
    }

    public boolean isValidMove(Board board) {
        
        if (toCell.getPiece() ==null || fromCell.getPiece().isWhite() == toCell.getPiece().isWhite()) {
            return false;
        }
        return true;
    }
}

class PeaceFactory {
    public static Piece createPiece(String name, boolean isWhite) {
        switch (name) {
            case "King":
                return new King(isWhite);
            case "Queen":
                return new Queen(isWhite);
            case "Rook":
                return new Rook(isWhite);
            case "Bishop":
                return new Bishop(isWhite);
            case "Knight":
                return new Knight(isWhite);
            case "Pawn":
                return new Pawn(isWhite);
            default:
                throw new IllegalArgumentException("Invalid piece name: " + name);
        }
    }
}

// we have 16 pieces for each player
abstract class Piece {
    String name;
    boolean isWhite;
    boolean isAlive;

    public Piece(String name, boolean isWhite) {
        this.name = name;
        this.isWhite = isWhite;
        this.isAlive = true;
    }

    public boolean isWhite() {
        return isWhite;
    }
    abstract public boolean canMove(Cell fromCell, Cell toCell);
}

class King extends Piece {// 1
    public King(boolean isWhite) {
        super("King", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for King's movement
        return true; // Placeholder, implement actual movement logic
    }
}

class Queen extends Piece { // 1
    public Queen(boolean isWhite) {
        super("Queen", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for Queen's movement
        return true; // Placeholder, implement actual movement logic
    }
}

class Rook extends Piece {// 2
    public Rook(boolean isWhite) {
        super("Rook", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for Rook's movement
        return true; // Placeholder, implement actual movement logic
    }
}

class Bishop extends Piece { // 2
    public Bishop(boolean isWhite) {
        super("Bishop", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for Bishop's movement
        return true; // Placeholder, implement actual movement logic
    }
}

class Knight extends Piece { // 2
    public Knight(boolean isWhite) {
        super("Knight", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for Knight's movement
        return true; // Placeholder, implement actual movement logic
    }
}

class Pawn extends Piece { // 8
    public Pawn(boolean isWhite) {
        super("Pawn", isWhite);
    }
    public boolean canMove(Cell fromCell, Cell toCell) {
        // Implement the logic for Pawn's movement
        return true; // Placeholder, implement actual movement logic
    }
}

interface PlayerStrategy {
    Cell[] makeMove(Board board, Scanner scanner);

    String getPlayerName();

    boolean isWhiteSide();
}

class HumanPlayer implements PlayerStrategy {
    private String name;
    private boolean isWhiteSide;

    public HumanPlayer(String name, boolean isWhiteSide) {
        this.name = name;
        this.isWhiteSide = isWhiteSide;
    }

    public String getPlayerName() {
        return name;
    }

    public Cell[] makeMove(Board board, Scanner scanner) {
        // Implement the logic for human player to make a move
        // System.out.println(currentPlayer.getPlayerName() + "'s turn. Enter your move
        // (e.g., e2 e4): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();
        int row2 = scanner.nextInt();
        int col2 = scanner.nextInt();
        Cell fromCell = board.getCell(row, col);
        Cell toCell = board.getCell(row2, col2);
        return new Cell[] { fromCell, toCell }; // Placeholder, implement actual move logic
    }

    public boolean isWhiteSide() {
        return isWhiteSide;
    }
}