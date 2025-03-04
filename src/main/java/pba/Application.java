package pba;

import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pba.service.ReplayService;

@SpringBootApplication
public class Application {

  @Autowired private ReplayService replayService;

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
