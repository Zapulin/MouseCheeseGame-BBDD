package Zstudio.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    private static Connection instance;

    private ConnectDB(){}

    public static Connection getInstance() throws SQLException{
        if(instance==null) {
            instance = DriverManager.getConnection(MYSQLConnection.url,
                    MYSQLConnection.username,
                    MYSQLConnection.password);
            System.out.println("Open Database");
        }

        return instance;
    }

    public static void closeConnection() throws SQLException {
        if (instance!=null){
            instance.close();
            System.out.println("Database closed");
        }
    }

}
