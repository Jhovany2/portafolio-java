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
@WebServlet(name = "TesisActualizarServlet", urlPatterns = {"/TesisActualizarServlet"})
public class TesisActualizarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            
            int id = Integer.parseInt(request.getParameter("id"));
            String titulo = request.getParameter("titulo");
            int estudianteId = Integer.parseInt(request.getParameter("estudianteId"));

            TesisDAO dao = new TesisDAO();
            boolean exito = dao.actualizarTesis(id, titulo, estudianteId);

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?msg=TesisActualizada");
            } else {
                response.sendRedirect(request.getContextPath() + "/AdminServlet?error=ErrorAlActualizar");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/AdminServlet?error=ErrorInternoActualizacion");
        }
    }
}