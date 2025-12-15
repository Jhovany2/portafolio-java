/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.UsuarioDAO;
import Modelos.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "UsuarioAdminServlet", urlPatterns = {"/UsuarioAdminServlet"})
public class UsuarioAdminServlet extends HttpServlet {

    // CAMBIA ESTO POR TU DOMINIO REAL
    private static final String DOMINIO_PERMITIDO = "@system.edu.pe"; 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        try {
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String rol = request.getParameter("rol");

            // 1. Validación de campos vacíos
            if (nombre == null || email == null || password == null || nombre.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=DatosIncompletos");
                return;
            }

            // 2. NUEVO: Validación de Dominio de Correo
            if (!email.toLowerCase().endsWith(DOMINIO_PERMITIDO.toLowerCase())) {
                String mensajeError = "El correo debe terminar en " + DOMINIO_PERMITIDO;
                // Codificamos el mensaje para que se vea bien en la URL
                String errorEncoded = URLEncoder.encode(mensajeError, "UTF-8");
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=" + errorEncoded);
                return; // Importante: Detenemos la ejecución aquí
            }

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setEmail(email);
            u.setRol(rol);
            
            UsuarioDAO dao = new UsuarioDAO();
            
            // Intentamos registrar
            boolean exito = dao.registrar(u, password);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?msg=UsuarioCreado"); 
            } else {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=NoSeGuardaronCambios");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            String msjError = "ErrorSQL: " + e.getMessage();
            String errorEncoded = URLEncoder.encode(msjError, "UTF-8");
            response.sendRedirect(request.getContextPath() + "/AdminServlet?error=" + errorEncoded);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/AdminServlet?error=ErrorInterno");
        }
    }
}