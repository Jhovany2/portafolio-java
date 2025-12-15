/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controladores;

import Datos.TesisDAO;
import Modelos.Evaluacion;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.Instant;

/**
 *
 * @author LENOVO
 */
@WebServlet(name = "EvaluarServlet", urlPatterns = {"/EvaluarServlet"})
public class EvaluarServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Recibir ID de la tesis y comentarios
            String idTesisStr = request.getParameter("idTesis");
            String comentarios = request.getParameter("comentarios");

            // Validación básica
            if (idTesisStr == null || idTesisStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/TeacherServlet?error=IdNulo");
                return;
            }
            int idTesis = Integer.parseInt(idTesisStr);

            // 2. Recibir y procesar las notas (Criterios 1-7)
            // Usamos un método auxiliar para convertir el texto a número sin errores
            int n1 = parseNota(request.getParameter("nota1"));
            int n2 = parseNota(request.getParameter("nota2"));
            int n3 = parseNota(request.getParameter("nota3"));
            int n4 = parseNota(request.getParameter("nota4"));
            int n5 = parseNota(request.getParameter("nota5"));
            int n6 = parseNota(request.getParameter("nota6"));
            int n7 = parseNota(request.getParameter("nota7"));

            // 3. Calcular Nota Final
            // La suma de todos los criterios da la nota sobre 20
            double notaFinal = n1 + n2 + n3 + n4 + n5 + n6 + n7;

            // 4. Crear objeto Evaluación
            Evaluacion ev = new Evaluacion();
            ev.setTesisId(idTesis);
            ev.setNotaOriginalidad(n1);
            ev.setNotaPlanteamiento(n2);
            ev.setNotaMetodologia(n3);
            ev.setNotaResultados(n4);
            ev.setNotaCoherencia(n5);
            ev.setNotaSustento(n6);
            ev.setNotaFormato(n7);
            ev.setComentarios(comentarios);
            ev.setNotaFinal(notaFinal);

            // 5. Guardar en Base de Datos usando el DAO
            TesisDAO dao = new TesisDAO();
            boolean exito = dao.registrarEvaluacion(ev);

            // 6. Redirigir al Panel del Docente
            // IMPORTANTE: Usamos getContextPath() para evitar el error 404
            if (exito) {
                response.sendRedirect(request.getContextPath() + "/TeacherServlet?msg=EvaluacionGuardada");
            } else {
                response.sendRedirect(request.getContextPath() + "/TeacherServlet?error=ErrorAlGuardar");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error inesperado, redirigir al servlet del docente con mensaje de error
            response.sendRedirect(request.getContextPath() + "/TeacherServlet?error=ErrorInterno");
        }
    }

    /**
     * Método auxiliar para convertir un String numérico a int.
     * Si viene nulo o vacío, devuelve 0 para no romper la ejecución.
     */
    private int parseNota(String notaStr) {
        try {
            if (notaStr == null || notaStr.trim().isEmpty()) {
                return 0;
            }
            return Integer.parseInt(notaStr);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}