/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author LENOVO
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    
    // CONFIGURACIÓN DE LA BASE DE DATOS
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_tesis?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    // Tu contraseña configurada previamente
    private static final String PASS = "75020005"; 

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection getConexion() {
        Connection con = null;
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USER, PASS);
            // System.out.println("Conexion exitosa a la Base de Datos!"); // Comentado para no ensuciar logs
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontro el Driver.");
        } catch (SQLException e) {
            System.err.println("❌ Error de Conexión SQL: " + e.getMessage());
        }
        return con;
    }
    
    // ==========================================
    // MÉTODO MAIN PARA DIAGNÓSTICO DE DATOS
    // ==========================================
    // Haz clic derecho en este archivo -> "Run File" para ejecutar esta prueba
    public static void main(String[] args) {
        System.out.println("--- INICIANDO DIAGNÓSTICO DE USUARIOS DOCENTES ---");
        Connection con = getConexion();
        
        if (con != null) {
            // Buscamos cualquier usuario que tenga 'docente' en el rol, sin importar mayúsculas
            String sql = "SELECT id, nombre, rol FROM usuarios";
            
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                System.out.println("ID | Nombre | Rol Exacto (entre corchetes para ver espacios)");
                System.out.println("----------------------------------------------------------");
                
                boolean hayDocentes = false;
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String rol = rs.getString("rol");
                    
                    // Imprimimos el rol entre corchetes [] para detectar espacios invisibles
                    System.out.printf("%d | %s | [%s]\n", id, nombre, rol);
                    
                    if (rol != null && rol.trim().equalsIgnoreCase("docente")) {
                        hayDocentes = true;
                    }
                }
                
                System.out.println("----------------------------------------------------------");
                if (hayDocentes) {
                    System.out.println("✅ RESULTADO: Sí existen usuarios con rol 'docente'.");
                    System.out.println("   Si la lista en la web sigue vacía, asegúrate de haber hecho 'Clean and Build'.");
                } else {
                    System.out.println("⚠️ RESULTADO: NO se detectaron usuarios con el rol 'docente' exacto.");
                    System.out.println("   Revisa la columna 'Rol Exacto' arriba. Si dice [administrador] o [estudiante], necesitas crear un docente.");
                }
                
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}