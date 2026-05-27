package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionBD {

    private static final String URL =
    "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=negocio;encrypt=true;trustServerCertificate=true";

    private static final String USUARIO = "sa";

    private static final String PASSWORD = "123456";

    public static Connection conectar() {

        Connection conexion = null;

        try {

            conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("Conexión exitosa");

        } catch (SQLException e) {

            System.out.println("Error al conectar");

            e.printStackTrace();
        }

        return conexion;
    }
}
