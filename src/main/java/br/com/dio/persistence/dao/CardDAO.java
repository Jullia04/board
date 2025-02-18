package br.com.dio.persistence.dao;

import br.com.dio.dto.CardDetailsDTO;
import br.com.dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static br.com.dio.persistence.converter.OffsetDateTimeConverter.toOffsetDateTime;
import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    private static final String INSERT_SQL = "INSERT INTO CARDS (title, description, board_column_id) values (?, ?, ?);";
    private static final String MOVE_TO_COLUMN_SQL = "UPDATE CARDS SET board_column_id = ? WHERE id = ?;";
    private static final String FIND_BY_ID_SQL =
            """
            SELECT c.id,
                   c.title,
                   c.description,
                   b.blocked_at,
                   b.block_reason,
                   c.board_column_id,
                   bc.name,
                   (SELECT COUNT(sub_b.id)
                           FROM BLOCKS sub_b
                          WHERE sub_b.card_id = c.id) blocks_amount
              FROM CARDS c
              LEFT JOIN BLOCKS b
                ON c.id = b.card_id
               AND b.unblocked_at IS NULL
             INNER JOIN BOARDS_COLUMNS bc
                ON bc.id = c.board_column_id
              WHERE c.id = ?;
            """;

    public CardEntity insert(final CardEntity entity) throws SQLException {
        try (var statement = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            setInsertParams(statement, entity);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }

            return entity;
        }
    }

    public void moveToColumn(final Long columnId, final Long cardId) throws SQLException {
        try (var statement = connection.prepareStatement(MOVE_TO_COLUMN_SQL)) {
            statement.setLong(1, columnId);
            statement.setLong(2, cardId);
            statement.executeUpdate();
        }
    }

    public Optional<CardDetailsDTO> findById(final Long id) throws SQLException {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var dto = new CardDetailsDTO(
                            resultSet.getLong("c.id"),
                            resultSet.getString("c.title"),
                            resultSet.getString("c.description"),
                            nonNull(resultSet.getString("b.block_reason")),
                            toOffsetDateTime(resultSet.getTimestamp("b.blocked_at")),
                            resultSet.getString("b.block_reason"),
                            resultSet.getInt("blocks_amount"),
                            resultSet.getLong("c.board_column_id"),
                            resultSet.getString("bc.name")
                    );
                    return Optional.of(dto);
                }
            }
        }
        return Optional.empty();
    }

    private void setInsertParams(PreparedStatement statement, CardEntity entity) throws SQLException {
        statement.setString(1, entity.getTitle());
        statement.setString(2, entity.getDescription());
        statement.setLong(3, entity.getBoardColumn().getId());
    }
}
