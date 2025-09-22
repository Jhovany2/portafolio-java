/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.zonajava.s2actividad1;

import vista.AplicaLapiceros;
/**
 *
 * @author LENOVO
 */
public class S2Actividad1 {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AplicaLapiceros().setVisible(true);
            }
        });
    }
}
