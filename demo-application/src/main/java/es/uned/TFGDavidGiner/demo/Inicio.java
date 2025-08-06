/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.demo;

import es.uned.TFGDavidGiner.components.containers.TabContainer;
import es.uned.TFGDavidGiner.demo.builders.MainFrameBuilder;
import javax.swing.JFrame;

/**
 *
 * @author david
 */
public class Inicio {
    public static void main(String[] args) {
    // El desarrollador crea su panel de negocio (ej. un TabContainer)
    TabContainer miContenido = new TabContainer();
    // ... añade componentes a miContenido ...

    // Usa el Builder para construir el Frame de forma segura
    JFrame miVentana = new MainFrameBuilder()
                            .withContent(miContenido) // Solo puede poner un contenido
                            .build();
    
    miVentana.setVisible(true);
}
    
}
