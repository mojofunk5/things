package com.bonney.things;

import com.bonney.things.domain.Games;

import io.javalin.Javalin;

public class ThingsApplication {

    private final Javalin javalin;
    private final Games games;

    public ThingsApplication(int port) {
        this.javalin = Javalin.create().start(port);
        this.games = new Games();
        new RestEndpoint(this.javalin, this.games);
    }

    public static void main(String[] args) {
        new ThingsApplication(Integer.parseInt(args[0]));
    }

    public int getPort() {
        return this.javalin.port();
    }

    public void stop() {
        this.javalin.stop();
    }
}
