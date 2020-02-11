package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class LongResultSetMapperTest {

    private LongResultSetMapper resultSetMapper;

    @Before
    public void setUp() {
        resultSetMapper = new LongResultSetMapper();
    }

    @Test
    public void testMapSuccessfully() throws Exception {
        final Long value = 1234567891234545645L;

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong(1)).thenReturn(value);

        final Long result = resultSetMapper.map(resultSet);
        assertEquals(value, result);
    }

    @Test(expected = DbCallResultSetMapperException.class)
    public void testMapWithInvalidResultSet() throws Exception {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getLong(1)).thenThrow(new SQLException());

        resultSetMapper.map(resultSet);
    }

}
