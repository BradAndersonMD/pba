package pba.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pba.models.exceptions.PbaRuntimeException;

@Slf4j
@Component
@NoArgsConstructor
public class ReplayFileReader {

  /**
   * Converts a replays file into a list of replay urls
   *
   * @param replaysFile the replay file to convert
   * @return a list of urls
   */
  public List<String> read(File replaysFile) {

    List<String> replayUrls = new ArrayList<>();
    try (FileReader fileReader = new FileReader(replaysFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {

      // split multi url lines, convert them to replay urls, append .log
      bufferedReader
          .lines()
          .map(this::splitLine)
          .flatMap(Collection::stream)
          .map(this::convertUrl)
          .forEach(replayUrls::add);

    } catch (Exception e) {
      String error =
          String.format(
              "Failed to load replay file [%s]", replaysFile.getAbsoluteFile().toString());
      log.error(error);
      throw new PbaRuntimeException(error, e);
    }

    return replayUrls;
  }

  /**
   * Converts a line with multiple replays into separate urls
   *
   * @param line the line to convert
   * @return a list of urls
   */
  private List<String> splitLine(String line) {
    if (line.contains("\t")) {
      return List.of(line.split("\t"));
    }
    return List.of(line);
  }

  /**
   * Converts the url into a replay id
   *
   * @param url the url to convert
   * @return a replay url
   */
  private String convertUrl(String url) {
    String replayId = url.substring(url.lastIndexOf("/") + 1);
    if(replayId.contains("battle-")) {
      return replayId.substring(replayId.indexOf("-") + 1);
    }

    return replayId;
  }
}
