package com.sg.guessinggame.dao;

import com.sg.guessinggame.dto.Round;

import java.util.List;

public interface RoundDao {

    Round add(Round round);

    void delete(int id);

    List<Round> getRounds(int gameId);
}
