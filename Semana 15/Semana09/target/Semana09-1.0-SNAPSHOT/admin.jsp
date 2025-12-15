<%-- 
    Document   : admin
    Created on : 13 dic. 2025, 9:13:17 p. m.
    Author     : LENOVO
--%>

<%@page import="java.util.List"%>
<%@page import="Modelos.Usuario"%>
<%@page import="Modelos.Tesis"%> 
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    // 1. Verificamos sesión
    if (session.getAttribute("usuario") == null) {
        response.sendRedirect("index.jsp");
        return;
    }

    // 2. Recuperamos los objetos
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("listaUsuarios");
    List<Tesis> listaTesis = (List<Tesis>) request.getAttribute("listaTesis"); 
    List<Usuario> listaDocentes = (List<Usuario>) request.getAttribute("listaDocentes");
    
    Map<String, Integer> stats = (Map<String, Integer>) request.getAttribute("stats");
    String error = request.getParameter("error");
    String msg = request.getParameter("msg");

    // 3. Valores por defecto
    int totalUsuarios = stats != null && stats.get("usuarios") != null ? stats.get("usuarios") : 0;
    int totalTesis = stats != null && stats.get("tesis") != null ? stats.get("tesis") : 0;
    int totalCompletadas = stats != null && stats.get("completadas") != null ? stats.get("completadas") : 0;
    int totalPendientes = stats != null && stats.get("pendientes") != null ? stats.get("pendientes") : 0;
%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        // --- LOGICA DE PESTAÑAS Y MODALES ---
        function openTab(tabName, btnId) {
            document.querySelectorAll('.tab-content').forEach(el => el.classList.add('hidden'));
            document.querySelectorAll('.tab-btn').forEach(btn => {
                btn.classList.remove('bg-white', 'text-indigo-600', 'shadow-sm');
                btn.classList.add('text-slate-500', 'hover:text-slate-700');
            });
            document.getElementById(tabName).classList.remove('hidden');
            document.getElementById(btnId).classList.add('bg-white', 'text-indigo-600', 'shadow-sm');
            document.getElementById(btnId).classList.remove('text-slate-500', 'hover:text-slate-700');
        }
        function openUserModal() { document.getElementById('userModal').classList.remove('hidden'); }
        function closeUserModal() { document.getElementById('userModal').classList.add('hidden'); }
        function openThesisModal() { document.getElementById('thesisModal').classList.remove('hidden'); }
        function closeThesisModal() { document.getElementById('thesisModal').classList.add('hidden'); }
        
        // Modal de Edición de Tesis
        function openEditThesisModal(id, titulo, estudianteId, nombreEstudiante) {
            document.getElementById('editThesisModal').classList.remove('hidden');
            document.getElementById('edit-id').value = id;
            document.getElementById('edit-titulo').value = titulo;
            document.getElementById('input-hidden-estudiante-edit').value = estudianteId;
            document.getElementById('input-search-estudiante-edit').value = nombreEstudiante;
        }
        function closeEditThesisModal() { document.getElementById('editThesisModal').classList.add('hidden'); }

        // --- FILTRO DE USUARIOS POR ROL ---
        function filtrarUsuarios(rol) {
            const filas = document.querySelectorAll('.fila-usuario');
            const botones = document.querySelectorAll('.btn-filtro-rol');
            
            // Actualizar estilo de botones
            botones.forEach(btn => {
                if(btn.dataset.rol === rol) {
                    btn.classList.add('bg-indigo-100', 'text-indigo-700', 'font-semibold');
                    btn.classList.remove('text-slate-500', 'hover:text-slate-700');
                } else {
                    btn.classList.remove('bg-indigo-100', 'text-indigo-700', 'font-semibold');
                    btn.classList.add('text-slate-500', 'hover:text-slate-700');
                }
            });

            // Mostrar/Ocultar filas
            filas.forEach(fila => {
                const rolFila = fila.dataset.rol.toLowerCase(); // 'administrador', 'docente', 'estudiante'
                if (rol === 'todos' || rolFila === rol) {
                    fila.style.display = '';
                } else {
                    fila.style.display = 'none';
                }
            });
        }

        // ==========================================
        // LÓGICA DE BUSCADORES (DOCENTES Y ESTUDIANTES)
        // ==========================================
        
        // 1. Docentes
        const docentesDisponibles = [
            <% if (listaDocentes != null) { 
                for (Usuario d : listaDocentes) { %>
                { id: "<%= d.getId() %>", nombre: "<%= d.getNombre() %>" },
            <%  } 
               } %>
        ];

        function mostrarDropdown(tesisId) {
            document.querySelectorAll('[id^="dropdown-list-"]').forEach(el => el.classList.add('hidden'));
            const list = document.getElementById('dropdown-list-' + tesisId);
            list.classList.remove('hidden');
            renderizarLista(tesisId, docentesDisponibles);
        }

        function filtrarDocentes(input, tesisId) {
            const texto = input.value.toLowerCase();
            const filtrados = docentesDisponibles.filter(d => d.nombre.toLowerCase().includes(texto));
            renderizarLista(tesisId, filtrados);
        }

        function renderizarLista(tesisId, lista) {
            const container = document.getElementById('dropdown-list-' + tesisId);
            container.innerHTML = ''; 
            if (lista.length === 0) {
                container.innerHTML = '<div class="p-2 text-xs text-gray-500">No hay coincidencias</div>';
                return;
            }
            lista.forEach(docente => {
                const div = document.createElement('div');
                div.className = "p-2 hover:bg-indigo-50 cursor-pointer text-sm text-gray-700";
                div.innerText = docente.nombre;
                div.onclick = function() {
                    seleccionarDocente(tesisId, docente.id, docente.nombre);
                };
                container.appendChild(div);
            });
        }

        function seleccionarDocente(tesisId, id, nombre) {
            document.getElementById('input-search-' + tesisId).value = nombre;
            document.getElementById('input-hidden-' + tesisId).value = id;
            document.getElementById('dropdown-list-' + tesisId).classList.add('hidden');
        }

        // 2. Estudiantes
        const estudiantesDisponibles = [
            <% 
            if (listaUsuarios != null) { 
                for (Usuario u : listaUsuarios) { 
                    if ("estudiante".equalsIgnoreCase(u.getRol())) {
            %>
                { id: "<%= u.getId() %>", nombre: "<%= u.getNombre() %>" },
            <%      }
                } 
            } 
            %>
        ];

        function mostrarDropdownEstudiantes() {
            const list = document.getElementById('dropdown-list-estudiantes');
            list.classList.remove('hidden');
            renderizarListaEstudiantes(estudiantesDisponibles, 'dropdown-list-estudiantes', 'input-search-estudiante', 'input-hidden-estudiante');
        }
        function filtrarEstudiantes(input) {
            const texto = input.value.toLowerCase();
            const filtrados = estudiantesDisponibles.filter(e => e.nombre.toLowerCase().includes(texto));
            renderizarListaEstudiantes(filtrados, 'dropdown-list-estudiantes', 'input-search-estudiante', 'input-hidden-estudiante');
        }

        function mostrarDropdownEstudiantesEdit() {
            const list = document.getElementById('dropdown-list-estudiantes-edit');
            list.classList.remove('hidden');
            renderizarListaEstudiantes(estudiantesDisponibles, 'dropdown-list-estudiantes-edit', 'input-search-estudiante-edit', 'input-hidden-estudiante-edit');
        }
        function filtrarEstudiantesEdit(input) {
            const texto = input.value.toLowerCase();
            const filtrados = estudiantesDisponibles.filter(e => e.nombre.toLowerCase().includes(texto));
            renderizarListaEstudiantes(filtrados, 'dropdown-list-estudiantes-edit', 'input-search-estudiante-edit', 'input-hidden-estudiante-edit');
        }

        function renderizarListaEstudiantes(lista, listId, inputSearchId, inputHiddenId) {
            const container = document.getElementById(listId);
            container.innerHTML = '';
            if (lista.length === 0) {
                container.innerHTML = '<div class="p-2 text-xs text-gray-500">No hay coincidencias</div>';
                return;
            }
            lista.forEach(est => {
                const div = document.createElement('div');
                div.className = "p-2 hover:bg-indigo-50 cursor-pointer text-sm text-gray-700";
                div.innerText = est.nombre;
                div.onclick = function() {
                    document.getElementById(inputSearchId).value = est.nombre;
                    document.getElementById(inputHiddenId).value = est.id;
                    container.classList.add('hidden');
                };
                container.appendChild(div);
            });
        }

        document.addEventListener('click', function(e) {
            if (!e.target.closest('.search-container')) {
                document.querySelectorAll('[id^="dropdown-list-"]').forEach(el => {
                    if (!el.id.includes('estudiantes')) el.classList.add('hidden');
                });
            }
            if (!e.target.closest('.search-container-estudiante')) {
                const el = document.getElementById('dropdown-list-estudiantes');
                if(el) el.classList.add('hidden');
            }
            if (!e.target.closest('.search-container-estudiante-edit')) {
                const el = document.getElementById('dropdown-list-estudiantes-edit');
                if(el) el.classList.add('hidden');
            }
        });
    </script>
</head>
<body class="bg-gray-50 min-h-screen font-sans pb-10">

    <header class="bg-white shadow-sm border-b sticky top-0 z-10">
        <div class="max-w-7xl mx-auto px-4 py-4 flex justify-between items-center">
            <div>
                <h1 class="text-xl sm:text-2xl font-semibold text-gray-800">Panel de Administración</h1>
                <p class="text-sm text-gray-600">Bienvenido, <%= usuario.getNombre() %></p>
            </div>
            <a href="LogoutServlet" class="inline-flex items-center justify-center rounded-md text-sm font-medium border border-slate-200 hover:bg-slate-100 h-9 px-3 transition-colors text-slate-700">
                Cerrar Sesión
            </a>
        </div>
    </header>

    <main class="max-w-7xl mx-auto px-4 py-8 space-y-6">
        
        <!-- Alertas -->
        <% if (error != null) { %>
            <div class="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded relative mb-4">
                <strong class="font-bold">¡Error!</strong> <span class="block sm:inline"><%= error %></span>
            </div>
        <% } %>
        <% if (msg != null) { %>
            <div class="bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded relative mb-4">
                <strong class="font-bold">¡Éxito!</strong> <span class="block sm:inline">Operación realizada correctamente.</span>
            </div>
        <% } %>

        <!-- ESTADÍSTICAS -->
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div class="rounded-xl border border-slate-200 bg-white p-6 text-center shadow-sm">
                <p class="text-3xl font-bold text-indigo-600"><%= totalUsuarios %></p>
                <p class="text-sm text-slate-500">Usuarios Totales</p>
            </div>
            <div class="rounded-xl border border-slate-200 bg-white p-6 text-center shadow-sm">
                <p class="text-3xl font-bold text-blue-600"><%= totalTesis %></p>
                <p class="text-sm text-slate-500">Tesis Registradas</p>
            </div>
            <div class="rounded-xl border border-slate-200 bg-white p-6 text-center shadow-sm">
                <p class="text-3xl font-bold text-green-600"><%= totalCompletadas %></p>
                <p class="text-sm text-slate-500">Tesis Evaluadas</p>
            </div>
            <div class="rounded-xl border border-slate-200 bg-white p-6 text-center shadow-sm">
                <p class="text-3xl font-bold text-yellow-600"><%= totalPendientes %></p>
                <p class="text-sm text-slate-500">Pendientes</p>
            </div>
        </div>

        <!-- CONTENEDOR PRINCIPAL -->
        <div class="rounded-xl border border-slate-200 bg-white shadow-sm overflow-hidden">
            <div class="border-b border-slate-200 bg-slate-50 px-6 py-3 flex items-center justify-between">
                <h2 class="font-semibold text-lg text-slate-800">Gestión del Sistema</h2>
            </div>
            
            <div class="p-6">
                <!-- PESTAÑAS -->
                <div class="flex space-x-1 rounded-xl bg-slate-100 p-1 mb-6">
                    <button id="btn-users" onclick="openTab('tab-users', 'btn-users')" class="tab-btn w-full rounded-lg py-2.5 text-sm font-medium bg-white text-indigo-600 shadow-sm">Usuarios</button>
                    <button id="btn-tesis" onclick="openTab('tab-tesis', 'btn-tesis')" class="tab-btn w-full rounded-lg py-2.5 text-sm font-medium text-slate-500 hover:text-slate-700">Tesis</button>
                    <button id="btn-asignaciones" onclick="openTab('tab-asignaciones', 'btn-asignaciones')" class="tab-btn w-full rounded-lg py-2.5 text-sm font-medium text-slate-500 hover:text-slate-700">Asignaciones</button>
                </div>

                <!-- 1. PESTAÑA USUARIOS (CON FILTRO DE ROLES) -->
                <div id="tab-users" class="tab-content">
                    <div class="flex justify-between items-center mb-4">
                        <h3 class="text-lg font-medium text-slate-900">Lista de Usuarios</h3>
                        <div class="flex gap-2">
                            <!-- NUEVOS BOTONES DE FILTRO -->
                            <button onclick="filtrarUsuarios('todos')" data-rol="todos" class="btn-filtro-rol px-3 py-1 text-sm rounded-md bg-indigo-100 text-indigo-700 font-semibold transition-colors">Todos</button>
                            <button onclick="filtrarUsuarios('administrador')" data-rol="administrador" class="btn-filtro-rol px-3 py-1 text-sm rounded-md text-slate-500 hover:text-slate-700 hover:bg-slate-100 transition-colors">Admin</button>
                            <button onclick="filtrarUsuarios('docente')" data-rol="docente" class="btn-filtro-rol px-3 py-1 text-sm rounded-md text-slate-500 hover:text-slate-700 hover:bg-slate-100 transition-colors">Docentes</button>
                            <button onclick="filtrarUsuarios('estudiante')" data-rol="estudiante" class="btn-filtro-rol px-3 py-1 text-sm rounded-md text-slate-500 hover:text-slate-700 hover:bg-slate-100 transition-colors">Estudiantes</button>
                            <!-- BOTÓN CREAR -->
                            <button onclick="openUserModal()" class="inline-flex items-center justify-center rounded-md text-sm font-medium bg-indigo-600 text-white hover:bg-indigo-700 h-8 px-4 shadow-sm ml-2">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="mr-1"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg> Nuevo
                            </button>
                        </div>
                    </div>
                    <div class="overflow-x-auto rounded-lg border border-slate-200">
                        <table class="w-full text-sm text-left">
                            <thead class="text-xs text-slate-500 uppercase bg-slate-50">
                                <tr>
                                    <th class="px-4 py-3 font-medium">Nombre</th>
                                    <th class="px-4 py-3 font-medium">Email</th>
                                    <th class="px-4 py-3 font-medium">Rol</th>
                                </tr>
                            </thead>
                            <tbody class="bg-white">
                                <% if (listaUsuarios != null) {
                                    for (Usuario u : listaUsuarios) {
                                        String rolClass = "bg-blue-100 text-blue-800";
                                        if ("administrador".equalsIgnoreCase(u.getRol())) rolClass = "bg-purple-100 text-purple-800";
                                        else if ("docente".equalsIgnoreCase(u.getRol())) rolClass = "bg-green-100 text-green-800";
                                %>
                                    <!-- AÑADIMOS data-rol y class="fila-usuario" PARA EL FILTRO -->
                                    <tr class="fila-usuario border-b border-slate-100 hover:bg-slate-50" data-rol="<%= u.getRol().toLowerCase() %>">
                                        <td class="px-4 py-3 font-medium text-slate-900"><%= u.getNombre() %></td>
                                        <td class="px-4 py-3 text-slate-500"><%= u.getEmail() %></td>
                                        <td class="px-4 py-3"><span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium <%= rolClass %>"><%= u.getRol() %></span></td>
                                    </tr>
                                <% }} else { %>
                                    <tr><td colspan="3" class="px-4 py-3 text-center text-slate-500">No hay usuarios cargados.</td></tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>

                <!-- 2. PESTAÑA TESIS (Con opción Editar) -->
                <div id="tab-tesis" class="tab-content hidden">
                    <div class="flex justify-between items-center mb-4">
                        <h3 class="text-lg font-medium text-slate-900">Repositorio de Tesis</h3>
                        <button onclick="openThesisModal()" class="inline-flex items-center justify-center rounded-md text-sm font-medium border border-slate-200 bg-white hover:bg-slate-50 h-9 px-4 shadow-sm text-slate-900">Subir Tesis</button>
                    </div>
                    
                    <% if (listaTesis == null || listaTesis.isEmpty()) { %>
                        <div class="text-center py-12 border-2 border-dashed border-slate-200 rounded-lg bg-slate-50">
                            <p class="text-slate-500 text-sm">No hay tesis registradas. ¡Sube la primera!</p>
                        </div>
                    <% } else { %>
                        <div class="overflow-x-auto rounded-lg border border-slate-200">
                            <table class="w-full text-sm text-left">
                                <thead class="text-xs text-slate-500 uppercase bg-slate-50">
                                    <tr>
                                        <th class="px-4 py-3 font-medium">Título</th>
                                        <th class="px-4 py-3 font-medium">Estudiante</th>
                                        <th class="px-4 py-3 font-medium">Estado</th>
                                        <th class="px-4 py-3 font-medium text-right">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody class="bg-white">
                                    <% for (Tesis t : listaTesis) { 
                                        String estadoClass = "bg-yellow-100 text-yellow-800";
                                        String estadoTexto = "Pendiente";
                                        if ("complet".equals(t.getEstado())) {
                                            estadoClass = "bg-green-100 text-green-800";
                                            estadoTexto = "Evaluado";
                                        } else if ("evaluac".equals(t.getEstado())) {
                                            estadoClass = "bg-blue-100 text-blue-800";
                                            estadoTexto = "En Evaluación";
                                        }
                                        String nombreEstudiante = (t.getEvaluador() != null) ? t.getEvaluador() : "ID: " + t.getEstudianteId();
                                    %>
                                        <tr class="border-b border-slate-100 hover:bg-slate-50">
                                            <td class="px-4 py-3 font-medium text-slate-900"><%= t.getTitulo() %></td>
                                            <td class="px-4 py-3 text-slate-500"><%= nombreEstudiante %></td>
                                            <td class="px-4 py-3">
                                                <span class="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium <%= estadoClass %>"><%= estadoTexto %></span>
                                            </td>
                                            <td class="px-4 py-3 text-right">
                                                <div class="flex justify-end items-center gap-3">
                                                    <% if (t.getArchivoRuta() != null && !t.getArchivoRuta().isEmpty()) { %>
                                                        <a href="DownloadServlet?id=<%= t.getId() %>" class="text-indigo-600 hover:text-indigo-900 font-medium text-xs underline">Descargar</a>
                                                    <% } %>
                                                    
                                                    <!-- BOTÓN EDITAR -->
                                                    <button onclick="openEditThesisModal('<%= t.getId() %>', '<%= t.getTitulo() %>', '<%= t.getEstudianteId() %>', '<%= nombreEstudiante %>')" class="text-slate-500 hover:text-slate-800" title="Editar">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path></svg>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>
                    <% } %>
                </div>

                <!-- 3. PESTAÑA ASIGNACIONES -->
                <div id="tab-asignaciones" class="tab-content hidden">
                    <h3 class="text-lg font-medium text-slate-900 mb-4">Asignar Evaluadores</h3>
                    <% if (listaTesis == null || listaTesis.isEmpty()) { %>
                         <div class="text-center py-10 text-slate-500">No hay tesis disponibles para asignar.</div>
                    <% } else { %>
                        <div class="grid gap-4 md:grid-cols-2">
                        <% for (Tesis t : listaTesis) { 
                             if (!"complet".equals(t.getEstado())) {
                                String nombreEstudiante = (t.getEvaluador() != null) ? t.getEvaluador() : "Estudiante ID: " + t.getEstudianteId();
                        %>
                            <div class="rounded-lg border border-slate-200 p-4 bg-white flex flex-col justify-between h-44 shadow-sm hover:shadow-md transition-shadow relative overflow-visible">
                                <div>
                                    <h4 class="font-medium text-slate-900 truncate" title="<%= t.getTitulo() %>"><%= t.getTitulo() %></h4>
                                    <p class="text-sm text-slate-500 mt-1">Estudiante: <%= nombreEstudiante %></p>
                                </div>
                                <div class="mt-4">
                                    <form action="${pageContext.request.contextPath}/AsignarEvaluadorServlet" method="POST" class="flex gap-2 items-end">
                                        <input type="hidden" name="idTesis" value="<%= t.getId() %>">
                                        <div class="w-full relative search-container">
                                            <label class="text-xs font-medium text-slate-700 block mb-1">Buscar Docente:</label>
                                            <input type="hidden" name="idDocente" id="input-hidden-<%= t.getId() %>" required>
                                            <input type="text" id="input-search-<%= t.getId() %>" placeholder="Escribe nombre..." class="w-full rounded-md border border-slate-300 text-sm h-9 px-2 bg-white focus:ring-2 focus:ring-indigo-600 outline-none" autocomplete="off" onfocus="mostrarDropdown('<%= t.getId() %>')" onkeyup="filtrarDocentes(this, '<%= t.getId() %>')">
                                            <div id="dropdown-list-<%= t.getId() %>" class="hidden absolute z-50 w-full bg-white border border-slate-200 rounded-md shadow-lg max-h-40 overflow-y-auto mt-1"></div>
                                        </div>
                                        <button type="submit" class="h-9 px-3 rounded-md bg-slate-900 text-white text-xs font-medium hover:bg-slate-800">Asignar</button>
                                    </form>
                                </div>
                            </div>
                        <% }} %>
                        </div>
                    <% } %>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL CREAR USUARIO -->
    <div id="userModal" class="hidden fixed inset-0 z-50 flex items-center justify-center">
        <div class="fixed inset-0 bg-black/50" onclick="closeUserModal()"></div>
        <div class="relative z-50 w-full max-w-md rounded-lg border bg-white p-6 shadow-lg">
            <h2 class="text-lg font-semibold mb-4 text-slate-900">Registrar Nuevo Usuario</h2>
            <form action="${pageContext.request.contextPath}/UsuarioAdminServlet" method="POST">
                <div class="space-y-4">
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Nombre Completo</label>
                        <input type="text" name="nombre" required class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none">
                    </div>
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Email Institucional</label>
                        <input type="email" name="email" required class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none">
                    </div>
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Contraseña</label>
                        <input type="password" name="password" required class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none">
                    </div>
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Rol</label>
                        <select name="rol" class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm bg-white focus:ring-2 focus:ring-indigo-600 outline-none">
                            <option value="estudiante">Estudiante</option>
                            <option value="docente">Docente</option>
                            <option value="administrador">Administrador</option>
                        </select>
                    </div>
                </div>
                <div class="flex justify-end gap-2 mt-6">
                    <button type="button" onclick="closeUserModal()" class="rounded-md border px-4 py-2 text-sm font-medium hover:bg-slate-100 text-slate-700">Cancelar</button>
                    <button type="submit" class="rounded-md bg-indigo-600 text-white px-4 py-2 text-sm font-medium hover:bg-indigo-700 shadow-sm">Guardar</button>
                </div>
            </form>
        </div>
    </div>

    <!-- MODAL SUBIR TESIS -->
    <div id="thesisModal" class="hidden fixed inset-0 z-50 flex items-center justify-center">
        <div class="fixed inset-0 bg-black/50" onclick="closeThesisModal()"></div>
        <div class="relative z-50 w-full max-w-md rounded-lg border bg-white p-6 shadow-lg overflow-visible">
            <h2 class="text-lg font-semibold mb-4 text-slate-900">Subir Nueva Tesis</h2>
            <form action="${pageContext.request.contextPath}/TesisAdminServlet" method="POST" enctype="multipart/form-data">
                <div class="space-y-4">
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Título de la Tesis</label>
                        <input type="text" name="titulo" required class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none">
                    </div>
                    <div class="space-y-2 relative search-container-estudiante">
                        <label class="text-sm font-medium text-slate-700">Estudiante</label>
                        <input type="hidden" name="estudianteId" id="input-hidden-estudiante" required>
                        <input type="text" id="input-search-estudiante" placeholder="Buscar nombre del estudiante..." class="flex w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none bg-white" autocomplete="off" onfocus="mostrarDropdownEstudiantes()" onkeyup="filtrarEstudiantes(this)">
                        <div id="dropdown-list-estudiantes" class="hidden absolute z-50 w-full bg-white border border-slate-200 rounded-md shadow-lg max-h-40 overflow-y-auto mt-1"></div>
                    </div>
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Archivo PDF</label>
                        <input type="file" name="archivo" accept=".pdf" class="flex w-full rounded-md border border-slate-300 px-3 py-2 text-sm file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-indigo-50 file:text-indigo-700 hover:file:bg-indigo-100">
                    </div>
                </div>
                <div class="flex justify-end gap-2 mt-6">
                    <button type="button" onclick="closeThesisModal()" class="rounded-md border px-4 py-2 text-sm font-medium hover:bg-slate-100 text-slate-700">Cancelar</button>
                    <button type="submit" class="rounded-md bg-indigo-600 text-white px-4 py-2 text-sm font-medium hover:bg-indigo-700 shadow-sm">Subir</button>
                </div>
            </form>
        </div>
    </div>

    <!-- MODAL EDITAR TESIS -->
    <div id="editThesisModal" class="hidden fixed inset-0 z-50 flex items-center justify-center">
        <div class="fixed inset-0 bg-black/50" onclick="closeEditThesisModal()"></div>
        <div class="relative z-50 w-full max-w-md rounded-lg border bg-white p-6 shadow-lg overflow-visible">
            <h2 class="text-lg font-semibold mb-4 text-slate-900">Editar Tesis</h2>
            <form action="${pageContext.request.contextPath}/TesisActualizarServlet" method="POST">
                <input type="hidden" name="id" id="edit-id">
                <div class="space-y-4">
                    <div class="space-y-2">
                        <label class="text-sm font-medium text-slate-700">Título de la Tesis</label>
                        <input type="text" name="titulo" id="edit-titulo" required class="flex h-10 w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none">
                    </div>
                    <div class="space-y-2 relative search-container-estudiante-edit">
                        <label class="text-sm font-medium text-slate-700">Cambiar Estudiante</label>
                        <input type="hidden" name="estudianteId" id="input-hidden-estudiante-edit" required>
                        <input type="text" id="input-search-estudiante-edit" placeholder="Buscar nombre del estudiante..." class="flex w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-600 outline-none bg-white" autocomplete="off" onfocus="mostrarDropdownEstudiantesEdit()" onkeyup="filtrarEstudiantesEdit(this)">
                        <div id="dropdown-list-estudiantes-edit" class="hidden absolute z-50 w-full bg-white border border-slate-200 rounded-md shadow-lg max-h-40 overflow-y-auto mt-1"></div>
                    </div>
                </div>
                <div class="flex justify-end gap-2 mt-6">
                    <button type="button" onclick="closeEditThesisModal()" class="rounded-md border px-4 py-2 text-sm font-medium hover:bg-slate-100 text-slate-700">Cancelar</button>
                    <button type="submit" class="rounded-md bg-indigo-600 text-white px-4 py-2 text-sm font-medium hover:bg-indigo-700 shadow-sm">Actualizar</button>
                </div>
            </form>
        </div>
    </div>

</body>
</html>