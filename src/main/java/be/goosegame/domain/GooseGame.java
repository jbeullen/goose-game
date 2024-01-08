package be.goosegame.domain;

import be.goosegame.Player;

import java.util.List;

public interface GooseGame {
    String createPlayer(String name, String nickName) throws GameException;

    List<Player> getPlayers();
}
