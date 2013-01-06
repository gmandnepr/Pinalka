package com.pinalka;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbConnection;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public abstract class IntegrationTest extends ContextAwareTest {

    protected static final Properties prop = new Properties();

    static {
        try {
            prop.load(new InputStreamReader(IntegrationTest.class.getResourceAsStream("/test_config.properties")));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static void generateDBXMLDump(final String filename) {
        try {
            final Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.0.2/pinalka_test?user=pinalka&password=pinalkapass");
            final HsqldbConnection dbunitConnection = new HsqldbConnection(connection, null);
            final IDataSet fullDataSet = dbunitConnection.createDataSet();
            final FileOutputStream xmlStream = new FileOutputStream(filename);
            XmlDataSet.write(fullDataSet, xmlStream);
            xmlStream.close();
            dbunitConnection.close();
            connection.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static void reInitDataFromTheDump() throws RuntimeException {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            final Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testDB");
            final HsqldbConnection dbunitConnection = new HsqldbConnection(connection, null);
            final InputStream is = IntegrationTest.class.getResourceAsStream("/test-data.xml");
            final IDataSet dataSet = new XmlDataSet(is);
            DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, dataSet);
            dbunitConnection.close();
            connection.close();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
