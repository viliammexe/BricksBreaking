package sk.tuke.bricksbreaking.service;

public class CommentException extends RuntimeException {
    public CommentException(String message) {
        super(message);
    }
    public CommentException(String message, Throwable cause) {
        super(message, cause);
    }
}
