/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author LENOVO
 */
import Modelos.Evaluacion;
import Modelos.Tesis;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TesisDAO {

    /**
     * Registra una nueva tesis en la base de datos (Usado por TesisAdminServlet).
     * El estado inicial será 'inicio' y la fecha de entrega la fecha actual.
     */
    public boolean registrarTesis(Tesis t) {
        String sql = "INSERT INTO tesis (titulo, estudiante_id, fecha_entrega, estado, archivo_ruta) VALUES (?, ?, CURDATE(), 'inicio', ?)";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, t.getTitulo());
            ps.setInt(2, t.getEstudianteId());
            ps.setString(3, t.getArchivoRuta()); // Guardamos el nombre del archivo PDF
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar tesis: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene la tesis asignada a un estudiante específico (Usado por StudentServlet).
     * Incluye el nombre del evaluador y la nota final si ya existe.
     */
    public Tesis obtenerPorEstudiante(int idEstudiante) {
        Tesis t = null;
        // Hacemos un LEFT JOIN con usuarios para obtener el nombre del evaluador (si tiene uno asignado)
        String sql = "SELECT t.*, u.nombre as nombre_evaluador " +
                     "FROM tesis t " +
                     "LEFT JOIN usuarios u ON t.evaluador_id = u.id " +
                     "WHERE t.estudiante_id = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEstudiante);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t = new Tesis();
                    t.setId(rs.getInt("id"));
                    t.setTitulo(rs.getString("titulo"));
                    t.setFechaEntrega(rs.getString("fecha_entrega")); // MySQL devuelve formato YYYY-MM-DD
                    t.setEstado(rs.getString("estado"));
                    
                    // Si el evaluador es NULL en la BD, mostramos "Por asignar"
                    String nombreEvaluador = rs.getString("nombre_evaluador");
                    t.setEvaluador(nombreEvaluador != null ? nombreEvaluador : "Por asignar");
                    
                    // Cargamos nota y comentarios si la tesis ya fue evaluada
                    cargarDatosEvaluacion(t);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener tesis por estudiante: " + e.getMessage());
        }
        return t;
    }

    /**
     * Lista todas las tesis asignadas a un docente evaluador (Usado por TeacherServlet).
     */
    public List<Tesis> listarPorEvaluador(int idEvaluador) {
        List<Tesis> lista = new ArrayList<>();
        String sql = "SELECT * FROM tesis WHERE evaluador_id = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idEvaluador);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Tesis t = new Tesis();
                    t.setId(rs.getInt("id"));
                    t.setTitulo(rs.getString("titulo"));
                    t.setFechaEntrega(rs.getString("fecha_entrega"));
                    t.setEstado(rs.getString("estado"));
                    
                    // Cargamos los datos de la evaluación para mostrar si ya tiene nota
                    cargarDatosEvaluacion(t);
                    
                    lista.add(t);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar tesis del docente: " + e.getMessage());
        }
        return lista;
    }
    
    /**
     * Lista TODAS las tesis registradas para el panel de Administrador.
     * Incluye el nombre del estudiante haciendo JOIN con la tabla usuarios.
     */
    public List<Tesis> listarTodas() {
        List<Tesis> lista = new ArrayList<>();
        // Unimos con la tabla usuarios para saber quién es el estudiante (u.nombre)
        String sql = "SELECT t.*, u.nombre as nombre_estudiante FROM tesis t " +
                     "LEFT JOIN usuarios u ON t.estudiante_id = u.id " +
                     "ORDER BY t.id DESC";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Tesis t = new Tesis();
                t.setId(rs.getInt("id"));
                t.setTitulo(rs.getString("titulo"));
                t.setEstudianteId(rs.getInt("estudiante_id"));
                
                // Para simplificar sin tocar Modelos/Tesis.java, vamos a setear el nombre del estudiante
                // en el campo 'evaluador' SOLO PARA ESTA LISTA, sabiendo que en el admin mostramos "Estudiante".
                t.setEvaluador(rs.getString("nombre_estudiante")); 
                
                t.setFechaEntrega(rs.getString("fecha_entrega"));
                t.setEstado(rs.getString("estado"));
                t.setArchivoRuta(rs.getString("archivo_ruta"));
                
                lista.add(t);
            }
        } catch (Exception e) {
            System.err.println("Error al listar todas las tesis: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Asigna un docente evaluador a una tesis específica.
     * Actualiza el estado a 'evaluac' si estaba en 'inicio'.
     */
    public boolean asignarEvaluador(int idTesis, int idDocente) {
        // Actualizamos el evaluador y cambiamos el estado a 'evaluac' (En Evaluación)
        // solo si la tesis aún no ha sido completada.
        String sql = "UPDATE tesis SET evaluador_id = ?, estado = IF(estado='inicio', 'evaluac', estado) WHERE id = ?";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, idDocente);
            ps.setInt(2, idTesis);
            
            int filas = ps.executeUpdate();
            return filas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al asignar evaluador: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza el título y el estudiante asignado a una tesis existente.
     * (Usado para editar tesis desde el panel de admin).
     */
    public boolean actualizarTesis(int id, String titulo, int estudianteId) {
        String sql = "UPDATE tesis SET titulo = ?, estudiante_id = ? WHERE id = ?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, titulo);
            ps.setInt(2, estudianteId);
            ps.setInt(3, id);
            
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar tesis: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra una nueva evaluación en la base de datos y actualiza el estado de la tesis a 'complet'.
     * Utiliza una TRANSACCIÓN para asegurar que ambas cosas ocurran o ninguna.
     * (Usado por EvaluarServlet).
     */
    public boolean registrarEvaluacion(Evaluacion ev) {
        Connection con = null;
        try {
            con = Conexion.getConexion();
            con.setAutoCommit(false); // Iniciar transacción manualmente

            // 1. Insertar el detalle de la evaluación (los 7 criterios)
            String sqlInsert = "INSERT INTO evaluaciones (tesis_id, nota_originalidad, nota_planteamiento, " +
                               "nota_metodologia, nota_resultados, nota_coherencia, nota_sustento, " +
                               "nota_formato, comentarios, nota_final) VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
                ps.setInt(1, ev.getTesisId());
                ps.setInt(2, ev.getNotaOriginalidad());
                ps.setInt(3, ev.getNotaPlanteamiento());
                ps.setInt(4, ev.getNotaMetodologia());
                ps.setInt(5, ev.getNotaResultados());
                ps.setInt(6, ev.getNotaCoherencia());
                ps.setInt(7, ev.getNotaSustento());
                ps.setInt(8, ev.getNotaFormato());
                ps.setString(9, ev.getComentarios());
                ps.setDouble(10, ev.getNotaFinal());
                ps.executeUpdate();
            }

            // 2. Actualizar el estado de la tesis en la tabla 'tesis'
            String sqlUpdate = "UPDATE tesis SET estado = 'complet' WHERE id = ?";
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, ev.getTesisId());
                psUpdate.executeUpdate();
            }

            con.commit(); // Confirmar cambios si todo salió bien
            return true;

        } catch (Exception e) {
            System.err.println("Error en transacción de evaluación: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback(); // Deshacer cambios si algo falló
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // Restaurar comportamiento por defecto
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * Método auxiliar privado para buscar la nota final y comentarios de una tesis
     * en la tabla 'evaluaciones' y asignarlos al objeto Tesis.
     * MODIFICADO: Ahora carga TAMBIÉN las notas parciales (n1 a n7).
     */
    private void cargarDatosEvaluacion(Tesis t) {
        String sql = "SELECT * FROM evaluaciones WHERE tesis_id = ? ORDER BY id DESC LIMIT 1";
        
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setInt(1, t.getId());
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    t.setNotaFinal(rs.getDouble("nota_final"));
                    t.setComentarios(rs.getString("comentarios"));
                    // Cargamos notas parciales
                    t.setNota1(rs.getInt("nota_originalidad"));
                    t.setNota2(rs.getInt("nota_planteamiento"));
                    t.setNota3(rs.getInt("nota_metodologia"));
                    t.setNota4(rs.getInt("nota_resultados"));
                    t.setNota5(rs.getInt("nota_coherencia"));
                    t.setNota6(rs.getInt("nota_sustento"));
                    t.setNota7(rs.getInt("nota_formato"));
                } else {
                    // Valores por defecto si no ha sido evaluada
                    t.setNotaFinal(0.0);
                    t.setComentarios("Sin comentarios aún.");
                }
            }
        } catch (Exception e) {
            // No imprimimos error crítico aquí, ya que es normal no encontrar evaluación si el estado es 'inicio'
        }
    }
}