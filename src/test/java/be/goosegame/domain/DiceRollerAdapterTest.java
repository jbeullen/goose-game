package be.goosegame.domain;

import be.goosegame.domain.impl.DiceRollerService;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DiceRollerAdapterTest {
    private DiceRollerAdapter diceRollerAdapter;

    @Before
    public void setup() {
        diceRollerAdapter = new DiceRollerService();
    }

    @Test
    public void roll() {
        final Dices roll = diceRollerAdapter.roll();

        assertThat(roll.first()).isGreaterThanOrEqualTo(1);
        assertThat(roll.first()).isLessThanOrEqualTo(6);

        assertThat(roll.second()).isGreaterThanOrEqualTo(1);
        assertThat(roll.second()).isLessThanOrEqualTo(6);
    }
}