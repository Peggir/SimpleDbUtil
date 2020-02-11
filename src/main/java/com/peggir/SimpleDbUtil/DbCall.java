package com.peggir.SimpleDbUtil;

import com.peggir.SimpleDbUtil.exceptions.DbCallException;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.intellij.lang.annotations.Language;
import play.db.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Provides database query operations. Use it to insert or retrieve data to or
 * from the database. It is syntactic sugar over the {@link Connection} class
 * provided by Java. It is made to be used with the Play! Framework
 * {@link Database} class.
 *
 * @param <T> Object used to map the database result sets to
 */
public class DbCall<T> {

    @Language("SQL")
    private String query;
    private Database db;
    private DbCallArguments arguments;
    private DbCallResultSetMapper<T> resultSetMapper;
    private boolean hasQueryArguments;

    /**
     * Prepare a database call with all arguments.
     *
     * @param db              Database to query from
     * @param resultSetMapper {@link DbCallResultSetMapper} for object mapping
     * @param query           SQL-query
     * @param arguments       SQL-query parameters
     */
    public DbCall(final Database db,
                  final DbCallResultSetMapper<T> resultSetMapper,
                  @Language("SQL") final String query,
                  final DbCallArguments arguments) {
        this.db = db;
        this.query = query;
        this.arguments = arguments;
        this.resultSetMapper = resultSetMapper;
    }

    /**
     * Prepare a database call without {@link DbCallArguments}, for when you
     * don't have query parameters.
     *
     * @param db              Database to query from
     * @param resultSetMapper {@link DbCallResultSetMapper} for object mapping
     * @param query           SQL-query
     */
    public DbCall(final Database db,
                  final DbCallResultSetMapper<T> resultSetMapper,
                  @Language("SQL") final String query) {
        this.db = db;
        this.query = query;
        this.resultSetMapper = resultSetMapper;
    }

    /**
     * Prepare a database call without object-mapping.
     *
     * @param db        Database to query from
     * @param query     SQL-query
     * @param arguments SQL-query parameters
     */
    public DbCall(final Database db, @Language("SQL") final String query, final DbCallArguments arguments) {
        this.db = db;
        this.query = query;
        this.arguments = arguments;
    }

    /**
     * Prepare a database call without object-mapping and arguments.
     *
     * @param db    Database to query from
     * @param query SQL-query
     */
    public DbCall(final Database db, @Language("SQL") final String query) {
        this.db = db;
        this.query = query;
    }

    /**
     * Returns the first mapped object from the query's result set.
     *
     * @return Mapped {@link T} or empty {@link Optional} when the query had no results
     * @throws DbCallException Thrown when unable to query database
     */
    public Optional<T> getOne() throws DbCallException {
        validateRetrievalArguments();

        try (final Connection conn = db.getConnection(); final PreparedStatement stmt = conn.prepareStatement(query)) {
            if (hasQueryArguments) {
                arguments.apply(stmt);
            }

            final ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.ofNullable(resultSetMapper.map(rs));
        } catch (final SQLException | DbCallResultSetMapperException e) {
            throw new DbCallException(DbCallException.DEFAULT_ERROR_MSG, e);
        }
    }

    /**
     * Returns a list of all mapped objects from the query's entire result set.
     *
     * @return List of mapped {@link T}
     * @throws DbCallException Thrown when unable to query database
     */
    public List<T> getAll() throws DbCallException {
        validateRetrievalArguments();
        final List<T> results = new ArrayList<>();
        try (final Connection conn = db.getConnection(); final PreparedStatement stmt = conn.prepareStatement(query)) {
            if (hasQueryArguments) {
                arguments.apply(stmt);
            }

            final ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(resultSetMapper.map(rs));
            }

            return results;
        } catch (final SQLException | DbCallResultSetMapperException e) {
            throw new DbCallException(DbCallException.DEFAULT_ERROR_MSG, e);
        }
    }

    /**
     * Returns whether the query has any results in the database.
     *
     * @return True when then query has any result and otherwise false
     * @throws DbCallException Thrown when unable to query database
     */
    public boolean hasResults() throws DbCallException {
        validateArguments();
        try (final Connection conn = db.getConnection(); final PreparedStatement stmt = conn.prepareStatement(query)) {
            if (hasQueryArguments) {
                arguments.apply(stmt);
            }

            final ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (final SQLException e) {
            throw new DbCallException(DbCallException.DEFAULT_ERROR_MSG, e);
        }
    }

    /**
     * Executes the query on the database.
     *
     * @throws DbCallException Thrown when unable to query database
     */
    public void execute() throws DbCallException {
        validateArguments();
        try (final Connection conn = db.getConnection(); final PreparedStatement stmt = conn.prepareStatement(query)) {
            if (hasQueryArguments) {
                arguments.apply(stmt);
            }

            stmt.executeUpdate();
        } catch (final SQLException e) {
            throw new DbCallException(DbCallException.DEFAULT_ERROR_MSG, e);
        }
    }

    /**
     * Validates whether a database, query, optional arguments and a result set
     * mapper are present.
     *
     * @throws IllegalArgumentException When missing arguments needed to
     *                                  execute the DbCall and return data
     */
    private void validateRetrievalArguments() throws IllegalArgumentException {
        validateArguments();

        if (resultSetMapper == null) {
            throw new IllegalArgumentException("No DbCallResultSetMapper set");
        }
    }

    /**
     * Validates whether a valid query, database and optional arguments are
     * presents.
     *
     * @throws IllegalArgumentException When missing arguments needed to
     *                                  execute the DbCall
     */
    private void validateArguments() throws IllegalArgumentException {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("No query set");
        }

        // TODO - Find another way to determine whether the query contains parameters
        hasQueryArguments = query.contains("?");

        if (db == null) {
            throw new IllegalArgumentException("No database set");
        }

        if (hasQueryArguments && arguments == null) {
            throw new IllegalArgumentException("No DbCallArguments set");
        }
    }

    /**
     * Sets a new database to execute the queries on.
     *
     * @param db New database
     */
    public void setDb(final Database db) {
        this.db = db;
    }

    /**
     * Sets a new SQL-statement to query on the database.
     *
     * @param query New SQL-statement
     */
    public void setQuery(@Language("SQL") final String query) {
        this.query = query;
    }

    /**
     * Sets new SQL-query parameters to inject in the statement.
     *
     * @param arguments New arguments
     */
    public void setArguments(final DbCallArguments arguments) {
        this.arguments = arguments;
    }

    /**
     * Sets a new result set mapper used to make results from the database to
     * {@link T}.
     *
     * @param resultSetMapper New result set mapper
     */
    public void setResultSetMapper(final DbCallResultSetMapper<T> resultSetMapper) {
        this.resultSetMapper = resultSetMapper;
    }

}
