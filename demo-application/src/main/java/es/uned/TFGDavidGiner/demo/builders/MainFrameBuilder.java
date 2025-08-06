package es.uned.TFGDavidGiner.demo.builders;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainerBuilder;
import es.uned.TFGDavidGiner.core.BaseComponent;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * Implementa el patrón Builder para la construcción controlada de la ventana
 * principal de la aplicación (JFrame).
 * <p>
 * Esta clase encapsula la lógica para ensamblar correctamente una ventana que
 * utiliza un {@link ButtonPanelContainer} como su panel de contenido principal.
 * Su objetivo es guiar al desarrollador de aplicaciones para que siga las
 * convenciones de diseño del framework, asegurando que solo se añada un
- * panel de contenido y que este sea envuelto por la botonera estándar.
 * <p>
 * Esta clase no forma parte de la biblioteca de componentes, sino que es un
 * ejemplo de cómo una aplicación cliente debe gestionar la construcción de su
 * interfaz principal.
 *
 * @author David Giner
 * @version 1.0
 */
public class MainFrameBuilder {

    /**
     * La ventana que se está construyendo.
     */
    private final JFrame frame;
    
    /**
     * El componente que actuará como el contenido principal de la ventana.
     */
    private BaseComponent mainContent;

    /**
     * Constructor que inicializa la ventana con una configuración por defecto.
     */
    public MainFrameBuilder() {
        frame = new JFrame("Aplicación de Demostración");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Se establece BorderLayout para asegurar que el panel principal ocupe todo el espacio.
        frame.setLayout(new BorderLayout());
    }

    /**
     * Establece el componente de contenido principal que se mostrará en la ventana,
     * envuelto por el contenedor de botones.
     * <p>
     * Este método solo puede ser invocado una vez para asegurar que la ventana
     * tenga un único panel de contenido.
     *
     * @param content El componente (simple o compuesto) que actuará como vista principal.
     * @return La misma instancia del builder para permitir el encadenamiento de métodos.
     * @throws IllegalStateException si el contenido principal ya ha sido establecido.
     */
    public MainFrameBuilder withContent(BaseComponent content) {
        if (this.mainContent != null) {
            throw new IllegalStateException("El contenido principal ya ha sido establecido.");
        }
        this.mainContent = content;
        return this;
    }

    /**
     * Ensambla y devuelve la ventana (JFrame) final.
     * <p>
     * Este método toma el contenido proporcionado, lo envuelve en un
     * {@link ButtonPanelContainer} utilizando su propio builder, y lo añade
     * al centro del JFrame.
     *
     * @return El JFrame final, configurado y listo para ser mostrado.
     * @throws IllegalStateException si no se ha establecido un contenido principal
     * antes de llamar a este método.
     */
    public JFrame build() {
        if (mainContent == null) {
            throw new IllegalStateException("Se debe establecer un contenido principal antes de construir la ventana.");
        }

        // Se utiliza el builder de la biblioteca para crear el panel final con la botonera.
        ButtonPanelContainer finalPanel = new ButtonPanelContainerBuilder()
                .withContent(mainContent)
                .build();
        
        // El panel final se añade a la posición central del JFrame para que ocupe todo el espacio.
        frame.add(finalPanel, BorderLayout.CENTER);
        frame.pack();
        
        // Establece el estado del frame a maximizado (tanto horizontal como verticalmente).
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        
        return frame;
    }
}