/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cuadrado;
import Vista.CuadradoVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
public class CuadradoControlador {
    private final Cuadrado modelo;
    private final CuadradoVista vista;
    public CuadradoControlador (Cuadrado modelo, CuadradoVista vista) {
      this.modelo = modelo;
      this.vista = vista;
      this.vista.agregarCalcularListener(new ActionListener() {
         @Override
         public void actionPerformed (ActionEvent e) {
             calcular();
            }
        });
      this.vista.agregarNuevoListener(new ActionListener() {
         @Override
         public void actionPerformed (ActionEvent e) {
             nuevo();
            }
        });
      this.vista.agregarSalirListener(new ActionListener() {
          @Override
          public void actionPerformed (ActionEvent e) {
             salir();
            }
        });
    }
    public void calcular() {
     double lado = vista.obtenerLado();
     modelo.setLado (lado);
     double area = modelo.calcularArea();
     double perimetro = modelo.calcularPerimetro();
     vista.mostrarResultados (area, perimetro);
    }
    public void nuevo() {
     vista.limpiarCampos();
    }
    public void salir() {
     System.exit(0);
    }    
}
