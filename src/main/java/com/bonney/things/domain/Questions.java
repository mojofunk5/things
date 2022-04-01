package com.bonney.things.domain;

import org.apache.commons.lang3.RandomStringUtils;

public class Questions {

    public String nextQuestion() {
        return RandomStringUtils.randomAlphanumeric(12);
    }
}