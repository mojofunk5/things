package com.bonney.things.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnswerDto {

    private final String answer;

    @JsonCreator
    public AnswerDto(@JsonProperty("answer") String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}
