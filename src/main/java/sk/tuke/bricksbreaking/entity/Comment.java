package sk.tuke.bricksbreaking.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "Comment.getComments",
        query = "SELECT c FROM Comment c WHERE c.game = :game ORDER BY c.commentedOn DESC")
@NamedQuery(name = "Comment.deleteComments",
        query = "DELETE FROM Comment")

public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ident; //identifikator

    private String game;
    private String player;
    private String comment;
    private Date commentedOn;

    public Comment(String game, String player, String comment, Date commentedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.commentedOn = commentedOn;
    }
    public Comment() {}


    public String getGame() {
        return game;
    }

    public String getPlayer() {
        return player;
    }

    public String getComment() {
        return comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public int getIdent() { return ident; }

    public void setIdent(int ident) { this.ident = ident; }
}
