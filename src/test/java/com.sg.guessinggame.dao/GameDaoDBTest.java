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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameDaoDBTest {

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
    public void testGetGames() {
        String answer1 = "1234";
        String answer2 = "2345";

        int gameId1 = gameDao.add(answer1);
        int gameId2 = gameDao.add(answer2);
        Game game1 = new Game();
        Game game2 = new Game();
        game1.setId(gameId1);
        game1.setStatus(false);
        game1.setAnswer(answer1);
        game2.setId(gameId2);
        game2.setStatus(false);
        game2.setAnswer(answer2);
        Game actual1 = gameDao.getGame(gameId1);
        assertEquals(actual1, game1);
        Game actual2 = gameDao.getGame(gameId2);
        assertEquals(actual2, game2);
        assertNotEquals(actual1, actual2);
        List<Game> games = gameDao.getGames();
        assertEquals(2, games.size());
        assertTrue(actual1.equals(games.get(0)) || actual1.equals(games.get(1)));
    }

    @Test
    public void testSetStatus(){//int gameId, boolean status) {
        String answer = "1234";
        int gameId = gameDao.add(answer);
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(false);
        game.setAnswer(answer);
        Game actual = gameDao.getGame(gameId);
        assertEquals(actual, game);

        assertTrue(!actual.getStatus());
        gameDao.setStatus(gameId, true);
        assertTrue(gameDao.getGame(gameId).getStatus());
    }

    @Test
    public void testAddGet() {
        String answer = "1234";
        int gameId = gameDao.add(answer);
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(false);
        game.setAnswer(answer);
        Game actual = gameDao.getGame(gameId);
        assertEquals(actual, game);
    }

    @Test
    public void testDelete() {//int id) {
        String answer = "1234";
        int gameId = gameDao.add(answer);
        Game game = new Game();
        game.setId(gameId);
        game.setStatus(false);
        game.setAnswer(answer);
        Game actual = gameDao.getGame(gameId);
        assertEquals(actual, game);

        gameDao.delete(game.getId());
        Game nullGame = gameDao.getGame(gameId);
        assertNull(nullGame);
    }
}
