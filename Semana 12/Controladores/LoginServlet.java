
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
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Recibir credenciales
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            // 2. Validar con la base de datos
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuario = dao.validar(email, password);

            if (usuario != null) {
                // 3. Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                
                // 4. Redirigir según el rol
                String rol = usuario.getRol().toLowerCase(); // Asegurar minúsculas
                String contextPath = request.getContextPath(); // "/Semana09"

                switch (rol) {
                    case "administrador":
                        // Corrección clave: Redirige a AdminServlet, no a 'admin'
                        response.sendRedirect(contextPath + "/AdminServlet");
                        break;
                    case "docente":
                        response.sendRedirect(contextPath + "/TeacherServlet");
                        break;
                    case "estudiante":
                        response.sendRedirect(contextPath + "/StudentServlet");
                        break;
                    default:
                        // Rol desconocido
                        response.sendRedirect(contextPath + "/index.jsp?error=RolDesconocido");
                        break;
                }
            } else {
                // Credenciales incorrectas
                request.setAttribute("error", "Credenciales incorrectas");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=ErrorInterno");
        }
    }
}