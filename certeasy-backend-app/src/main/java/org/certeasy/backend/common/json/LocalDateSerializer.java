package org.certeasy.backend.common.json;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.json.stream.JsonGenerator;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

    @Override
    public void serialize(LocalDate localDate, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        String formattedDate = localDate.format(DATE_FORMATTER);
        gen.writeString(formattedDate);
    }
}
