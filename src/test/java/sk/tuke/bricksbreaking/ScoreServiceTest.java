package sk.tuke.bricksbreaking;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.bricksbreaking.entity.Score;
import sk.tuke.bricksbreaking.service.ScoreServiceJDBC;

import java.util.List;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class ScoreServiceTest {
    private ScoreServiceJDBC scoreService;

    @BeforeEach
    void setUp() {
        scoreService = new ScoreServiceJDBC();
        scoreService.reset(); // Vymaže všetky skóre pred každým testom
    }

    @Test
    void testAddScore() {
        Score score = new Score("BricksBreaking", "Player1", 100, new Timestamp(System.currentTimeMillis()));
        scoreService.addScore(score);

        List<Score> scores = scoreService.getTopScores("BricksBreaking");

        assertFalse(scores.isEmpty(), "Score list should not be empty");
        assertEquals(1, scores.size(), "There should be 1 score in the database");
        assertEquals("Player1", scores.get(0).getPlayer(), "Player should be Player1");
        assertEquals(100, scores.get(0).getScore(), "Score should be 100");
    }

    @Test
    void testMultipleScores() {
        scoreService.addScore(new Score("BricksBreaking", "Player1", 100, new Timestamp(System.currentTimeMillis())));
        scoreService.addScore(new Score("BricksBreaking", "Player2", 200, new Timestamp(System.currentTimeMillis())));
        scoreService.addScore(new Score("BricksBreaking", "Player3", 50, new Timestamp(System.currentTimeMillis())));

        List<Score> scores = scoreService.getTopScores("BricksBreaking");

        assertEquals(3, scores.size(), "There should be 3 scores in the database");
        assertEquals("Player2", scores.get(0).getPlayer(), "Top player should be Player2");
        assertEquals(200, scores.get(0).getScore(), "Top score should be 200");
    }

    @Test
    void testResetScores() {
        scoreService.addScore(new Score("BricksBreaking", "Player1", 100, new Timestamp(System.currentTimeMillis())));
        scoreService.reset();

        List<Score> scores = scoreService.getTopScores("BricksBreaking");
        assertTrue(scores.isEmpty(), "Score list should be empty after reset");
    }


}
