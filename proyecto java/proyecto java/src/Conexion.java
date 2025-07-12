import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/clinica?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "Admin123*";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }



}
