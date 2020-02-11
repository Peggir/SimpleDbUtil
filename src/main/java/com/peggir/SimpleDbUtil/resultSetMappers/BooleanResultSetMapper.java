package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a {@link DbCallResultSetMapper} used to map a {@link ResultSet}
 * containing a boolean to a {@link Boolean}.
 */
public class BooleanResultSetMapper implements DbCallResultSetMapper<Boolean> {

    /**
     * Maps the given {@link ResultSet} to a {@link Boolean}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link Boolean} represented by the {@link ResultSet}
     * @throws DbCallResultSetMapperException Thrown when unable to map the
     *                                        {@link ResultSet} to a
     *                                        {@link Boolean}
     */
    @Override
    public Boolean map(final ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return rs.getBoolean(1);
        } catch (final SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }

}
