package pba.properties;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration("pba")
public class PbaProperties {

    private String resultsOutputDirectory;

}
