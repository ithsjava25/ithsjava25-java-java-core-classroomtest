package org.example;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BasicTest {
        @Test
        void shouldAlwaysPass() {
            assertThat(1 + 1).isEqualTo(2);
        }
}
