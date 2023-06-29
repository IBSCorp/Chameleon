package ru.ibsqa.chameleon.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import ru.ibsqa.chameleon.utils.spring.ChameleonSpringExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.fail;

@Slf4j
@ExtendWith(ChameleonSpringExtension.class)
@ContextConfiguration("classpath:spring.xml")
@TestExecutionListeners(inheritListeners = false, listeners =
        {DependencyInjectionTestExecutionListener.class})
public class H2Test {

    @Test
    public void dbTest() throws SQLException {
        insertWithStatement();
        insertWithPreparedStatement();
    }

    @BeforeAll
    public static void prepareTest() {
    }

    @AfterAll
    public static void finishTest() {
    }

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;user=;password=";

    public static void insertWithPreparedStatement() throws SQLException {
        Connection connection = getDBConnection();
        PreparedStatement createPreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        PreparedStatement selectPreparedStatement = null;
        PreparedStatement dropPreparedStatement = null;

        String CreateQuery = "CREATE TABLE PERSON(id int primary key, name varchar(255))";
        String InsertQuery = "INSERT INTO PERSON" + "(id, name) values" + "(?,?)";
        String SelectQuery = "select * from PERSON";
        String DropQuery = "DROP TABLE PERSON";

        try {
            connection.setAutoCommit(false);

            createPreparedStatement = connection.prepareStatement(CreateQuery);
            createPreparedStatement.executeUpdate();
            createPreparedStatement.close();

            insertPreparedStatement = connection.prepareStatement(InsertQuery);
            insertPreparedStatement.setInt(1, 1);
            insertPreparedStatement.setString(2, "Jose");
            insertPreparedStatement.executeUpdate();
            insertPreparedStatement.close();

            selectPreparedStatement = connection.prepareStatement(SelectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            log.info("H2 In-Memory Database inserted through PreparedStatement");
            while (rs.next()) {
                log.info("Id " + rs.getInt("id") + " Name " + rs.getString("name"));
            }
            selectPreparedStatement.close();

            dropPreparedStatement = connection.prepareStatement(DropQuery);
            dropPreparedStatement.executeUpdate();
            dropPreparedStatement.close();

            connection.commit();
        } catch (SQLException e) {
            log.info("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            connection.close();
        }
    }

    public static void insertWithStatement() throws SQLException {
        Connection connection = getDBConnection();
        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            stmt.execute("CREATE TABLE PERSON(id int primary key, name varchar(255))");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(1, 'Anju')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(2, 'Sonia')");
            stmt.execute("INSERT INTO PERSON(id, name) VALUES(3, 'Asha')");

            ResultSet rs = stmt.executeQuery("select * from PERSON");
            log.info("H2 In-Memory Database inserted through Statement");
            while (rs.next()) {
                log.info("Id " + rs.getInt("id") + " Name " + rs.getString("name"));
            }

            stmt.execute("DROP TABLE PERSON");
            stmt.close();
            connection.commit();
        } catch (SQLException e) {
            log.info("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            connection.close();
        }
    }

    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            fail(String.format("Драйвер %s не найден", DB_DRIVER));
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION);
            return dbConnection;
        } catch (SQLException e) {
            log.info(e.getMessage());
        }
        return dbConnection;
    }
}
