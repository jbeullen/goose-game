package be.goosegame.domain;

import be.goosegame.domain.impl.GooseGameImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class GooseGameTest {
    private GooseGame gooseGame;

    @Before
    public void setUp() {
        gooseGame = new GooseGameImpl();
    }

    @Test
    public void given_newGame_when_createPlayer_then_playerIsAddWithValidUUID() {
        assertThat(UUID.fromString(gooseGame.createPlayer("Thijs", "CoolDude"))).isNotNull();
        assertThat(gooseGame.getPlayers()).hasSize(1);
    }

    @Test
    public void given_newGameWithExistingPlayersAndNewPlayerNicknameAlreadyUsed_when_createPlayer_then_playerNotAddedAndExceptionThrown() {
        assertThat(gooseGame.createPlayer("Thijs", "CoolDude")).isNotNull();
        assertThatThrownBy(() -> gooseGame.createPlayer("Jeroen", "CoolDude")).isInstanceOf(GameException.class)
                .hasMessage("nickname already taken: CoolDude");
        assertThat(gooseGame.getPlayers()).hasSize(1);
    }

    @Test
    public void given_newGameWithFourExisting_when_createPlayer_then_playerNotAddedAndExceptionThrown() {
        assertThat(gooseGame.createPlayer("Player1", "Goose1")).isNotNull();
        assertThat(gooseGame.createPlayer("Player2", "Goose2")).isNotNull();
        assertThat(gooseGame.createPlayer("Player3", "Goose3")).isNotNull();
        assertThat(gooseGame.createPlayer("Player4", "Goose4")).isNotNull();

        assertThatThrownBy(() -> gooseGame.createPlayer("Jeroen", "Jerre")).isInstanceOf(GameException.class)
                .hasMessage("too many players already: Player1, Player2, Player3, Player4");
        assertThat(gooseGame.getPlayers()).hasSize(4);
    }

    @Test
    public void given_newGame_when_roll_then_gameNotStartedExceptionThrown() {
        assertThatThrownBy(() -> gooseGame.roll(UUID.randomUUID().toString())).isInstanceOf(GameException.class)
                .hasMessage("Game not started, waiting for more players");
    }

    @Test
    public void given_newGameWithFourPlayersAndIdDoesNotMatchAny_when_roll_then_userDoesNotExistExceptionThrown() {
        assertThat(gooseGame.createPlayer("Player1", "Goose1")).isNotNull();
        assertThat(gooseGame.createPlayer("Player2", "Goose2")).isNotNull();
        assertThat(gooseGame.createPlayer("Player3", "Goose3")).isNotNull();
        assertThat(gooseGame.createPlayer("Player4", "Goose4")).isNotNull();
        assertThat(gooseGame.getPlayers()).hasSize(4);

        assertThatThrownBy(() -> gooseGame.roll(UUID.randomUUID().toString())).isInstanceOf(GameException.class)
                .hasMessage("User rolling dice was not in game!");
    }

    @Test
    public void given_newGameWithFourPlayersAndIdDoesNotMatchFirstPlayer_when_roll_then_notYourTurnExceptionThrown() {
        assertThat(gooseGame.createPlayer("Player1", "Goose1")).isNotNull();
        final String player2Id = gooseGame.createPlayer("Player2", "Goose2");
        assertThat(player2Id).isNotNull();
        assertThat(gooseGame.createPlayer("Player3", "Goose3")).isNotNull();
        assertThat(gooseGame.createPlayer("Player4", "Goose4")).isNotNull();
        assertThat(gooseGame.getPlayers()).hasSize(4);

        assertThatThrownBy(() -> gooseGame.roll(player2Id)).isInstanceOf(GameException.class)
                .hasMessage("Is not your turn Player2!");
    }

    @Test
    public void given_endedGame_when_roll_then_gameOverExceptionThrown() {
        final String player1Id = gooseGame.createPlayer("Player1", "Goose1");
        assertThat(player1Id).isNotNull();
        assertThat(gooseGame.createPlayer("Player2", "Goose2")).isNotNull();
        assertThat(gooseGame.createPlayer("Player3", "Goose3")).isNotNull();
        assertThat(gooseGame.createPlayer("Player4", "Goose4")).isNotNull();
        assertThat(gooseGame.getPlayers()).hasSize(4);

        //TODO:  Need to refactor this when we have control over the dice
        gooseGame.getPlayers().get(0).setPosition(63);

        assertThatThrownBy(() -> gooseGame.roll(player1Id)).isInstanceOf(GameException.class)
                .hasMessage("The game is over");
    }
}