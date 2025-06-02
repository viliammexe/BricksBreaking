package sk.tuke.bricksbreaking;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.bricksbreaking.entity.Rating;
import sk.tuke.bricksbreaking.service.RatingServiceJDBC;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private RatingServiceJDBC ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceJDBC();
        ratingService.reset(); // Vymaže všetky hodnotenia pred testom
    }

    @Test
    void testSetRating() {
        Rating rating = new Rating("BricksBreaking", "Player1", 5, new Timestamp(System.currentTimeMillis()));
        ratingService.setRating(rating);

        double avgRating = ratingService.getAverageRating("BricksBreaking");
        assertEquals(5.0, avgRating, "Average rating should be 5.0");
    }

    @Test
    void testMultipleRatings() {
        ratingService.setRating(new Rating("BricksBreaking", "Player1", 5, new Timestamp(System.currentTimeMillis())));
        ratingService.setRating(new Rating("BricksBreaking", "Player2", 3, new Timestamp(System.currentTimeMillis())));

        double avgRating = ratingService.getAverageRating("BricksBreaking");
        assertEquals(4.0, avgRating, "Average rating should be (5+3)/2 = 4.0");
    }

    @Test
    void testUpdateRating() {
        ratingService.setRating(new Rating("BricksBreaking", "Player1", 2, new Timestamp(System.currentTimeMillis())));
        ratingService.setRating(new Rating("BricksBreaking", "Player1", 4, new Timestamp(System.currentTimeMillis())));

        double avgRating = ratingService.getAverageRating("BricksBreaking");
        assertEquals(4.0, avgRating, "Player1's rating should update to 4.0");
    }

    @Test
    void testResetRatings() {
        ratingService.setRating(new Rating("BricksBreaking", "Player1", 5, new Timestamp(System.currentTimeMillis())));
        ratingService.reset();

        double avgRating = ratingService.getAverageRating("BricksBreaking");
        assertEquals(0.0, avgRating, "Average rating should be 0 after reset");
    }
}
