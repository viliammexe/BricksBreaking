package sk.tuke.bricksbreaking;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sk.tuke.bricksbreaking.entity.Comment;
import sk.tuke.bricksbreaking.service.CommentService;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JpaTestConfig.class)
@Transactional
public class CommentServiceJPATest {

    @Autowired
    private CommentService commentService;

    @Test
    public void testAddAndGetComments() {
        int before = commentService.getComments("BricksBreaking").size();

        Comment comment = new Comment("BricksBreaking", "tester", "super test hry", new Date());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("BricksBreaking");
        assertEquals(before + 1, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("tester")));
    }
}
