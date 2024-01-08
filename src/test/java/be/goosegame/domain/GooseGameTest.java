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
        assertThatThrownBy(() -> {
            gooseGame.createPlayer("Jeroen", "CoolDude");
        }).isInstanceOf(GameException.class)
                .hasMessage("nickname already taken: CoolDude");
        assertThat(gooseGame.getPlayers()).hasSize(1);
    }

    @Test
    public void given_newGameWithFourExisting_when_createPlayer_then_playerNotAddedAndExceptionThrown() {
        assertThat(gooseGame.createPlayer("Player1", "Goose1")).isNotNull();
        assertThat(gooseGame.createPlayer("Player2", "Goose2")).isNotNull();
        assertThat(gooseGame.createPlayer("Player3", "Goose3")).isNotNull();
        assertThat(gooseGame.createPlayer("Player4", "Goose4")).isNotNull();

        assertThatThrownBy(() -> {
            gooseGame.createPlayer("Jeroen", "Jerre");
        }).isInstanceOf(GameException.class)
                .hasMessage("too many players already: Player1, Player2, Player3, Player4");
        assertThat(gooseGame.getPlayers()).hasSize(4);
    }
}