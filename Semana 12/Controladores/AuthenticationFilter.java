/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package Controladores;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author LENOVO
 */
// Intercepta TODAS las peticiones (/*)
@WebFilter(filterName = "AuthenticationFilter", urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        
        // 1. Identificar recursos públicos (que NO requieren login)
        boolean isLoginRequest = uri.endsWith("index.jsp") || uri.endsWith("LoginServlet") || uri.endsWith(".css") || uri.endsWith(".js") || uri.contains("cdn");
        
        // 2. Verificar si hay sesión activa
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("usuario") != null);

        // 3. Lógica de bloqueo
        if (isLoggedIn || isLoginRequest) {
            // Si está logueado O va a una página pública -> Dejar pasar
            chain.doFilter(request, response);
        } else {
            // Si intenta entrar a una página privada sin sesión -> Mandar al Login
            res.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
