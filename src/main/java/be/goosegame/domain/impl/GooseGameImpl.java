package be.goosegame.domain.impl;

import be.goosegame.Player;
import be.goosegame.domain.GameException;
import be.goosegame.domain.GooseGame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static be.goosegame.domain.GameException.*;

public class GooseGameImpl implements GooseGame {

    final List<Player> players = new LinkedList<>();
    int currentIndex = 0;

    @Override
    public String createPlayer(final String name, final String nickName) throws GameException {
        final UUID uuid = UUID.randomUUID();
        final Player player = new Player(name, nickName, uuid);
        if (moreThanFourPlayer()) {
            throw tooManyPlayersException(players);
        }
        if (exist(player)) {
            throw duplicatePlayerException(player);
        }
        players.add(player);
        return uuid.toString();
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public void roll(final String playerId) throws GameException {
        if (!moreThanFourPlayer()) {
            throw gameNotStartedException();
        }

        final Player player = players.stream()
                .filter(p -> p.getUuid().toString().equals(playerId))
                .findFirst()
                .orElseThrow(GameException::userDoesNotExistException);

        if (isNotCurrentPlayer(player)) {
            throw notYourTurnException(player);
        }

        if (isGameOver(players)) {
            throw gameOverException();
        }

        currentIndex = (currentIndex + 1) % 4;
    }

    private boolean isGameOver(final List<Player> players) {
        return players.stream().anyMatch(p -> p.getPosition() == 63);
    }

    private boolean isNotCurrentPlayer(final Player player) {
        return !player.equals(players.get(currentIndex));
    }

    private boolean exist(final Player player) {
        return players.stream().anyMatch(p -> p.getNickname().equals(player.getNickname()));
    }

    private boolean moreThanFourPlayer() {
        return players.size() >= 4;
    }
}
