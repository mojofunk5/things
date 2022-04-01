package com.bonney.things;

import java.util.UUID;
import java.util.function.Function;

import com.bonney.things.domain.Games;
import com.bonney.things.domain.Player;
import com.bonney.things.dto.AnswerDto;
import com.bonney.things.dto.GameDto;
import com.bonney.things.dto.PlayerDto;
import com.bonney.things.state.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import io.javalin.Javalin;
import io.javalin.core.validation.JavalinValidation;
import io.javalin.http.Context;

public class RestEndpoint {

    private static final String SESSION_ATTRIBUTE_PLAYER = "player";

    private final ObjectMapper objectMapper;
    private final Games games;

    public RestEndpoint(Javalin javalin, Games games) {
        this.objectMapper = new ObjectMapper();
        this.games = games;
        mapEndpoints(javalin);
        JavalinValidation.register(UUID.class, UUID::fromString);
    }

    private void mapEndpoints(Javalin javalin) {
        javalin.post("game", this::newGame);
        javalin.get("game/:gameId", this::getGame);
        javalin.post("game/:gameId/player", this::join);
        javalin.post("game/:gameId/round", this::newRound);
        javalin.post("game/:gameId/round/current", this::startRound);
        javalin.post("game/:gameId/round/current/answer", this::submitAnswer);
    }

    private void newGame(Context context) {
        UUID uuid = this.games.newGame();
        context.json(ImmutableMap.of("gameId", uuid));
    }

    private void getGame(Context context) {
        UUID gameId = getGameId(context);
        GameDto gameDto = this.games.get(gameId).toDto();
        context.json(gameDto);
    }

    private void join(Context context) {
        PlayerDto playerDto = context.bodyAsClass(PlayerDto.class);
        Player player = Player.from(playerDto);
        UUID gameId = getGameId(context);
        Game game = this.games.get(gameId);
        game.add(player);
        setPlayerOnSession(context, player);
    }

    private void newRound(Context context) {
        UUID gameId = getGameId(context);
        Game newState = doTheThing(gameId, Game::newRound);
        Player player = newState.getCurrentTurn().get();
        context.json(player);
    }

    private void startRound(Context context) {
        UUID gameId = getGameId(context);
        doTheThing(gameId, Game::startRound);
    }

    private void submitAnswer(Context context) {
        UUID gameId = getGameId(context);
        AnswerDto answer = context.bodyAsClass(AnswerDto.class);
        Player player = getPlayerFromSession(context);
        doTheThing(gameId, game -> game.submitAnswer(player, answer.getAnswer()));
    }

    private Game doTheThing(UUID gameId, Function<Game, Game> theThingToDo) {
        Game initialState = this.games.get(gameId);
        Game newState = theThingToDo.apply(initialState);
        this.games.update(gameId, newState);
        return newState;
    }

    private void setPlayerOnSession(Context context, Player player) {
        context.sessionAttribute(SESSION_ATTRIBUTE_PLAYER, player);
    }

    private Player getPlayerFromSession(Context context) {
        return context.sessionAttribute(SESSION_ATTRIBUTE_PLAYER);
    }

    private UUID getGameId(Context context) {
        return context.pathParam("gameId", UUID.class).get();
    }
}