package br.com.dio.persistence.dao;

import br.com.dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private Connection connection;

    private static final String INSERT_SQL = "INSERT INTO BOARDS (name) values (?);";
    private static final String DELETE_SQL = "DELETE FROM BOARDS WHERE id = ?;";
    private static final String FIND_BY_ID_SQL = "SELECT id, name FROM BOARDS WHERE id = ?;";
    private static final String EXISTS_SQL = "SELECT 1 FROM BOARDS WHERE id = ?;";

    public BoardEntity insert(final BoardEntity entity) throws SQLException {
        try (var statement = connection.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
            }

            return entity;
        }
    }

    public void delete(final Long id) throws SQLException {
        try (var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<BoardEntity> findById(final Long id) throws SQLException {
        try (var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var entity = new BoardEntity();
                    entity.setId(resultSet.getLong("id"));
                    entity.setName(resultSet.getString("name"));
                    return Optional.of(entity);
                }
            }
            return Optional.empty();
        }
    }

    public boolean exists(final Long id) throws SQLException {
        try (var statement = connection.prepareStatement(EXISTS_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
