/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.TesisDAO;
import Modelos.Tesis;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "TesisAdminServlet", urlPatterns = {"/TesisAdminServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB (Tamaño máximo del PDF)
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class TesisAdminServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Recibir campos de texto
            String titulo = request.getParameter("titulo");
            String estudianteIdStr = request.getParameter("estudianteId");
            
            if (estudianteIdStr == null || estudianteIdStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/admin.jsp?error=FaltaEstudianteID");
                return;
            }
            int estudianteId = Integer.parseInt(estudianteIdStr);

            // 2. Recibir el archivo PDF
            Part filePart = request.getPart("archivo");
            
            // Validar que se haya subido un archivo
            if (filePart == null || filePart.getSize() == 0) {
                response.sendRedirect(request.getContextPath() + "/admin.jsp?error=ArchivoVacio");
                return;
            }

            // 3. Obtener nombre del archivo original
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            // Generar nombre único para evitar que se sobrescriban (ej: 1715629_tesis.pdf)
            String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

            // 4. Definir ruta de guardado
            // Se guardará en una carpeta 'archivos' dentro de la aplicación desplegada
            String uploadPath = getServletContext().getRealPath("") + File.separator + "archivos";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Crea la carpeta si no existe
            }

            // 5. Guardar el archivo físico en el disco
            String fullPath = uploadPath + File.separator + uniqueFileName;
            filePart.write(fullPath);
            System.out.println("--> Archivo subido a: " + fullPath);

            // 6. Guardar metadatos en la Base de Datos
            Tesis t = new Tesis();
            t.setTitulo(titulo);
            t.setEstudianteId(estudianteId);
            t.setArchivoRuta(uniqueFileName); // Guardamos solo el nombre para linkearlo después
            
            TesisDAO dao = new TesisDAO();
            boolean exito = dao.registrarTesis(t);

            if (exito) {
                // CORRECCIÓN: Usar getContextPath() asegura que la redirección apunte a /Semana09/AdminServlet
                response.sendRedirect(request.getContextPath() + "/AdminServlet"); 
            } else {
                response.sendRedirect(request.getContextPath() + "/admin.jsp?error=ErrorBaseDatos");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error en TesisAdminServlet: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin.jsp?error=ErrorInterno");
        }
    }
}