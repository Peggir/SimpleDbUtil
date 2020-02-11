package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class FloatResultSetMapperTest {

    private FloatResultSetMapper resultSetMapper;

    @Before
    public void setUp() {
        resultSetMapper = new FloatResultSetMapper();
    }

    @Test
    public void testMapSuccessfully() throws Exception {
        final Float value = 123.45f;

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getFloat(1)).thenReturn(value);

        final Float result = resultSetMapper.map(resultSet);
        assertEquals(value, result);
    }

    @Test(expected = DbCallResultSetMapperException.class)
    public void testMapWithInvalidResultSet() throws Exception {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getFloat(1)).thenThrow(new SQLException());

        resultSetMapper.map(resultSet);
    }

}
