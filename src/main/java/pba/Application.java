package pba;

import java.io.File;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pba.service.replay.ReplayService;

@ConfigurationPropertiesScan
@RequiredArgsConstructor
@SpringBootApplication
public class Application {

  private final ReplayService replayService;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> this.start(args[0]);
  }

  public void start(String fileName) {
    replayService.processFile(new File(fileName));
  }
}
