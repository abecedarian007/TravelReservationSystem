package backend.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.sun.istack.internal.NotNull;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBController {
    private static final String propertiesPath = "druid.properties";

    public static Connection getConnection() throws Exception {
        // 使用 try-with-resources 来确保资源被正确关闭
        try (InputStream input = DBController.class.getClassLoader().getResourceAsStream(propertiesPath)) {
            if (input == null) {
                throw new IllegalStateException("Unable to find " + propertiesPath + " in the classpath");
            }

            Properties properties = new Properties();
            properties.load(input);

            DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
            return dataSource.getConnection();
        }
    }

    public static void closeConnection(@NotNull @org.jetbrains.annotations.NotNull Connection connection) throws Exception {
        connection.close();
    }

}
