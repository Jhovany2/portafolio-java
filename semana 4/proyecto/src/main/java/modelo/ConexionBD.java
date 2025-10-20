
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    
    // Cambia USER y PASSWORD si tu MySQL usa otros datos
    private static final String URL = "jdbc:mysql://localhost:3306/finanzas_personales";
    private static final String USER = "root";
    private static final String PASSWORD = "75020005"; 

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conectado correctamente a la base de datos MySQL.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}
