/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.demo.modelo;

import es.uned.TFGDavidGiner.components.containers.*;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Modelo principal que ensambla una GUI compleja con un árbol de navegación,
 * agregando las GUIs de otros objetos del modelo.
 */
public class ModeloTreePrincipal implements IGuiAssembler {
    // Agrega instancias de otros modelos como si fueran objetos asociados.
    private ModeloSimpleString modeloSimple = new ModeloSimpleString();
    private ModeloTabInt modeloTab = new ModeloTabInt();

    /**
     * Constructor por defecto.
     */
    public ModeloTreePrincipal() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    @Override
    public BaseComponent assembleGui() {
        TreeContainer panel = ContainerFactory.createTreeContainer();

        // 1. Construir el árbol de navegación
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Menú Principal");
        DefaultMutableTreeNode nodoSimple = new DefaultMutableTreeNode("Vista Simple (String)");
        DefaultMutableTreeNode nodoTab = new DefaultMutableTreeNode("Vista Pestañas (Integer)");
        raiz.add(nodoSimple);
        raiz.add(nodoTab);
        panel.setEstructuraArbol(new DefaultTreeModel(raiz));

        // 2. Pedir a los modelos agregados que ensamblen sus GUIs y añadirlas al panel.
        // El orden de adición debe coincidir con el de las hojas del árbol.
        panel.getContentPane().add(modeloSimple.assembleGui());
        panel.getContentPane().add(modeloTab.assembleGui());
        
        return panel;
    }
}
