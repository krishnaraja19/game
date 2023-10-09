package com.kindred.game.controller;


import com.kindred.game.entity.GameResult;
import com.kindred.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game/api/v0")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/place-bet")
    public GameResult placeBet(@RequestParam Long userId, @RequestParam double betAmount, @RequestParam int chosenNumber) {
        return gameService.placeBet(userId, betAmount, chosenNumber);
    }
}
