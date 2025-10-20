
package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import modelo.Finanza;
import modelo.Usuario;



public class DataStore {
    
    // Guarda todos los usuarios registrados
    public static List<Usuario> usuarios = new ArrayList<>();

    // Guarda todos los ingresos y gastos
    public static List<Finanza> ingresos = new ArrayList<>();
    public static List<Finanza> gastos = new ArrayList<>();

    // Usuario actualmente logueado
    public static Usuario usuarioActivo = null;
}
    
  
