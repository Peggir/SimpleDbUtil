package com.peggir.SimpleDbUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Provides an interface to set the parameters of a parameterized query in a
 * {@link DbCall}.
 */
public interface DbCallArguments {

    /**
     * Applies parameters to the matching SQL-statement in the {@link DbCall}.
     * Use this to safely parameterize queries.
     *
     * @param stmt {@link PreparedStatement} to inject parameters on to
     * @throws SQLException When unable to inject parameters to prepared
     *                      statement
     */
    void apply(final PreparedStatement stmt) throws SQLException;

}
