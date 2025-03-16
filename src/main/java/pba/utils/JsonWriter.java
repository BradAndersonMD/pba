package pba.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Generated;
import lombok.experimental.UtilityClass;
import pba.models.exceptions.PbaRuntimeException;

/**
 * Utility class for writing an object as a JSON String
 */
@Generated
@UtilityClass
public class JsonWriter {

    private final ObjectMapper mapper = new ObjectMapper();

    public String write(Object obj) {
        try {
            return mapper.writer().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new PbaRuntimeException("Failed to write obj=[" + obj + "] as string", e);
        }
    }

}
