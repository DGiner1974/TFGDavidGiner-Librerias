/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.demo.modelo;

import es.uned.TFGDavidGiner.components.containers.*;
import es.uned.TFGDavidGiner.components.leafs.*;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;

/**
 * Modelo que sabe cómo ensamblar una GUI simple con componentes de tipo String.
 * <p>
 * Implementa la interfaz {@link IGuiAssembler} para proporcionar una
 * representación gráfica de sí mismo, consistente en un {@link SimpleContainer}
 * con un {@link ComponenteA} y un {@link ComponenteB}.
 */
public class ModeloSimpleString implements IGuiAssembler {

    /**
     * Constructor por defecto.
     */
    public ModeloSimpleString() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    /**
     * Ensambla y devuelve la representación gráfica de este modelo.
     * <p>
     * Utiliza las factorías para crear los componentes y los ensambla en un
     * {@code SimpleContainer} con orientación vertical.
     *
     * @return un {@link BaseComponent} que representa la GUI ensamblada.
     */
    @Override
    public BaseComponent assembleGui() {
        SimpleContainer panel = ContainerFactory.createSimpleContainer();
        panel.setOrientation(es.uned.TFGDavidGiner.core.SplitOrientation.VERTICAL);

        panel.getContentPane().add(LeafComponentFactory.createComponenteA());
        panel.getContentPane().add(LeafComponentFactory.createComponenteB());

        return panel;
    }
}