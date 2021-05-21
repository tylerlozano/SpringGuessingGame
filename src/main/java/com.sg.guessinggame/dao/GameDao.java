package com.sg.guessinggame.dao;

import com.sg.guessinggame.dto.Round;
import com.sg.guessinggame.dto.Game;

import java.util.List;

public interface GameDao {
    // static final int ANSWER_LENGTH = 4;

    List<Game> getGames();

    Game getGame(int gameId);

    void setStatus(int gameId, boolean status);

    int add(String answer);

    void delete(int gameId);

}




