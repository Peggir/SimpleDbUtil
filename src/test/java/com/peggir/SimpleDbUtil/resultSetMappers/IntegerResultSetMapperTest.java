package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class IntegerResultSetMapperTest {

    private IntegerResultSetMapper resultSetMapper;

    @Before
    public void setUp() {
        resultSetMapper = new IntegerResultSetMapper();
    }

    @Test
    public void testMapSuccessfully() throws Exception {
        final Integer value = 123;

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getInt(1)).thenReturn(value);

        final Integer result = resultSetMapper.map(resultSet);
        assertEquals(value, result);
    }

    @Test(expected = DbCallResultSetMapperException.class)
    public void testMapWithInvalidResultSet() throws Exception {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getInt(1)).thenThrow(new SQLException());

        resultSetMapper.map(resultSet);
    }

}
