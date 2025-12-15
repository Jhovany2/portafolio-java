<%-- 
    Document   : teacher
    Created on : 13 dic. 2025, 12:52:38 a. m.
    Author     : LENOVO
--%>

<%@page import="java.util.List"%>
<%@page import="Modelos.Tesis"%>
<%@page import="Modelos.Usuario"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // 1. Verificamos sesión: Si no hay usuario, mandamos al login
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    // 2. Recuperamos los objetos enviados por el Servlet
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("estadisticas");
    List<Tesis> lista = (List<Tesis>) request.getAttribute("listaTesis");
    
    // 3. Valores por defecto para evitar errores si los datos son nulos
    int total = stats != null && stats.get("total") != null ? stats.get("total") : 0;
    int sinEvaluar = stats != null && stats.get("sinEvaluar") != null ? stats.get("sinEvaluar") : 0;
    int completas = stats != null && stats.get("completas") != null ? stats.get("completas") : 0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel del Docente</title>
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        // --- Funciones para el Modal ---
        function abrirModal(id, titulo) {
            document.getElementById('modalEvaluacion').classList.remove('hidden');
            document.getElementById('tesisIdInput').value = id;
            document.getElementById('tituloTesisModal').innerText = titulo;
        }

        function cerrarModal() {
            document.getElementById('modalEvaluacion').classList.add('hidden');
        }

        // --- Lógica de Filtrado en Cliente ---
        function filtrarTesis(filtro, botonId) {
            const tarjetas = document.querySelectorAll('.tesis-card');
            const botones = document.querySelectorAll('.filtro-btn');
            const badges = document.querySelectorAll('.status-badge');

            // 1. Actualizar estado visual de los botones
            botones.forEach(btn => {
                if (btn.id === botonId) {
                    btn.classList.add('bg-white', 'text-slate-950', 'shadow-sm');
                    btn.classList.remove('text-slate-500', 'hover:text-slate-900');
                } else {
                    btn.classList.remove('bg-white', 'text-slate-950', 'shadow-sm');
                    btn.classList.add('text-slate-500', 'hover:text-slate-900');
                }
            });

            // 2. Mostrar/Ocultar tarjetas según el filtro
            tarjetas.forEach(card => {
                const estado = card.getAttribute('data-estado'); // 'inicio', 'evaluac', 'complet'
                
                if (filtro === 'todas') {
                    card.style.display = 'flex';
                } else if (filtro === 'sin_evaluar') {
                    // Mostrar si es 'inicio' o 'evaluac'
                    if (estado === 'inicio' || estado === 'evaluac') {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                } else if (filtro === 'evaluadas') {
                    // Mostrar solo si es 'complet'
                    if (estado === 'complet') {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                }
            });

            // 3. Controlar visibilidad de las etiquetas (badges)
            // Solo se muestran cuando vemos "Todas" para diferenciar visualmente
            badges.forEach(badge => {
                if (filtro === 'todas') {
                    badge.style.display = 'inline-flex';
                } else {
                    badge.style.display = 'none';
                }
            });
        }
    </script>
</head>
<body class="bg-gray-50 min-h-screen font-sans pb-10">

    <!-- ENCABEZADO -->
    <header class="bg-white shadow-sm border-b sticky top-0 z-10">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <div>
                <h1 class="text-xl sm:text-2xl font-semibold text-gray-800">Panel del Docente</h1>
                <p class="text-sm text-gray-600">Bienvenido, <%= usuario.getNombre() %></p>
            </div>
            <!-- Botón Logout -->
            <a href="LogoutServlet" class="inline-flex items-center justify-center rounded-md text-sm font-medium border border-slate-200 hover:bg-slate-100 h-9 px-3 transition-colors text-slate-700">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" x2="9" y1="12" y2="12"></line></svg>
                Cerrar Sesión
            </a>
        </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-8 space-y-6">
        
        <!-- TARJETAS DE ESTADÍSTICAS -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
                <div class="p-6 pt-6 text-center">
                    <p class="text-3xl font-bold text-indigo-600"><%= total %></p>
                    <p class="text-sm text-gray-500 mt-1">Total Asignadas</p>
                </div>
            </div>
            <div class="rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
                <div class="p-6 pt-6 text-center">
                    <p class="text-3xl font-bold text-yellow-600"><%= sinEvaluar %></p>
                    <p class="text-sm text-gray-500 mt-1">Sin Evaluar</p>
                </div>
            </div>
            <div class="rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
                <div class="p-6 pt-6 text-center">
                    <p class="text-3xl font-bold text-green-600"><%= completas %></p>
                    <p class="text-sm text-gray-500 mt-1">Completadas</p>
                </div>
            </div>
        </div>

        <!-- LISTA DE TESIS -->
        <div class="rounded-xl border border-slate-200 bg-white text-slate-950 shadow-sm">
            <div class="flex flex-col space-y-1.5 p-6">
                <h3 class="font-semibold leading-none tracking-tight text-lg">Tesis Asignadas</h3>
            </div>
            <div class="p-6 pt-0">
                
                <!-- BOTONES DE FILTRO -->
                <div class="inline-flex h-10 items-center justify-center rounded-md bg-slate-100 p-1 text-slate-500 mb-4 w-full sm:w-auto">
                    <button id="btn-todas" onclick="filtrarTesis('todas', 'btn-todas')" class="filtro-btn inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all bg-white text-slate-950 shadow-sm">Todas</button>
                    <button id="btn-sin-evaluar" onclick="filtrarTesis('sin_evaluar', 'btn-sin-evaluar')" class="filtro-btn inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all text-slate-500 hover:text-slate-900">Sin Evaluar</button>
                    <button id="btn-evaluadas" onclick="filtrarTesis('evaluadas', 'btn-evaluadas')" class="filtro-btn inline-flex items-center justify-center whitespace-nowrap rounded-sm px-3 py-1.5 text-sm font-medium ring-offset-background transition-all text-slate-500 hover:text-slate-900">Evaluadas</button>
                </div>

                <div class="space-y-3">
                    <% 
                        if (lista != null && !lista.isEmpty()) {
                            for (Tesis t : lista) {
                    %>
                        <!-- Tarjeta Individual de Tesis -->
                        <div class="tesis-card flex flex-col sm:flex-row items-start sm:items-center justify-between p-4 bg-white border rounded-lg hover:shadow-md transition-shadow gap-4" data-estado="<%= t.getEstado() %>">
                            <div class="flex items-start gap-3">
                                <div class="p-2 bg-slate-100 rounded text-slate-500">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"></path><polyline points="14 2 14 8 20 8"></polyline><line x1="16" x2="8" y1="13" y2="13"></line><line x1="16" x2="8" y1="17" y2="17"></line><line x1="10" x2="8" y1="9" y2="9"></line></svg>
                                </div>
                                <div>
                                    <h3 class="font-medium text-gray-900"><%= t.getTitulo() %></h3>
                                    <div class="text-sm text-gray-500 flex flex-col sm:flex-row gap-1 sm:gap-4 mt-1">
                                        <span class="flex items-center">Estudiante: Estudiante Demo</span>
                                        <span class="flex items-center">Fecha: <%= t.getFechaEntrega() %></span>
                                    </div>
                                </div>
                            </div>
                            <div class="flex items-center gap-3 w-full sm:w-auto justify-between sm:justify-end">
                                <!-- BADGES (Controlados por JS) -->
                                <% 
                                    String estado = t.getEstado();
                                    if ("inicio".equals(estado) || "evaluac".equals(estado)) { 
                                %>
                                    <span class="status-badge inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold bg-yellow-500 text-white">Sin Evaluar</span>
                                <% } else if ("complet".equals(estado)) { %>
                                    <span class="status-badge inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold bg-green-500 text-white">Completado</span>
                                <% } %>

                                <!-- BOTÓN DE ACCIÓN -->
                                <button onclick="abrirModal('<%= t.getId() %>', '<%= t.getTitulo() %>')" class="inline-flex items-center justify-center rounded-md text-sm font-medium bg-slate-900 text-white hover:bg-slate-800 h-9 px-3 transition-colors">
                                    <%= "complet".equals(estado) ? "Ver" : "Evaluar" %>
                                </button>
                            </div>
                        </div>
                    <% 
                            } 
                        } else {
                    %>
                        <div class="text-center py-10 text-gray-500">No hay tesis asignadas.</div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL DE EVALUACIÓN (7 CRITERIOS) -->
    <div id="modalEvaluacion" class="hidden fixed inset-0 z-50 flex items-center justify-center">
        <!-- Fondo Oscuro -->
        <div class="fixed inset-0 bg-black/50 transition-opacity" onclick="cerrarModal()"></div>
        
        <!-- Contenido Modal -->
        <div class="relative z-50 w-full max-w-2xl rounded-lg border bg-white p-0 shadow-lg flex flex-col max-h-[90vh] animate-[fade-in_0.2s_ease-out]">
            <div class="p-6 pb-2 border-b">
                <h2 class="text-xl font-bold leading-none tracking-tight text-slate-900">Rúbrica de Evaluación de Tesis</h2>
                <p id="tituloTesisModal" class="text-sm text-slate-500 mt-2 truncate">Título...</p>
            </div>
            
            <div class="p-6 pt-4 overflow-y-auto flex-1">
                <form action="EvaluarServlet" method="POST" id="formEvaluacion">
                    <input type="hidden" id="tesisIdInput" name="idTesis" value="">
                    
                    <div class="space-y-6">
                        <!-- 1. Originalidad -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">1. Originalidad y Relevancia</label><input type="number" name="nota1" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">¿El trabajo hace una contribución original? ¿Es el tema significativo?</p>
                        </div>
                        <!-- 2. Planteamiento -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">2. Planteamiento del Problema</label><input type="number" name="nota2" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">¿Problema claramente definido y objetivos lógicos?</p>
                        </div>
                        <!-- 3. Metodología -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">3. Rigor Metodológico</label><input type="number" name="nota3" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">¿Diseño, población, muestra e instrumentos apropiados?</p>
                        </div>
                        <!-- 4. Resultados -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">4. Resultados y Análisis</label><input type="number" name="nota4" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">Presentación objetiva y clara de hallazgos.</p>
                        </div>
                        <!-- 5. Coherencia -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">5. Coherencia Interna</label><input type="number" name="nota5" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">¿Conclusiones derivadas directamente de resultados?</p>
                        </div>
                        <!-- 6. Sustento -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">6. Sustento Teórico</label><input type="number" name="nota6" min="0" max="3" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-3"></div>
                            <p class="text-xs text-slate-500 text-justify">Profundidad, actualidad y uso de fuentes confiables.</p>
                        </div>
                        <!-- 7. Formato -->
                        <div class="space-y-2 pb-4 border-b border-slate-100">
                            <div class="flex justify-between items-center"><label class="text-sm font-bold text-slate-800">7. Formato y Redacción</label><input type="number" name="nota7" min="0" max="2" class="w-16 h-8 rounded-md border border-slate-300 px-2 text-sm text-center focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="0-2"></div>
                            <p class="text-xs text-slate-500 text-justify">Ortografía, redacción académica y normas APA.</p>
                        </div>

                        <div class="space-y-2">
                            <label class="text-sm font-bold text-slate-800">Comentarios Generales</label>
                            <textarea name="comentarios" rows="3" class="flex w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none" placeholder="Ingrese feedback detallado..."></textarea>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Footer del Modal -->
            <div class="p-4 border-t bg-gray-50 rounded-b-lg flex justify-between items-center">
                <span class="text-xs text-slate-500">Puntaje Máximo: 20</span>
                <div class="flex gap-2">
                    <button type="button" onclick="cerrarModal()" class="rounded-md border bg-white px-4 py-2 text-sm hover:bg-slate-50 font-medium text-slate-700 shadow-sm">Cancelar</button>
                    <button type="submit" form="formEvaluacion" class="rounded-md bg-indigo-600 text-white px-4 py-2 text-sm hover:bg-indigo-700 font-medium shadow-sm flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-2"><line x1="22" y1="2" x2="11" y2="13"></line><polygon points="22 2 15 22 11 13 2 9 22 2"></polygon></svg>
                        Guardar Evaluación
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>