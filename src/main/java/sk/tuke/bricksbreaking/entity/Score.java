package sk.tuke.bricksbreaking.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Score.getTopScores",
        query = "SELECT s FROM Score s WHERE s.game=:game ORDER BY s.score DESC")
@NamedQuery( name = "Score.resetScores",
        query = "DELETE FROM Score")
public class Score implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident;

    private String game;
    private String player;
    private int score;
    private Date playedOn;


    public Score() {}

    public Score(String game, String player, int score, Date playedOn) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    @Override
    public String toString() {
        return "Score{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", score=" + score +
                ", playedOn=" + playedOn +
                '}';
    }
    public int getIdent() { return ident; }

    public void setIdent(int ident) { this.ident = ident; }
}
