/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.zonajava.cejemplo04;

import Controlador.CuadradoControlador;
import Controlador.CirculoControlador;
import Controlador.TrianguloControlador;
import Modelo.Cuadrado;
import Modelo.Circulo;
import Modelo.Triangulo;
import Vista.VentanaPrincipal;
/**
 *
 * @author LENOVO
 */
public class CEjemplo04 {

    public static void main(String[] args) {
         // Crear ventana principal
        VentanaPrincipal ventana = new VentanaPrincipal();

        // Crear modelos
        Cuadrado modeloCuadrado = new Cuadrado(0);
        Circulo modeloCirculo = new Circulo(0);
        Triangulo modeloTriangulo = new Triangulo(0, 0, 0);

        // Asociar controladores con sus vistas
        new CuadradoControlador(modeloCuadrado, ventana.getCuadradoVista());
        new CirculoControlador(modeloCirculo, ventana.getCirculoVista());
        new TrianguloControlador(modeloTriangulo, ventana.getTrianguloVista());

        // Mostrar ventana
        ventana.setVisible(true);
    }
}
