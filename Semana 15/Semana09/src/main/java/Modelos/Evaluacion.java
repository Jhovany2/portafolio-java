/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author LENOVO
 */
import java.sql.Timestamp;

public class Evaluacion {
    private int id;
    private int tesisId;
    private int notaOriginalidad;
    private int notaPlanteamiento;
    private int notaMetodologia;
    private int notaResultados;
    private int notaCoherencia;
    private int notaSustento;
    private int notaFormato;
    private String comentarios;
    private double notaFinal;
    private Timestamp fechaEvaluacion;

    // Constructor vacío
    public Evaluacion() {
    }

    // Constructor completo
    public Evaluacion(int id, int tesisId, int notaOriginalidad, int notaPlanteamiento, int notaMetodologia, int notaResultados, int notaCoherencia, int notaSustento, int notaFormato, String comentarios, double notaFinal, Timestamp fechaEvaluacion) {
        this.id = id;
        this.tesisId = tesisId;
        this.notaOriginalidad = notaOriginalidad;
        this.notaPlanteamiento = notaPlanteamiento;
        this.notaMetodologia = notaMetodologia;
        this.notaResultados = notaResultados;
        this.notaCoherencia = notaCoherencia;
        this.notaSustento = notaSustento;
        this.notaFormato = notaFormato;
        this.comentarios = comentarios;
        this.notaFinal = notaFinal;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    // Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTesisId() {
        return tesisId;
    }

    public void setTesisId(int tesisId) {
        this.tesisId = tesisId;
    }

    public int getNotaOriginalidad() {
        return notaOriginalidad;
    }

    public void setNotaOriginalidad(int notaOriginalidad) {
        this.notaOriginalidad = notaOriginalidad;
    }

    public int getNotaPlanteamiento() {
        return notaPlanteamiento;
    }

    public void setNotaPlanteamiento(int notaPlanteamiento) {
        this.notaPlanteamiento = notaPlanteamiento;
    }

    public int getNotaMetodologia() {
        return notaMetodologia;
    }

    public void setNotaMetodologia(int notaMetodologia) {
        this.notaMetodologia = notaMetodologia;
    }

    public int getNotaResultados() {
        return notaResultados;
    }

    public void setNotaResultados(int notaResultados) {
        this.notaResultados = notaResultados;
    }

    public int getNotaCoherencia() {
        return notaCoherencia;
    }

    public void setNotaCoherencia(int notaCoherencia) {
        this.notaCoherencia = notaCoherencia;
    }

    public int getNotaSustento() {
        return notaSustento;
    }

    public void setNotaSustento(int notaSustento) {
        this.notaSustento = notaSustento;
    }

    public int getNotaFormato() {
        return notaFormato;
    }

    public void setNotaFormato(int notaFormato) {
        this.notaFormato = notaFormato;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public Timestamp getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(Timestamp fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
}
