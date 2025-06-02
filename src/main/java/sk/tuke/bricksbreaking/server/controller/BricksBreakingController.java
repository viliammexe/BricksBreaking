package sk.tuke.bricksbreaking.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.bricksbreaking.entity.Comment;
import sk.tuke.bricksbreaking.entity.Rating;
import sk.tuke.bricksbreaking.entity.Score;
import sk.tuke.bricksbreaking.game.core.*;

import sk.tuke.bricksbreaking.service.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class BricksBreakingController {

    private Field field;
    private MagicWand magicWand;
    private String playerName;
    private boolean gameStarted = false;
    private boolean scoreSaved = false;
    private boolean bombMode = false;
    //private long startTimeMillis;

    @Autowired private ScoreService scoreService;
    @Autowired private CommentService commentService;
    @Autowired private RatingService ratingService;



    @PostMapping("/bricksbreaking/start")
    public String startGame(@RequestParam String player, @RequestParam int difficulty) {
        this.playerName = player;
        this.magicWand = new MagicWand(difficulty);
        this.field = new Field(6);
        this.gameStarted = true;
        this.scoreSaved = false;
        //this.startTimeMillis = System.currentTimeMillis();
        return "redirect:/bricksbreaking/play";
    }

    @GetMapping("/bricksbreaking")
    public String showIntro() {
        return "BricksBreakingIntro"; // názov tvojej šablóny v templates/

    }

    @GetMapping("/bricksbreaking/play")
    public String playGame(Model model) {
        if (!gameStarted) return "redirect:/bricksbreaking";

        if (field.isGameOver(magicWand)) {
            field.setState(GameState.LOST);
            if(!scoreSaved) {
                saveScore();
                scoreSaved = true;
            }

        }


        model.addAttribute("score", field.getScore());
        model.addAttribute("magicWand", magicWand.getRemainingUses());
        model.addAttribute("gameOver", field.getState() == GameState.LOST);
        model.addAttribute("bricksBreakingController", this);

        return "BricksBreakingGame";
    }

    @GetMapping("/bricksbreaking/open")
    public String open(@RequestParam int row, @RequestParam int column) {
        if (field == null || field.getState() != GameState.PLAYING) {
            return "redirect:/bricksbreaking"; // vráti používateľa späť na úvodnú obrazovku
        }

        field.removeBrick(row, column, magicWand);
        if (field.areAllBricksRemoved()) {
            field.generateBricks();
        }

        return "redirect:/bricksbreaking/play";
    }

    @PostMapping("/bricksbreaking/addComment")
    public String addComment(@RequestParam String comment) {
        commentService.addComment(new Comment("BricksBreaking", playerName, comment, new Timestamp(System.currentTimeMillis())));
        return "redirect:/bricksbreaking/play";
    }

    @PostMapping("/bricksbreaking/addRating")
    public String addRating(@RequestParam int rating) {
        ratingService.setRating(new Rating("BricksBreaking", playerName, rating, new Timestamp(System.currentTimeMillis())));
        return "redirect:/bricksbreaking/play";
    }

    private void saveScore() {
        scoreService.addScore(new Score("BricksBreaking", playerName, field.getScore(), new Timestamp(System.currentTimeMillis())));
    }

    @GetMapping("/bricksbreaking/deleteRow")
    public String clearRow(@RequestParam int row) {
        if (field != null && field.getState() == GameState.PLAYING) {
            field.deleteRow(row);
        }
        return "redirect:/bricksbreaking/play";
    }

    public String getHtmlField() {
        StringBuilder html = new StringBuilder();
        html.append("<table class='game-grid'>");

        for (int row = 0; row < field.getRows(); row++) {
            html.append("<tr>");
            for (int col = 0; col < field.getColumns(); col++) {
                Brick brick = field.getBrick(row, col);
                String color = (brick == null) ? "empty" : brick.getColor().name().toLowerCase();
                String extraClass = (brick == null) ? "removing" : "";

                String href = bombMode
                        ? String.format("/bricksbreaking/useBomb?row=%d&column=%d", row, col)
                        : String.format("/bricksbreaking/open?row=%d&column=%d", row, col);

                html.append(String.format(
                        "<td class='brick %s %s'><a href='%s'>&nbsp;</a></td>",
                        color, extraClass, href
                ));

            }
            if(!field.wasDeleted) {
                html.append(String.format(
                        "<td><a class='clear-row-btn' href='/bricksbreaking/deleteRow?row=%d'></a></td>",
                        row
                ));
            }else {
                html.append(""); // prázdna bunka
            }
        }

            html.append("</tr>");

        html.append("</table>");
        return html.toString();
    }



    @ModelAttribute("comments")
    public Object comments() {
        return commentService.getComments("BricksBreaking");
    }

    @ModelAttribute("scores")
    public Object scores() {
        return scoreService.getTopScores("BricksBreaking");
    }

    @ModelAttribute("averageRating")
    public Double avgRating() {
        return ratingService.getAverageRating("BricksBreaking");
    }








}