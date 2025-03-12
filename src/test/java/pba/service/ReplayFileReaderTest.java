package pba.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReplayFileReaderTest {

  @Test
  void itShouldReadSingleLine() {
    ReplayFileReader replayFileReader = new ReplayFileReader();
    ReplayFileReaderTest.class.getClassLoader().getResource("input/replayList.txt");
    URL resource = ReplayFileReaderTest.class.getClassLoader().getResource("input/replayList.txt");
    File file = new File(resource.getFile());
    List<String> actual = replayFileReader.read(file);
    List<String> expected = getExpected();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void itShouldReadMultiLineReplay() {
    ReplayFileReader replayFileReader = new ReplayFileReader();
    URL resource =
        ReplayFileReaderTest.class.getClassLoader().getResource("input/multilineReplayList.txt");
    File file = new File(resource.getFile());
    List<String> actual = replayFileReader.read(file);
    List<String> expected = getExpected();
    assertThat(actual).isEqualTo(expected);
  }

  private List<String> getExpected() {
    URL resource = ReplayFileReaderTest.class.getClassLoader().getResource("output/output.txt");
    File file = new File(resource.getFile());
    try (FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      return bufferedReader.lines().toList();
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse file multilineReplayList.txt", e);
    }
  }
}
