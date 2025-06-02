package sk.tuke.bricksbreaking.game.ConsoleUI;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.bricksbreaking.entity.Comment;
import sk.tuke.bricksbreaking.entity.Rating;
import sk.tuke.bricksbreaking.entity.Score;
import sk.tuke.bricksbreaking.game.core.Brick;
import sk.tuke.bricksbreaking.game.core.Field;
import sk.tuke.bricksbreaking.game.core.GameState;
import sk.tuke.bricksbreaking.game.core.MagicWand;
import sk.tuke.bricksbreaking.service.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ConsoleUI {

    private final Field board;
    private List<Integer> moves;
    private MagicWand magicWand;
    private Scanner scanner;
    private String playerName;


    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;

    public ConsoleUI(int rows){
        this.moves = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.board = new Field(rows);



    }

    public void startGame() {
        System.out.println("▗▄▄▖ ▗▄▄▖ ▗▄▄▄▖ ▗▄▄▖▗▖ ▗▖ ▗▄▄▖▗▄▄▖ ▗▄▄▖ ▗▄▄▄▖ ▗▄▖ ▗▖ ▗▖▗▄▄▄▖▗▖  ▗▖ ▗▄▄▖\n" +
                "▐▌ ▐▌▐▌ ▐▌  █  ▐▌   ▐▌▗▞▘▐▌   ▐▌ ▐▌▐▌ ▐▌▐▌   ▐▌ ▐▌▐▌▗▞▘  █  ▐▛▚▖▐▌▐▌   \n" +
                "▐▛▀▚▖▐▛▀▚▖  █  ▐▌   ▐▛▚▖  ▝▀▚▖▐▛▀▚▖▐▛▀▚▖▐▛▀▀▘▐▛▀▜▌▐▛▚▖   █  ▐▌ ▝▜▌▐▌▝▜▌\n" +
                "▐▙▄▞▘▐▌ ▐▌▗▄█▄▖▝▚▄▄▖▐▌ ▐▌▗▄▄▞▘▐▙▄▞▘▐▌ ▐▌▐▙▄▄▖▐▌ ▐▌▐▌ ▐▌▗▄█▄▖▐▌  ▐▌▝▚▄▞▘\n" +
                "                                                                       \n" +
                "                                                                       \n" +
                "                                                                       ");
        System.out.println("=================================");
        System.out.println("  Welcome to BricksBreaking! 🎮");
        System.out.println("=================================");

        System.out.print("Enter your name: ");
        this.playerName = scanner.nextLine();
        this.magicWand = new MagicWand(selectDifficulty());

        while (board.getState() == GameState.PLAYING) {
            show();
            handleInput();
            if(board.areAllBricksRemoved()){
                board.generateBricks();
            }
            if(board.isGameOver(this.magicWand)){
                board.setState(GameState.LOST) ;
            }
        }

        show();
        System.out.println("❌ Game Over!");
        saveGameResults();
    }

    private void handleInput() {
        System.out.println("Enter row and column (example: 1 2) or -1 to exit: ");

        int row, col;

        try {
            row = scanner.nextInt();

            if (row == -1) {
                System.out.println("Game exited. Thanks for playing!");
                this.board.setState(GameState.LOST);
                return;
            }

            col = scanner.nextInt();

            if (row >= 0 && row < board.getRows() && col >= 0 && col < board.getColumns()) {
                board.removeBrick(row, col, magicWand);
            } else {
                System.out.println("Invalid input. Try again.");
            }

        } catch (Exception e) {
            System.out.println("Invalid input! Please enter two numbers (row and column).");
            scanner.nextLine();
        }
    }

    private void saveGameResults() {
        int finalScore = board.getScore();
        System.out.println("Final Score: " + finalScore);

        // ulozenie skore hraca
        scoreService.addScore(new Score("BricksBreaking", playerName, finalScore, new Timestamp(System.currentTimeMillis())));
        System.out.println("🏆 Score saved!");

        // moznost pridat komentar
        String commentChoice;
        do {
            System.out.print("Do you want to add a comment? (y/n): ");
            commentChoice = scanner.next().trim().toLowerCase();
        } while (!commentChoice.equals("y") && !commentChoice.equals("n"));

        if (commentChoice.equals("y")) {
            System.out.print("Enter your comment: ");
            scanner.nextLine(); // vycistenie bufferu
            String commentText = scanner.nextLine();
            commentService.addComment(new Comment("BricksBreaking", playerName, commentText, new Timestamp(System.currentTimeMillis())));
            System.out.println("📝 Comment saved!");
        }

        //moznost pridat rating
        String ratingChoice;
        do {
            System.out.print("Do you want to rate the game? (y/n): ");
            ratingChoice = scanner.next().trim().toLowerCase();
        } while (!ratingChoice.equals("y") && !ratingChoice.equals("n"));

        if (ratingChoice.equals("y")) {
            int rating;
            do {
                System.out.print("Rate the game (1-5): ");
                while (!scanner.hasNextInt()) {
                    System.out.print("Invalid input. Enter a number between 1-5: ");
                    scanner.next();
                }
                rating = scanner.nextInt();
            } while (rating < 1 || rating > 5);

            ratingService.setRating(new Rating("BricksBreaking", playerName, rating, new Timestamp(System.currentTimeMillis())));
            System.out.println("⭐ Rating saved!");
        }

        showTopScores();
        showComments();
        showAverageRating();
    }

    private int selectDifficulty() {
        System.out.println("\nChoose difficulty level:");
        System.out.println("1 - Easy (4 Magic Wand uses)");
        System.out.println("2 - Medium (3 Magic Wand uses)");
        System.out.println("3 - Hard (2 Magic Wand uses)");

        int magicWandUses = 0;
        while (magicWandUses == 0) {
            System.out.print("Enter difficulty (1/2/3): ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    magicWandUses = 4;
                    System.out.println("Selected Difficulty: Easy");
                    System.out.println(" ");
                    break;
                case "2":
                    magicWandUses = 3;
                    System.out.println("Selected Difficulty: Medium");
                    System.out.println(" ");
                    break;
                case "3":
                    magicWandUses = 2;
                    System.out.println("Selected Difficulty: Hard");
                    System.out.println(" ");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter 1, 2, or 3.");
            }
        }
        return magicWandUses;
    }


    private void show() {
        System.out.print("    ");
        for (int col = 0; col < this.board.getColumns(); col++) {
            System.out.print(col + " ");
        }
        System.out.println("\n  ------------------");

        for (int row = 0; row < this.board.getRows(); row++) {
            System.out.print(row + " | ");
            for (int col = 0; col < this.board.getColumns(); col++) {
                Brick currentBrick = this.board.getBrick(row, col);
                System.out.print(currentBrick == null ? "  " : currentBrick.getColor().name().charAt(0) + " ");
            }
            System.out.println("|");
        }

        System.out.println("  ------------------");
        System.out.println("Magic Wand uses : " + this.magicWand.getRemainingUses() + "  Your Score : " + this.board.getScore());
    }

    private void showTopScores() {
        System.out.println("=== Top Scores ===");
        List<Score> scores = scoreService.getTopScores("BricksBreaking");
        if (scores.isEmpty()) {
            System.out.println("No scores recorded yet.");
        } else {
            scores.forEach(score -> System.out.println(score.getPlayer() + ": " + score.getScore()));
        }
    }

    private void showComments() {
        System.out.println("=== Player Comments ===");
        List<Comment> comments = commentService.getComments("BricksBreaking");
        if (comments.isEmpty()) {
            System.out.println("No comments yet.");
        } else {
            comments.forEach(comment -> System.out.println(comment.getPlayer() + ": " + comment.getComment()));
        }
    }

    private void showAverageRating() {
        double avgRating = ratingService.getAverageRating("BricksBreaking");
        System.out.println("=== Average Rating: " + (avgRating > 0 ? avgRating : "No ratings yet") + " ===");
    }

}
