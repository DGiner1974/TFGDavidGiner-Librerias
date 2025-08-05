package es.uned.TFGDavidGiner.components.containers;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.SimpleBeanInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Proporciona la información del JavaBean (BeanInfo) para el componente {@link TreeContainer}.
 * <p>
 * Esta clase es fundamental para que un entorno de desarrollo integrado (IDE)
 * entienda cómo interactuar con el componente {@code TreeContainer}. Sus principales
 * responsabilidades son:
 * <ol>
 * <li>Declarar que {@code TreeContainer} es un contenedor visual.</li>
 * <li>Especificar cuál es el sub-componente que gestiona la adición de hijos.</li>
 * <li>Personalizar el editor de propiedades para una experiencia de diseño mejorada.</li>
 * </ol>
 *
 * @author David Giner
 * @version 1.0
 * @since 13-07-2025
 * @see java.beans.SimpleBeanInfo
 * @see es.uned.TFGDavidGiner.components.containers.TreeContainer
 */
public class TreeContainerBeanInfo extends SimpleBeanInfo {

    /**
     * Constructor por defecto.
     */
    public TreeContainerBeanInfo() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    /**
     * Devuelve un {@link BeanDescriptor} para el {@code TreeContainer}.
     * <p>
     * Este método es crucial para informar al IDE sobre las propiedades
     * generales del bean.
     * <ul>
     * <li><b>isContainer</b>: Se establece a {@code true} para indicar al IDE que este
     * componente puede alojar otros componentes.</li>
     * <li><b>containerDelegate</b>: Especifica el nombre del método ("getContentPane")
     * que el IDE debe invocar para obtener el contenedor real donde se deben
     * añadir los componentes hijos.</li>
     * </ul>
     * También establece un nombre y una descripción para mostrar en la paleta de
     * componentes del IDE.
     *
     * @return un {@link BeanDescriptor} que describe el comportamiento del bean.
     */
    @Override
    public BeanDescriptor getBeanDescriptor() {
        BeanDescriptor bd = new BeanDescriptor(TreeContainer.class, null);

        // --- Metadatos para el diseñador visual ---

        // 1. Indica al IDE que este bean es un contenedor.
        bd.setValue("isContainer", Boolean.TRUE);
        // 2. Indica al IDE a qué subcomponente debe añadir los hijos.
        bd.setValue("containerDelegate", "getContentPane");
        // 3. Metadatos del componente para mostrarlos en el IDE.
        bd.setDisplayName("TreeContainer");
        bd.setShortDescription("Contenedor que organiza componentes según un árbol de navegación.");
        
        return bd;
    }

    /**
     * Devuelve los descriptores de las propiedades del bean, personalizando cómo
     * se editan en el inspector de propiedades del IDE.
     * <p>
     * Esta implementación realiza varias personalizaciones:
     * <ul>
     * <li><b>Agrupa</b> las propiedades clave del componente en una categoría personalizada.</li>
     * <li>Asigna un **editor visual** a la propiedad {@code designTimeSelectionPath}.</li>
     * <li>Establece un **valor por defecto** para la propiedad {@code estructuraArbol},
     * mejorando la experiencia en el editor de modelos de árbol del IDE.</li>
     * </ul>
     *
     * @return un array de {@link PropertyDescriptor} configurado para el IDE.
     */
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            // Se utiliza Introspector para obtener de forma robusta la lista completa de propiedades.
            final BeanInfo defaultInfo = Introspector.getBeanInfo(TreeContainer.class, Introspector.IGNORE_IMMEDIATE_BEANINFO);
            List<PropertyDescriptor> propertyList = new ArrayList<>(Arrays.asList(defaultInfo.getPropertyDescriptors()));

            // Se elimina el descriptor por defecto de 'designTimeSelectionPath' para reemplazarlo.
            propertyList.removeIf(pd -> pd.getName().equals("designTimeSelectionPath"));

            // Se crea un descriptor totalmente personalizado para 'designTimeSelectionPath'.
            PropertyDescriptor pathDescriptor = new PropertyDescriptor("designTimeSelectionPath", TreeContainer.class) {
                /**
                 * Sobrescribe este método para devolver una instancia de nuestro editor personalizado.
                 * La clave es que recibimos el 'bean' (la instancia de TreeContainer que se está
                 * editando) y se lo pasamos al constructor del editor, permitiendo que el editor
                 * acceda al árbol del componente.
                 * @param bean La instancia del componente que está siendo editada en el IDE.
                 * @return una nueva instancia de {@link TreeNodeSelectionEditor}.
                 */
                @Override
                public PropertyEditor createPropertyEditor(Object bean) {
                    // Pasamos la instancia de TreeContainer al editor para que pueda acceder a su JTree.
                    return new TreeNodeSelectionEditor((TreeContainer) bean);
                }
            };
            
            // Configuramos los metadatos de esta propiedad para el IDE.
            pathDescriptor.setDisplayName("Nodo Visible por Defecto");
            pathDescriptor.setShortDescription("Define qué nodo del árbol se muestra al iniciar en el diseñador.");
            
            // Añadimos nuestro descriptor personalizado a la lista.
            propertyList.add(pathDescriptor);
            
            final String CATEGORIA_PROPIA = "Propiedades del Contenedor";

            // Se itera sobre la lista final de propiedades para aplicar personalizaciones.
            for (PropertyDescriptor pd : propertyList) {
                String propertyName = pd.getName();
                // Se agrupan las propiedades personalizadas en una categoría propia.
                if (propertyName.equals("designTimeSelectionPath") || propertyName.equals("estructuraArbol")) {
                    pd.setValue("category", CATEGORIA_PROPIA);
                }
                
                // Se establece un valor por defecto para 'estructuraArbol' para mejorar la experiencia en el editor del IDE.
                if ("estructuraArbol".equals(pd.getName())) {
                    // Creamos el modelo de árbol vacío que queremos como valor por defecto
                    TreeNode rootNode = new DefaultMutableTreeNode("root");
                    TreeModel emptyModel = new javax.swing.tree.DefaultTreeModel(rootNode);

                    // Establecemos este objeto como el "valor por defecto" de la propiedad
                    pd.setValue("defaultValue", emptyModel);
                    break; // Salimos del bucle una vez encontrado y modificado
                }
            }
            
            // 7. Devolvemos la lista final de descriptores como un array.
            return propertyList.toArray(new PropertyDescriptor[0]);

        } catch (IntrospectionException e) {
            e.printStackTrace();
            return null; // En caso de error, el IDE usará el comportamiento por defecto.
        }
    }
}