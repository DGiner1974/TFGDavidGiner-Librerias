package es.uned.TFGDavidGiner.components.containers;

import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.BaseContainer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Contenedor que sincroniza un árbol de navegación ({@link javax.swing.JTree}) con un panel
 * de visualización por capas ({@link javax.swing.JLayeredPane}).
 * <p>
 * Este componente extiende {@link BaseContainer} y se estructura mediante un {@link javax.swing.JSplitPane}.
 * A la izquierda se muestra un árbol, y a la derecha, el contenido. Al seleccionar un
 * nodo hoja en el árbol, el componente correspondiente se hace visible en el panel
 * derecho, ocultando los demás.
 * <p>
 * Los componentes hijos se añaden al {@code JLayeredPane} interno y deben ser
 * instancias de {@link BaseComponent}. Automáticamente son envueltos en un
 * {@link JScrollPane} para permitir el desplazamiento.
 *
 * @author David Giner
 * @version 1.0
 * @since 13-07-2025
 */
public class TreeContainer extends BaseContainer {

    //<editor-fold defaultstate="collapsed" desc="Campos de la clase y componentes UI">
    
    /**
     * El panel por capas donde se muestran los componentes hijos.
     */
    private JLayeredPaneCustom jLayeredPane1;
    /**
     * El panel de desplazamiento que contiene el árbol de navegación.
     */
    private javax.swing.JScrollPane jScrollPane1;
    /**
     * El panel divisor que separa el árbol del contenido.
     */
    private javax.swing.JSplitPane jSplitPane1;
    /**
     * El árbol de navegación que controla qué componente es visible.
     */
    private javax.swing.JTree jTree1;
    
    /**
     * El modelo de datos que define la estructura del JTree.
     */
    private TreeModel tree;

    /**
     * Mapa que conecta cada nodo del árbol ({@link TreeNode}) con su componente de UI
     * correspondiente ({@link Component}). Es el núcleo de la sincronización.
     */
    private final Map<TreeNode, Component> nodeComponentMap = new HashMap<>();

    /**
     * Indicador para controlar el estado de inicialización del componente.
     * Se usa para prevenir la ejecución de lógica de actualización durante la carga
     * inicial del componente en un diseñador visual.
     */
    private boolean isDuringInitializationOrLoading = false;
    
    /**
     * Soporte para la gestión de eventos de cambio de propiedad (PropertyChangeEvent).
     * Esencial para el cumplimiento del estándar JavaBeans.
     */
    private PropertyChangeSupport pcs;
    
    /**
     * Almacena la ruta de selección del árbol en tiempo de diseño como una cadena.
     * Esta propiedad permite preseleccionar un nodo desde el editor de propiedades del IDE.
     */
    private String designTimeSelectionPath;

    /**
     * Mantiene una lista ordenada de los componentes hijos según fueron añadidos,
     * para asegurar un mapeo consistente y predecible con los nodos del árbol.
     */
    private final List<Component> componentAdditionOrder = new ArrayList<>();
    //</editor-fold>

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa los componentes visuales (JSplitPane, JTree, JLayeredPane),
     * establece el layout y marca el inicio del modo de inicialización.
     */
    public TreeContainer() {
        try {
            isDuringInitializationOrLoading = true;
            setLayout(new BorderLayout());
            //setSize(400, 400);
            setPreferredSize(new Dimension(200, 150));
            //setMinimumSize(new Dimension(400, 400));
            initComponents();
            // Establece la posición inicial del divisor para que ambos paneles sean visibles.
            jSplitPane1.setDividerLocation(150);
                               
            // Se crea un nodo raíz sin texto
            TreeNode emptyNode = new DefaultMutableTreeNode();
            TreeModel emptyModel = new javax.swing.tree.DefaultTreeModel(emptyNode);
            //Asignar este modelo a la propiedad al iniciar el componente
            this.setEstructuraArbol(emptyModel);
            //Ocultar el nodo raíz para que el árbol parezca totalmente vacío
            jTree1.setRootVisible(false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar TreeContainer: " + e.getMessage());
        }
    }
    
    /**
     * Este método se llama automáticamente justo antes de que el componente sea visible.
     * <p>
     * Realiza la vinculación automática entre los nodos del árbol y los componentes,
     * y finaliza el modo de inicialización para permitir la ejecución de la lógica de actualización.
     */
    @Override
    public void addNotify() {
        super.addNotify(); // Es muy importante llamar al método de la clase padre.
        performAutomaticLinking();
//        SwingUtilities.invokeLater(() -> {
//            isDuringInitializationOrLoading = false;
//        });
        SwingUtilities.invokeLater(() -> {
            if (designTimeSelectionPath != null && !designTimeSelectionPath.isEmpty()) {
                TreePath path = findPathFromString(designTimeSelectionPath);
                if (path != null) {
                    jTree1.setSelectionPath(path);
                    jTree1.scrollPathToVisible(path);
                } else {
                    jTree1.setSelectionPath(null);
                }
            }
            
            // Forzamos una actualización del panel visible DESPUÉS de haber
            // establecido la selección inicial. Esto asegura que si no hay nada
            // seleccionado (path es null), todos los paneles se oculten.
            updateVisibleComponent();
        
            // Marcamos el final del proceso de inicialización.
            isDuringInitializationOrLoading = false;
        });
    }
    
    //<editor-fold defaultstate="collapsed" desc="Lógica de Vinculación Automática (Linking)">

    /**
     * Realiza la vinculación automática entre los nodos hoja del árbol y los
     * componentes hijos.
     * <p>
     * Este método limpia cualquier mapeo existente y luego asocia cada nodo hoja
     * con un componente hijo basándose en su orden de adición.
     */
    private void performAutomaticLinking() {
        nodeComponentMap.clear();

        if (jTree1.getModel() == null) {
            return; // No hay nada que vincular si no hay modelo de árbol.
        }

        // 1. Obtiene todos los nodos hoja del árbol en orden.
        List<TreeNode> leafNodes = getAllLeafNodes();
        
        // 2. Obtiene todos los componentes añadidos por el usuario ordenados.
        List<Component> userComponents = new ArrayList<>(this.componentAdditionOrder);
        
        // 3. Vincula nodos con componentes en el mapa.
        int linkCount = Math.min(leafNodes.size(), userComponents.size());
        for (int i = 0; i < linkCount; i++) {
            nodeComponentMap.put(leafNodes.get(i), userComponents.get(i));
        }
    }

    /**
     * Obtiene una lista de todos los nodos hoja del árbol.
     * @return Una {@link List} de objetos {@link TreeNode}.
     */
    private List<TreeNode> getAllLeafNodes() {
        List<TreeNode> leafNodes = new ArrayList<>();
        if (jTree1.getModel().getRoot() instanceof TreeNode) {
            findLeaves((TreeNode) jTree1.getModel().getRoot(), leafNodes);
        }
        return leafNodes;
    }

    /**
     * Método auxiliar recursivo para encontrar todos los nodos hoja.
     * @param node El nodo actual a explorar.
     * @param leaves La lista donde se acumulan los nodos hoja encontrados.
     */
    private void findLeaves(TreeNode node, List<TreeNode> leaves) {
        if (node.isLeaf()) {
            leaves.add(node);
        } else {
            Enumeration<? extends TreeNode> children = node.children();
            while (children.hasMoreElements()) {
                findLeaves(children.nextElement(), leaves);
            }
        }
    }

    /**
     * Obtiene los componentes de usuario (desenvueltos del JScrollPane)
     * contenidos dentro del JLayeredPane.
     * @return Una {@link List} de los componentes {@link es.uned.TFGDavidGiner.core.BaseComponent} añadidos.
     */
    private List<Component> getContainedUserComponents() {
        List<Component> userComponents = new ArrayList<>();
        for (Component wrapper : jLayeredPane1.getComponents()) {
            if (wrapper instanceof JScrollPane) {
                Component content = ((JScrollPane) wrapper).getViewport().getView();
                if (content instanceof BaseComponent) {
                    userComponents.add((BaseComponent) content);
                }
            }
        }
        return userComponents;
    }
    //</editor-fold>
    
    /**
     * Inicializa los componentes internos de Swing (JSplitPane, JTree, etc.)
     * y configura sus listeners.
     */
    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree(); 
        jLayeredPane1 = new JLayeredPaneCustom();
        
        // El JLayeredPane necesita un layout nulo para posicionar los componentes por coordenadas.
        jLayeredPane1.setLayout(null); 

        jScrollPane1.setViewportView(jTree1);
        jSplitPane1.setLeftComponent(jScrollPane1);
        jSplitPane1.setRightComponent(jLayeredPane1);
        this.add(jSplitPane1, BorderLayout.CENTER);

        // Listener para reaccionar a la selección de nodos en el árbol.
        jTree1.addTreeSelectionListener(this::jTree1ValueChanged);

        // Listener para redimensionar los componentes hijos cuando el panel cambie de tamaño.
        jLayeredPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                for (Component comp : jLayeredPane1.getComponents()) {
                    comp.setBounds(0, 0, jLayeredPane1.getWidth(), jLayeredPane1.getHeight());
                }
            }
        });
    }

    /**
     * Manejador de eventos para la selección de nodos en el JTree.
     * <p>
     * Hace visible el componente asociado al nodo seleccionado y oculta los demás.
     * @param evt El evento de selección del árbol.
     */
    private void jTree1ValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        updateVisibleComponent();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters y Setters de Propiedades">
    
    /**
     * Devuelve el panel de contenido interno.
     * Este método es el "containerDelegate" para que el IDE sepa dónde añadir los componentes.
     * @return El {@link JLayeredPane} interno.
     */
    public JLayeredPane getContentPane() {
        return jLayeredPane1;
    }

    /**
     * Obtiene el modelo de datos del árbol.
     * @return El {@link TreeModel} actual.
     */
    public TreeModel getEstructuraArbol() {
        return tree;
    }

    /**
     * Establece un nuevo modelo de datos para el árbol.
     * @param newTree El nuevo {@link TreeModel}.
     */
    public void setEstructuraArbol(TreeModel newTree) {
        TreeModel oldTree = this.tree;
        this.tree = newTree;
        this.jTree1.setModel(newTree);
        performAutomaticLinking(); // Vuelve a vincular con el nuevo árbol.
        getSupport().firePropertyChange("estructuraArbol", oldTree, newTree);
        //setDesignTimeSelectionPath(""); // Resetea la selección.
    }

    /**
     * Obtiene la ruta de selección en tiempo de diseño como una cadena.
     * @return La ruta de selección.
     */
    public String getDesignTimeSelectionPath() {
        return designTimeSelectionPath;
    }

    /**
     * Establece la selección del árbol en tiempo de diseño a partir de una cadena de texto.
     * @param pathString La ruta al nodo, con elementos separados por ", ".
     */
    public void setDesignTimeSelectionPath(String pathString) {
        this.designTimeSelectionPath = pathString;
        System.out.println("pathString: " + pathString);
        TreePath path = findPathFromString(pathString);
        if (path != null) {
            System.out.println("path: " + path.toString());
            jTree1.setSelectionPath(path);
            jTree1.scrollPathToVisible(path);
         } else {
            jTree1.clearSelection();
         }
            
    }
    
    @Override
    public Dimension getPreferredSize() {
        // Devuelve un tamaño por defecto para que el diseñador de GUI sepa cómo dibujarlo.
        return new Dimension(400, 400);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Métodos Auxiliares para Navegación del Árbol">
    
    /**
     * Busca un {@link TreePath} a partir de una representación en cadena.
     * @param pathString La ruta como cadena (ej. "Raíz, NodoHijo, NodoHoja").
     * @return El {@link TreePath} correspondiente, o {@code null} si no se encuentra.
     */
    private TreePath findPathFromString(String pathString) {
        System.out.println("P1");
        if (pathString == null || pathString.isEmpty() || !(jTree1.getModel().getRoot() instanceof DefaultMutableTreeNode)) {
            System.out.println("P2");
            return null;
        }
        System.out.println("P3");
        try {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) jTree1.getModel().getRoot();
            String[] pathElements = pathString.split(", ");
            System.out.println("P4");
            if (!currentNode.getUserObject().toString().equals(pathElements[0])) return null;
            System.out.println("P5");
            TreePath treePath = new TreePath(currentNode);
            for (int i = 1; i < pathElements.length; i++) {
                DefaultMutableTreeNode nextNode = findChild(currentNode, pathElements[i]);
                System.out.println("P6");
                if (nextNode != null) {
                    currentNode = nextNode;
                    treePath = treePath.pathByAddingChild(currentNode);
                    System.out.println("P7");
                } else {
                    System.out.println("P8");
                    return null; // No se encontró un elemento de la ruta.
                }
            }
            System.out.println("P9");
            return treePath;
        } catch (Exception e) {
            return null; // Retorna null en caso de cualquier error.
        }
    }

    /**
     * Busca un nodo hijo con un objeto de usuario específico.
     * @param parent El nodo padre donde buscar.
     * @param userObject El objeto de usuario (texto del nodo) a encontrar.
     * @return El nodo hijo encontrado, o {@code null}.
     */
    private DefaultMutableTreeNode findChild(DefaultMutableTreeNode parent, String userObject) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
                if (child.getUserObject().toString().equals(userObject)) {
                    return child;
                }
            }
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gestión de PropertyChangeListeners">
    
    /**
     * Obtiene la instancia de PropertyChangeSupport, creándola si es necesario.
     * @return La instancia de PropertyChangeSupport.
     */
    private PropertyChangeSupport getSupport() {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
        return pcs;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        getSupport().addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        getSupport().removePropertyChangeListener(listener);
    }
    //</editor-fold>
    
   /**
    * Actualiza qué componente es visible en el JLayeredPane basándose
    * en el nodo actualmente seleccionado en el JTree.
    */
    private void updateVisibleComponent() {
       Object lastNode = jTree1.getLastSelectedPathComponent();
       if (!(lastNode instanceof TreeNode)) {
           // Si no hay nada seleccionado, ocultamos todos los componentes
           for (Component scrollPane : jLayeredPane1.getComponents()) {
               scrollPane.setVisible(false);
           }
           return;
       }

       Component componentToShow = nodeComponentMap.get((TreeNode) lastNode);

       for (Component scrollPane : jLayeredPane1.getComponents()) {
           if (scrollPane instanceof JScrollPane) {
               Component innerComponent = ((JScrollPane) scrollPane).getViewport().getView();
               scrollPane.setVisible(innerComponent == componentToShow);
           }
       }
       jLayeredPane1.revalidate();
       jLayeredPane1.repaint();
    }

    /**
    * Construye y devuelve la ruta (TreePath) desde el nodo raíz hasta un nodo específico.
    * @param node El nodo para el cual se debe encontrar la ruta.
    * @return un objeto TreePath que representa la ruta completa al nodo.
    */
    private TreePath getPathForNode(TreeNode node) {
       if (node == null) {
           return null;
       }
       // DefaultMutableTreeNode tiene un método conveniente para obtener la ruta.
       if (node instanceof DefaultMutableTreeNode) {
           return new TreePath(((DefaultMutableTreeNode) node).getPath());
       }
       // Fallback por si se usan otros tipos de nodos (más genérico).
       List<TreeNode> path = new ArrayList<>();
       path.add(node);
       TreeNode parent = node.getParent();
       while (parent != null) {
           path.add(0, parent);
           parent = parent.getParent();
       }
       return new TreePath(path.toArray(new TreeNode[0]));
    }
    
    /**
    * Sobrescribe el método de eliminación de componentes para asegurar que la
    * vinculación entre nodos y componentes se actualice correctamente.
    * <p>
    * Cuando un componente es eliminado (por ejemplo, desde el panel Navigator del IDE),
    * este método se encarga de quitarlo de la vista y de invocar de nuevo el
    * proceso de vinculación automática. Esto permite que los componentes que pudieran
    * haber quedado sin un nodo asignado ocupen los huecos libres.
    *
    * @param comp El componente a eliminar.
    */
   @Override
   public void remove(Component comp) {
       // Busca el JScrollPane que envuelve al componente que se quiere eliminar.
       Component wrapperToRemove = null;
       for (Component wrapper : jLayeredPane1.getComponents()) {
           if (wrapper instanceof JScrollPane && ((JScrollPane) wrapper).getViewport().getView() == comp) {
               wrapperToRemove = wrapper;
               break;
           }
       }

       // Si se encuentra el envoltorio, se elimina del JLayeredPane.
       if (wrapperToRemove != null) {
           jLayeredPane1.remove(wrapperToRemove);
       }

        // Se elimina el componente de nuestra lista de orden
        componentAdditionOrder.remove(comp);
       
       // Se llama al método de la superclase para completar el proceso de eliminación.
       super.remove(comp);

       // Se fuerza una revinculación y un refresco de la UI.
       performAutomaticLinking();
       updateVisibleComponent();
       revalidate();
       repaint();
   }

   /**
    * Sobrescribe el método de eliminación por índice para que delegue en la
    * lógica principal de eliminación.
    *
    * @param index El índice del componente a eliminar.
    */
   @Override
   public void remove(int index) {
       // Obtiene el componente en el índice y llama a la versión sobrecargada.
       if (index >= 0 && index < getComponentCount()) {
           Component comp = getComponent(index);
           remove(comp);
       }
   }
    
    /**
     * Subclase personalizada de JLayeredPane que envuelve automáticamente los
     * componentes hijos en JScrollPanes y valida su tipo.
     */
    private class JLayeredPaneCustom extends JLayeredPane {
        
        /**
        * Prepara un componente antes de ser añadido, deshabilitando su borde
        * si se trata de un contenedor.
        * @param component El componente a preparar.
        */
        private void prepareComponentForAddition(Component component) {
            if (component instanceof BaseContainer) {
                ((BaseContainer) component).setShowBorder(false);
            }
        }
        
        /**
         * Crea un JScrollPane para contener el componente dado.
         * @param componente El componente a envolver.
         * @return Un nuevo JScrollPane que contiene el componente.
         */
        private JScrollPane crearPanelContenedor(Component componente) {
            JScrollPane panelContenedor = new JScrollPane(componente);
            panelContenedor.setBounds(0, 0, this.getWidth(), this.getHeight());
            panelContenedor.setVisible(false); // Por defecto, los componentes están ocultos.
            return panelContenedor;
        }
        
        //<editor-fold defaultstate="collapsed" desc="Sobrescritura de métodos 'add' y 'remove'">
        /**
         * Sobrescribe el método de adición para validar el tipo de componente,
         * envolverlo en un JScrollPane, registrar su orden de adición y actualizar
         * la interfaz de usuario.
         *
         * @param comp El componente a añadir.
         * @param index La posición en la que se añadirá.
         * @return El componente original que se ha añadido.
         */
        @Override
        public Component add(Component comp, int index) {
            // 1. Lógica de validación 
            // Un ButtonPanelContainer nunca puede ser un componente hijo.
            if (comp instanceof ButtonPanelContainer) {
                if (!TreeContainer.this.isDuringInitializationOrLoading) {
                    JOptionPane.showMessageDialog(this,
                            "Un ButtonPanelContainer no puede ser añadido dentro de otro contenedor.",
                            "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                }
                return comp; // Se devuelve sin añadirlo.
            }
            
            // Los componentes hijo sólo pueden ser BaseComponent.
            if (!(comp instanceof BaseComponent)) {
                if (!TreeContainer.this.isDuringInitializationOrLoading) {
                    JOptionPane.showMessageDialog(this,
                            "Solo se pueden arrastrar componentes de tipo BaseComponent a este contenedor.",
                            "Componente no válido", JOptionPane.WARNING_MESSAGE);
                }
                return comp;
            }
            prepareComponentForAddition(comp);

            // 2. Se añade el componente a la UI y a nuestra lista de orden
            JScrollPane panelContenedor = crearPanelContenedor(comp);
            super.add(panelContenedor, index);
            componentAdditionOrder.add(comp);

            // 3. Se actualiza el mapa que vincula nodos y componentes
            performAutomaticLinking();

            // 4. Buscamos el nodo que corresponde al componente que acabamos de añadir.
            TreeNode nodeToSelect = null;
            for (Map.Entry<TreeNode, Component> entry : nodeComponentMap.entrySet()) {
                if (entry.getValue() == comp) {
                    nodeToSelect = entry.getKey();
                    break;
                }
            }

            // 5. Si encontramos el nodo, construimos su ruta y la seleccionamos en el árbol.
            if (nodeToSelect != null) {
                TreePath pathToSelect = getPathForNode(nodeToSelect); // (Asegúrate de tener este método auxiliar)
                if (pathToSelect != null) {
                    jTree1.setSelectionPath(pathToSelect);
                    jTree1.scrollPathToVisible(pathToSelect); // Asegura que el nodo sea visible
                }
            }

            // 6. Se llama a updateVisibleComponent, que ahora usará la nueva selección.
            updateVisibleComponent();
            repaint();

            return comp;
        }
        
        @Override
        public void add(Component comp, Object constraints) {
            if (comp instanceof BaseComponent) {
                // Llamar al método de ayuda
                prepareComponentForAddition(comp);
        
                JScrollPane panelContenedor = crearPanelContenedor(comp);
                super.add(panelContenedor, constraints);
                repaint();
            }
        }
        
        @Override
        public Component add(Component comp) {
            return this.add(comp, -1);
        }
        
        @Override
        public void add(Component comp, Object constraints, int index) {
            if (comp instanceof BaseComponent) {
                // Llamar al método de ayuda
                prepareComponentForAddition(comp);
                
                JScrollPane panelContenedor = crearPanelContenedor(comp);
                super.add(panelContenedor, constraints, index);
                repaint();
            }
        }
        
        @Override
        public Component add(String name, Component comp) {
            if (comp instanceof BaseComponent) {
                // Llamar al método de ayuda
                prepareComponentForAddition(comp);
                
                JScrollPane panelContenedor = crearPanelContenedor(comp);
                super.add(name, panelContenedor);
                repaint();
            }
            return comp;
        }
        
        /**
        * Sobrescribe el método de eliminación para delegar la lógica al
        * TreeContainer principal, asegurando que se actualice el estado interno.
        * @param comp El componente a eliminar.
        */
       @Override
       public void remove(Component comp) {
           // En lugar de manejar la lógica aquí, la delegamos al método remove
           // del TreeContainer, que ya contiene la lógica correcta para
           // actualizar la lista de orden y revincular los componentes.
           TreeContainer.this.remove(comp);
       }
        //</editor-fold>
    
    }
   
}