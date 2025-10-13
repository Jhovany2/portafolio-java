/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Circulo;
import Vista.CirculoVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CirculoControlador {
    private final Circulo modelo;
    private final CirculoVista vista;

    public CirculoControlador(Circulo modelo, CirculoVista vista) {
        this.modelo = modelo;
        this.vista = vista;

        this.vista.agregarCalcularListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });

        this.vista.agregarNuevoListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });

        this.vista.agregarSalirListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });
    }

    public void calcular() {
        double radio = vista.obtenerRadio();
        modelo.setRadio(radio);
        double area = modelo.calcularArea();
        double perimetro = modelo.calcularPerimetro();
        vista.mostrarResultados(area, perimetro);
    }

    public void nuevo() {
        vista.limpiarCampos();
    }

    public void salir() {
        System.exit(0);
    }
}
