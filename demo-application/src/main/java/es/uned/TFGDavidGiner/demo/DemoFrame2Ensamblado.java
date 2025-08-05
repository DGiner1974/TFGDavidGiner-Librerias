/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.demo;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainerBuilder;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.demo.modelo.ModeloTreePrincipal;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
* Ventana de demostración que ilustra el ensamblado de una GUI compleja
* utilizando el patrón de diseño polimórfico a través del modelo de datos.
* <p>
* Esta clase muestra cómo el desarrollador de aplicaciones puede construir
* una interfaz gráfica completa y anidada con una botonera de validación,
* delegando toda la lógica de construcción a los objetos del modelo
* (`ModeloTreePrincipal`) y utilizando un Builder para el ensamblado final.
*
* @author david
* @version 1.0
* @since 2025-07-27
*/
public class DemoFrame2Ensamblado extends JFrame {

    /**
     * Constructor por defecto.
     * <p>
     * Configura la ventana y ensambla la interfaz gráfica completa siguiendo
     * los pasos definidos:
     * 1. Instancia el modelo de datos principal.
     * 2. Invoca el método polimórfico {@code assembleGui()} del modelo.
     * 3. Envuelve la GUI resultante con un {@code ButtonPanelContainer} usando un Builder.
     * 4. Añade el panel final a la ventana.
     */
    public DemoFrame2Ensamblado() {
        setTitle("DemoFrame2 Refactorizado con Ensamblado Polimórfico");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));

        // 1. El desarrollador solo necesita instanciar el objeto principal de su modelo.
        ModeloTreePrincipal modeloPrincipal = new ModeloTreePrincipal();

        // 2. Se pide al modelo que construya su propia GUI de forma polimórfica.
        // El frame no sabe ni le importa cómo está construida esta GUI internamente.
        BaseComponent guiEnsamblada = modeloPrincipal.assembleGui();
        
        // 3. (Opcional) Se envuelve la GUI resultante en un contenedor con botones.
        ButtonPanelContainer panelFinal = new ButtonPanelContainerBuilder()
                .withContent(guiEnsamblada)
                .build();

        // 4. Se añade el panel final a la ventana.
        this.add(panelFinal);
        setLocationRelativeTo(null);
    }

    /**
     * Punto de entrada principal para la aplicación de demostración.
     * <p>
     * Crea y muestra el formulario {@code DemoFrame2Ensamblado} en el
     * Event Dispatch Thread (EDT) de Swing para garantizar la seguridad de los hilos.
     *
     * @param args los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DemoFrame2Ensamblado().setVisible(true));
    }
}
