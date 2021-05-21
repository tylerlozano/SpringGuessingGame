package com.sg.guessinggame.dao;

import com.sg.guessinggame.GameService;
import com.sg.guessinggame.TestApplicationConfiguration;
import com.sg.guessinggame.dto.Game;
import com.sg.guessinggame.dto.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDaoDBTest {

    @Autowired RoundDao roundDao;
    @Autowired GameDao gameDao;
    @Autowired GameService gs;

    @Before
    public void setUp() {
        List<Game> games = gameDao.getGames();
        for (Game game: games) {
            gameDao.delete(game.getId());
        }
    }

    @Test
    public void testAddRound() {
        int gameId = gameDao.add("1234");
        Round round = new Round();
        round.setGameId(gameId);
        round.setGuess("3456");
        round.setResult("p:2:e:0");
        Round actual = roundDao.add(round);
        List<Round> test = roundDao.getRounds(gameId);
        assertEquals(actual, test.get(0));
    }

    @Test
    public void testDeleteRound() {
        int gameId = gameDao.add("1234");
        Round round = new Round();
        round.setGameId(gameId);
        round.setGuess("3456");
        round.setResult("p:2:e:0");
        Round actual = roundDao.add(round);
        List<Round> test = roundDao.getRounds(gameId);
        assertEquals(actual, test.get(0));
        roundDao.delete(actual.getId());
        List<Round> test2 = roundDao.getRounds(gameId);
        assertEquals(0, test2.size());
    }

    @Test
    public void testGetRounds() {
        int gameId = gameDao.add("2345");
        Round round1 = new Round();
        round1.setGameId(gameId);
        round1.setGuess("3456");
        round1.setResult("p:2:e:0");
        Round actual1 = roundDao.add(round1);
        Round round2 = new Round();
        round2.setGameId(gameId);
        round2.setGuess("5634");
        round2.setResult("p:2:e:0");
        Round actual2 = roundDao.add(round2);
        assertNotEquals(actual1, actual2);
        List<Round> rounds = roundDao.getRounds(gameId);
        assertEquals(2, rounds.size());
        assertTrue(rounds.get(0).getTime().before(rounds.get(1).getTime()));
        assertEquals(actual1, rounds.get(0));
        assertEquals(actual2, rounds.get(1));
    }







}