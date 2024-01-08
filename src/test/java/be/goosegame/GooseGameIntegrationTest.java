package be.goosegame;

import okhttp3.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static spark.Spark.*;

public class GooseGameIntegrationTest {
    private static final String CREATE_PLAYER_URL = "http://localhost:8080/players";
    private static final MediaType TYPE = MediaType.get("application/json");
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
    public void createPlayerTest() throws Exception {
        // Create a Player
        Response response = createPlayer("Thijs", "CoolDude");
        assertCreatePlayerSuccessResponse("Thijs", "CoolDude", response);

        // Create a Player with same nickname
        Response duplicatePlayerResponse = createPlayer("Thijs", "CoolDude");
        assertPlayerAlreadyExistsResponse("CoolDude", duplicatePlayerResponse);

        // Add 3 more Player
        assertCreatePlayerSuccessResponse("Player1", "goose1", createPlayer("Player1", "goose1"));
        assertCreatePlayerSuccessResponse("Player2", "goose2", createPlayer("Player2", "goose2"));
        assertCreatePlayerSuccessResponse("Player3", "goose3", createPlayer("Player3", "goose3"));


        // Create a Player
        Response tooManyPlayersResponse = createPlayer("Jeroen", "Jerre");
        assertTooManyPlayersResponse("Thijs", "Player1", "Player2", "Player3", tooManyPlayersResponse);


    }

    private void assertPlayerAlreadyExistsResponse(String expectedNickname, Response response) throws Exception{
        assertEquals(400, response.code());
        assertEquals(String.format("nickname already taken: %s", expectedNickname), new Utils().fromJson(response.body().string()).getString("error"));
    }

    private void assertTooManyPlayersResponse(String player1, String player2, String player3, String player4, Response response) throws Exception{
        assertEquals(400, response.code());
        assertEquals(String.format("too many players already: %s, %s, %s, %s", player1, player2, player3, player4),new Utils().fromJson(response.body().string()).getString("error"));
    }


    private Response createPlayer(String name, String nickname) throws Exception{
        String body = String.format("{ \"name\": \"%s\", \"nickname\": \"%s\"}", name, nickname);
        return httpClient.newCall(
                        new Request.Builder()
                                .url(CREATE_PLAYER_URL)
                                .post(RequestBody.create(TYPE, body))
                                .build())
                .execute();
    }

    private void assertCreatePlayerSuccessResponse(String expectedName, String expectedNickname, Response response) throws Exception{
        assertEquals(201, response.code());
        JSONObject jsonObject = new Utils().fromJson(response.body().string());
        assertEquals(expectedName, jsonObject.get("name"));
        assertEquals(expectedNickname, jsonObject.get("nickname"));
        assertNotNull(UUID.fromString((String) jsonObject.get("id")));
    }
}
