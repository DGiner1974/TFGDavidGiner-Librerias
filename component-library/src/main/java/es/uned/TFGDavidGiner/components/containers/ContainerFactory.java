/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.components.containers;

/**
 * Factoría para la creación de los contenedores base del framework.
 * Sigue el patrón de diseño Factory para centralizar la instanciación
 * y ocultar la complejidad de la construcción.
 * * @author david
 */
public class ContainerFactory {

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     * Al tener solo métodos estáticos, no se debe crear un objeto de esta clase.
     */
    private ContainerFactory() {
        // Esta clase no debe ser instanciada.
    }

    
    /**
     * Crea una nueva instancia de SimpleContainer.
     * @return un nuevo SimpleContainer.
     */
    public static SimpleContainer createSimpleContainer() {
        return new SimpleContainer();
    }

    /**
     * Crea una nueva instancia de TabContainer.
     * @return un nuevo TabContainer.
     */
    public static TabContainer createTabContainer() {
        return new TabContainer();
    }

    /**
     * Crea una nueva instancia de TreeContainer.
     * @return un nuevo TreeContainer.
     */
    public static TreeContainer createTreeContainer() {
        return new TreeContainer();
    }
}