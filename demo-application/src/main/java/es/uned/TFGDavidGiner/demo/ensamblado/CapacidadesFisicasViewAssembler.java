package es.uned.TFGDavidGiner.demo.ensamblado;

import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.SimpleContainer;
import es.uned.TFGDavidGiner.components.leafs.*;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;

/**
 * Implementa la interfaz {@link IGuiAssembler} para construir la vista de "Capacidades Físicas"
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
public class CapacidadesFisicasViewAssembler implements IGuiAssembler {
    /**
     * Ensambla y devuelve la interfaz gráfica para la vista de "Capacidades Físicas".
     * <p>
     * Este método utiliza las factorías del framework para crear los componentes
     * hoja (`SliderPress`, `SliderSquad`, `GraficoRendimiento`) y los contenedores
     * (`SimpleContainer`) necesarios, y los compone en una jerarquía visual.
     * El resultado es un único componente compuesto listo para ser integrado en
     * un contenedor de nivel superior, como un {@link es.uned.TFGDavidGiner.components.containers.TreeContainer}.
     *
     * @return un {@link BaseComponent} que representa la GUI completa de esta vista.
     */
    @Override
    public BaseComponent assembleGui() {
        // --- Creación de componentes hoja ---
        SliderPress sliderPress = LeafComponentFactory.createSliderPress();
        SliderSquad sliderSquad = LeafComponentFactory.createSliderSquad();
        GraficoRendimiento graficoRendimiento = LeafComponentFactory.createGraficoRendimiento();

        // --- Ensamblado de la vista ---
        SimpleContainer panelSliders = ContainerFactory.createSimpleContainer();
        panelSliders.setOrientation(SplitOrientation.VERTICAL);
        panelSliders.setDividerLocation(50);
        panelSliders.getContentPane().add(sliderPress);
        panelSliders.getContentPane().add(sliderSquad);
        
        SimpleContainer vistaCapacidades = ContainerFactory.createSimpleContainer();
        vistaCapacidades.setOrientation(SplitOrientation.VERTICAL);
        vistaCapacidades.getContentPane().add(panelSliders);
        vistaCapacidades.getContentPane().add(graficoRendimiento);
        
        return vistaCapacidades;
    }
}