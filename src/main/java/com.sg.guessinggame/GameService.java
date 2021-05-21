package com.sg.guessinggame;

import com.sg.guessinggame.dao.GameDao;
import com.sg.guessinggame.dao.RoundDao;
import com.sg.guessinggame.dto.Game;
import com.sg.guessinggame.dto.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {

    private final GameDao gameDao;
    private final RoundDao roundDao;
    private final static String RESULT_FORMAT = "p:%d:e:%d";
    // private final static Boolean WIN_CONDITION = (Map<String, Integer> m) -> { m.get("exact") == 4; };

    @Autowired
    public GameService(GameDao gameDao, RoundDao roundDao) {
        this.gameDao = gameDao;
        this.roundDao = roundDao;
    }

    // helpers
    private String randomAnswer() {
        String answer = "";
        int prevNum = -1;
        for (int i = 0; i < 4; i++) {
            int num = ThreadLocalRandom.current().nextInt(0, 10);
            while(num == prevNum) {
                num = ThreadLocalRandom.current().nextInt(0, 10);
            }
            prevNum = num;
            answer = answer + num;
        }
        return answer;
    }

     // O(n) alg from GFG
    private boolean uniqueCharacters(String str)
    {
        // Assuming string can have characters a-z
        // this has 32 bits set to 0
        int checker = 0;

        for (int i = 0; i < str.length(); i++) {
            int bitAtIndex = str.charAt(i) - 'a';

            // if that bit is already set in checker,
            // return false
            if ((checker & (1 << bitAtIndex)) > 0)
                return false;

            // otherwise update and continue by
            // setting that bit in the checker
            checker = checker | (1 << bitAtIndex);
        }

        // no duplicates encountered, return true
        return true;
    }

    private Boolean validGuess(String guess) {
        return guess.length() == 4 && uniqueCharacters(guess);
    }

    private Map<String, Integer> result(String answer, String guess)  {

        Map<String, Integer> result_ = new HashMap<>();
        result_.put("exact", 0); result_.put("partial", 0);

        // value -> index
        // TODO: can store in redis as game answer map and retire when game is finished
        Map<Character, Integer> answer_  = new HashMap<>();
        for (int i = 0; i < answer.length(); i++)
        {
            answer_.put(answer.charAt(i), i);
        }
        for (int i = 0; i < guess.length(); i++)
        {
            int d = answer_.getOrDefault(guess.charAt(i), -1);
            if (d > -1) {
                if (d == i) result_.put("exact", result_.get("exact") + 1);
                else result_.put("partial", result_.get("partial") + 1);
            }
        }

        return result_;
    }

    // decouple format from semantics to check win condition
    private Boolean isWin(Map<String, Integer> result_) {
        return result_.get("exact") == 4;
    }

    private String formatResult(Map<String, Integer> result_) {
        return String.format(RESULT_FORMAT, result_.get("partial"), result_.get("exact"));
    }


    public int begin() {
        return gameDao.add(randomAnswer());
    }

    // transactional in dao
    public Round guess(int gameId, String guess_) throws IllegalArgumentException {
        if (! validGuess(guess_)) {throw new IllegalArgumentException("Guess must be 4 unique digits"); }
        String answer = gameDao.getGame(gameId).getAnswer();
        Map<String, Integer> result_ = result(answer, guess_);
        boolean status = isWin(result_);
        String fResult = formatResult(result_);
        if (status) gameDao.setStatus(gameId, status);

        Round round = new Round();
        round.setResult(fResult);
        round.setGameId(gameId);
        round.setGuess(guess_);
        return roundDao.add(round);
    }

    public List<Game> getGames() {
        List<Game> games = gameDao.getGames();
        for (Game game : games) {
            if (! game.getStatus()) game.setAnswer("****"); //.repeat(gameDao.ANSWER_LENGTH));
        }

        return games;
    }

    public Game getGame(int gameId) {
        Game game = gameDao.getGame(gameId);
        if (! game.getStatus()) game.setAnswer("****"); //.repeat(gameDao.ANSWER_LENGTH));
        return game;
    }

    public List<Round> getRounds(int gameId) {
        return roundDao.getRounds(gameId);
    }
}

//        "begin" - POST – Starts a game, generates an answer, and sets the correct status. Should return a 201 CREATED message as well as the created gameId.
//        "guess" – POST – Makes a guess by passing the guess and gameId in as JSON. The program must calculate the results of the guess and mark the game finished if the guess is correct. It returns the Round object with the results filled in.
//        "game" – GET – Returns a list of all games. Be sure in-progress games do not display their answer.
//        "game/{gameId}" - GET – Returns a specific game based on ID. Be sure in-progress games do not display their answer.
//        "rounds/{gameId} – GET – Returns a list of rounds for the specified game sorted by time.
