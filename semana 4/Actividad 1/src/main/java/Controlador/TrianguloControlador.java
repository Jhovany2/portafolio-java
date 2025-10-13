/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Triangulo;
import Vista.TrianguloVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrianguloControlador {
    private final Triangulo modelo;
    private final TrianguloVista vista;

    public TrianguloControlador(Triangulo modelo, TrianguloVista vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Listener para Calcular
        this.vista.agregarCalcularListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });

        // Listener para Nuevo
        this.vista.agregarNuevoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });

        // Listener para Salir
        this.vista.agregarSalirListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });
    }

    private void calcular() {
        double lado1 = vista.obtenerLado1();
        double lado2 = vista.obtenerLado2();
        double lado3 = vista.obtenerLado3();

        modelo.setLado1(lado1);
        modelo.setLado2(lado2);
        modelo.setLado3(lado3);

        double area = modelo.calcularArea();
        double perimetro = modelo.calcularPerimetro();

        vista.mostrarResultados(area, perimetro);
    }

    private void nuevo() {
        vista.limpiarCampos();
    }

    private void salir() {
        System.exit(0);
    }
}