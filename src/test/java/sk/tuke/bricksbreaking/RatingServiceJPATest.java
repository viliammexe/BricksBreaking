package sk.tuke.bricksbreaking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.bricksbreaking.entity.Rating;
import sk.tuke.bricksbreaking.service.RatingService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JpaTestConfig.class)
@Transactional
public class RatingServiceJPATest {

    @Autowired
    private RatingService ratingService;

    @Test
    public void testSetAndGetRating() {
        Rating rating = new Rating("BricksBreaking", "tester", 5, new Date());
        ratingService.setRating(rating);

        int actual = ratingService.getRating("BricksBreaking", "tester");
        assertEquals(5, actual);

        double avg = ratingService.getAverageRating("BricksBreaking");
        assertTrue(avg >= 1 && avg <= 5);
    }
}
