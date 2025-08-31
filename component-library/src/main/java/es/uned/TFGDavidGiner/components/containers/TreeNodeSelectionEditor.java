package es.uned.TFGDavidGiner.components.containers;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Editor de propiedades personalizado para la propiedad {@code designTimeSelectionPath}
 * del componente {@link TreeContainer}.
 * <p>
 * Este editor proporciona una interfaz gráfica (un {@link JTree}) que permite al
 * usuario seleccionar visualmente un nodo del árbol del componente que está editando.
 * El valor de la propiedad se gestiona como una cadena de texto que representa la
 * ruta al nodo seleccionado (ej. "Raíz, NodoHijo, NodoHoja").
 * <p>
 * Implementa {@link PropertyChangeListener} para mantenerse sincronizado si el
 * modelo del árbol en el {@code TreeContainer} cambia.
 *
 * @author david
 * @version 1.0
 * @since 13-07-2025
 * @see java.beans.PropertyEditorSupport
 * @see es.uned.TFGDavidGiner.components.containers.TreeContainer
 */
public class TreeNodeSelectionEditor extends PropertyEditorSupport implements PropertyChangeListener {

    //<editor-fold defaultstate="collapsed" desc="Campos de la clase">
    /**
     * Referencia a la instancia del componente (el TreeContainer) que se está editando.
     * Es fundamental para poder acceder a su modelo de árbol.
     */
    private final TreeContainer beanInstance;

    /**
     * Componente UI del editor (un JScrollPane que contiene el JTree).
     * Se inicializa de forma perezosa (lazy-loaded) la primera vez que se necesita.
     */
    private JScrollPane editorComponent;

    /**
     * El árbol utilizado en el panel del editor para que el usuario realice la selección.
     */
    private JTree selectionTree;
    //</editor-fold>

    /**
     * Constructor del editor.
     *
     * @param bean La instancia del {@link TreeContainer} que se está editando en el IDE.
     */
    public TreeNodeSelectionEditor(TreeContainer bean) {
        this.beanInstance = bean;
        // Se registra como listener de cambios en el bean para saber si su árbol cambia.
        if (this.beanInstance != null) {
            this.beanInstance.addPropertyChangeListener(this);
        }
    }

    /**
     * Indica al IDE que este editor proporciona una interfaz gráfica personalizada.
     * @return siempre {@code true}.
     */
    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    /**
     * Devuelve el componente de la interfaz gráfica que se mostrará al usuario
     * para editar la propiedad.
     * <p>
     * Este método se encarga de:
     * <ol>
     * <li>Crear el JTree la primera vez que se solicita (inicialización perezosa).</li>
     * <li>Configurar un listener en el JTree para actualizar el valor de la propiedad
     * cuando el usuario selecciona un nodo.</li>
     * <li>Sincronizar el modelo de datos del JTree del editor con el del componente
     * que se está editando.</li>
     * </ol>
     * @return Un {@link Component} (en este caso, un JScrollPane con un JTree) para la edición.
     */
    @Override
    public Component getCustomEditor() {
        // 1. Si es la primera vez que se abre el editor, se crea su UI.
        if (editorComponent == null) {
            selectionTree = new JTree();
            editorComponent = new JScrollPane(selectionTree);

            // 2. Se añade un listener al árbol del editor.
            selectionTree.getSelectionModel().addTreeSelectionListener(selectionEvent -> {
                TreePath selectedPath = selectionEvent.getNewLeadSelectionPath();
                if (selectedPath != null) {
                    // 3. Cuando el usuario selecciona un nodo, se convierte la ruta a String.
                    String pathString = convertPathToString(selectedPath);
                    // 4. Se establece el nuevo valor en el editor. El IDE lo aplicará si se pulsa "Aceptar".
                    setValue(pathString);
                }
            });
        }

        // 5. Cada vez que se abre el editor, se asegura de que tiene el modelo de árbol más reciente.
        if (this.beanInstance != null) {
            TreeModel currentModel = this.beanInstance.getEstructuraArbol();
            if (currentModel != null) {
                selectionTree.setModel(currentModel);
            }
        }

        // 6. Se devuelve el componente UI del editor.
        return editorComponent;
    }

    /**
     * Escucha los cambios de propiedad en el bean que se está editando.
     * <p>
     * Si la propiedad "estructuraArbol" del {@code TreeContainer} cambia,
     * este método actualiza el modelo del árbol en el editor para que ambos
     * permanezcan sincronizados.
     *
     * @param evt El evento de cambio de propiedad.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("estructuraArbol".equals(evt.getPropertyName()) && selectionTree != null) {
            // Si el modelo del árbol en el bean cambia, actualizamos el de nuestro editor.
            selectionTree.setModel((TreeModel) evt.getNewValue());
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Gestión del valor como String">
    /**
     * Genera la cadena de texto de inicialización de Java para el valor de esta propiedad.
     * <p>
     * El IDE utiliza esta cadena para generar el código en el método {@code initComponents}.
     * Por ejemplo, si el valor es "Raíz, Nodo1", este método devolverá {@code "\"Raíz, Nodo1\""}.
     *
     * @return La cadena de inicialización de código Java.
     */
    @Override
    public String getJavaInitializationString() {
        String value = (String) getValue();
        if (value == null || value.isEmpty()) {
            return "null";
        }
        // Escapa caracteres especiales para que sea una cadena Java válida.
        value = value.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + value + "\"";
    }

    /**
     * Devuelve el valor de la propiedad como una cadena de texto.
     * @return El valor actual como un {@link String}.
     */
    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null) ? value.toString() : "";
    }
    
    /**
     * Convierte un {@link TreePath} a su representación en cadena de texto.
     * @param path La ruta del árbol a convertir.
     * @return Una cadena con los nombres de los nodos separados por ", ".
     */
    private String convertPathToString(TreePath path) {
        StringBuilder pathString = new StringBuilder();
        Object[] pathArray = path.getPath();
        for (int i = 0; i < pathArray.length; i++) {
            if (pathArray[i] instanceof DefaultMutableTreeNode) {
                pathString.append(((DefaultMutableTreeNode) pathArray[i]).getUserObject().toString());
                if (i < pathArray.length - 1) {
                    pathString.append(", ");
                }
            }
        }
        return pathString.toString();
    }
    //</editor-fold>
}