package be.goosegame.domain;

import be.goosegame.Player;

import java.util.List;
import java.util.stream.Collectors;

public class GameException extends RuntimeException {

    private GameException(final String message) {
        super(message);
    }

    public static GameException tooManyPlayersException(final List<Player> players) {
        final String names = players.stream().map(Player::getName).collect(Collectors.joining(", "));
        return new GameException(String.format("too many players already: %s", names));
    }

    public static GameException duplicatePlayerException(final Player player) {
        return new GameException(String.format("nickname already taken: %s", player.getNickname()));
    }

    public static GameException gameNotStartedException() {
        return new GameException("Game not started, waiting for more players");
    }

    public static GameException userDoesNotExistException() {
        return new GameException("User rolling dice was not in game!");
    }

    public static GameException gameOverException() {
        return new GameException("The game is over");
    }

    public static GameException notYourTurnException(final Player player) {
        return new GameException(String.format("Is not your turn %s!", player.getName()));
    }
}
