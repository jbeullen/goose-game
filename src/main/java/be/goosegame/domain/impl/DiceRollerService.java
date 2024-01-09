package be.goosegame.domain.impl;

import be.goosegame.domain.DiceRollerAdapter;
import be.goosegame.domain.Dices;
import org.json.JSONObject;

import java.util.Random;

public class DiceRollerService implements DiceRollerAdapter {
    private final Random generator;

    public DiceRollerService() {
        generator = new Random(System.currentTimeMillis());
    }

    @Override
    public Dices roll() {
        final int firstThrow = rollDice();
        final int secondThrow = rollDice();
        return new DicesImpl(firstThrow, secondThrow);
    }

    private int rollDice() {
        return (generator.nextInt(Integer.MAX_VALUE) % 6) + 1;
    }

    public JSONObject rollOld() {
        final String rollResult = String.format("{\"success\":true,\"dice\":[{\"value\":%s,\"type\":\"d6\"},{\"value\":%s,\"type\":\"d6\"}]}", rollDice(), rollDice());
        return new JSONObject(rollResult);

    }
}