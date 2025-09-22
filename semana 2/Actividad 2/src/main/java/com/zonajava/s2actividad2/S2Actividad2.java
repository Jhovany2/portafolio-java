/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.zonajava.s2actividad2;

import vista.AplicaSueldo;
/**
 *
 * @author LENOVO
 */
public class S2Actividad2 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplicaSueldo().setVisible(true);
            }
        });
    }
}
