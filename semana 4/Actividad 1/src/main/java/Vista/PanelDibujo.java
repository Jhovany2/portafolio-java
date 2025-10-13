/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author LENOVO
 */

/**
 * PanelDibujo
 * 
 * Clase reutilizable para dibujar diferentes figuras geométricas
 * (cuadrado, círculo o triángulo) en un panel.
 * 
 * Se utiliza el método setFigura("nombre") para definir qué figura se dibujará.
 * 
 * Ejemplo:
 * PanelDibujo p = new PanelDibujo();
 * p.setFigura("circulo");
 */
public class PanelDibujo extends JPanel {
    
    private String figura; // Puede ser: "cuadrado", "circulo" o "triangulo"

    // Constructor
    public PanelDibujo() {
        this.figura = ""; // No dibuja nada por defecto
        this.setBackground(Color.WHITE);
    }

    // Método para indicar qué figura se debe dibujar
    public void setFigura(String figura) {
        this.figura = figura.toLowerCase();
        repaint(); // Redibuja el panel cada vez que cambia la figura
    }

    // Método que dibuja la figura seleccionada
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Configuración del color y coordenadas base
        g.setColor(Color.BLUE);
        int width = getWidth();
        int height = getHeight();

        int size = Math.min(width, height) - 40; // Tamaño con márgenes
        int x = (width - size) / 2;
        int y = (height - size) / 2;

        switch (figura) {
            case "cuadrado":
                g.fillRect(x, y, size, size);
                break;

            case "circulo":
                g.fillOval(x, y, size, size);
                break;

            case "triangulo":
                int[] xPoints = {x + size / 2, x, x + size};
                int[] yPoints = {y, y + size, y + size};
                g.fillPolygon(xPoints, yPoints, 3);
                break;

            default:
                g.setColor(Color.GRAY);
                g.drawString("Seleccione una figura...", x + 20, y + size / 2);
        }
    }
    
}
