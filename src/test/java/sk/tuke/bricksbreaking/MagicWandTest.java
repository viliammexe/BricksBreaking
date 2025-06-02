package sk.tuke.bricksbreaking;

import sk.tuke.bricksbreaking.game.core.Brick;
import sk.tuke.bricksbreaking.game.core.Field;
import sk.tuke.bricksbreaking.game.core.MagicWand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MagicWandTest {

    @Test
    void testMagicWandUsage() {
        Field field = new Field(3);
        MagicWand magicWand = new MagicWand(1);


        field.fillWithNoMovesTestSetup();
        System.out.println("Field before removing brick:");
        printField(field);

        // Kocka na (2,0) nemá žiadneho suseda rovnakej farby
        assertNotNull(field.getBrick(2, 0), "Brick at (2,0) should exist before removal");
        assertEquals(1, magicWand.getRemainingUses(), "MagicWand should have 1 use before removal");


        field.removeBrick(2, 0, magicWand);

        System.out.println("Field after attempting to remove brick:");
        printField(field);

        // ocakavame ze magic wand sa pouzila a kocka bola odstranena
        assertNull(field.getBrick(2, 2), "Brick at (0,0) should be removed by MagicWand");
        assertEquals(0, magicWand.getRemainingUses(), "MagicWand should have 0 uses after removal");
    }

    @Test
    void testMagicWandNotUsedWhenUnavailable() {
        Field field = new Field(3);
        MagicWand magicWand = new MagicWand(0);

        field.fillWithNoMovesTestSetup();


        System.out.println("Field before removing brick:");
        printField(field);


        field.removeBrick(2, 0, magicWand);


        System.out.println("Field after attempting to remove brick:");
        printField(field);

        //ocakavame ze kocka nebola odstranena
        assertNotNull(field.getBrick(2,0), "Brick at (2,0) should not be removed since MagicWand is unavailable");
        assertEquals(0, magicWand.getRemainingUses(), "MagicWand should still have 0 uses");
    }

    // pomocna metoda na vykreslenie pola pri testoch
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
