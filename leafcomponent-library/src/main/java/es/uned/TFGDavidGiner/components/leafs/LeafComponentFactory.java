/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.components.leafs;

/**
 * Factoría para la creación de los componentes hoja (leaf) del framework.
 * <p>
 * Sigue el patrón de diseño Factory para ofrecer un punto de acceso centralizado 
 * para la instanciación programática de los diferentes componentes hoja,
 * desacoplando al desarrollador de las clases concretas.
 *
 * @author david
 * @version 1.0
 * @since 2025-07-27
 */
public class LeafComponentFactory {

    /**
     * Constructor privado para prevenir la instanciación de esta clase de utilidad.
     * Al tener solo métodos estáticos, no se debe crear un objeto de esta clase.
     */
    private LeafComponentFactory() {
        // Esta clase no debe ser instanciada.
    }

    /**
     * Crea una nueva instancia de {@link ComponenteA}.
     * @return un nuevo {@code ComponenteA}.
     */    
    public static ComponenteA createComponenteA() {
        return new ComponenteA();
    }

    /**
     * Crea una nueva instancia de {@link ComponenteAInt}.
     * @return un nuevo {@code ComponenteAInt}.
     */    
    public static ComponenteAInt createComponenteAInt() {
        return new ComponenteAInt();
    }

    /**
     * Crea una nueva instancia de {@link ComponenteB}.
     * @return un nuevo {@code ComponenteB}.
     */
    public static ComponenteB createComponenteB() {
        return new ComponenteB();
    }

    /**
     * Crea una nueva instancia de {@link ComponenteBInt}.
     * @return un nuevo {@code ComponenteBInt}.
     */
    public static ComponenteBInt createComponenteBInt() {
        return new ComponenteBInt();
    }

    /**
     * Crea una nueva instancia de {@link ComponenteC}.
     * @return un nuevo {@code ComponenteC}.
     */
    public static ComponenteC createComponenteC() {
        return new ComponenteC();
    }

    /**
     * Crea una nueva instancia de {@link ComponenteCInt}.
     * @return un nuevo {@code ComponenteCInt}.
     */   
    public static ComponenteCInt createComponenteCInt() {
        return new ComponenteCInt();
    }
    
    /**
     * Crea una nueva instancia de {@link ComponenteD}.
     * @return un nuevo {@code ComponenteD}.
     */
    public static ComponenteD createComponenteD() {
        return new ComponenteD();
    }
}
