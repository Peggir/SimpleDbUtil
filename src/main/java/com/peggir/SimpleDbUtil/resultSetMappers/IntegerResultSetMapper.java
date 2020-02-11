package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a {@link DbCallResultSetMapper} used to map a {@link ResultSet}
 * containing a number to an {@link Integer}.
 */
public class IntegerResultSetMapper implements DbCallResultSetMapper<Integer> {

    /**
     * Maps the given {@link ResultSet} to an {@link Integer}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link Integer} represented by the {@link ResultSet}
     * @throws DbCallResultSetMapperException Thrown when unable to map the
     *                                        {@link ResultSet} to an
     *                                        {@link Integer}
     */
    @Override
    public Integer map(final ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return rs.getInt(1);
        } catch (final SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }

}
