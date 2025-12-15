/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author LENOVO
 */
import Modelos.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public Usuario validar(String email, String password) {
        Usuario usu = null;
        String sql = "SELECT * FROM usuarios WHERE email=? AND password=?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ps.setString(2, password); 
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usu = new Usuario();
                    usu.setId(rs.getInt("id"));
                    usu.setNombre(rs.getString("nombre"));
                    usu.setEmail(rs.getString("email"));
                    usu.setRol(rs.getString("rol"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al validar usuario: " + e.getMessage());
        }
        return usu;
    }

    public boolean registrar(Usuario u, String password) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, email, password, rol) VALUES (?, ?, ?, ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getEmail());
            ps.setString(3, password); 
            ps.setString(4, u.getRol());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        }
    }
    
    /**
     * Lista todos los usuarios con rol de DOCENTE.
     * ESTRATEGIA INFALIBLE: Traemos todos y filtramos en Java para evitar problemas de SQL.
     */
    public List<Usuario> listarDocentes() {
        List<Usuario> lista = new ArrayList<>();
        // Traemos todos los usuarios sin filtrar por SQL
        String sql = "SELECT * FROM usuarios";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String rol = rs.getString("rol");
                
                // Filtramos aquí en Java: Si contiene "docente" (ignorando mayúsculas/espacios)
                if (rol != null && rol.trim().toLowerCase().contains("docente")) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setEmail(rs.getString("email"));
                    u.setRol(rol);
                    lista.add(u);
                }
            }
            System.out.println("--> [UsuarioDAO] Docentes encontrados (Java Filter): " + lista.size());
            
        } catch (Exception e) {
            System.err.println("Error al listar docentes: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setEmail(rs.getString("email"));
                u.setRol(rs.getString("rol"));
                lista.add(u);
            }
        } catch (Exception e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}