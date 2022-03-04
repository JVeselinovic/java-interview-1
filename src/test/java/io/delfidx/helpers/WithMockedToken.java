package io.delfidx.helpers;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;

public class WithMockedToken {
    @Inject
    protected TokenMock tokenMock;

    @AfterEach
    void tearDown() {
        tokenMock.reset();
    }
    @Value("${test.validToken}")
    protected String validToken;
}
