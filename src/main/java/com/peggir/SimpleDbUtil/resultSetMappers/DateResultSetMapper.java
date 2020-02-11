package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Provides a {@link DbCallResultSetMapper} used to map a {@link ResultSet}
 * containing a date to a {@link Date}.
 */
public class DateResultSetMapper implements DbCallResultSetMapper<Date> {

    /**
     * Maps the given {@link ResultSet} to a {@link Date}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link Date} represented by the {@link ResultSet}
     * @throws DbCallResultSetMapperException Thrown when unable to map the
     *                                        {@link ResultSet} to a
     *                                        {@link Date}
     */
    @Override
    public Date map(final ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return rs.getTimestamp(1);
        } catch (final SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }

}
