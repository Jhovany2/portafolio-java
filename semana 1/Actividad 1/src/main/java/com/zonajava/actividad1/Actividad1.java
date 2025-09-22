/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.zonajava.actividad1;

import vista.AplicaCuadro;

/**
 *
 * @author LENOVO
 */
public class Actividad1 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
         java.awt.EventQueue.invokeLater(new Runnable() {
         public void run() {
            new AplicaCuadro().setVisible(true);
        }
    });

    }
}
