package es.uned.TFGDavidGiner.demo.builders;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainerBuilder;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;
import es.uned.TFGDavidGiner.demo.ensamblado.MainViewAssembler;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Implementa el patrón Builder para ensamblar la interfaz compleja de la aplicación
 * de demostración "Gestor de Actividad de Gimnasio", utilizando el procedimiento
 * de ensamblado polimórfico.
 * <p>
 * Esta clase demuestra el flujo de trabajo ideal para un desarrollador de aplicaciones.
 * En lugar de construir la GUI manualmente, delega la creación de la vista principal
 * a un objeto que implementa {@link IGuiAssembler} (en este caso, {@link MainViewAssembler}).
 * Posteriormente, utiliza el {@link ButtonPanelContainerBuilder} para envolver la
 * GUI resultante y añadir la funcionalidad transaccional.
 * <p>
 * Este enfoque separa limpiamente las responsabilidades y simplifica enormemente
 * la construcción de la ventana principal.
 *
 * @author David Giner
 * @version 1.0
 * @see IGuiAssembler
 * @see MainViewAssembler
 * @see ButtonPanelContainerBuilder
 */
public class GimnasioDemoFrameBuilder {
    /**
     * La ventana (JFrame) que se está construyendo.
     */
    private final JFrame frame;
    
    /**
     * La lista de datos de prueba que se pasará a los ensambladores.
     */
    private final List<Usuario> datosDePrueba;

    /**
     * Constructor que inicializa el builder, prepara la ventana principal con su
     * configuración por defecto y genera los datos de prueba.
     */
    public GimnasioDemoFrameBuilder() {
        frame = new JFrame("Gestor de Actividad de Gimnasio (Ensamblado Polimórfico)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.datosDePrueba = crearDatosDePrueba();
    }

    /**
     * Ensambla todos los componentes y construye el JFrame final.
     * <p>
     * Este método orquesta todo el proceso:
     * <ol>
     * <li>Crea una instancia del ensamblador principal (`MainViewAssembler`).</li>
     * <li>Invoca su método polimórfico `assembleGui()` para obtener la GUI completa.</li>
     * <li>Envuelve la GUI resultante en un `ButtonPanelContainer`.</li>
     * <li>Configura y devuelve el `JFrame` final.</li>
     * </ol>
     * @return El JFrame de la aplicación, completamente construido y listo para ser mostrado.
     */
    public JFrame build() {
        // 1. Se crea una instancia del ensamblador principal.
        IGuiAssembler mainAssembler = new MainViewAssembler(datosDePrueba);

        // 2. Se invoca el método polimórfico para obtener la GUI completa.
        BaseComponent mainContent = mainAssembler.assembleGui();

        // 3. Se envuelve el contenido en un ButtonPanelContainer.
        ButtonPanelContainer finalPanel = new ButtonPanelContainerBuilder()
                .withContent(mainContent)
                .build();

        // 4. Se añade el panel final al frame.
        frame.add(finalPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        return frame;
    }

    /**
     * Crea y devuelve una lista de usuarios de ejemplo para poblar la tabla.
     * @return Una lista de objetos {@link Usuario}.
     */    
    private List<Usuario> crearDatosDePrueba() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Ana", "García", "Intermedio", 60, 90));
        usuarios.add(new Usuario("Carlos", "Martínez", "Avanzado", 120, 160));
        usuarios.add(new Usuario("Elena", "Ruiz", "Principiante", 30, 40));
        return usuarios;
    }

    /**
     * Punto de entrada para ejecutar la aplicación de demostración construida
     * con este Builder.
     * @param args los argumentos de la línea de comandos (no se utilizan).
     */    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new GimnasioDemoFrameBuilder().build();
            frame.setVisible(true);
        });
    }
}