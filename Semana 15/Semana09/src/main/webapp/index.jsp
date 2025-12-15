<%-- 
    Document   : index
    Created on : 12 dic. 2025, 11:09:01 p. m.
    Author     : LENOVO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - Sistema de Tesis</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="min-h-screen w-full bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4 font-sans">
    <div class="w-full max-w-md rounded-xl border border-slate-200 bg-white text-slate-950 shadow-lg overflow-hidden">
        <div class="flex flex-col space-y-1.5 p-6 text-center">
            <div class="flex justify-center mb-4">
                <div class="bg-indigo-600 p-3 rounded-full shadow-md">
                    <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 10v6M2 10l10-5 10 5-10 5z"></path><path d="M6 12v5c3 3 9 3 12 0v-5"></path></svg>
                </div>
            </div>
            <h3 class="font-semibold tracking-tight text-3xl text-gray-900">Sistema de Gestión de Tesis</h3>
            <p class="text-sm text-slate-500">Ingrese sus credenciales para acceder</p>
        </div>
        <div class="p-6 pt-0">
            <form action="LoginServlet" method="POST" class="space-y-4">
                <div class="space-y-2">
                    <label class="text-sm font-medium leading-none text-gray-700">Correo Electrónico</label>
                    <input type="email" name="email" placeholder="usuario@universidad.edu" required class="flex h-10 w-full rounded-md border border-slate-300 bg-transparent px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-600">
                </div>
                <div class="space-y-2">
                    <label class="text-sm font-medium leading-none text-gray-700">Contraseña</label>
                    <input type="password" name="password" placeholder="••••••••" required class="flex h-10 w-full rounded-md border border-slate-300 bg-transparent px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-600">
                </div>
                <% if (error != null) { %>
                    <div class="bg-red-50 text-red-600 p-3 rounded-md text-sm border border-red-200 flex items-center gap-2">
                        <span>⚠️ <%= error %></span>
                    </div>
                <% } %>
                <button type="submit" class="inline-flex items-center justify-center rounded-md text-sm font-medium bg-indigo-600 text-white hover:bg-indigo-700 h-10 px-4 py-2 w-full shadow-sm">Iniciar Sesión</button>
            </form>
        </div>
    </div>
</body>
</html>