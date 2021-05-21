package com.sg.guessinggame.dto;

public class Guess {
    public String guess_;
    public int gameId;

    public Guess() {}

    public Guess(String guess_, int gameId) {
        this.guess_ = guess_;
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getGuess() {
        return guess_;
    }

    public void setGuess(String guess_) {
        this.guess_ = guess_;
    }
}
