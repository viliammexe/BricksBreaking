package sk.tuke.bricksbreaking;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.bricksbreaking.entity.Comment;
import sk.tuke.bricksbreaking.service.CommentServiceJDBC;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {
    private CommentServiceJDBC commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentServiceJDBC();
        commentService.reset(); //vymaze komentare pred testom
    }

    @Test
    void testAddComment() {
        Comment comment = new Comment("BricksBreaking", "Player1", "Great game!", new Timestamp(System.currentTimeMillis()));
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("BricksBreaking");

        assertFalse(comments.isEmpty(), "Comment list should not be empty");
        assertEquals(1, comments.size(), "There should be 1 comment in the database");
        assertEquals("Player1", comments.get(0).getPlayer(), "Player should be Player1");
        assertEquals("Great game!", comments.get(0).getComment(), "Comment text should match");
    }

    @Test
    void testMultipleComments() {
        commentService.addComment(new Comment("BricksBreaking", "Player1", "Fun game!", new Timestamp(System.currentTimeMillis())));
        commentService.addComment(new Comment("BricksBreaking", "Player2", "Challenging!", new Timestamp(System.currentTimeMillis())));
        commentService.addComment(new Comment("BricksBreaking", "Player3", "Could be better!", new Timestamp(System.currentTimeMillis())));

        List<Comment> comments = commentService.getComments("BricksBreaking");

        assertEquals(3, comments.size(), "There should be 3 comments in the database");
        assertEquals("Player3", comments.get(0).getPlayer(), "Most recent comment should be from Player3");
    }

    @Test
    void testResetComments() {
        commentService.addComment(new Comment("BricksBreaking", "Player1", "Awesome!", new Timestamp(System.currentTimeMillis())));
        commentService.reset();

        List<Comment> comments = commentService.getComments("BricksBreaking");
        assertTrue(comments.isEmpty(), "Comment list should be empty after reset");
    }
}
