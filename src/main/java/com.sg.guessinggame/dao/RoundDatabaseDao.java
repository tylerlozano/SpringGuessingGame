package com.sg.guessinggame.dao;

import java.sql.*;
import java.util.List;
import java.util.Date.*;

import com.sg.guessinggame.dao.RoundDao;
import com.sg.guessinggame.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

// TODO: ensure check for bad data so nullchecks in rest controller can work
@Repository
@Profile("prod")
public class RoundDatabaseDao implements RoundDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Round add(Round round) {

        final String sql = "INSERT INTO round(gameId, result, guess, time) VALUES(?,?,?,?);";
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGameId());
            statement.setString(2, round.getResult());
            statement.setString(3, round.getGuess());
            statement.setTimestamp(4, ts);

            return statement;

        }, keyHolder);
        round.setId(keyHolder.getKey().intValue());
        round.setTime(ts);

        return round;
    }

    public void delete(int id) {
        final String sql = "DELETE FROM round WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Round> getRounds(int gameId) {
        final String sql = "SELECT id, gameId, result, guess, time FROM round WHERE gameId = ? ORDER BY time;";
        return jdbcTemplate.query(sql, new RoundMapper(), gameId);
    }

    private static final class RoundMapper implements RowMapper<Round> {

        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setId(rs.getInt("id"));
            round.setGameId(rs.getInt("gameId"));
            round.setResult(rs.getString("result"));
            round.setGuess(rs.getString("guess"));
            round.setTime(rs.getTimestamp("time"));
            return round;
        }
    }
}