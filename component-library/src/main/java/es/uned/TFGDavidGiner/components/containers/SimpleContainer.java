package es.uned.TFGDavidGiner.components.containers;

import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.BaseContainer;
import javax.swing.*;
import java.awt.*;

/**
 * Contenedor simple que utiliza un {@link JSplitPane} para mostrar uno o dos
 * componentes hijos, dividiendo el espacio entre ellos.
 * <p>
 * Esta clase, que hereda de {@link BaseContainer}, actúa como un contenedor
 * fundamental en el framework, permitiendo la composición de interfaces. Solo
 * se pueden añadir componentes que hereden de {@link BaseComponent}.
 * Gestiona la adición y eliminación de componentes en su {@code JSplitPane} interno
 * y expone propiedades para controlar la orientación y la posición del divisor.
 *
 * @author david
 * @version 1.0
 * @since 13-07-2025
 */
public class SimpleContainer extends BaseContainer {

    /**
     * Indicador para controlar el estado de inicialización del componente.
     * <p>
     * Se usa para prevenir la ejecución de lógica de actualización (como validaciones
     * o repintados) durante la carga inicial del componente en un diseñador visual.
     */
    private boolean isDuringInitializationOrLoading = false;

    /**
     * Panel interno que actúa como delegado del contenedor.
     * <p>
     * Este panel es el responsable real de gestionar la adición de componentes hijos.
     */
    private CustomPanel contentPane;

    /**
     * El {@link JSplitPane} que divide el área de contenido en dos.
     */
    private JSplitPane splitPane;

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa la estructura del componente, configurando el layout,
     * el {@link JSplitPane} y el panel de contenido personalizado.
     */
    public SimpleContainer() {
        try {
            // Marca el inicio del proceso de carga para evitar ejecuciones prematuras.
            isDuringInitializationOrLoading = true;
            
            // Configuración del layout principal del contenedor.
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEtchedBorder());
            setPreferredSize(new Dimension(200, 150));

            // Se crea el panel de contenido que gestionará la adición de componentes.
            contentPane = new CustomPanel();
            contentPane.setPreferredSize(new Dimension(0, 0));
            add(contentPane, BorderLayout.NORTH); // Se añade para que el IDE lo detecte.

            // Se crea y configura el JSplitPane.
            splitPane = new JSplitPane();
            splitPane.setLeftComponent(null);
            splitPane.setRightComponent(null);
            add(splitPane, BorderLayout.CENTER);

            // Establece una orientación por defecto.
            setOrientation(SplitOrientation.HORIZONTAL);
            
            if (getShowBorder()) {
                setBorder(BorderFactory.createEtchedBorder());
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar SimpleContainer: " + e.getMessage());
        }
    }

    /**
     * Se notifica a este componente que ha sido añadido a un contenedor.
     * <p>
     * Se aprovecha este método del ciclo de vida de Swing para marcar el fin
     * de la inicialización, permitiendo que la lógica de actualización se ejecute.
     */
    @Override
    public void addNotify() {
        SwingUtilities.invokeLater(() -> {
            super.addNotify();
            isDuringInitializationOrLoading = false;
        });
    }

    /**
     * Devuelve el panel de contenido interno.
     * <p>
     * Este método es fundamental para que los entornos de desarrollo (IDEs)
     * sepan cuál es el contenedor real al que deben añadirse los componentes
     * hijos al arrastrar y soltar (el "container delegate").
     *
     * @return El {@link JPanel} que actúa como contenedor delegado.
     */
    public JPanel getContentPane() {
        return contentPane;
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters y Setters de Propiedades del JSplitPane">
    
    /**
     * Obtiene la orientación actual del divisor.
     * @return El valor de {@link SplitOrientation} (HORIZONTAL o VERTICAL).
     */
    public SplitOrientation getOrientation() {
        int currentSplitOrientation = splitPane.getOrientation();
        if (currentSplitOrientation == JSplitPane.HORIZONTAL_SPLIT) {
            return SplitOrientation.HORIZONTAL;
        } else {
            return SplitOrientation.VERTICAL;
        }
    }

    /**
     * Establece la orientación del divisor.
     * @param newOrientation La nueva orientación (no puede ser nula).
     */
    public void setOrientation(SplitOrientation newOrientation) {
        if (newOrientation == null) {
            throw new IllegalArgumentException("La orientación no puede ser nula.");
        }
        splitPane.setOrientation(newOrientation.getSwingConstant());
    }

    /**
     * Obtiene la posición del divisor en píxeles.
     * @return La ubicación actual del divisor.
     */
    public int getDividerLocation() {
        return splitPane.getDividerLocation();
    }

    /**
     * Establece la posición del divisor.
     * @param location La nueva ubicación en píxeles.
     */
    public void setDividerLocation(int location) {
        splitPane.setDividerLocation(location);
    }

    /**
     * Obtiene el tamaño del divisor en píxeles.
     * @return El tamaño actual del divisor.
     */
    public int getDividerSize() {
        return splitPane.getDividerSize();
    }

    /**
     * Establece el tamaño del divisor.
     * @param size El nuevo tamaño en píxeles.
     */
    public void setDividerSize(int size) {
        splitPane.setDividerSize(size);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Gestión de Componentes (Añadir/Eliminar)">

    /**
     * Sobrescribe el método {@code remove} para manejar la eliminación de un componente
     * que está visualmente en el {@link JSplitPane}.
     *
     * @param comp el componente a eliminar.
     */
    @Override
    public void remove(Component comp) {
        // Comprueba si el componente a eliminar está en el JSplitPane y lo quita.
        if (comp == splitPane.getLeftComponent()) {
            splitPane.setLeftComponent(null);
        } else if (comp == splitPane.getRightComponent()) {
            splitPane.setRightComponent(null);
        }

        super.remove(comp); // Llama a la implementación base.

        // Si no estamos en fase de carga, se actualiza la UI.
        if (!this.isDuringInitializationOrLoading) {
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Sobrescribe el método {@code remove} que usa un índice.
     * @param index el índice del componente a eliminar.
     */
    @Override
    public void remove(int index) {
        // La lógica principal se gestiona en remove(Component), que es más robusto
        // para este caso de uso. Se deja por si el IDE lo invoca.
    }

    /**
     * Clase interna que actúa como el verdadero contenedor de componentes hijos.
     * <p>
     * Controla qué componentes se pueden añadir, restringiéndolos a instancias
     * de {@link BaseComponent} y asegurando que no se añadan más de dos.
     */
    private class CustomPanel extends JPanel {

        /**
         * Sobrescribe el método de adición para validar y colocar los componentes.
         *
         * @param component el componente a añadir.
         * @param index la posición donde añadirlo (no se usa directamente).
         * @return el componente añadido.
         */
        @Override
        public Component add(Component component, int index) {
            
            // 1. Un ButtonPanelContainer nunca puede ser un componente hijo.
            if (component instanceof ButtonPanelContainer) {
                JOptionPane.showMessageDialog(this,
                        "Un ButtonPanelContainer no puede ser añadido dentro de otro contenedor.",
                        "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                return component; // Se devuelve sin añadirlo.
            }

            // 2. Valida que solo se puedan añadir componentes del framework.
            if (!(component instanceof BaseComponent)) {
                if (!SimpleContainer.this.isDuringInitializationOrLoading) {
                    JOptionPane.showMessageDialog(this,
                            "Solo se pueden arrastrar componentes de tipo BaseComponent a este contenedor.",
                            "Componente no válido", JOptionPane.WARNING_MESSAGE);
                }
                return component; // Se devuelve para que el diseñador lo maneje.
            }

            // 3. Evita añadir el mismo componente dos veces.
            if (component == splitPane.getLeftComponent() || component == splitPane.getRightComponent()) {
                return component;
            }

            if (component instanceof BaseContainer) {
                // Si el componente añadido es un contenedor, le quitamos el borde.
                ((BaseContainer) component).setShowBorder(false);
            }
            
            // 4. Añade el componente al primer hueco libre en el JSplitPane.
            if (splitPane.getLeftComponent() == null) {
                splitPane.setLeftComponent(component);
            } else if (splitPane.getRightComponent() == null) {
                splitPane.setRightComponent(component);
            } else {
                // Si ambos huecos están ocupados, muestra un error.
                JOptionPane.showMessageDialog(this,
                        "Solo se pueden añadir 2 componentes como máximo.",
                        "Contenedor lleno", JOptionPane.WARNING_MESSAGE);
            }

            // 5. Actualiza la UI si es necesario.
            if (!SimpleContainer.this.isDuringInitializationOrLoading) {
                SimpleContainer.this.revalidate();
                SimpleContainer.this.repaint();
            }

            return component;
        }

        /**
         * Sobrescribe el método de adición simple para delegar a la versión con más lógica.
         */
        @Override
        public Component add(Component comp) {
            return this.add(comp, -1);
        }

        /**
         * Sobrescribe el método de eliminación para delegar al contenedor principal.
         */
        @Override
        public void remove(Component comp) {
            // Redirige la llamada al método remove del contenedor padre.
            SimpleContainer.this.remove(comp);
        }
    }
    //</editor-fold>
}