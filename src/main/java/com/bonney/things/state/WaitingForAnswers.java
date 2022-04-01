package com.bonney.things.state;

import com.bonney.things.domain.Player;
import com.bonney.things.domain.Round;

public class WaitingForAnswers extends BaseGameState {

    private final Round round;

    WaitingForAnswers(Context context) {
        super(context);
        this.round = context.newRound();
    }

    @Override
    public Game submitAnswer(Player player, String answer) {
        this.round.submitAnswer(player, answer);
        if (this.round.everyPlayerAnswered()) {
            return new IdentifyDuplicateAnswers(getContext(), this.round);
        }
        return this;
    }
}