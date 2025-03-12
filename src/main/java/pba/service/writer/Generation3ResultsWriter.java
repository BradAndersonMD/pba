package pba.service.writer;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pba.models.exceptions.PbaRuntimeException;
import pba.models.pokemon.PokemonData;
import pba.models.replay.results.Generation3Results;
import pba.properties.PbaProperties;

import java.io.FileWriter;

@Slf4j
@Component
@RequiredArgsConstructor
public class Generation3ResultsWriter implements ResultsWriter<Generation3Results> {

    private final PbaProperties pbaProperties;

    @Override
    public void write(Generation3Results results) {
        String fileName = pbaProperties.getResultsOutputDirectory() + "/results.csv";
        try(FileWriter writer = new FileWriter(fileName)) {
            StatefulBeanToCsv<PokemonData> beanToCsv = new StatefulBeanToCsvBuilder<PokemonData>(writer).build();
            beanToCsv.write(results.getPokemonData());
        } catch (Exception e) {
            throw new PbaRuntimeException("Failed to parse results file into csv", e);
        }
        log.info("Finished writing results to file=[{}]", fileName);
    }

}
