/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.demo;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainerBuilder;
import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.SimpleContainer;
import es.uned.TFGDavidGiner.components.leafs.ComponenteAInt;
import es.uned.TFGDavidGiner.components.leafs.ComponenteBInt;
import es.uned.TFGDavidGiner.components.leafs.LeafComponentFactory; // Importar la nueva factoría
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Ventana de demostración que ilustra el uso de los patrones de diseño
 * Factory (para hojas y contenedores) y Builder para ensamblar una GUI.
 *
 * @author david
 */
public class FramePatronesDemo extends JFrame {

    /**
     * Constructor que ensambla la GUI utilizando los patrones.
     */
    public FramePatronesDemo() {
        // --- Configuración básica del JFrame ---
        setTitle("Demo con Patrones Factory y Builder");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));

        // --- PASO 1: Usar la FACTORÍA de componentes HOJA ---
        // El desarrollador pide a la nueva factoría los componentes que necesita.
        ComponenteAInt spinnerComponent = LeafComponentFactory.createComponenteAInt();
        ComponenteBInt sliderComponent = LeafComponentFactory.createComponenteBInt();
        
        // --- PASO 2: Usar la FACTORÍA de CONTENEDORES ---
        // El desarrollador pide a la factoría de contenedores un panel para organizar los componentes.
        SimpleContainer contentPanel = ContainerFactory.createSimpleContainer();
        
        // Añadir los componentes diseñados al contenedor.
        contentPanel.getContentPane().add(spinnerComponent);
        contentPanel.getContentPane().add(sliderComponent);

        // --- PASO 3: Usar el patrón BUILDER para "envolver" la GUI ---
        // El desarrollador utiliza el Builder para añadir la botonera de Aceptar/Cancelar.
        ButtonPanelContainer mainPanel = new ButtonPanelContainerBuilder()
                .withContent(contentPanel)
                .build();

        // --- PASO 4: Añadir el componente final ensamblado al frame ---
        this.add(mainPanel);
        
        setLocationRelativeTo(null); // Centrar la ventana
    }

    /**
     * Punto de entrada para ejecutar la demostración.
     * @param args los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new FramePatronesDemo().setVisible(true);
        });
    }
}