package be.goosegame.domain.impl;

import be.goosegame.domain.Dices;

public class DicesImpl implements Dices {

    private final int first, second;

    public DicesImpl(final int first, final int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int first() {
        return this.first;
    }

    @Override
    public int second() {
        return this.second;
    }
}
