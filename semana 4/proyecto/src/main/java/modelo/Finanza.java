
package modelo;

import java.util.Date;

public class Finanza {
    
   private String tipo; // "Ingreso" o "Gasto"
    private Date fecha;
    private double monto;
    private String categoria;
    private String descripcion;
    private String correoUsuario;

    public Finanza(String tipo, Date fecha, double monto, String categoria, String descripcion, String correoUsuario) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.monto = monto;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.correoUsuario = correoUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public Date getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }
    
}
