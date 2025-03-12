package pba.properties;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration("pba")
public class PbaProperties {

    private String resultsOutputDirectory;

}
