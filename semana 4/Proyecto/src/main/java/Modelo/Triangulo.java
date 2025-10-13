/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author LENOVO
 */
public class Triangulo {
   private double lado1;
    private double lado2;
    private double lado3;

    public Triangulo() {
    }

    public Triangulo(double lado1, double lado2, double lado3) {
        this.lado1 = lado1;
        this.lado2 = lado2;
        this.lado3 = lado3;
    }

    public double getLado1() {
        return lado1;
    }

    public void setLado1(double lado1) {
        this.lado1 = lado1;
    }

    public double getLado2() {
        return lado2;
    }

    public void setLado2(double lado2) {
        this.lado2 = lado2;
    }

    public double getLado3() {
        return lado3;
    }

    public void setLado3(double lado3) {
        this.lado3 = lado3;
    }

    // Método para calcular perímetro
    public double calcularPerimetro() {
        return lado1 + lado2 + lado3;
    }

    // Método para calcular área (fórmula de Herón)
    public double calcularArea() {
        double s = calcularPerimetro() / 2.0;
        return Math.sqrt(s * (s - lado1) * (s - lado2) * (s - lado3));
    }

}
