package pba.service.writer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pba.models.replay.results.Generation3Results;
import pba.properties.PbaProperties;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Generation3ResultsWriterIT {

    private final ObjectMapper mapper = new ObjectMapper();

    @TempDir
    private Path tempDir;

    @Mock
    private PbaProperties pbaProperties;

    @InjectMocks
    private Generation3ResultsWriter resultsWriter;

    @Test
    void itShouldWriteResults() throws IOException {
        Generation3Results generation3Results = loadInput();
        when(pbaProperties.getResultsOutputDirectory()).thenReturn(tempDir.toAbsolutePath().toString());
        assertDoesNotThrow(() -> resultsWriter.write(generation3Results));
    }

    private Generation3Results loadInput() throws IOException {
        URL input =
                Generation3ResultsWriterIT.class.getClassLoader().getResource("writer/generation3/input.json");
        return mapper.readValue(input, Generation3Results.class);
    }


}
