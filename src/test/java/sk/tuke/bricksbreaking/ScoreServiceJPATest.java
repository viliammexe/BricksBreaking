package sk.tuke.bricksbreaking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.bricksbreaking.entity.Score;
import sk.tuke.bricksbreaking.service.ScoreService;


import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JpaTestConfig.class)
@Transactional
public class ScoreServiceJPATest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void testAddAndGetTopScores() {
        int before = scoreService.getTopScores("BricksBreaking").size();

        Score s = new Score("BricksBreaking", "tester", 1234, new Date());
        scoreService.addScore(s);

        List<Score> scores = scoreService.getTopScores("BricksBreaking");
        scores.forEach(System.out::println);
        assertEquals(before + 1, scores.size());
        assertTrue(scores.stream().anyMatch(score -> score.getPlayer().equals("tester")));
    }
}
