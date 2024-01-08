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
}
