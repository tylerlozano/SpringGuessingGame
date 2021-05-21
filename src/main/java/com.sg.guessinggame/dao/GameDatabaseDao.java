package com.sg.guessinggame.dao;

import com.sg.guessinggame.dao.GameDao;
import com.sg.guessinggame.dao.RoundDao;
import com.sg.guessinggame.dto.Game;
import com.sg.guessinggame.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@Profile("prod")
public class GameDatabaseDao implements GameDao {

    private final JdbcTemplate jdbcTemplate;
    private final RoundDao roundDao;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate, RoundDao roundDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.roundDao = roundDao;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setId(rs.getInt("id"));
            game.setAnswer(rs.getString("answer"));
            game.setStatus(rs.getBoolean("status"));
            return game;
        }
    }

    public List<Game> getGames() {
        final String sql = "SELECT id, answer, status FROM game;";
        return jdbcTemplate.query(sql, new GameDatabaseDao.GameMapper());
    }

    public Game getGame(int gameId) {
        try {
            final String sql = "SELECT id, answer, status FROM game WHERE id = ?;";
            return jdbcTemplate.queryForObject(sql, new GameDatabaseDao.GameMapper(), gameId);
        }
        catch(DataAccessException ex) {
            return null;
        }
    }

    public void setStatus(int gameId, boolean status) {
        final String sql = "UPDATE game SET status = ?;";
        jdbcTemplate.update(sql, status);
    }

    public int add(String answer) {
        final String sql = "INSERT INTO game(answer) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, answer);
            return statement;

        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void delete(int id) {
        List<Round> rounds = roundDao.getRounds(id);
        for (Round round : rounds) {
            roundDao.delete(round.getId());
        }
        final String sql = "DELETE FROM game WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }
}