package com.zonajava.finanzaspersonales;

import javax.swing.SwingUtilities;
import vista.LoginFrame;


public class FinanzasPersonales {

    public static void main(String[] args) {
        
       // Iniciar la aplicación en el hilo de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crear y mostrar la ventana de Login
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
                login.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
            }
        });
        
    }
}
