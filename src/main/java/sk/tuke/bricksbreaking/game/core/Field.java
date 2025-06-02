package sk.tuke.bricksbreaking.game.core;

import java.util.Random;
import java.util.Stack;

public class Field {
    private final int rows;
    private final int columns;
    private  Brick[][] bricks;
    private GameState  state;
    private int score;
    //private int bombRemainingUses;
    private Stack<Brick[][]> history = new Stack<>();
    public boolean wasDeleted;

    public Field(int rows) {
        this.rows = rows;
        this.columns = rows;
        this.bricks = new Brick[rows][columns];
        this.state = GameState.PLAYING;
        this.score = 0;
        this.wasDeleted = false;
        //this.bombRemainingUses= 3;

        generateBricks();
        if(areAllBricksRemoved()){
            generateBricks();
        }

    }



    public GameState getState() {

        return state;
    }




    public void setState(GameState state) {

        this.state = state;
    }

    public void setBricks(Brick[][] bricks) {
        this.bricks = bricks;
    }

    public Brick getBrick(int row, int col) {

        return bricks[row][col];
    }

    public int getRows(){
        return this.rows;

    }

    public int getColumns(){
        return this.columns;
    }

    public int getScore() {
        return score;
    }

    public void generateBricks() {
        Random random = new Random();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                bricks[row][col] = new Brick(BrickColor.values()[random.nextInt(BrickColor.values().length)]);
            }
        }
    }

    public void clearBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                bricks[row][col] = null;
            }
        }
    }


    public void removeBrick(int row, int col, MagicWand magicWand) {
        if (bricks[row][col] != null) {
            removeAllBricksAroundForColor(bricks[row][col].getColor(), row, col, magicWand,false);
            applyGravity();
            shiftColumns();
        } else {
            System.out.println("YOU HIT EMPTY SLOT on " + row + " " + col);
        }
    }





    private void removeAllBricksAroundForColor(BrickColor color, int row, int col, MagicWand magicWand,boolean hasNgb) {
        Brick currentBrick = bricks[row][col];
        bricks[row][col] = null;
        score += 5;


        // hore
        if (row > 0 && bricks[row - 1][col] != null && bricks[row - 1][col].getColor().equals(color)) {
            hasNgb = true;
            removeAllBricksAroundForColor(color, row - 1, col, magicWand,true);
        }

        // dole
        if (row + 1 < rows && bricks[row + 1][col] != null && bricks[row + 1][col].getColor().equals(color)) {
            hasNgb = true;
            removeAllBricksAroundForColor(color, row + 1, col, magicWand,true);
        }

        // nalavo
        if (col > 0 && bricks[row][col - 1] != null && bricks[row][col - 1].getColor().equals(color)) {
            hasNgb = true;
            removeAllBricksAroundForColor(color, row, col - 1, magicWand,true);
        }

        // napravo
        if (col + 1 < columns && bricks[row][col + 1] != null && bricks[row][col + 1].getColor().equals(color)) {
            hasNgb = true;
            removeAllBricksAroundForColor(color, row, col + 1, magicWand,true);
        }


         if (!hasNgb) {
            if (magicWand != null && magicWand.getRemainingUses() > 0) {
                magicWand.useMagicWand();
            } else {
                bricks[row][col] = currentBrick;
                score -= 5;
            }
         }

    }

    public void deleteRow(int row) {
        for (int i = 0; i < rows; i++) {
            bricks[row][i] = null;
        }
        this.wasDeleted = true;
    }



    public void applyGravity() {
        for (int col = 0; col < columns; col++) {
            int emptyRow = rows - 1;

            for (int row = rows - 1; row >= 0; row--) {
                if (bricks[row][col] != null) {
                    if (row != emptyRow) {
                        bricks[emptyRow][col] = bricks[row][col];
                        bricks[row][col] = null;
                    }
                    emptyRow--;
                }
            }
        }
    }

    private boolean isColumnEmpty(int col) {
        for (int row = 0; row < rows; row++) {
            if (bricks[row][col] != null) {
                return false;
            }
        }
        return true;
    }

    public void shiftColumns() {
        int writeIndex = 0;

        for (int col = 0; col < columns; col++) {
            if (!isColumnEmpty(col)) {
                if (col != writeIndex) {
                    for (int row = 0; row < rows; row++) {
                        bricks[row][writeIndex] = bricks[row][col];
                        bricks[row][col] = null;
                    }
                }
                writeIndex++;
            }
        }
    }




    public boolean areAllBricksRemoved() {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                if (bricks[row][col] != null) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isGameOver(MagicWand magicWand) {
        if (!hasPossibleMoves() && magicWand.getRemainingUses() == 0) {
            return true;
        }
        return false;
    }




    private boolean hasSameColorNeighbor(int row, int col) {
        Brick currentBrick = bricks[row][col];
        if (currentBrick == null) return false;

        BrickColor currentColor = currentBrick.getColor();
        boolean hasNeighbor = false;


        // hore
        if (row > 0 && bricks[row - 1][col] != null) {
            if (bricks[row - 1][col].getColor() == currentColor) {

                hasNeighbor = true;
            }
        }
        // dole
        if (row < bricks.length - 1 && bricks[row + 1][col] != null) {
            if (bricks[row + 1][col].getColor() == currentColor) {

                hasNeighbor = true;
            }
        }
        // vľavo
        if (col > 0 && bricks[row][col - 1] != null) {
            if (bricks[row][col - 1].getColor() == currentColor) {

                hasNeighbor = true;
            }
        }
        // vpravo
        if (col < bricks[row].length - 1 && bricks[row][col + 1] != null) {
            if (bricks[row][col + 1].getColor() == currentColor) {

                hasNeighbor = true;
            }
        }

        return hasNeighbor;
    }





    public boolean hasPossibleMoves() {
        for (int row = 0; row < bricks.length; row++) {
            for (int col = 0; col < bricks[row].length; col++) {
                if (bricks[row][col] != null && hasSameColorNeighbor(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }


    // pre testy
    public void fillWithPossibleMove() {
        bricks = new Brick[3][3]; // 3x3 pole

        bricks[0][0] = new Brick(BrickColor.RED);
        bricks[0][1] = new Brick(BrickColor.RED); // Možný ťah (dve susediace rovnaké farby)
        bricks[0][2] = new Brick(BrickColor.BLUE);

        bricks[1][0] = new Brick(BrickColor.GREEN);
        bricks[1][1] = new Brick(BrickColor.GREEN);
        bricks[1][2] = new Brick(BrickColor.BLUE);

        bricks[2][0] = new Brick(BrickColor.GREEN);
        bricks[2][1] = new Brick(BrickColor.GREEN);
        bricks[2][2] = new Brick(BrickColor.RED);
    }

    public void fillWithNoMovesTestSetup() {
        bricks = new Brick[3][3]; // 3x3 pole

        bricks[2][0] = new Brick(BrickColor.RED);
        bricks[2][1] = new Brick(BrickColor.BLUE);
        bricks[2][2] = new Brick(BrickColor.GREEN);
    }

}







    //    public void useBomb(int row, int col) {
//        if (state != GameState.PLAYING ) return;
//
//        if(this.bombRemainingUses > 0){
//            this.bombRemainingUses--;
//            for (int c = 0; c < columns; c++) {
//                if (bricks[row][c] != null) {
//                    bricks[row][c] = null;
//                    score += 5;
//                }
//            }
//            for (int r = 0; r < rows; r++) {
//                if (r != row && bricks[r][col] != null) { // r != row aby sa nezrátalo dvakrát
//                    bricks[r][col] = null;
//                    score += 5;
//                }
//            }
//
//            applyGravity();
//            shiftColumns();
//        }
//
//    }

// pred akýmkoľvek ťahom si uložíš aktuálny stav:
//public void saveState() {
//    Brick[][] copy = new Brick[rows][columns];
//    for (int r = 0; r < rows; r++) {
//        for (int c = 0; c < columns; c++) {
//            if (bricks[r][c] != null)
//                copy[r][c] = new Brick(bricks[r][c].getColor());
//        }
//    }
//    history.push(copy);
//}
//
//public void undo() {
//    if (!history.isEmpty()) {
//        bricks = history.pop();
//    }
//}






