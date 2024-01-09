package be.goosegame.domain;

import be.goosegame.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GameExceptionTest {

    @Test
    public void tooManyPlayersException() {
        final GameException exception = GameException.tooManyPlayersException(
                Arrays.asList(
                        new Player("Jeroen", "Jerre", UUID.randomUUID()),
                        new Player("Thijs", "CoolDude", UUID.randomUUID())
                )
        );
        assertThat(exception.getMessage()).isEqualTo("too many players already: Jeroen, Thijs");
    }

    @Test
    public void duplicatePlayerException() {
        final GameException exception = GameException.duplicatePlayerException(new Player("Jeroen", "Jerre", UUID.randomUUID()));
        assertThat(exception.getMessage()).isEqualTo("nickname already taken: Jerre");
    }

    @Test
    public void gameNotStartedException() {
        final GameException exception = GameException.gameNotStartedException();
        assertThat(exception.getMessage()).isEqualTo("Game not started, waiting for more players");
    }

    @Test
    public void userDoesNotExistException() {
        final GameException exception = GameException.userDoesNotExistException();
        assertThat(exception.getMessage()).isEqualTo("User rolling dice was not in game!");
    }

    @Test
    public void gameOverException() {
        final GameException exception = GameException.gameOverException();
        assertThat(exception.getMessage()).isEqualTo("The game is over");
    }

    @Test
    public void notYourTurnException() {
        final GameException exception = GameException.notYourTurnException(new Player("Jeroen", "Jerre", UUID.randomUUID()));
        assertThat(exception.getMessage()).isEqualTo("Is not your turn Jeroen!");
    }
}