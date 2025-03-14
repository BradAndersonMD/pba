package pba;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationTest {

    // Ensures that Spring can load its application context
    @Test
    void itShouldLoadContext() {
        boolean loadsContext = true;
        assertThat(loadsContext).isTrue();
    }

}
