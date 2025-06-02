package sk.tuke.bricksbreaking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.bricksbreaking.entity.Rating;
import sk.tuke.bricksbreaking.service.RatingService;

import java.util.Objects;

public class RatingServiceRestClient implements RatingService {
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public double getAverageRating(String game) {
        Integer response = restTemplate.getForObject(url + "/" + game, Integer.class);
        return Objects.requireNonNullElse(response, 0);
    }

    @Override
    public int getRating(String game, String player) {
        Integer response = restTemplate.getForObject(url + "/" + game + "/" + player, Integer.class);
        return Objects.requireNonNullElse(response, 0);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
