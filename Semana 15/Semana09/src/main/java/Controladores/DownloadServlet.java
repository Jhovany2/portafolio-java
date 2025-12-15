/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

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
@WebServlet(name = "DownloadServlet", urlPatterns = {"/DownloadServlet"})
public class DownloadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String id = request.getParameter("id");
        
        // Configurar la respuesta para que el navegador sepa que es una descarga
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Tesis_Evaluacion_" + id + ".pdf\"");
        
        // SIMULACIÓN: Como no tenemos un PDF real, enviaremos un texto plano disfrazado
        // En un sistema real, aquí leerías el archivo del disco con FileInputStream
        try (PrintWriter out = response.getWriter()) {
            out.println("%PDF-1.4");
            out.println("Este es un archivo simulado para la tesis ID: " + id);
            out.println("En un sistema real, aqui iria el contenido binario del PDF.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
