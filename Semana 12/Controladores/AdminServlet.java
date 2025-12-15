/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.TesisDAO;
import Datos.UsuarioDAO;
import Modelos.Tesis;
import Modelos.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LENOVO
 */
// IMPORTANTE: El urlPatterns debe ser "/AdminServlet" para coincidir con la redirección
@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Verificar sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (!"administrador".equals(usuario.getRol())) {
            response.sendRedirect("index.jsp?error=NoAutorizado");
            return;
        }

        try {
            // 2. Cargar datos necesarios
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            TesisDAO tesisDAO = new TesisDAO();

            // Lista general de usuarios (para la tabla de usuarios y para asignar estudiantes)
            List<Usuario> listaUsuarios = usuarioDAO.listarTodos();
            request.setAttribute("listaUsuarios", listaUsuarios);

            // IMPORTANTE: Cargar lista de DOCENTES para el selector de asignación
            // Si esto faltaba, el buscador de docentes en el JSP salía vacío.
            List<Usuario> listaDocentes = usuarioDAO.listarDocentes();
            request.setAttribute("listaDocentes", listaDocentes);

            // Cargar todas las tesis
            List<Tesis> listaTesis = tesisDAO.listarTodas();
            request.setAttribute("listaTesis", listaTesis);

            // 3. Calcular estadísticas
            int totalUsuarios = (listaUsuarios != null) ? listaUsuarios.size() : 0;
            int totalTesis = (listaTesis != null) ? listaTesis.size() : 0;
            int totalCompletadas = 0; 
            int totalPendientes = 0;  

            if (listaTesis != null) {
                for(Tesis t : listaTesis) {
                    if("complet".equals(t.getEstado())) {
                        totalCompletadas++;
                    } else {
                        totalPendientes++;
                    }
                }
            }

            Map<String, Integer> stats = new HashMap<>();
            stats.put("usuarios", totalUsuarios);
            stats.put("tesis", totalTesis);
            stats.put("completadas", totalCompletadas);
            stats.put("pendientes", totalPendientes);
            
            request.setAttribute("stats", stats);

            // 4. Enviar al JSP
            request.getRequestDispatcher("admin.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error en AdminServlet: " + e.getMessage());
            response.sendRedirect("index.jsp?error=ErrorCargandoAdmin");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}