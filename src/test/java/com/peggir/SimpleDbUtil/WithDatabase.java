package com.peggir.SimpleDbUtil;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import play.db.Database;
import play.db.Databases;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public abstract class WithDatabase {

    protected Database database;

    @Before
    public void setUp() throws Exception {
        initializeDatabase();
        executeTestSet();
    }

    @After
    public void tearDown() {
        database.shutdown();
    }

    private void initializeDatabase() {
        database = Databases.inMemory(
                "test_database",
                ImmutableMap.of("MODE", "PostgreSQL"),
                ImmutableMap.of("logStatements", false));
    }

    private void executeTestSet() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource("testSet.sql").getFile());
        final String c = FileUtils.readFileToString(file, "UTF-8");
        try (final Connection conn = database.getConnection();
             final PreparedStatement stmt = conn.prepareStatement(c)) {
            stmt.executeUpdate();
        }
    }

}
