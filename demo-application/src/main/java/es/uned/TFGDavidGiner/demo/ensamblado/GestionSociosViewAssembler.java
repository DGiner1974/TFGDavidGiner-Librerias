package es.uned.TFGDavidGiner.demo.ensamblado;

import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.SimpleContainer;
import es.uned.TFGDavidGiner.components.containers.TabContainer;
import es.uned.TFGDavidGiner.components.leafs.*;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;
import java.util.List;

/**
 * Implementa la interfaz {@link IGuiAssembler} para construir la vista de "Gestión de Socios"
 * de la aplicación de demostración.
 * <p>
 * Esta clase encapsula la lógica de creación y composición de los componentes
 * visuales específicos para esta sección de la GUI. Su única responsabilidad
 * es ensamblar y devolver un {@link BaseComponent} que representa su parte de la
 * interfaz, demostrando el principio de ensamblado polimórfico.
 * <p>
 * Utiliza las factorías del framework ({@link ContainerFactory}, {@link LeafComponentFactory})
 * para instanciar los componentes necesarios.
 *
 * @author David Giner
 * @version 1.0
 * @see IGuiAssembler
 * @see MainViewAssembler
 */
public class GestionSociosViewAssembler implements IGuiAssembler {
    /**
     * La lista de datos de prueba que se pasará a la tabla de usuarios.
     */
    private final List<Usuario> datosDePrueba;
    
    /**
     * Constructor que inicializa el ensamblador con los datos necesarios para
     * poblar la vista.
     *
     * @param datosDePrueba La lista de objetos {@link Usuario} que se mostrará en la tabla.
     */
    public GestionSociosViewAssembler(List<Usuario> datosDePrueba) {
        this.datosDePrueba = datosDePrueba;
    }

    /**
     * Ensambla y devuelve la interfaz gráfica para la vista de "Gestión de Socios".
     * <p>
     * Este método utiliza las factorías del framework para crear los componentes
     * hoja (`TablaUsuarios`, `PanelDatosUsuario`, etc.) y los contenedores
     * (`SimpleContainer`, `TabContainer`) necesarios, y los compone en una
     * jerarquía visual compleja. El resultado es un único componente compuesto
     * listo para ser integrado en un contenedor de nivel superior.
     *
     * @return un {@link BaseComponent} que representa la GUI completa de esta vista.
     */
    @Override
    public BaseComponent assembleGui() {
        // --- Creación de los componentes hoja necesarios para esta vista ---
        TablaUsuarios tablaUsuarios = LeafComponentFactory.createTablaUsuarios();
        PanelDatosUsuario panelDatos = LeafComponentFactory.createPanelDatosUsuario();
        GraficoRendimiento graficoRendimiento = LeafComponentFactory.createGraficoRendimiento();
        SpinnerPress spinnerPress = LeafComponentFactory.createSpinnerPress();
        SpinnerSquat spinnerSquat = LeafComponentFactory.createSpinnerSquat();

        // Carga de datos
        tablaUsuarios.setUsuarios(datosDePrueba);

        // --- Ensamblado de la vista mediante composición de contenedores---
        // Se crea un panel para los spinners de rendimiento
        SimpleContainer panelSpinners = ContainerFactory.createSimpleContainer();
        panelSpinners.getContentPane().add(spinnerPress);
        panelSpinners.getContentPane().add(spinnerSquat);

        // Se crea un panel que combina el formulario de datos y los spinners
        SimpleContainer panelDatosCompleto = ContainerFactory.createSimpleContainer();
        panelDatosCompleto.setOrientation(SplitOrientation.VERTICAL);
        panelDatosCompleto.getContentPane().add(panelDatos);
        panelDatosCompleto.getContentPane().add(panelSpinners);

        // Se crea un contenedor de pestañas para el panel de datos y el gráfico
        TabContainer tabDatos = ContainerFactory.createTabContainer();
        tabDatos.setTabTitles("Datos Usuario,Gráfico Rendimiento");
        tabDatos.getContentPane().add(panelDatosCompleto);
        tabDatos.getContentPane().add(graficoRendimiento);
        
        // Se crea la vista final, combinando la tabla y el contenedor de pestañas
        SimpleContainer vistaGestionSocios = ContainerFactory.createSimpleContainer();
        vistaGestionSocios.setOrientation(SplitOrientation.VERTICAL);
        vistaGestionSocios.setDividerLocation(150);
        vistaGestionSocios.getContentPane().add(tablaUsuarios);
        vistaGestionSocios.getContentPane().add(tabDatos);

        return vistaGestionSocios;
    }
}