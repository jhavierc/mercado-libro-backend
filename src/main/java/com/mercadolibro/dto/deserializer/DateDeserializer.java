package com.mercadolibro.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mercadolibro.exception.IncorrectDateFormatException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateDeserializer extends StdDeserializer<LocalDate> {

    public DateDeserializer() {
        this(null);
    }

    public DateDeserializer(Class<?> vc) {
        super(vc);
    }

    private final static DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {

        String date = jsonparser.getText();
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new IncorrectDateFormatException("Date format should be 'YYYY-MM-DD'.");
        }
    }
}
