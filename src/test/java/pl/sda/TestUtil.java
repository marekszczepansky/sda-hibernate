package pl.sda;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * Created by pzawa on 02.02.2017.
 */
public class TestUtil {

    private static void cleanUpDatabase(Connection connection) throws IOException, SQLException {
        InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream("sda.sql");
        String sqlContent = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        connection.createStatement().executeUpdate(sqlContent);
    }

    public static void cleanUpDatabase(SessionFactory sessionFactory){
        try(Session session = sessionFactory.openSession()){
            session.doWork(new Work() {
                @Override
                public void execute(Connection connection) throws SQLException {
                    try {
                        TestUtil.cleanUpDatabase(connection);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}
