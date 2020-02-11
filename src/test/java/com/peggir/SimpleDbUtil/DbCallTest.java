package com.peggir.SimpleDbUtil;

import com.peggir.SimpleDbUtil.exceptions.DbCallException;
import com.peggir.SimpleDbUtil.exceptions.DbCallResultSetMapperException;
import com.peggir.SimpleDbUtil.resultSetMappers.StringResultSetMapper;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DbCallTest extends WithDatabase {

    @Language("SQL")
    private static final String SELECT_NAME_PARAMETERIZED = "SELECT \"name\" FROM \"person\" WHERE \"id\"=?;";

    @Language("SQL")
    private static final String SELECT_PERSON_AGE_10 = "SELECT \"name\" FROM \"person\" WHERE \"age\"=10;";

    @Language("SQL")
    private static final String SELECT_PERSON_AGE_100 = "SELECT \"name\" FROM \"person\" WHERE \"age\"=100;";

    @Test
    public void testGetOneWithQueryParameterAndExistingResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 0));
        final Optional<String> result = call.getOne();

        assertTrue(result.isPresent());
        assertEquals("Harry Potter", result.get());
    }

    @Test
    public void testGetOneWithQueryParameterAndNoResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 999));
        final Optional<String> result = call.getOne();

        assertFalse(result.isPresent());
    }

    @Test
    public void testGetOneWithoutQueryParametersWithResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_PERSON_AGE_10);
        final Optional<String> result = call.getOne();

        assertTrue(result.isPresent());
        assertEquals("Hermione Granger", result.get());
    }

    @Test
    public void testGetOneWithoutQueryParametersWithoutResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_PERSON_AGE_100);
        final Optional<String> result = call.getOne();

        assertFalse(result.isPresent());
    }

    @Test(expected = DbCallException.class)
    public void testGetOneWhenThereAreMappingErrors() throws Exception {
        new DbCall<>(
                database,
                (DbCallResultSetMapper<String>) rs -> {
                    throw new DbCallResultSetMapperException("", null);
                },
                "SELECT \"name\" FROM \"person\";"
        ).getOne();
    }

    @Test(expected = DbCallException.class)
    public void testGetOneWhenThereAreQueryErrors() throws Exception {
        new DbCall<>(database, new StringResultSetMapper(), "SELECT \"name\" FROM \"i_dont_exist\";").getOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOneWhenResultSetMapperIsMissing() throws Exception {
        new DbCall<>(database, null, "SELECT * FROM \"person\";").getOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOneWhenQueryIsMissing() throws Exception {
        new DbCall<>(database, new StringResultSetMapper(), null).getOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOneWhenQueryIsEmpty() throws Exception {
        new DbCall<>(database, new StringResultSetMapper(), "").getOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOneWhenDatabaseIsMissing() throws Exception {
        new DbCall<>(null, new StringResultSetMapper(), "SELECT 1 FROM \"person\";").getOne();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOneWithArgumentsButWithoutBindings() throws Exception {
        new DbCall<>(database, new StringResultSetMapper(), "SELECT 1 FROM \"person\" WHERE \"age\"=?;").getOne();
    }

    @Test
    public void testGetAllWithQueryParameterAndExistingResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 0));
        final List<String> result = call.getAll();

        assertEquals(1, result.size());
        assertEquals("Harry Potter", result.get(0));
    }

    @Test
    public void testGetAllWithQueryParameterAndNoResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 999));
        final List<String> result = call.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetAllWithoutQueryParametersWithResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                "SELECT \"name\" FROM \"person\" ORDER BY \"id\";");
        final List<String> result = call.getAll();

        assertEquals(4, result.size());
        assertEquals("Harry Potter", result.get(0));
        assertEquals("Hermione Granger", result.get(1));
        assertEquals("Ron Weasley", result.get(2));
        assertEquals("Severus Snape", result.get(3));
    }

    @Test
    public void testGetAllWithoutQueryParametersWithoutResult() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                "SELECT \"name\" FROM \"person\" WHERE \"age\">=100;");
        final List<String> result = call.getAll();

        assertTrue(result.isEmpty());
    }

    @Test(expected = DbCallException.class)
    public void testGetAllWhenThereAreMappingErrors() throws Exception {
        new DbCall<>(
                database,
                (DbCallResultSetMapper<String>) rs -> {
                    throw new DbCallResultSetMapperException("", null);
                },
                "SELECT \"name\" FROM \"person\";"
        ).getAll();
    }

    @Test(expected = DbCallException.class)
    public void testGetAllWhenThereAreQueryErrors() throws Exception {
        new DbCall<>(database, new StringResultSetMapper(), "SELECT * FROM \"i_dont_exist\";").getAll();
    }

    @Test
    public void testHasResultWithArgumentsAndResults() throws Exception {
        final DbCall call = new DbCall(database, SELECT_NAME_PARAMETERIZED, stmt -> stmt.setInt(1, 0));
        assertTrue(call.hasResults());
    }

    @Test
    public void testHasResultsWithArgumentsAndNoResults() throws Exception {
        final DbCall call = new DbCall(database, SELECT_NAME_PARAMETERIZED, stmt -> stmt.setInt(1, 999));
        assertFalse(call.hasResults());
    }

    @Test
    public void testHasResultWithoutArgumentsAndResults() throws Exception {
        final DbCall call = new DbCall(database, SELECT_PERSON_AGE_10);
        assertTrue(call.hasResults());
    }

    @Test
    public void testHasResultsWithoutArgumentsAndNoResults() throws Exception {
        final DbCall call = new DbCall(database, SELECT_PERSON_AGE_100);
        assertFalse(call.hasResults());
    }

    @Test(expected = DbCallException.class)
    public void testHasResultsWithDatabaseErrors() throws Exception {
        new DbCall(database, "SELECT 1 FROM \"i_dont_exist\";").hasResults();
    }

    @Test
    public void testExecuteWithArgumentsSuccessfully() throws Exception {
        final String newName = "Albus Dumbledore";
        new DbCall<>(
                database,
                "INSERT INTO \"person\" (\"id\", \"name\", \"age\", \"isStudent\") " +
                        "VALUES (?,?,?,?);",
                stmt -> {
                    stmt.setInt(1, 666);
                    stmt.setString(2, newName);
                    stmt.setInt(3, 100);
                    stmt.setBoolean(4, false);
                }
        ).execute();

        final Optional<String> result = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_PERSON_AGE_100
        ).getOne();
        assertTrue(result.isPresent());
        assertEquals(newName, result.get());
    }

    @Test
    public void testExecuteWithoutArgumentsSuccessfully() throws Exception {
        final String newName = "Albus Dumbledore";
        new DbCall<>(
                database,
                "INSERT INTO \"person\" (\"id\", \"name\", \"age\", \"isStudent\") " +
                        "VALUES (666,'Albus Dumbledore',100,FALSE);"
        ).execute();

        final Optional<String> result = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_PERSON_AGE_100
        ).getOne();
        assertTrue(result.isPresent());
        assertEquals(newName, result.get());
    }

    @Test(expected = DbCallException.class)
    public void testExecuteWithSqlErrors() throws Exception {
        new DbCall<>(database, "INSERT INTO \"i_dont_exist\" (\"id\") VALUES (1) ").execute();
    }

    @Test
    public void testSetDbSuccessfully() throws Exception {
        final DbCall<String> call = new DbCall<>(
                null,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 0));
        call.setDb(database);

        final Optional<String> result = call.getOne();
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetQuerySuccessfully() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                "",
                stmt -> stmt.setInt(1, 0));
        call.setQuery(SELECT_NAME_PARAMETERIZED);

        final Optional<String> result = call.getOne();
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetArgumentsSuccessfully() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                new StringResultSetMapper(),
                SELECT_NAME_PARAMETERIZED);
        call.setArguments(stmt -> stmt.setInt(1, 0));
        call.setQuery(SELECT_NAME_PARAMETERIZED);

        final Optional<String> result = call.getOne();
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetResultSetMapperSuccessfully() throws Exception {
        final DbCall<String> call = new DbCall<>(
                database,
                null,
                SELECT_NAME_PARAMETERIZED,
                stmt -> stmt.setInt(1, 0));
        call.setResultSetMapper(new StringResultSetMapper());
        call.setQuery(SELECT_NAME_PARAMETERIZED);

        final Optional<String> result = call.getOne();
        assertTrue(result.isPresent());
    }

}
