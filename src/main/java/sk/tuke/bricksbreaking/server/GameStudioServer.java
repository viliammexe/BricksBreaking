package sk.tuke.bricksbreaking.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sk.tuke.bricksbreaking.service.*;

@SpringBootApplication
@Configuration
@EntityScan("sk.tuke.bricksbreaking.entity")
public class GameStudioServer {

    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public RatingService ratingService() {
        return new RatingServiceJPA();
    }

    @Bean
    public CommentService commentService() {
        return new CommentServiceJPA();
    }



    @Controller
    public class HomeRedirectController {
        @GetMapping("/")
        public String redirectToIntro() {
            return "BricksBreakingIntro";
        }
    }
}