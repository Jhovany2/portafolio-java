/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.TesisDAO;
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
// CORRECCIÓN: Aseguramos que la URL coincida con la redirección del Login (/TeacherServlet)
// CORRECCIÓN CRÍTICA:
// El urlPattern DEBE ser "/TeacherServlet" (tal cual lo llama EvaluarServlet)
@WebServlet(name = "TeacherServlet", urlPatterns = {"/TeacherServlet"})
public class TeacherServlet extends HttpServlet {

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
        
        // Verificar rol
        if (!"docente".equals(usuario.getRol())) {
             response.sendRedirect("index.jsp?error=AccesoDenegado");
             return;
        }

        try {
            // 2. Obtener lista de tesis asignadas al docente
            TesisDAO dao = new TesisDAO();
            List<Tesis> listaTesis = dao.listarPorEvaluador(usuario.getId());

            // 3. Calcular estadísticas rápidas para las tarjetas
            int total = (listaTesis != null) ? listaTesis.size() : 0;
            int sinEvaluar = 0;
            int completas = 0;

            if (listaTesis != null) {
                for (Tesis t : listaTesis) {
                    if ("complet".equals(t.getEstado())) {
                        completas++;
                    } else {
                        sinEvaluar++;
                    }
                }
            }

            Map<String, Integer> estadisticas = new HashMap<>();
            estadisticas.put("total", total);
            estadisticas.put("sinEvaluar", sinEvaluar);
            estadisticas.put("completas", completas);

            // 4. Pasar datos al JSP
            request.setAttribute("listaTesis", listaTesis);
            request.setAttribute("estadisticas", estadisticas);
            
            // 5. Cargar la vista
            request.getRequestDispatcher("teacher.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=ErrorCargandoDocente");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si por error se llama por POST, redirigir al GET para mostrar la lista
        doGet(request, response);
    }
}