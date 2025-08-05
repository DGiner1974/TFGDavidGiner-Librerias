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
 * Modelo que sabe cómo ensamblar una GUI con pestañas y componentes de tipo Integer.
 */
public class ModeloTabInt implements IGuiAssembler {

    /**
     * Constructor por defecto.
     */
    public ModeloTabInt() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    @Override
    public BaseComponent assembleGui() {
        TabContainer panel = ContainerFactory.createTabContainer();
        panel.setTabTitles("Spinner (Int),Slider (Int),Progreso (Int)");

        panel.getContentPane().add(LeafComponentFactory.createComponenteAInt());
        panel.getContentPane().add(LeafComponentFactory.createComponenteBInt());
        panel.getContentPane().add(LeafComponentFactory.createComponenteCInt());

        return panel;
    }
}
