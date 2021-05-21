//        "begin" - POST – Starts a game, generates an answer, and sets the correct status. Should return a 201 CREATED message as well as the created gameId.
//        "guess" – POST – Makes a guess by passing the guess and gameId in as JSON. The program must calculate the results of the guess and mark the game finished if the guess is correct. It returns the Round object with the results filled in.
//        "game" – GET – Returns a list of all games. Be sure in-progress games do not display their answer.
//        "game/{gameId}" - GET – Returns a specific game based on ID. Be sure in-progress games do not display their answer.
//        "rounds/{gameId} – GET – Returns a list of rounds for the specified game sorted by time.
package com.sg.guessinggame.controllers;


import com.sg.guessinggame.GameService;
import com.sg.guessinggame.dto.Game;
import com.sg.guessinggame.dto.Guess;
import com.sg.guessinggame.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService service;

    @Autowired
    public GameController(GameService service) {
        this.service = service;
    }

    @PostMapping("/begin")
    public ResponseEntity<Integer> begin() {
        return new ResponseEntity(service.begin(), HttpStatus.CREATED);
    }

    @PostMapping("/guess")
    public Round guess(@RequestBody Guess guess_){
        return service.guess(guess_.getGameId(), guess_.getGuess());
    }

    @GetMapping("/games")
    public List<Game> games() {
        return service.getGames();
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> game(@PathVariable int gameId) {
        Game game_ = service.getGame(gameId);
        if (game_ == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(game_);
    }

    @GetMapping("/rounds/{gameId}")
    public ResponseEntity<List<Round>> rounds(@PathVariable int gameId) {
        List<Round> rounds_ = service.getRounds(gameId);
        if (rounds_ == null) {
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(rounds_);
    }


}