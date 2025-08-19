/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package es.uned.TFGDavidGiner.components.containers;

import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.BaseContainer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener; 
import java.awt.event.ActionEvent; 
import java.awt.Component;
import es.uned.TFGDavidGiner.components.widgets.ButtonPanel;
import es.uned.TFGDavidGiner.core.interfaces.IValidation;

/**
 * Contenedor especializado que combina un área de contenido principal con un
 * button panel de acciones estándar (Aceptar/Cancelar).
 * <p>
 * Utiliza un {@link JSplitPane} para distribuir el espacio entre el componente
 * principal (que debe heredar de {@link BaseComponent}) y la
 * {@link es.uned.TFGDavidGiner.components.widgets.ButtonPanel}. Está diseñado para actuar como un
 * contenedor que aloja un único componente de negocio y le asocia
 * automáticamente los botones de acción.
 *
 * @author david
 * @version 1.0
 * @since 12-07-2025
 */
public class ButtonPanelContainer extends BaseContainer implements ActionListener {

    /**
     * Indicador para controlar el estado de inicialización del componente.
     * <p>
     * Se usa para prevenir la ejecución de lógica de actualización (como validaciones
     * o repintados) durante la carga inicial del componente en un diseñador visual
     * o en tiempo de ejecución.
     */
    private boolean isDuringInitializationOrLoading = false;

    /**
     * Panel interno que actúa como delegado del contenedor.
     * <p>
     * Este panel es el responsable real de gestionar la adición de componentes hijos,
     * aplicando la lógica de validación para asegurar que solo se añadan los componentes permitidos.
     * Se expone a través de {@code getContentPane()} para que los diseñadores visuales
     * interactúen con él.
     */
    private CustomPanel contentPane;
    
    /**
     * El {@link JSplitPane} que divide el área de contenido y el ButtonPanel.
     */
    private JSplitPane splitPane;
    
    /**
     * Instancia de button panel de acciones.
     */
    private ButtonPanel buttonPanel1;

    /**
     * Panel que actúa como un marcador de posición vacío en el {@link JSplitPane}.
     * Ocupa el espacio del componente principal hasta que uno es añadido.
     */
    private final JPanel leftPlaceholder = new JPanel();

    /**
     * Constructor por defecto de {@code ButtonPanelContainer}.
     * <p>
     * Inicializa la estructura del componente, configurando el layout,
     * el {@link JSplitPane}, el panel de contenido personalizado y el ButtonPanel.
     */
    public ButtonPanelContainer() {
        try {
            // Se marca el inicio del proceso de carga.
            isDuringInitializationOrLoading = true;

            // Configuración inicial del panel marcador de posición.
            leftPlaceholder.setPreferredSize(new Dimension(150, 0));
            
            // Configuración del layout principal del contenedor.
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEtchedBorder());
            setPreferredSize(new Dimension(200, 150));
            
            // Se crea el panel de contenido que gestionará la adición de componentes.
            contentPane = new CustomPanel();
            contentPane.setPreferredSize(new Dimension(0, 0));
            add(contentPane, BorderLayout.NORTH); // Se añade en una posición, aunque no será visible directamente.

            // 1. Instanciar y configurar ButtonPanel.
            buttonPanel1 = new ButtonPanel();
            buttonPanel1.setPreferredSize(new Dimension(200, buttonPanel1.getPreferredSize().height));

            // 2. Crear y configurar el JSplitPane.
            splitPane = new JSplitPane();
            splitPane.setLeftComponent(leftPlaceholder); // El contenido irá a la izquierda.
            splitPane.setRightComponent(buttonPanel1);      // El ButtonPanel irá a la derecha (abajo).
            splitPane.setResizeWeight(1.0); // Todo el espacio extra al redimensionar se asigna al componente izquierdo.
            splitPane.setEnabled(false);    // Deshabilita el movimiento del divisor por el usuario.

            buttonPanel1.addActionListener(this); 

            
            // Se añade el splitPane al layout principal.
            add(splitPane, BorderLayout.CENTER);

            // Se establece la orientación por defecto.
            setOrientation(SplitOrientation.VERTICAL);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar ButtonPanelContainer: " + e.getMessage());
        }
    }
    
    /**
     * Maneja los eventos de acción provenientes del {@link ButtonPanel} interno.
     * <p>
     * Este método actúa como el controlador principal para la lógica de Aceptar/Cancelar.
     * Basándose en el comando de acción del evento, invoca de forma recursiva
     * los métodos {@link #validar()} o {@link #configurar()} sobre el componente
     * de contenido principal, propagando la acción a toda la jerarquía de
     * componentes anidados.
     *
     * @param evt El {@link ActionEvent} recibido desde el panel de botones.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        // Obtenemos los componentes del contenedor aunque sólo aplicamos la lógica de validar/cancelar sobre los que implementan IValidation
        // que sólo será uno que aplicará la validación de forma recursiva si es un contenedor.
        Component[] componentesAValidar = this.splitPane.getComponents();

        // Comprobamos el comando del evento para saber qué botón se pulsó
        String command = evt.getActionCommand();

        if (ButtonPanel.ACCEPT_COMMAND.equals(command)) {
            // --- LÓGICA DE ACEPTAR (VALIDAR) ---
            for (Component c : componentesAValidar) {
                if (c instanceof IValidation) {
                    IValidation componenteValidable = (IValidation) c;
                    if (!componenteValidable.validar()) {
                        JOptionPane.showMessageDialog(this, componenteValidable.getError(), "Error de validación", JOptionPane.ERROR_MESSAGE);
                        return; 
                    }
                }
            }
            // Opcional: Si todo es válido, mostrar un mensaje de éxito.
            JOptionPane.showMessageDialog(this, "Todos los componentes son válidos.", "Validación Correcta", JOptionPane.INFORMATION_MESSAGE);

        } else if (ButtonPanel.CANCEL_COMMAND.equals(command)) {
            // --- LÓGICA DE CANCELAR (CONFIGURAR/RESETEAR) ---
            for (Component c : componentesAValidar) {
                if (c instanceof IValidation) {
                    ((IValidation) c).configurar();
                }
            }
        }
    }

    /**
     * Se notifica a este componente que ha sido añadido a un contenedor.
     * <p>
     * Se aprovecha este método del ciclo de vida de Swing para marcar el fin
     * de la inicialización, usando {@code SwingUtilities.invokeLater} para asegurar
     * que se ejecute después de que todos los eventos de AWT hayan sido procesados.
     */
    @Override
    public void addNotify() {
        SwingUtilities.invokeLater(() -> {
            super.addNotify();
            // La inicialización ha terminado, se permite la ejecución de lógica de actualización.
            isDuringInitializationOrLoading = false;
        });
    }

    /**
     * Devuelve el panel de contenido interno.
     * <p>
     * Este método es fundamental para que los entornos de desarrollo (IDEs)
     * sepan cuál es el contenedor real al que deben añadirse los componentes
     * hijos al arrastrar y soltar.
     *
     * @return El {@link JPanel} que actúa como contenedor delegado.
     */
    public JPanel getContentPane() {
        return contentPane;
    }
    
    /**
     * Establece la orientación del {@link JSplitPane} interno.
     * <p>
     * Este método es privado para asegurar que la orientación del contenedor
     * sea siempre vertical, manteniendo una disposición estándar con el contenido
     * arriba y la botonera abajo.
     *
     * @param newOrientation La nueva orientación (siempre {@code VERTICAL}).
     */
    private void setOrientation(SplitOrientation newOrientation) {
        if (newOrientation == null) {
            throw new IllegalArgumentException("La orientación no puede ser nula.");
        }
        // Actualiza la orientación del JSplitPane.
        splitPane.setOrientation(newOrientation.getSwingConstant());
    }

    /**
     * Elimina un componente de este contenedor.
     * <p>
     * Este método se sobrescribe para interceptar la eliminación de componentes
     * en el diseñador visual. Si el componente que se elimina es el que está
     * en el {@link JSplitPane}, lo reemplaza por el marcador de posición.
     *
     * @param comp El componente a eliminar.
     */
    @Override
    public void remove(Component comp) {
        // Si el componente a eliminar es el que ocupa el panel izquierdo...
        if (comp == splitPane.getLeftComponent()) {
            // ...lo reemplazamos con el panel vacío.
            splitPane.setLeftComponent(leftPlaceholder);
        }
        
        // Llamada al método de la superclase para comportamiento estándar.
        super.remove(comp);

        // Si no estamos en fase de carga, se actualiza la UI.
        if (!this.isDuringInitializationOrLoading) {
            this.revalidate();
            this.repaint();
        }
    }

    /**
     * Elimina un componente en un índice específico.
     * <p>
     * Se sobrescribe por completitud, aunque la lógica principal de eliminación
     * se gestiona en {@link #remove(Component)}.
     *
     * @param index El índice del componente a eliminar.
     */
    @Override
    public void remove(int index) {
        // La lógica principal está en remove(Component), que es más robusto
        // para este caso de uso. Este método se deja por si el IDE lo invoca.
        super.remove(index);
    }

    /**
     * Clase interna que actúa como el verdadero contenedor de componentes hijos.
     * <p>
     * Controla qué componentes se pueden añadir, restringiéndolos a instancias
     * de {@link BaseComponent} y asegurando que solo haya un componente hijo
     * principal a la vez.
     */
    private class CustomPanel extends JPanel {

        /**
         * Sobrescribe el método de adición para validar los componentes.
         *
         * @param component el componente a añadir.
         * @param index     la posición donde añadirlo (no se usa directamente).
         * @return el componente añadido.
         */
        @Override
        public Component add(Component component, int index) {
            
            // 1. Un ButtonPanelContainer nunca puede ser un componente hijo.
            if (component instanceof ButtonPanelContainer) {
                if (!ButtonPanelContainer.this.isDuringInitializationOrLoading) {
                    JOptionPane.showMessageDialog(this,
                            "Un ButtonPanelContainer no puede ser añadido dentro de otro contenedor.",
                            "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                }
                return component; // Se devuelve sin añadirlo.
            }
            
            // 2. Validar que el componente sea del tipo correcto.
            if (!(component instanceof BaseComponent)) {
                if (!ButtonPanelContainer.this.isDuringInitializationOrLoading) {
                    String errorMessage = "Sólo se pueden añadir componentes de tipo BaseComponent a este contenedor.";
                    JOptionPane.showMessageDialog(this, errorMessage, "Componente no válido", JOptionPane.WARNING_MESSAGE);
                }
                return component; // Se devuelve para que el IDE lo maneje.
            }

            // 3. Evitar añadir el mismo componente dos veces.
            if (component == splitPane.getLeftComponent()) {
                return component;
            }

            // 4. Añadir el componente al JSplitPane si no hay otro ya.
            if (splitPane.getLeftComponent() == leftPlaceholder) {
                splitPane.setLeftComponent(component);
            } else {
                // Si ya hay un componente, mostrar error.
                String errorMessage = "El contenedor del ButtonPanel sólo puede contener un componente.";
                JOptionPane.showMessageDialog(this, errorMessage, "Contenedor lleno", JOptionPane.WARNING_MESSAGE);
            }

            // 5. Actualizar la UI si es necesario.
            if (!ButtonPanelContainer.this.isDuringInitializationOrLoading) {
                ButtonPanelContainer.this.revalidate();
                ButtonPanelContainer.this.repaint();
            }

            return component;
        }

        /**
         * Sobrescribe el método de adición simple.
         *
         * @param comp el componente a añadir.
         * @return el componente añadido.
         */
        @Override
        public Component add(Component comp) {
            return this.add(comp, -1); // Delega a la versión con más lógica.
        }

        /**
         * Sobrescribe el método de eliminación para limpiar el JSplitPane.
         *
         * @param comp el componente a eliminar.
         */
        @Override
        public void remove(Component comp) {
            // Redirige la llamada al método remove del contenedor padre,
            // que contiene la lógica correcta.
            ButtonPanelContainer.this.remove(comp);
        }
    }
}