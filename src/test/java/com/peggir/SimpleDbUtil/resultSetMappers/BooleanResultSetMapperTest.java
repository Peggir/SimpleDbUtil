package com.peggir.SimpleDbUtil.resultSetMappers;

import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BooleanResultSetMapperTest {

    private BooleanResultSetMapper resultSetMapper;

    @Before
    public void setUp() {
        resultSetMapper = new BooleanResultSetMapper();
    }

    @Test
    public void testMapSuccessfully() throws Exception {
        final Boolean value = true;

        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getBoolean(1)).thenReturn(value);

        final Boolean result = resultSetMapper.map(resultSet);
        assertEquals(value, result);
    }

    @Test(expected = DbCallResultSetMapperException.class)
    public void testMapWithInvalidResultSet() throws Exception {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getBoolean(1)).thenThrow(new SQLException());

        resultSetMapper.map(resultSet);
    }

}
