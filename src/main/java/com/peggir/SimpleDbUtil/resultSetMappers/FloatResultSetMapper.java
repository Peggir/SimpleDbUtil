package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.DbCallResultSetMapper;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides a {@link DbCallResultSetMapper} used to map a {@link ResultSet}
 * containing a float to a {@link Float}.
 */
public class FloatResultSetMapper implements DbCallResultSetMapper<Float> {

    /**
     * Maps the given {@link ResultSet} to a {@link Float}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link Float} represented by the {@link ResultSet}
     * @throws DbCallResultSetMapperException Thrown when unable to map the
     *                                        {@link ResultSet} to a
     *                                        {@link Float}
     */
    @Override
    public Float map(final ResultSet rs) throws DbCallResultSetMapperException {
        try {
            return rs.getFloat(1);
        } catch (final SQLException e) {
            throw new DbCallResultSetMapperException(DbCallResultSetMapperException.DEFAULT_ERROR_MSG, e);
        }
    }

}
