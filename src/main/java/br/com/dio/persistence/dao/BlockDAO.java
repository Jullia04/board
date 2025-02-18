package br.com.dio.persistence.dao;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toTimestamp;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    private static final String BLOCK_SQL = "INSERT INTO BLOCKS (blocked_at, block_reason, card_id) VALUES (?, ?, ?);";
    private static final String UNBLOCK_SQL = "UPDATE BLOCKS SET unblocked_at = ?, unblock_reason = ? WHERE card_id = ? AND unblock_reason IS NULL;";

    public void block(final String reason, final Long cardId) throws SQLException {
        try (var statement = connection.prepareStatement(BLOCK_SQL)) {
            setBlockParams(statement, reason, cardId);
            statement.executeUpdate();
        }
    }

    public void unblock(final String reason, final Long cardId) throws SQLException {
        try (var statement = connection.prepareStatement(UNBLOCK_SQL)) {
            setUnblockParams(statement, reason, cardId);
            statement.executeUpdate();
        }
    }

    private void setBlockParams(PreparedStatement statement, String reason, Long cardId) throws SQLException {
        int index = 1;
        statement.setTimestamp(index++, toTimestamp(OffsetDateTime.now()));
        statement.setString(index++, reason);
        statement.setLong(index, cardId);
    }

    private void setUnblockParams(PreparedStatement statement, String reason, Long cardId) throws SQLException {
        int index = 1;
        statement.setTimestamp(index++, toTimestamp(OffsetDateTime.now()));
        statement.setString(index++, reason);
        statement.setLong(index, cardId);
    }
}
