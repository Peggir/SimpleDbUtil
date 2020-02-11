package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class DateResultSetMapperTest {

    private DateResultSetMapper resultSetMapper;

    @Before
    public void setUp() {
        resultSetMapper = new DateResultSetMapper();
    }

    @Test
    public void testMapSuccessfully() throws Exception {
        final int value = 234234234;

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getTimestamp(1)).thenReturn(new Timestamp(value));

        final Date result = resultSetMapper.map(resultSet);
        assertEquals(value, result.getTime());
    }

    @Test(expected = DbCallResultSetMapperException.class)
    public void testMapWithInvalidResultSet() throws Exception {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getTimestamp(1)).thenThrow(new SQLException());

        resultSetMapper.map(resultSet);
    }

}
