/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author LENOVO
 */
public class Tesis {
    private int id;
    private String titulo;
    private int estudianteId;
    private int evaluadorId;
    private String fechaEntrega;
    private String estado;
    private String archivoRuta;
    
    // Datos extendidos para mostrar en las vistas (JOINs)
    private String evaluador; // Nombre del docente
    private double notaFinal;
    private String comentarios;
    
    // NUEVO: Notas individuales de la rúbrica
    private int nota1; // Originalidad (Max 3)
    private int nota2; // Planteamiento (Max 3)
    private int nota3; // Metodología (Max 3)
    private int nota4; // Resultados (Max 3)
    private int nota5; // Coherencia (Max 3)
    private int nota6; // Sustento (Max 3)
    private int nota7; // Formato (Max 2)

    public Tesis() {
    }

    // Getters y Setters básicos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

    public int getEvaluadorId() { return evaluadorId; }
    public void setEvaluadorId(int evaluadorId) { this.evaluadorId = evaluadorId; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getArchivoRuta() { return archivoRuta; }
    public void setArchivoRuta(String archivoRuta) { this.archivoRuta = archivoRuta; }

    public String getEvaluador() { return evaluador; }
    public void setEvaluador(String evaluador) { this.evaluador = evaluador; }

    public double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(double notaFinal) { this.notaFinal = notaFinal; }

    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }

    // Getters y Setters para las notas individuales
    public int getNota1() { return nota1; }
    public void setNota1(int nota1) { this.nota1 = nota1; }

    public int getNota2() { return nota2; }
    public void setNota2(int nota2) { this.nota2 = nota2; }

    public int getNota3() { return nota3; }
    public void setNota3(int nota3) { this.nota3 = nota3; }

    public int getNota4() { return nota4; }
    public void setNota4(int nota4) { this.nota4 = nota4; }

    public int getNota5() { return nota5; }
    public void setNota5(int nota5) { this.nota5 = nota5; }

    public int getNota6() { return nota6; }
    public void setNota6(int nota6) { this.nota6 = nota6; }

    public int getNota7() { return nota7; }
    public void setNota7(int nota7) { this.nota7 = nota7; }
}