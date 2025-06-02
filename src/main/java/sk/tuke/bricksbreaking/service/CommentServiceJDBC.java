package sk.tuke.bricksbreaking.service;



import org.springframework.web.bind.annotation.GetMapping;
import sk.tuke.bricksbreaking.entity.Comment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBC implements CommentService {
    public static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";

    public static final String INSERT = "INSERT INTO comment (game, player, comment, commented_on) VALUES (?, ?, ?, ?)";
    public static final String SELECT = "SELECT game, player, comment, commented_on FROM comment WHERE game = ? ORDER BY commented_on DESC LIMIT 10";
    public static final String DELETE = "DELETE FROM comment";

    @Override
    public void addComment(Comment comment) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, comment.getGame());
            statement.setString(2, comment.getPlayer());
            statement.setString(3, comment.getComment());
            statement.setTimestamp(4, new Timestamp(comment.getCommentedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new CommentException("Problem inserting comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT);
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                List<Comment> comments = new ArrayList<>();
                while (rs.next()) {
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                }
                return comments;
            }
        } catch (SQLException e) {
            throw new CommentException("Problem selecting comments", e);
        }
    }

    @Override
    public void reset() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new CommentException("Problem deleting comments", e);
        }
    }
}










//controller
//    @GetMapping("/bricksbreaking/activateBombMode")
//    public String activateBombMode() {
//        this.bombMode = true;
//        return "redirect:/bricksbreaking/play";
//    }

//    @GetMapping("/bricksbreaking/useBomb")
//    public String useBomb(@RequestParam int row, @RequestParam int column) {
//
//        if (this.field != null ) {
//            this.field.useBomb(row, column);
//        }
//
//        this.bombMode = false;
//        return "redirect:/bricksbreaking/play";
//    }
//
//     public long getElapsedSeconds() {
//        return (System.currentTimeMillis() - startTimeMillis) / 1000;
//    }


//@GetMapping("/bricksbreaking/undo")
//public String undo() {
//    if (field != null) {
//        field.undo();
//    }
//    return "redirect:/bricksbreaking/play";
//}
