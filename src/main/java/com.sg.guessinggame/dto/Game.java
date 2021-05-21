package com.sg.guessinggame.dto;

import java.util.Objects;

public class Game {
    private int id;
    private String answer;
    private boolean status; // 1 for finished

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && status == game.status && Objects.equals(answer, game.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer, status);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}