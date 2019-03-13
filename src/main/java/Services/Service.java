package Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Service {
    private static Service service;
   //private static String URL = "jdbc:h2:tcp://localhost/~/practica3";

    public static Service getInstancia() {
        if (service == null) {
            service = new Service();
        }
        return service;
    }


    public Connection getConnection() {
        Connection _conn = null;
        try {
            String url = "jdbc:h2:tcp://localhost/~/practica3";
            _conn = DriverManager.getConnection(url, "sa", "");
            //_conn = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return _conn;
    }

}
