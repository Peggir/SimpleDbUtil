package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a {@link DbCallResultSetMapper} used to map a {@link ResultSet}
 * containing text to a {@link String}.
 */
public class StringResultSetMapper implements DbCallResultSetMapper<String> {

    /**
     * Maps the given {@link ResultSet} to a {@link String}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link String} represented by the {@link ResultSet}
     * @throws DbCallResultSetMapperException Thrown when unable to map the
     *                                        {@link ResultSet} to a
     *                                        {@link String}
     */
    @Override
    public String map(final ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return rs.getString(1);
        } catch (final SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }

}
