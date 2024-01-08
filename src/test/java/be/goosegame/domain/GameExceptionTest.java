package be.goosegame.domain;

import be.goosegame.Player;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class GameExceptionTest {

    @Test
    public void tooManyPlayersException() {
        GameException exception = GameException.tooManyPlayersException(
                Arrays.asList(
                        new Player("Jeroen", "Jerre", UUID.randomUUID()),
                        new Player("Thijs", "CoolDude", UUID.randomUUID())
                )
        );
        assertEquals("too many players already: Jeroen, Thijs", exception.getMessage());
    }

    @Test
    public void duplicatePlayerException() {
        GameException exception = GameException.duplicatePlayerException(new Player("Jeroen", "Jerre", UUID.randomUUID()));
        assertEquals("nickname already taken: Jerre", exception.getMessage());
    }
}