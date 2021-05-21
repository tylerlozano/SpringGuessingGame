package com.sg.guessinggame.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Round {

    private int id;
    private int gameId;
    private String guess_;
    private String result;
    private Timestamp time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", guess_='" + guess_ + '\'' +
                ", result='" + result + '\'' +
                ", time=" + time +
                '}';
    }

    public Timestamp getTime(){
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setGuess(String guess) {
        this.guess_ = guess;
    }

    public String getGuess() {
        return guess_;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return id == round.id && gameId == round.gameId && Objects.equals(guess_, round.guess_) && Objects.equals(result, round.result) && Objects.equals(time, round.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gameId, guess_, result, time);
    }
}