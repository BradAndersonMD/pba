package pba.service.writer;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import pba.models.exceptions.PbaRuntimeException;
import pba.models.replay.results.Generation3Results;
import pba.models.replay.results.ResultsData;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CsvWriter {

  private final CustomMappingStrategy<ResultsData> mappingStrategy;

  public CsvWriter() {
    this.mappingStrategy = new CustomMappingStrategy<>();
    this.mappingStrategy.setType(ResultsData.class);
  }

  public Mono<Void> write(ServerHttpResponse response, Generation3Results results) {
    ZeroCopyHttpOutputMessage zeroCopyResponse = (ZeroCopyHttpOutputMessage) response;
    response.getHeaders().set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=results.csv");

    // Convert to csv
    File file = convertToCsv(results.getResults());
    return zeroCopyResponse.writeWith(file, 0, file.length()).then(deleteFile(file));
  }

  private File convertToCsv(List<ResultsData> results) {
    try {
      File tempCsv = Files.createTempFile("temp", Long.toString(System.nanoTime())).toFile();
      return writeToCsv(results, tempCsv);
    } catch (Exception e) {
      throw new PbaRuntimeException("Failed to create temp directory", e);
    }
  }

  private File writeToCsv(List<ResultsData> results, File tempCsv) {
    try (FileWriter writer = new FileWriter(tempCsv)) {
      StatefulBeanToCsv<ResultsData> beanToCsv =
          new StatefulBeanToCsvBuilder<ResultsData>(writer)
              .withMappingStrategy(mappingStrategy)
              .build();
      beanToCsv.write(results);
      return tempCsv;
    } catch (Exception e) {
      throw new PbaRuntimeException("Failed to write to CSV", e);
    }
  }

  private Mono<Void> deleteFile(File file) {
    return Mono.fromRunnable(
            () -> {
              try {
                Files.delete(file.toPath());
              } catch (IOException e) {
                throw new PbaRuntimeException("Failed to delete file", e);
              }
            })
        .then();
  }
}
