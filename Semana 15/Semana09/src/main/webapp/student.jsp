<%-- 
    Document   : student
    Created on : 12 dic. 2025
    Author     : LENOVO
--%>

<%@page import="Modelos.Tesis"%>
<%@page import="Modelos.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // 1. Verificación de Seguridad
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    // 2. Recuperar datos
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    Tesis tesis = (Tesis) request.getAttribute("tesis");
    
    // 3. Manejo de nulos (Objeto vacío si no hay tesis asignada)
    if (tesis == null) {
        tesis = new Tesis();
        tesis.setTitulo("No se encontró información de la tesis");
        tesis.setFechaEntrega("-");
        tesis.setEvaluador("Por asignar");
        tesis.setNotaFinal(0.0);
        tesis.setComentarios("Sin comentarios aún.");
        tesis.setEstado("inicio");
    }
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel del Estudiante</title>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-50 min-h-screen font-sans">

    <!-- ENCABEZADO -->
    <header class="bg-white shadow-sm border-b sticky top-0 z-10">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <div>
                <h1 class="text-xl sm:text-2xl font-semibold text-gray-800">Panel del Estudiante</h1>
                <p class="text-sm text-gray-600">Bienvenido, <%= usuario.getNombre() %></p>
            </div>
            
            <a href="LogoutServlet" class="inline-flex items-center justify-center rounded-md text-sm font-medium border border-slate-200 hover:bg-slate-100 h-9 px-3 transition-colors text-slate-700">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" x2="9" y1="12" y2="12"></line></svg>
                Cerrar Sesión
            </a>
        </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-8 grid gap-6">

        <!-- 1. TARJETA DE ESTADO GENERAL -->
        <div class="rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
            <div class="flex flex-col space-y-1.5 p-6">
                <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                    <div>
                        <h3 class="font-semibold leading-none tracking-tight text-lg">Estado de mi Tesis</h3>
                        <p class="text-sm text-slate-500 mt-1">Información general del proyecto</p>
                    </div>
                    
                    <% if ("complet".equals(tesis.getEstado())) { %>
                        <span class="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold bg-green-500 text-white w-fit">
                            <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-1"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
                            Evaluado
                        </span>
                    <% } else if ("evaluac".equals(tesis.getEstado())) { %>
                         <span class="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold bg-blue-500 text-white w-fit">En Evaluación</span>
                    <% } else { %>
                        <span class="inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold bg-yellow-500 text-white w-fit">Pendiente</span>
                    <% } %>
                </div>
            </div>
            
            <div class="p-6 pt-0 space-y-4">
                <div>
                    <h3 class="text-sm text-gray-500 font-medium uppercase tracking-wider">Título del Proyecto</h3>
                    <p class="mt-1 text-gray-900 font-medium text-lg"><%= tesis.getTitulo() %></p>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 pt-4 border-t border-slate-100">
                    <div>
                         <h3 class="text-sm text-gray-500 font-medium">Fecha de Entrega</h3>
                        <p class="mt-1 flex items-center text-slate-700">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2 text-slate-400"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
                            <%= tesis.getFechaEntrega() %>
                        </p>
                    </div>
                    <div>
                        <h3 class="text-sm text-gray-500 font-medium">Docente Evaluador</h3>
                        <p class="mt-1 text-slate-700"><%= tesis.getEvaluador() %></p>
                    </div>
                    <div>
                        <h3 class="text-sm text-gray-500 font-medium">Última Actualización</h3>
                        <p class="mt-1 text-slate-700">Hoy</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- 2. SECCIÓN DE RESULTADOS -->
        <div class="grid md:grid-cols-3 gap-6">
            
            <div class="md:col-span-2 rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
                <div class="flex flex-col space-y-1.5 p-6">
                    <h3 class="font-semibold leading-none tracking-tight text-lg">Desglose de Calificación</h3>
                    <p class="text-sm text-slate-500">Puntaje obtenido por criterio académico</p>
                </div>
                <div class="p-6 pt-0">
                
                    <div class="space-y-4">
                        <%-- NOTA: Ahora las barras son DINÁMICAS usando los datos reales de la BD --%>
                        
                        <!-- Criterio 1: Originalidad (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                             <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Originalidad y Relevancia</span>
                                <span class="text-xs text-slate-400">Contribución al conocimiento</span>
                             </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota1() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota1() %>/3</span>
                            </div>
                        </div>

                        <!-- Criterio 2: Planteamiento (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                            <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Planteamiento del Problema</span>
                                <span class="text-xs text-slate-400">Objetivos y claridad</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota2() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota2() %>/3</span>
                            </div>
                         </div>

                        <!-- Criterio 3: Metodología (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                            <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Rigor Metodológico</span>
                                <span class="text-xs text-slate-400">Diseño y aplicación</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota3() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota3() %>/3</span>
                             </div>
                        </div>

                        <!-- Criterio 4: Resultados (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                             <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Resultados y Análisis</span>
                                <span class="text-xs text-slate-400">Presentación de hallazgos</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                     <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota4() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota4() %>/3</span>
                            </div>
                        </div>

                        <!-- Criterio 5: Coherencia (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                            <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Coherencia Interna</span>
                                <span class="text-xs text-slate-400">Hipótesis vs Conclusiones</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota5() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota5() %>/3</span>
                            </div>
                        </div>

                        <!-- Criterio 6: Sustento (Max 3) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                            <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Sustento Teórico</span>
                                <span class="text-xs text-slate-400">Bibliografía</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota6() / 3.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota6() %>/3</span>
                            </div>
                        </div>

                        <!-- Criterio 7: Formato (Max 2) -->
                        <div class="flex items-center justify-between p-3 bg-slate-50 rounded-lg">
                            <div class="flex flex-col">
                                <span class="text-slate-700 font-medium text-sm">Formato y Redacción</span>
                                <span class="text-xs text-slate-400">Normas APA</span>
                            </div>
                            <div class="flex items-center gap-3">
                                <div class="hidden sm:block w-24 bg-slate-200 rounded-full h-2 overflow-hidden">
                                    <div class="bg-indigo-600 h-2 rounded-full" style="width: <%= (tesis.getNota7() / 2.0) * 100 %>%"></div>
                                </div>
                                <span class="font-bold text-slate-900 w-8 text-right text-xs"><%= tesis.getNota7() %>/2</span>
                            </div>
                        </div>
                    </div>

                    <!-- SECCIÓN DE COMENTARIOS -->
                    <div class="mt-6 pt-6 border-t border-slate-100">
                        <h3 class="text-sm font-semibold mb-3 text-gray-700 flex items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2 text-indigo-600"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"></path></svg>
                            Comentario del Docente
                        </h3>
                        <div class="bg-blue-50 border-l-4 border-blue-500 p-4 rounded text-sm text-blue-900 leading-relaxed">
                            <%= tesis.getComentarios() %>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 3. TARJETA DE NOTA FINAL -->
            <div class="h-fit rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm sticky top-24">
                <div class="p-6 flex flex-col items-center justify-center text-center space-y-6">
                    <div>
                        <p class="text-sm text-gray-500 mb-2 font-medium uppercase tracking-wide">Calificación Final</p>
                        
                        <div class="inline-flex items-baseline justify-center">
                            <span class="text-6xl font-bold text-indigo-600 tracking-tighter"><%= tesis.getNotaFinal() %></span>
                            <span class="text-gray-400 ml-1 text-xl font-medium">/ 20</span>
                        </div>
                    </div>
                    
                    <!-- Barra de Progreso General -->
                    <div class="w-full bg-slate-100 rounded-full h-2.5">
                        <div class="bg-indigo-600 h-2.5 rounded-full transition-all duration-1000" style="width: <%= (tesis.getNotaFinal() / 20.0) * 100 %>%"></div>
                    </div>
                    
                    <p class="text-xs text-slate-500">
                        Estado: 
                        <% if (tesis.getNotaFinal() >= 13) { %>
                            <span class="font-semibold text-green-600">Aprobado</span>
                        <% } else if (tesis.getNotaFinal() > 0) { %>
                            <span class="font-semibold text-red-600">Observado</span>
                        <% } else { %>
                            <span class="font-semibold text-gray-400">Sin Calificar</span>
                        <% } %>
                    </p>

                    <div class="w-full pt-4 border-t border-slate-100 grid gap-3">
                        <a href="DownloadServlet?id=<%= tesis.getId() %>" class="inline-flex items-center justify-center rounded-md text-sm font-medium bg-indigo-600 text-white hover:bg-indigo-700 h-10 px-4 py-2 w-full transition-colors shadow-sm">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" x2="8" y1="13" y2="13"></line><line x1="16" x2="8" y1="17" y2="17"></line><line x1="10" x2="8" y1="9" y2="9"></line></svg>
                            Descargar Informe PDF
                        </a>
                    </div>
                </div>
            </div>
        </div>

    </main>
</body>
</html>