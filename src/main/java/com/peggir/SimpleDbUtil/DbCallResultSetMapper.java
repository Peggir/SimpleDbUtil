package com.peggir.SimpleDbUtil;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;

import java.sql.ResultSet;

/**
 * Provides an interface used to map a {@link ResultSet} to a {@link T}-object.
 *
 * @param <T> Object represented by the {@link ResultSet}
 */
public interface DbCallResultSetMapper<T> {

    /**
     * Maps the given {@link ResultSet} to a {@link T}.
     *
     * @param rs {@link ResultSet} to map from
     * @return {@link T} representing the result set
     * @throws DbCallResultSetMapperException Thrown then unable to map the
     *                                        {@link ResultSet} to a {@link T}
     */
    T map(final ResultSet rs) throws DbCallResultSetMapperException;

}
