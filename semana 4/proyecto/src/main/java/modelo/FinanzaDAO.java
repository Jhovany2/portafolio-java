
package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanzaDAO {
    
    /**
     * Registra una finanza (Ingreso o Gasto) asociada a idUsuario.
     */
    public boolean registrarFinanza(Finanza f, int idUsuario) {
        String sql = "INSERT INTO finanzas (id_usuario, fecha, monto, categoria, descripcion, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setDate(2, new java.sql.Date(f.getFecha().getTime()));
            ps.setDouble(3, f.getMonto());
            ps.setString(4, f.getCategoria());
            ps.setString(5, f.getDescripcion());
            // Aseguramos el tipo en mayúscula tal como definimos en la tabla
            String tipo = f.getTipo();
            if (tipo != null) tipo = tipo.equalsIgnoreCase("Ingreso") ? "Ingreso" : "Gasto";
            ps.setString(6, tipo);

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error FinanzaDAO.registrarFinanza: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista todas las finanzas (ingresos y gastos) de un usuario por su id.
     */
    public List<Finanza> listarPorUsuario(int idUsuario) {
        List<Finanza> lista = new ArrayList<>();
        String sql = "SELECT fecha, monto, categoria, descripcion, tipo FROM finanzas WHERE id_usuario = ? ORDER BY fecha DESC";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    java.util.Date fecha = rs.getDate("fecha");
                    double monto = rs.getDouble("monto");
                    String categoria = rs.getString("categoria");
                    String descripcion = rs.getString("descripcion");

                    Finanza f = new Finanza(tipo, fecha, monto, categoria, descripcion, null);
                    lista.add(f);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error FinanzaDAO.listarPorUsuario: " + e.getMessage());
        }
        return lista;
    }

    public double[] obtenerResumenMensual(int idUsuario) {
       double ingresos = 0, gastos = 0;
    String sql = "SELECT tipo, SUM(monto) AS total FROM finanzas WHERE id_usuario = ? "
               + "AND MONTH(fecha) = MONTH(CURDATE()) AND YEAR(fecha) = YEAR(CURDATE()) "
               + "GROUP BY tipo";
    try (Connection con = ConexionBD.conectar();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, idUsuario);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                double total = rs.getDouble("total");
                if ("Ingreso".equalsIgnoreCase(tipo)) ingresos = total;
                else if ("Gasto".equalsIgnoreCase(tipo)) gastos = total;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error FinanzaDAO.obtenerResumenMensual: " + e.getMessage());
    }
    double saldo = ingresos - gastos;
    return new double[]{ingresos, gastos, saldo};
    }
    
    
}
