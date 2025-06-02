package sk.tuke.bricksbreaking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.bricksbreaking.game.core.*;

import static org.junit.jupiter.api.Assertions.*;


public class FieldTest {
    private Field board;

    @BeforeEach
    void setUp() {
        board = new Field(5);
    }

    @Test
    void testBoardInitialization() {
        assertNotNull(board, "GameBoard should be initialized");
        assertEquals(5, board.getRows(), "Row count should be 5");
        assertEquals(5, board.getColumns(), "Column count should be 5");
    }

    @Test
    void testBrickPlacement() {
        Brick brick = board.getBrick(2, 2);
        assertNotNull(brick, "Brick at (2,2) should not be null");
    }

    @Test
    void testRemoveBricks() {
        Field field = new Field(3);
        MagicWand magicWand = new MagicWand(1);

        field.fillWithNoMovesTestSetup();
        field.removeBrick(2, 0, magicWand);


        assertNull(field.getBrick(2, 2), "Brick at (2,2) should be null");
    }


    @Test
    public void testGameOverWhenNoMovesLeft() {
        Field field = new Field(3);
        field.fillWithNoMovesTestSetup();


        System.out.println("Testing field with no moves:");
        printField(field);

        //kontrola ci hra je playing
        assertEquals(GameState.PLAYING, field.getState(), "Game should start in PLAYING state.");


        assertTrue(field.isGameOver(new MagicWand(0)), "Game should be over when no moves are left.");


    }


    @Test
    void testShiftColumns() {
        Field field = new Field(3);
        field.clearBoard();


        Brick[][] bricks = new Brick[3][3];
        bricks[2][0] = new Brick(BrickColor.RED);
        bricks[2][1] = null;
        bricks[2][2] = new Brick(BrickColor.GREEN);

        bricks[1][0] = new Brick(BrickColor.RED);
        bricks[1][1] = null;
        bricks[1][2] = new Brick(BrickColor.GREEN);

        bricks[0][0] = null;
        bricks[0][1] = null;
        bricks[0][2] = null;

        field.setBricks(bricks);


        System.out.println("Field before shifting:");
        printField(field);


        field.shiftColumns();


        System.out.println("Field after shifting:");
        printField(field);

        // ocakavane pozicie tehal
        assertNotNull(field.getBrick(2, 0), "Brick should be in first column after shift.");
        assertNotNull(field.getBrick(1, 0), "Brick should be in first column after shift.");
        assertNull(field.getBrick(0, 2), "Rightmost column should now be empty.");
    }


    @Test
    void testApplyGravity() {
        Field field = new Field(3);


        Brick[][] bricks = new Brick[3][3];

        bricks[0][0] = new Brick(BrickColor.RED);
        bricks[0][1] = new Brick(BrickColor.GREEN);
        bricks[0][2] = null;

        bricks[1][0] = null;
        bricks[1][1] = new Brick(BrickColor.BLUE);
        bricks[1][2] = null;

        bricks[2][0] = new Brick(BrickColor.RED);
        bricks[2][1] = null;
        bricks[2][2] = null;


        field.setBricks(bricks);

        System.out.println("Field before gravity:");
        printField(field);


        field.applyGravity();

        System.out.println("Field after gravity:");
        printField(field);

        // ocakavane pozicie tehal

        assertNull(field.getBrick(0, 0), "Brick at (0,0) should have fallen");
        assertNotNull(field.getBrick(2, 0), "Brick should be at (2,0) after gravity");
        assertNull(field.getBrick(0, 1), "Brick at (0,1) should have fallen");
        assertNotNull(field.getBrick(2, 1), "Brick should be at (2,1) after gravity");
        assertNotNull(field.getBrick(1, 1), "Brick should be at (1,1) after gravity");
    }





    private void printField(Field field) {
        for (int row = 0; row < field.getRows(); row++) {
            System.out.print("| ");
            for (int col = 0; col < field.getColumns(); col++) {
                Brick brick = field.getBrick(row, col);
                System.out.print((brick == null ? " " : brick.getColor().name().charAt(0)) + " ");
            }
            System.out.println("|");
        }
        System.out.println(" ------------- ");
    }
}