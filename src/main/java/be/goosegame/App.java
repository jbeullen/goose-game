package be.goosegame;

import be.goosegame.domain.GameException;
import be.goosegame.domain.GooseGame;
import be.goosegame.domain.impl.DiceRollerService;
import be.goosegame.domain.impl.GooseGameImpl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private final DiceRollerService diceRollerService = new DiceRollerService();
    private final GooseGame game = new GooseGameImpl();
    private Player nextPlayer = null;

    public String createPlayer(final Request req, final Response res) {
        res.type("application/json");
        try {
            final JSONObject json = new Utils().fromJson(req.body());
            final String name = json.getString("name");
            final String nickname = json.getString("nickname");
            final String uuid = game.createPlayer(name, nickname);

            res.status(201);
            return String.format("{\"id\": \"%s\", \"name\": \"%s\", \"nickname\": \"%s\"}", uuid, name, nickname);
        } catch (final GameException e) {
            res.status(400);
            return String.format("{\"error\": \"%s\"}", e.getMessage());
        }
    }

    public String roll(final Request req, final Response res) {
        res.type("application/json");
        try {
            final String id = req.params("id");
            game.roll(id);

            if (id != null) {
                // Does it still throw exception?
                try {
                    if (nextPlayer == null) {
                        nextPlayer = getPlayers().stream().findFirst().orElse(null);
                    }
                    final Player player = getPlayers().stream().filter(it -> it.getUuid().toString().equals(id)).collect(Collectors.toList()).get(0);
                    final String movePlayer = movePlayer(player);
                    nextPlayer = getPlayers().get((getPlayers().indexOf(player) + 1) % getPlayers().size());
                    logger.info("next player is {}", nextPlayer);

                    res.status(200);
                    return movePlayer;
                } catch (final Exception e) {
                    logger.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                res.status(400);
                return "{\"error\": \"User rolling dice was not specified!\"}";
            }
        } catch (final GameException e) {
            res.status(400);
            return String.format("{\"error\": \"%s\"}", e.getMessage());
        }
    }

    private String movePlayer(final Player currentPlayer) {
        final JSONArray roll = roll();
        final int firstThrow = roll.getJSONObject(0).getInt("value");
        final int secondThrow = roll.getJSONObject(1).getInt("value");

        int startPosition = 0, newPosition = 0;
        startPosition = currentPlayer.getPosition();
        newPosition = currentPlayer.getPosition() + firstThrow + secondThrow;
        currentPlayer.setPosition(newPosition);
        String message = String.format("%s moves from %s to %s. ", currentPlayer.getName(), cellName(startPosition), cellName(newPosition));

        final Optional<Player> playerInPosition = getPlayers().stream().filter(p -> p.getPosition() == currentPlayer.getPosition() && !p.getUuid().equals(currentPlayer.getUuid())).findFirst();
        if (playerInPosition.isPresent()) {
            playerInPosition.get().setPosition(startPosition);
            message += String.format("On %s there was %s, who is moved back to %s. ", newPosition, playerInPosition.get().getName(), startPosition);
        }

        if (isGoose(currentPlayer.getPosition())) {
            startPosition = currentPlayer.getPosition();
            newPosition = currentPlayer.getPosition() + firstThrow + secondThrow;
            currentPlayer.setPosition(newPosition);
            message = message.substring(0, message.length() - 2);
            message += String.format(", goose. %s moves from %s to %s. ", currentPlayer.getName(), cellName(startPosition), cellName(newPosition));
        }
        if (currentPlayer.getPosition() > 63) {
            currentPlayer.setPosition(63 - (currentPlayer.getPosition() - 63));
            message += String.format("%s bounced! %s goes back to %s", currentPlayer.getName(), currentPlayer.getName(), currentPlayer.getPosition());
        } else if (currentPlayer.getPosition() == 6) {
            currentPlayer.setPosition(currentPlayer.getPosition() + 6);
            message += String.format("%s jumps to %s", currentPlayer.getName(), currentPlayer.getPosition());
        }
        if (currentPlayer.getPosition() == 63) {
            message += String.format("%s wins!", currentPlayer.getName());
        }

        logger.info("{} moved", currentPlayer);
        return "{\"roll\":" + printRoll(firstThrow, secondThrow) + ", \"position\":" + currentPlayer.getPosition() + ", \"message\": \"" + message.trim() + "\" }";
    }

    private JSONArray roll() {
        final JSONObject jsonObject = diceRollerService.rollOld();
        return jsonObject.getJSONArray("dice");
    }

    private boolean isGoose(final int position) {
        return Arrays.asList(5, 14, 23, 9, 18, 27).contains(position);
    }

    private String printRoll(final int firstThrow, final int secondThrow) {
        return "[" + firstThrow + ", " + secondThrow + "]";
    }

    private String cellName(final int position) {
        if (position == 0)
            return "Start";
        if (position == 6)
            return "The Bridge";
        return String.valueOf(position);
    }

    private List<Player> getPlayers() {
        return game.getPlayers();
    }
}
