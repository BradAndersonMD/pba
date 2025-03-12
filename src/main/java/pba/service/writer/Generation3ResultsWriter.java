package pba.service.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pba.models.exceptions.PbaRuntimeException;
import pba.models.replay.results.Generation3Results;
import pba.properties.PbaProperties;

import java.io.File;

@Component
@RequiredArgsConstructor
public class Generation3ResultsWriter implements ResultsWriter<Generation3Results> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CsvMapper csvMapper = new CsvMapper();
    private final PbaProperties pbaProperties;

    @Override
    public void write(Generation3Results results) {
        try {
            CsvSchema columns = csvMapper.schemaFor(Generation3Results.class);
            objectMapper.writer(columns.withUseHeader(true)).writeValue(new File(pbaProperties.getResultsOutputDirectory() + "results.csv"), results);
        } catch (Exception e) {
            throw new PbaRuntimeException("Failed to parse results file into csv");
        }
    }

}
