/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package es.uned.TFGDavidGiner.components.containers;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Proporciona la información del JavaBean (BeanInfo) para la clase {@link ButtonPanelContainer}.
 * <p>
 * Esta clase es utilizada por los entornos de desarrollo (IDEs) para entender cómo
 * mostrar y manejar el componente {@code ButtonPanelContainer} en un diseñador visual.
 * Define metadatos como el nombre a mostrar, la descripción, y cómo debe comportarse
 * como un contenedor.
 *
 * @see ButtonPanelContainer
 * @author david
 */
public class ButtonPanelContainerBeanInfo extends SimpleBeanInfo {

    /**
     * Constructor por defecto.
     * No realiza ninguna operación especial.
     */
    public ButtonPanelContainerBeanInfo() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    /**
     * Obtiene el descriptor del Bean, que contiene información global sobre el componente.
     * <p>
     * Aquí se especifica que el componente es un contenedor y se delega la gestión
     * de los hijos a un método específico.
     *
     * @return un {@link BeanDescriptor} con la configuración del componente.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(ButtonPanelContainer.class, null);
        
        // --- Metadatos para el diseñador visual ---

        // 1. Indica al IDE que este componente puede contener otros.
        bd.setValue("isContainer", Boolean.TRUE);
        
        // 2. Indica al IDE a qué método debe llamar para obtener el panel
        //    real donde se añadirán los componentes hijos (al arrastrar y soltar).
        bd.setValue("containerDelegate", "getContentPane");
        
        // 3. Define el nombre que se mostrará en la paleta de componentes del IDE.
        bd.setDisplayName("Button Panel Container");
        
        // 4. Define una descripción breve que aparece como tooltip o en la lista de propiedades.
        bd.setShortDescription("Contenedor que aloja un único componente junto a una botonera de acciones.");
        
        return bd;
    }

    /**
     * Obtiene los descriptores de las propiedades del Bean.
     * <p>
     * Este método se utiliza para personalizar cómo se muestran las propiedades
     * en el editor de propiedades del IDE. En este caso, no se realizan modificaciones,
     * por lo que se devuelven los descriptores por defecto.
     *
     * @return un array de {@link PropertyDescriptor} para las propiedades del bean.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            // Se obtienen todos los descriptores que el IDE infiere por defecto
            // a través de la introspección (analizando los métodos get/set).
            PropertyDescriptor[] pds = super.getPropertyDescriptors();

            // En esta implementación no se modifica ninguna propiedad, pero se
            // podría iterar sobre 'pds' para encontrar una propiedad por su nombre
            // y personalizar su editor (por ejemplo, para crear un desplegable).
            
            return pds;

        } catch (Exception e) {
            // En caso de error durante la introspección, se imprime el error
            // y se devuelve null para que el IDE use su comportamiento por defecto.
            e.printStackTrace();
            return null;
        }
    }
}