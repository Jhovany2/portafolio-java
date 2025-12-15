/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.TesisDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "AsignarEvaluadorServlet", urlPatterns = {"/AsignarEvaluadorServlet"})
public class AsignarEvaluadorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Recibir parámetros
            String idTesisStr = request.getParameter("idTesis");
            String idDocenteStr = request.getParameter("idDocente");

            if (idTesisStr == null || idDocenteStr == null) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=DatosFaltantesAsignacion");
                return;
            }

            int idTesis = Integer.parseInt(idTesisStr);
            int idDocente = Integer.parseInt(idDocenteStr);

            // 2. Llamar al DAO
            TesisDAO dao = new TesisDAO();
            boolean exito = dao.asignarEvaluador(idTesis, idDocente);

            // 3. Redirigir
            if (exito) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?msg=EvaluadorAsignado");
            } else {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=ErrorAlAsignar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/AdminServlet?error=ErrorInternoAsignacion");
        }
    }
}