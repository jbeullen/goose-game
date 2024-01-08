package be.goosegame;

import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;

public class Player {
    private final String name;
    private final String nickname;
    private final UUID uuid;
    private int position = 0;

    public Player(final String name, final String nickname, final UUID uuid) {
        this.name = name;
        this.nickname = nickname;
        this.uuid = uuid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        this.position = position;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Player player = (Player) o;
        return Objects.equals(getUuid(), player.getUuid());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nickname='" + nickname + "'")
                .add("uuid=" + uuid)
                .add("position=" + position)
                .toString();
    }
}
