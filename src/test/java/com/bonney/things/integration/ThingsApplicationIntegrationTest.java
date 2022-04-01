package com.bonney.things.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.bonney.things.ThingsApplication;
import com.bonney.things.dto.AnswerDto;
import com.bonney.things.dto.GameDto;
import com.bonney.things.dto.PlayerDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ThingsApplicationIntegrationTest {

    @Test
    public void canRunThroughAGameFromStartToFinish() throws IOException {
        ThingsApplication application = new ThingsApplication(0);

        int port = application.getPort();
        String baseUrl = "http://localhost:" + port;
        ThingsClient client1 = new ThingsClient(baseUrl);
        ThingsClient client2 = new ThingsClient(baseUrl);

        UUID gameId = client1.newGame();
        client1.join(gameId, "bob");
        client2.join(gameId, "alice");

        GameDto viewOfGameWhenPlayersHaveJoined = client1.getGame(gameId);
        assertThat(viewOfGameWhenPlayersHaveJoined.getPlayers(), hasSize(2));
        assertThat(viewOfGameWhenPlayersHaveJoined.getCurrentRound(), is(nullValue()));
        assertThat(viewOfGameWhenPlayersHaveJoined.getPlayedRounds(), is(empty()));
        assertThat(viewOfGameWhenPlayersHaveJoined.isFinished(), is(false));

        String playerToStart = client1.newRound(gameId);

        GameDto viewOfGameWhenNewRoundCreated = client1.getGame(gameId);
        assertThat(viewOfGameWhenNewRoundCreated.getCurrentRound(), is(notNullValue()));
        assertThat(viewOfGameWhenNewRoundCreated.getCurrentRound().getCurrentTurn().getName(), is(playerToStart));

        client1.submitAnswer(gameId, "bob did it");
        client2.submitAnswer(gameId, "bob did it");

        assertThat(client1.getGame(gameId).getCurrentRound().getUnguessedAnswers(), hasSize(2));

        ThingsClient clientToStart = playerToStart.equals("bob") ? client1 : client2;
        clientToStart.startRound(gameId);

        application.stop();
    }

    public static class ThingsClient {

        public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        private final ObjectMapper objectMapper;
        private final OkHttpClient client;
        private final String baseUrl;

        public ThingsClient(String baseUrl) {
            this.objectMapper = new ObjectMapper();
            this.client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager())).build();
            this.baseUrl = baseUrl;
        }

        public UUID newGame() {
            Map<String, String> response = post("game", Map.class);
            return UUID.fromString(response.get("gameId"));
        }

        public GameDto getGame(UUID gameId) {
            return get("game/" + gameId, GameDto.class);
        }

        public void join(UUID gameId, String playerName) {
            PlayerDto dto = new PlayerDto(playerName);
            post("game/" + gameId + "/player", dto);
        }

        public String newRound(UUID gameId) {
            return post("game/" + gameId + "/round", PlayerDto.class).getName();
        }

        public void submitAnswer(UUID gameId, String answer) {
            AnswerDto dto = new AnswerDto(answer);
            post("game/" + gameId + "/round/current/answer", dto);
        }

        public void startRound(UUID gameId) {
            post("game/" + gameId + "/round/current");
        }

        private Response post(String path, Object body) {
            try {
                return post(path, RequestBody.create(MEDIA_TYPE_JSON, objectMapper.writeValueAsString(body)));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }

        private <T> T post(String path, Class<T> responseClazz) {
            return bodyAsClass(post(path), responseClazz);
        }

        private Response post(String path) {
            return post(path, RequestBody.create(MEDIA_TYPE_JSON, new byte[0]));
        }

        private Response post(String path, RequestBody body) {
            Request request = new Request.Builder()
                    .post(body)
                    .url(baseUrl + "/" + path)
                    .build();
            return execute(request);
        }

        private <T> T get(String path, Class<T> responseClazz) {
            return bodyAsClass(get(path), responseClazz);
        }

        private Response get(String path) {
            Request request = new Request.Builder()
                    .get()
                    .url(baseUrl + "/" + path)
                    .build();
            return execute(request);
        }

        private Response execute(Request request) {
            try {
                return client.newCall(request).execute();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private <T> T bodyAsClass(Response response, Class<T> clazz) {
            try {
                String json = response.body().string();
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        private CookieManager cookieManager() {
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            return cookieManager;
        }
    }
}
