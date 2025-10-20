
package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    /**
     * Registra usuario en la tabla usuarios.
     * Retorna true si se registró correctamente.
     */
    public boolean registrarUsuario(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre, apellido, correo, contrasena) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, u.getContrasena());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error UsuarioDAO.registrarUsuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Intenta iniciar sesión. Si encuentra el usuario, devuelve un objeto Usuario con el id.
     * Retorna null si no existe.
     */
    public Usuario iniciarSesion(String correo, String contrasena) {
        String sql = "SELECT id, nombre, apellido, correo, contrasena FROM usuarios WHERE correo=? AND contrasena=?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String c = rs.getString("correo");
                    String pass = rs.getString("contrasena");
                    return new Usuario(id, nombre, apellido, c, pass);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error UsuarioDAO.iniciarSesion: " + e.getMessage());
        }
        return null;
    }
}
