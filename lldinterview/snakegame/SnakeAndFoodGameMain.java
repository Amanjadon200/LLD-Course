package lldinterview.snakegame;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/*
requirements:  1. snake which is rovoling in a grid m*n
                2. it will grow as it eat food by  1 unit or x unit depending on food vlaue
                3.score increase as it get food.
                4. game will end af snake touches the wall or itself
*/
/*
entities       1. snake
                2. Board (grid)
                3. food
                4. SnakeAndFoodGame (class which will handle all game logic or its a actual game class)

*/
public class SnakeAndFoodGameMain {
    public static void main(String[] args) {
        int[][] foodPositions = {
                { 5, 5 }, // Initial food
                { 10, 8 }, // Second food
                { 3, 12 }, // Third food
                { 8, 17 }, // Fourth food
                { 12, 3 } // Fifth food
        };
        HashMap<Integer, FoodItem> foodMap = new HashMap<>();
        FoodItem f1= FoodFactory.createFood(4, 5, 1);
        FoodItem f2= FoodFactory.createFood(10, 8, 5);
        FoodItem f3= FoodFactory.createFood(3, 12, 1);
        FoodItem f4= FoodFactory.createFood(8, 17, 5);
        FoodItem f5= FoodFactory.createFood(12, 3, 1);
        foodMap.put(0, f1);
        foodMap.put(1, f2);
        foodMap.put(2, f3);
        foodMap.put(3, f4);
        foodMap.put(4, f5);
        SnakeGame game = new SnakeGame(foodPositions, foodMap);
        game.startGame();
    }
}

class SnakeGame {
    private Snake snake;
    private MovementStrategy movementStrategy;
    private Board board;
    int[][] foodPos;
    int score;
    Map<Integer, FoodItem> foodMap;

    public SnakeGame(int[][] foodPos, Map<Integer, FoodItem> foodMap) {
        snake = new Snake();
        movementStrategy = new HumanMovementStrategy();
        board = new Board(20, 20);
        this.foodPos = foodPos;
        score = 0;
        this.foodMap = foodMap;
    }

    final Random random = new Random();
    public void startGame() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            String direction = sc.nextLine();
            int result = move(direction);
            if (result == -1) {
                System.out.println("game over");
                break;
            }

        }
        sc.close();
    }

    public int move(String direction) {
        Deque<Pair> snakeBody = snake.getBody();
        Pair head = snakeBody.peekFirst();
        Pair newPostion = movementStrategy.makeMove(head, direction);
        // int newHeadRow = newPostion.getRow();
        // int newHeadCol = newPostion.getCol();
        boolean food = false;
        for (int i = 0; i < foodPos.length; i++) {
            if (foodPos[i][0] == newPostion.getRow() && foodPos[i][1] == newPostion.getCol()) {
                int randomRow = random.nextInt(board.getRow());
                int randomCol = random.nextInt(board.getCol());
                while (snake.getSnakePosMap().containsKey(new Pair(randomRow, randomCol))) {
                    randomRow = random.nextInt(board.getRow());
                    randomCol = random.nextInt(board.getCol());
                }
                foodPos[i][0] = randomRow;
                foodPos[i][1] = randomCol;
                food = true;
                this.score += foodMap.get(i).getPoints();
                break;
            }
        }
        if (food == false) {
            Pair lastPos = snakeBody.removeLast();
            HashMap<Pair, Boolean> snakePosMap = snake.getSnakePosMap();
            snakePosMap.remove(lastPos);
        }
        if (!board.validMove(newPostion, snake)) {
            System.out.println(score);
            return -1;
        }
        snakeBody.addFirst(newPostion);
        HashMap<Pair, Boolean> snakePosMap = snake.getSnakePosMap();
        snakePosMap.put(newPostion, true);
        return score;
    }
}

class Snake {
    private Deque<Pair> body;
    private HashMap<Pair, Boolean> snakePosMap;

    public Snake() {
        this.body = new LinkedList<>();
        this.snakePosMap = new HashMap<>();
        Pair initialPos = new Pair(0, 0);
        this.snakePosMap.put(initialPos, true);
        this.body.add(initialPos);
    }
    public Deque<Pair> getBody() {
        return body;
    }
    public HashMap<Pair, Boolean> getSnakePosMap() {
        return snakePosMap;
    }
}

abstract class FoodItem {
    private int points;
    private int row;
    private int col;

    public FoodItem(int row, int col, int points) {
        this.col = col;
        this.row = row;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}

class NormalFood extends FoodItem {
    public NormalFood(int row, int col) {
        super(row, col, 1);
    }

}

class BonusFood extends FoodItem {
    public BonusFood(int row, int col) {
        super(row, col, 5);
    }

}

class FoodFactory {
    public static FoodItem createFood(int row, int col, int points) {
        if (points == 1) {
            return new NormalFood(row, col);
        } else if (points == 5) {
            return new BonusFood(row, col);
        }
        return null;
    }
}

class Board {
    private int rows;
    private int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public boolean validMove(Pair newPosition, Snake snake) {
        int row = newPosition.getRow();
        int col = newPosition.getCol();
        HashMap<Pair, Boolean> snakePosMap = snake.getSnakePosMap();
        if (row < 0 || col < 0 || row >= this.getRow() || col >= this.getCol()
                || snakePosMap.containsKey(newPosition) == true)
            return false;
        return true;
    }

    public int getRow() {
        return rows;
    }

    public int getCol() {
        return cols;
    }

}

interface MovementStrategy {
    Pair makeMove(Pair currentPosition, String direction); // abstract public
}

class HumanMovementStrategy implements MovementStrategy {
    public Pair makeMove(Pair currentPosition, String direction) {
        if (direction.equals("L")) {
            return new Pair(currentPosition.getRow(), currentPosition.getCol() - 1);
        } else if (direction.equals("R")) {
            return new Pair(currentPosition.getRow(), currentPosition.getCol() + 1);
        } else if (direction.equals("D")) {
            return new Pair(currentPosition.getRow() + 1, currentPosition.getCol());
        } else if (direction.equals("U")) {
            return new Pair(currentPosition.getRow() - 1, currentPosition.getCol());
        }
        return currentPosition;
    }
}

class Pair {
    private int row;
    private int col;

    public Pair(int row, int col) {
        this.row = row;
        this.col = col;
    }
 @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pair)) return false;

        Pair other = (Pair) obj;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }

}