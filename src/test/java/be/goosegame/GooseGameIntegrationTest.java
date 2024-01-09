package be.goosegame;

import okhttp3.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static spark.Spark.*;

public class GooseGameIntegrationTest {
    private static final String PLAYER_URL = "http://localhost:8080/players";
    private static final String ROLL_URL = PLAYER_URL + "/%s/roll";
    private static final MediaType TYPE = MediaType.get("application/json");

    private final List<String> playerIds = new LinkedList<>();
    private String lastMessage = "";
    private int i = 0;

    private OkHttpClient httpClient;

    @Before
    public void setUp() {
        main.main(new String[0]);
        awaitInitialization();
        httpClient = new OkHttpClient();
    }

    @After
    public void tearDown() {
        stop();
        awaitStop();
    }

    @Test
    public void simulateAllScenarios() throws Exception {
        // Create a Player
        assertCreatePlayerSuccessResponse("Thijs", "CoolDude", createPlayer("Thijs", "CoolDude"));

        // Try to start the game
        assertGameNotStartedResponse(roll(UUID.randomUUID().toString()));

        // Create a Player with same nickname
        assertPlayerAlreadyExistsResponse("CoolDude", createPlayer("Thijs", "CoolDude"));

        // Add 3 more Player
        assertCreatePlayerSuccessResponse("Player1", "goose1", createPlayer("Player1", "goose1"));
        assertCreatePlayerSuccessResponse("Player2", "goose2", createPlayer("Player2", "goose2"));
        assertCreatePlayerSuccessResponse("Player3", "goose3", createPlayer("Player3", "goose3"));

        // Create a Player
        final Response tooManyPlayersResponse = createPlayer("Jeroen", "Jerre");
        assertTooManyPlayersResponse("Thijs", "Player1", "Player2", "Player3", tooManyPlayersResponse);

        // Start game with non existing player
        assertUserNotInGameResponse(roll(UUID.randomUUID().toString()));

        // Start game with wrong player
        assertWrongPlayerResponse("Player1", roll(playerIds.get(1)));


        while (!lastMessage.contains("wins")) {
            assertRollSuccessResponse(roll(playerIds.get(i)));
            i = (i + 1) % 4;
        }

        // Play while game is over
        assertGameOverResponse(roll(playerIds.get(i)));
    }

    private void assertGameOverResponse(final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals("The game is over", new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertRollSuccessResponse(final Response response) throws Exception {
        assertEquals(200, response.code());
        final JSONObject jsonObject = new Utils().fromJson(response.body().string());
        assertThat(Integer.parseInt(jsonObject.get("position").toString())).isLessThanOrEqualTo(63);
        lastMessage = jsonObject.getString("message").toString();
    }

    private void assertWrongPlayerResponse(final String name, final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals(String.format("Is not your turn %s!", name), new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertUserNotInGameResponse(final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals("User rolling dice was not in game!", new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertGameNotStartedResponse(final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals("Game not started, waiting for more players", new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertPlayerAlreadyExistsResponse(final String expectedNickname, final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals(String.format("nickname already taken: %s", expectedNickname), new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertTooManyPlayersResponse(final String player1, final String player2, final String player3, final String player4, final Response response) throws Exception {
        assertEquals(400, response.code());
        assertEquals(String.format("too many players already: %s, %s, %s, %s", player1, player2, player3, player4), new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertCreatePlayerSuccessResponse(final String expectedName, final String expectedNickname, final Response response) throws Exception {
        assertEquals(201, response.code());
        final JSONObject jsonObject = new Utils().fromJson(response.body().string());
        assertEquals(expectedName, jsonObject.get("name"));
        assertEquals(expectedNickname, jsonObject.get("nickname"));
        final Object id = jsonObject.get("id");
        assertNotNull(UUID.fromString((String) id));
        playerIds.add(id.toString());
    }

    private Response createPlayer(final String name, final String nickname) throws Exception {
        final String body = String.format("{ \"name\": \"%s\", \"nickname\": \"%s\"}", name, nickname);
        return httpClient.newCall(
                        new Request.Builder()
                                .url(PLAYER_URL)
                                .post(RequestBody.create(TYPE, body))
                                .build())
                .execute();
    }

    private Response roll(final String playerId) throws Exception {
        return httpClient.newCall(
                        new Request.Builder()
                                .url(String.format(ROLL_URL, playerId))
                                .post(RequestBody.create(TYPE, "{}"))
                                .build())
                .execute();
    }
}
