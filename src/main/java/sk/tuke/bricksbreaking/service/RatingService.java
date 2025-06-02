package sk.tuke.bricksbreaking.service;

import sk.tuke.bricksbreaking.entity.Rating;

public interface RatingService {
    void setRating(Rating rating);



    double getAverageRating(String game);

    void reset();

    int getRating(String game,String player);
}
