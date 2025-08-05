
package es.uned.TFGDavidGiner.components.containers;

import java.beans.*;

import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Arrays;
import java.util.List;

/**
 * Proporciona la información del JavaBean (BeanInfo) para el componente {@link TabContainer}.
 * <p>
 * Esta clase es esencial para que las herramientas de desarrollo visual (como el diseñador de GUI de un IDE)
 * reconozcan a {@code TabContainer} como un contenedor en el que se pueden soltar otros componentes.
 * Define metadatos clave como el nombre para mostrar, la descripción, su comportamiento
 * como contenedor y la categorización de sus propiedades personalizadas.
 *
 * @author David Giner
 * @version 1.1
 * @see java.beans.SimpleBeanInfo
 * @see es.uned.TFGDavidGiner.components.containers.TabContainer
 */
public class TabContainerBeanInfo extends SimpleBeanInfo {
    
    /**
     * Constructor por defecto.
     */
    public TabContainerBeanInfo() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
     /**
     * Devuelve el BeanDescriptor, que contiene información global sobre el componente.
     * <p>
     * Este método es crucial para la integración con el diseñador de GUI.
     * <ul>
     * <li><b>isContainer</b>: {@code TRUE} indica al IDE que este
     * componente puede alojar otros componentes hijos.</li>
     * <li><b>containerDelegate</b>: Especifica el nombre del método ("getContentPane")
     * que el IDE debe invocar para obtener el contenedor real donde se deben añadir los componentes
     * hijos. Esto es necesario porque el contenedor real no es el propio {@code TabContainer},
     * sino que delega esa función el ContentPane interno.</li>
     * </ul>
     * También establece un nombre y una descripción para mostrar en la paleta de componentes del IDE.
     *
     * @return un {@link BeanDescriptor} que describe el comportamiento del bean.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(TabContainer.class, null); // Use null for customizer class unless you have one
        // Indica al IDE que este bean es un contenedor.
        bd.setValue("isContainer", Boolean.TRUE);
        //  Indica al IDE a qué sub-componente debe añadir los hijos.
        bd.setValue("containerDelegate", "getContentPane");
        // Metadatos del componente para mostrarlos en el IDE.
        bd.setDisplayName("TabContainer");
        bd.setShortDescription("Contenedor de componentes de tipo BaseComponent que se organizan en pestañas.");
        return bd;
    }
    
    /**
     * Devuelve los descriptores de las propiedades del bean que serán expuestas en un
     * editor de propiedades (por ejemplo, el panel "Properties" de NetBeans).
     * <p>
     * Este método utiliza introspección para obtener las propiedades de {@code TabContainer}
     * y luego modifica sus descriptores para agrupar la propiedad personalizada
     * {@code tabTitles} en una categoría específica, mejorando la organización
     * y la usabilidad en el diseñador visual.
     *
     * @return un array de {@link PropertyDescriptor} configurado para el IDE.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
                try {
            // Se utiliza Introspector para obtener de forma robusta la lista completa
            // de propiedades del componente, ignorando este BeanInfo para evitar recursión.
            BeanInfo info = Introspector.getBeanInfo(TabContainer.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
            List<PropertyDescriptor> propertyList = new java.util.ArrayList<>(Arrays.asList(info.getPropertyDescriptors()));

            final String CATEGORIA_PROPIA = "Propiedades del Contenedor";

            // Se itera sobre la lista de propiedades para aplicar personalizaciones.
            for (PropertyDescriptor pd : propertyList) {
                String propertyName = pd.getName();
                // Si la propiedad es la nuestra, se le asigna la categoría personalizada.
                if (propertyName.equals("tabTitles")) {
                    pd.setValue("category", CATEGORIA_PROPIA);
                }
            }

            // Se devuelve la lista final de descriptores ya modificada.
            return propertyList.toArray(new PropertyDescriptor[0]);

        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null; // En caso de error, el IDE usará su comportamiento por defecto.
        }
    }
}