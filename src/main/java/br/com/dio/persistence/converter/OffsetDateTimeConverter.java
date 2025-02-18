package br.com.dio.persistence.converter;

import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    private static final ZoneOffset DEFAULT_OFFSET = ZoneOffset.UTC;

    public static OffsetDateTime toOffsetDateTime(final Timestamp value) {
        return nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), DEFAULT_OFFSET) : null;
    }

    public static Timestamp toTimestamp(final OffsetDateTime value) {
        return nonNull(value) ? Timestamp.valueOf(value.atZoneSameInstant(DEFAULT_OFFSET).toLocalDateTime()) : null;
    }

    // Permite fuso horário personalizado
    public static OffsetDateTime toOffsetDateTime(final Timestamp value, ZoneOffset zoneOffset) {
        return nonNull(value) ? OffsetDateTime.ofInstant(value.toInstant(), nonNull(zoneOffset) ? zoneOffset : DEFAULT_OFFSET) : null;
    }

    // Permite fuso horário personalizado
    public static Timestamp toTimestamp(final OffsetDateTime value, ZoneOffset zoneOffset) {
        return nonNull(value) ? Timestamp.valueOf(value.atZoneSameInstant(nonNull(zoneOffset) ? zoneOffset : DEFAULT_OFFSET).toLocalDateTime()) : null;
    }
}
