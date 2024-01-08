package be.goosegame.domain.impl;

import be.goosegame.Player;
import be.goosegame.domain.GameException;
import be.goosegame.domain.GooseGame;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class GooseGameImpl implements GooseGame {

    final List<Player> players = new LinkedList<>();

    @Override
    public String createPlayer(final String name, final String nickName) throws GameException {
        final UUID uuid = UUID.randomUUID();
        final Player player = new Player(name, nickName, uuid);
        if (moreThanFourPlayer()) {
            throw GameException.tooManyPlayersException(players);
        }
        if (exist(player)) {
            throw GameException.duplicatePlayerException(player);
        }
        players.add(player);
        return uuid.toString();
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    private boolean exist(final Player player) {
        return players.stream().anyMatch(p -> p.getNickname().equals(player.getNickname()));
    }

    private boolean moreThanFourPlayer() {
        return players.size() >= 4;
    }
}
