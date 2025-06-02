package sk.tuke.bricksbreaking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;
import java.util.Date;


@Entity
@NamedQuery( name = "Rating.insertRating",
        query = "SELECT r FROM Rating r WHERE r.game =:game ORDER BY r.rating DESC")
@NamedQuery( name = "Rating.getRating",
        query = "SELECT s FROM Rating s WHERE s.game=:game ORDER BY s.rating DESC")
@NamedQuery( name = "Rating.delete",
        query = "DELETE FROM Rating ")
public class Rating implements Serializable  {
    @Id
    @GeneratedValue
    private int ident; //identifikator
    private String game;
    private String player;
    private int rating;
    private Date ratedOn;

    public Rating() {}

    public Rating(String game, String player, int rating, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public String getGame() {
        return game;
    }


    public String getPlayer() {
        return player;
    }


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    public int getIdent() { return ident; }

    public void setIdent(int ident) { this.ident = ident; }
}
