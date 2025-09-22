/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.zonajava.actividad2;

import vista.AplicaFeria;
/**
 *
 * @author LENOVO
 */
public class Actividad2 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplicaFeria().setVisible(true);
            }
        });
    }
}
