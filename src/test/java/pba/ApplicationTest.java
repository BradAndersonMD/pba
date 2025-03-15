package pba;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

  // Ensures that Spring can load its application context
  @Test
  void itShouldLoadContext() {
    boolean loadsContext = true;
    assertThat(loadsContext).isTrue();
  }
}
