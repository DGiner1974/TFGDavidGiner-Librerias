package es.uned.TFGDavidGiner.components.containers;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Proporciona la información del JavaBean (BeanInfo) para la clase {@link SimpleContainer}.
 * <p>
 * Esta clase es utilizada por los entornos de desarrollo (IDEs) para entender cómo
 * mostrar y manejar el componente {@code SimpleContainer} en un diseñador visual.
 * Define metadatos como el nombre a mostrar, su comportamiento como contenedor y
 * personaliza la presentación de sus propiedades en el editor del IDE, agrupándolas
 * en categorías personalizadas.
 *
 * @see SimpleContainer
 * @author david
 * @version 1.1
 * @since 13-07-2025
 */
public class SimpleContainerBeanInfo extends SimpleBeanInfo {

    /**
     * Constructor por defecto.
     */
    public SimpleContainerBeanInfo() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    /**
     * Obtiene el descriptor del Bean, que contiene información global sobre el componente.
     * <p>
     * Especifica que el componente es un contenedor, a qué método debe delegar la
     * adición de hijos y cómo debe mostrarse en la paleta del IDE.
     *
     * @return un {@link BeanDescriptor} con la configuración del componente.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(SimpleContainer.class, null);

        // --- Metadatos para el diseñador visual ---

        // 1. Indica al IDE que este componente puede contener otros.
        bd.setValue("isContainer", Boolean.TRUE);

        // 2. Indica al IDE a qué método debe llamar para obtener el panel
        //    real donde se añadirán los componentes hijos (al arrastrar y soltar).
        bd.setValue("containerDelegate", "getContentPane");

        // 3. Define el nombre que se mostrará en la paleta de componentes del IDE.
        bd.setDisplayName("SimpleContainer");

        // 4. Define una descripción breve que aparece como tooltip o en la lista de propiedades.
        bd.setShortDescription("Contenedor que divide el espacio para un máximo de 2 componentes.");

        return bd;
    }

    /**
     * Obtiene los descriptores de las propiedades del Bean, permitiendo su personalización.
     * <p>
     * Este método utiliza la introspección de Java para obtener todas las propiedades
     * del componente {@code SimpleContainer}. Luego, modifica los descriptores para
     * agrupar las propiedades personalizadas (`orientation`, `dividerLocation`, `dividerSize`)
     * en una categoría específica ("Propiedades del Contenedor") dentro de la ventana de
     * propiedades del IDE, mejorando la usabilidad para el diseñador.
     *
     * @return un array de {@link PropertyDescriptor} para las propiedades del bean.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            // Se utiliza Introspector para obtener de forma robusta la lista completa
            // de propiedades del componente, ignorando este BeanInfo para evitar recursión.
            BeanInfo info = Introspector.getBeanInfo(SimpleContainer.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
            List<PropertyDescriptor> propertyList = new ArrayList<>(Arrays.asList(info.getPropertyDescriptors()));

            final String CATEGORIA_PROPIA = "Propiedades del Contenedor";

            // Se itera sobre la lista de propiedades para aplicar personalizaciones.
            for (PropertyDescriptor pd : propertyList) {
                String propertyName = pd.getName();
                // Si la propiedad es una de las nuestras, se le asigna la categoría personalizada.
                if (propertyName.equals("dividerLocation") || propertyName.equals("dividerSize") || propertyName.equals("orientation")) {
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