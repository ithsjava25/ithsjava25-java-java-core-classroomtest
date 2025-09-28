package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EdgeCaseTest {
    @Test
    void shouldAlwaysFail() {
        assertThat("actual").isEqualTo("expected");
    }
}
