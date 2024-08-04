package ru.company.walletservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import ru.company.walletservice.common.dto.OperationType;
import ru.company.walletservice.exception.InvalidOperationTypeException;

import java.io.IOException;

@Slf4j
public class OperationTypeDeserializer extends JsonDeserializer<OperationType> {

    @Override
    public OperationType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String text = node.asText();
        try {
            return OperationType.valueOf(text.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid operation type: {}", text);
            throw new InvalidOperationTypeException("Invalid operation type: " + text);
        }
    }
}