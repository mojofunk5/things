package com.bonney.things.state;

public class RoundFinished extends BaseGameState {

    RoundFinished(Context context) {
        super(context);
    }

    @Override
    public Game newRound() {
        return new WaitingForAnswers(getContext());
    }

    @Override
    public Game finishGame() {
        return new GameFinished(getContext());
    }
}