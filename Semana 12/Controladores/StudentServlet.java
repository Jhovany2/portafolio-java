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

/**
 *
 * @author LENOVO
 */
// CORRECCIÓN: Aseguramos que la URL coincida con la redirección del Login (/StudentServlet)
@WebServlet(name = "StudentServlet", urlPatterns = {"/StudentServlet"})
public class StudentServlet extends HttpServlet {

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
        if (!"estudiante".equals(usuario.getRol())) {
             response.sendRedirect("index.jsp?error=AccesoDenegado");
             return;
        }

        try {
            // 2. Obtener datos de la tesis del estudiante
            TesisDAO dao = new TesisDAO();
            Tesis tesis = dao.obtenerPorEstudiante(usuario.getId());

            // 3. Pasar datos al JSP
            request.setAttribute("tesis", tesis);
            
            // 4. Redirigir al JSP de vista
            request.getRequestDispatcher("student.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=ErrorCargandoEstudiante");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}