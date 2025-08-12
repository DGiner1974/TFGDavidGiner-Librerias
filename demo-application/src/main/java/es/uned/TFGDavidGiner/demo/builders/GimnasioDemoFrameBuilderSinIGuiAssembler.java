package es.uned.TFGDavidGiner.demo.builders;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainerBuilder;
import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.SimpleContainer;
import es.uned.TFGDavidGiner.components.containers.TabContainer;
import es.uned.TFGDavidGiner.components.containers.TreeContainer;
import es.uned.TFGDavidGiner.components.leafs.*;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Implementa el patrón Builder para ensamblar la interfaz compleja de la aplicación
 * de demostración "Gestor de Actividad de Gimnasio".
 * <p>
 * Esta clase utiliza las factorías del framework ({@link ContainerFactory}, {@link LeafComponentFactory})
 * y otros builders ({@link ButtonPanelContainerBuilder}) para construir la GUI de forma
 * programática, demostrando el flujo de trabajo de un desarrollador de aplicaciones.
 */
public class GimnasioDemoFrameBuilderSinIGuiAssembler {

    private final JFrame frame;
    private final List<Usuario> datosDePrueba;

    /**
     * Constructor que inicializa el builder y prepara los datos de prueba.
     */
    public GimnasioDemoFrameBuilderSinIGuiAssembler() {
        frame = new JFrame("Gestor de Actividad de Gimnasio (Construido con Builder)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.datosDePrueba = crearDatosDePrueba();
    }

    /**
     * Ensambla todos los componentes y construye el JFrame final.
     * @return El JFrame de la aplicación, completamente construido y listo para ser mostrado.
     */
    public JFrame build() {
        // 1. Ensamblar el contenido principal de la aplicación.
        BaseComponent mainContent = createMainContentPanel();

        // 2. Envolver el contenido en un ButtonPanelContainer usando su propio builder.
        ButtonPanelContainer finalPanel = new ButtonPanelContainerBuilder()
                .withContent(mainContent)
                .build();

        // 3. Añadir el panel final al frame.
        frame.add(finalPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);

        return frame;
    }

    /**
     * Método privado que encapsula toda la lógica de ensamblado de la interfaz.
     * @return Un BaseComponent con toda la GUI de la aplicación.
     */
    private BaseComponent createMainContentPanel() {
        // --- Creación de todos los componentes hoja necesarios usando la factoría ---
        TablaUsuarios tablaUsuarios = LeafComponentFactory.createTablaUsuarios();
        PanelDatosUsuario panelDatos = LeafComponentFactory.createPanelDatosUsuario();
        GraficoRendimiento graficoRendimiento = LeafComponentFactory.createGraficoRendimiento();
        SpinnerPress spinnerPress = LeafComponentFactory.createSpinnerPress();
        SpinnerSquad spinnerSquad = LeafComponentFactory.createSpinnerSquad();
        SliderPress sliderPress = LeafComponentFactory.createSliderPress();
        SliderSquad sliderSquad = LeafComponentFactory.createSliderSquad();
        GraficoRendimiento graficoRendimiento2 = LeafComponentFactory.createGraficoRendimiento();

        // Carga de datos en la tabla
        tablaUsuarios.setUsuarios(datosDePrueba);

        // --- Ensamblado de la VISTA DE GESTIÓN DE SOCIOS ---
        SimpleContainer panelSpinners = ContainerFactory.createSimpleContainer();
        panelSpinners.getContentPane().add(spinnerPress);
        panelSpinners.getContentPane().add(spinnerSquad);

        SimpleContainer panelDatosCompleto = ContainerFactory.createSimpleContainer();
        panelDatosCompleto.setOrientation(SplitOrientation.VERTICAL);
        panelDatosCompleto.getContentPane().add(panelDatos);
        panelDatosCompleto.getContentPane().add(panelSpinners);

        TabContainer tabDatos = ContainerFactory.createTabContainer();
        tabDatos.setTabTitles("Datos Usuario,Gráfico Rendimiento");
        tabDatos.getContentPane().add(panelDatosCompleto);
        tabDatos.getContentPane().add(graficoRendimiento);
        
        SimpleContainer vistaGestionSocios = ContainerFactory.createSimpleContainer();
        vistaGestionSocios.setOrientation(SplitOrientation.VERTICAL);
        vistaGestionSocios.setDividerLocation(150);
        vistaGestionSocios.getContentPane().add(tablaUsuarios);
        vistaGestionSocios.getContentPane().add(tabDatos);

        // --- Ensamblado de la VISTA DE CAPACIDADES FÍSICAS ---
        SimpleContainer panelSliders = ContainerFactory.createSimpleContainer();
        panelSliders.setOrientation(SplitOrientation.VERTICAL);
        panelSliders.setDividerLocation(50);
        panelSliders.getContentPane().add(sliderPress);
        panelSliders.getContentPane().add(sliderSquad);
        
        SimpleContainer vistaCapacidades = ContainerFactory.createSimpleContainer();
        vistaCapacidades.setOrientation(SplitOrientation.VERTICAL);
        vistaCapacidades.getContentPane().add(panelSliders);
        vistaCapacidades.getContentPane().add(graficoRendimiento2);

        // --- Ensamblado de la VISTA PRINCIPAL con TreeContainer ---
        TreeContainer vistaPrincipal = ContainerFactory.createTreeContainer();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Gimnasio");
        root.add(new DefaultMutableTreeNode("Gestión de Socios"));
        root.add(new DefaultMutableTreeNode("Capacidades Físicas"));
        vistaPrincipal.setEstructuraArbol(new DefaultTreeModel(root));
        
        vistaPrincipal.getContentPane().add(vistaGestionSocios);
        vistaPrincipal.getContentPane().add(vistaCapacidades);
        vistaPrincipal.setDesignTimeSelectionPath("Raíz, Gestión de Socios");
        
        return vistaPrincipal;
    }

    private List<Usuario> crearDatosDePrueba() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Ana", "García", "Intermedio", 60, 90));
        usuarios.add(new Usuario("Carlos", "Martínez", "Avanzado", 120, 160));
        usuarios.add(new Usuario("Elena", "Ruiz", "Principiante", 30, 40));
        return usuarios;
    }

    /**
     * Punto de entrada para ejecutar la aplicación construida con el Builder.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new GimnasioDemoFrameBuilderSinIGuiAssembler().build();
            frame.setVisible(true);
        });
    }
}