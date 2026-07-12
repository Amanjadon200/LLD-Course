package lldinterview;

import java.util.Arrays;
import java.util.Scanner;

/*requirement
1.  is it a 3*3 board is should be square matrix of any size
2.  two players?? 
3 win or lose or draw
4. alternating turn for players
5. validation rules to maintain no bias is putting on already filled cell.

*/
/* key components
symbol : X or O or EMPTY 
Board,
player
TictackToeGame
*/
public class TictackToeGameMain {
    public static void main(String[] args) {

        Symbol[][] grid = new Symbol[3][3];
        for (int i = 0; i < grid.length; i++) {
            Arrays.fill(grid[i], Symbol.EMPTY);
        }
        Board board = new Board(3, 3, grid);
        PlayerStrategy p1 = new HumanPlayer("Player 1", Symbol.X);
        PlayerStrategy p2 = new HumanPlayer("Player 2", Symbol.O);
        TictackToeGame tictackToeGame = new TictackToeGame(board, p1, p2, p1);
        tictackToeGame.play();
    }
}

enum Symbol {
    X, O, EMPTY
}

interface BoardGames {
    void play();// abstract and public
}
class TictackToeGame implements BoardGames {
    private Board board;
    private PlayerStrategy p1;
    private PlayerStrategy p2;
    private PlayerStrategy currentPlayer;

    public TictackToeGame(Board board, PlayerStrategy p1, PlayerStrategy p2, PlayerStrategy currentPlayer) {
        this.board = board;
        this.p1 = p1;
        this.p2 = p2;
        this.currentPlayer = currentPlayer;
    }

    public void play() {
        while (true) {
            int[] move = currentPlayer.makeMove(board);
            board.makeMove(move[0], move[1], currentPlayer);
            board.display(); // Display the board after each move
            if (board.hasWon(currentPlayer.getSymbol())) {
                System.out.println(currentPlayer.getPlayerName() + " has won the game");
                break;
            }
            // switch player
            if (board.isBoardFull()) {
                System.out.println("Tie game");
                break;
            }
            if (currentPlayer == p1) {
                currentPlayer = p2;
            } else {
                currentPlayer = p1;
            }
        }
    }

}


class Board {
    private int row;
    private int col;
    private Symbol[][] grid;

    public Board(int row, int col, Symbol grid[][]) {
        this.row = row;
        this.col = col;
        this.grid = grid;
    }

    public boolean isBoardFull() {
        // Check if any player has won
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == Symbol.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidMove(int row, int col) {
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            return false;
        }
        if (grid[row][col] != Symbol.EMPTY) {
            return false;
        }
        return true;
    }

    public void makeMove(int row, int col, PlayerStrategy player) {
        // assuming player is p1 or p2 and we can identify their symbol
        Symbol symbol = player.getSymbol();
        grid[row][col] = symbol;

    }

    public boolean hasWon(Symbol symbol) {

        // Check rows
        for (int i = 0; i < row; i++) {
            boolean win = true;

            for (int j = 0; j < col; j++) {
                if (grid[i][j] != symbol) {
                    win = false;
                    break;
                }
            }

            if (win) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < col; j++) {
            boolean win = true;

            for (int i = 0; i < row; i++) {
                if (grid[i][j] != symbol) {
                    win = false;
                    break;
                }
            }

            if (win) {
                return true;
            }
        }

        // Check main diagonal
        boolean win = true;
        for (int i = 0; i < row; i++) {
            if (grid[i][i] != symbol) {
                win = false;
                break;
            }
        }

        if (win) {
            return true;
        }

        // Check anti-diagonal
        win = true;
        for (int i = 0; i < row; i++) {
            if (grid[i][row - i - 1] != symbol) {
                win = false;
                break;
            }
        }

        return win;
    }

    public void display() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}

interface PlayerStrategy {
    Symbol getSymbol();

    int[] makeMove(Board board);
    String getPlayerName();
}

class HumanPlayer implements PlayerStrategy {
    private String playerName;
    private Scanner scanner;
    private Symbol symbol;

    public HumanPlayer(String playerName, Symbol symbol) {
        this.playerName = playerName;
        this.symbol = symbol;
        this.scanner = new Scanner(System.in);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int[] makeMove(Board board) {
        System.out.println(playerName + ", enter your move (row and column): ");
        while (true) {
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            if (board.isValidMove(row, col)) {
                // Make the move on the board
                return new int[] { row, col };
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
    }
}
