package es.uned.TFGDavidGiner.components.containers;

import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.BaseContainer;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contenedor que organiza componentes hijos en una serie de pestañas.
 * <p>
 * Este componente, que hereda de {@link BaseContainer}, utiliza un {@link JTabbedPane}
 * interno para mostrar cada componente añadido en su propia pestaña. Admite cualquier
 * componente que herede de {@link BaseComponent}, permitiendo anidar tanto
 * componentes simples como otros contenedores.
 * <p>
 * Una característica clave es la propiedad {@code tabTitles}, que permite configurar
 * los títulos de todas las pestañas desde el inspector de propiedades del IDE mediante
 * una simple cadena de texto separada por comas.
 *
 * @author David Giner
 * @version 1.0
 * @since 13-07-2025
 */
public class TabContainer extends BaseContainer {

    /**
     * Propiedad de tiempo de diseño que almacena los títulos de las pestañas como una
     * cadena de texto separados por comas. Se utiliza para generar los títulos de las 
     * pestañas. Se actualiza con las modificaciones que se realizan en el diseñador.
     */
    private String tabTitlesDesignTime = "";

    /**
     * Flag para determinar las acciones que se deben realizar cuando se está incializando o
     * cargando el componente.
     * De esta forma podemos evitar que se ejecute la lógica de actualización durante la carga, 
     * como puede ser la actualización de los títulos de las pestañas.
     */
    private boolean isDuringInitializationOrLoading = false;

    /**
     * Instancia del componente {@link CustomTab} contiene y muestra las pestañas.
     */
    private CustomTab contentPane;

    /**
     * Constructor por defecto.
     * Inicializa el layout, el borde, el tamaño preferido y crea la instancia
     * del panel de pestañas interno. También se registra a sí mismo
     * como listener de eventos del contenedor para reaccionar a la adición o
     * eliminación de componentes.
     */
    public TabContainer() {
        try {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEtchedBorder());
            setPreferredSize(new Dimension(200, 150));
            contentPane = new CustomTab();
            add(contentPane, BorderLayout.CENTER);

            isDuringInitializationOrLoading = true;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al inicializar TabContainer: " + e.getMessage());
        }
    }

    /**
     * Obtiene la cadena de texto con los títulos de las pestañas, separados por comas (,).
     * <p>
     * Si la propiedad {@code tabTitlesDesignTime} está vacía, intenta reconstruirla a partir
     * de las pestañas existentes, asegurándose de devolver el estado actual del componente.
     *
     * @return Una cadena con los títulos de las pestañas.
     */
    public String getTabTitles() {
        // Si no hay títulos de pestañas guardados, los generamos. 
        if (tabTitlesDesignTime == null || tabTitlesDesignTime.isEmpty()) {
            // Si no estamos en la fase de carga o si ya hay pestañas,
            // es seguro reconstruir los títulos desde el propio JTabbedPane.
            if (!isDuringInitializationOrLoading || contentPane.getTabCount() > 0) {
                return rebuildTitulosPestanasStringFromTabs();
            }
        }
        return tabTitlesDesignTime;
    }

    /**
     * Establece los títulos de las pestañas a partir de la cadena de texto separada
     * por comas (,) que los contiene.
     * <p>
     * Este método actualiza la propiedad interna que contiene los títulos de las 
     * pestañas {@code tabTitlesDesignTime} y, si no se está en fase de inicialización,
     * actualiza la UI para reflejar los nuevos títulos.
     * Dispara un evento de cambio de propiedad para notificar al IDE.
     *
     * @param tabTitles La nueva cadena de títulos, separados por comas.
     */
    public void setTabTitles(String tabTitles) {
        if (tabTitles == null) tabTitles = "";
        if (!this.tabTitlesDesignTime.equals(tabTitles)) {
            String oldValue = this.tabTitlesDesignTime;
            this.tabTitlesDesignTime = tabTitles;

            // Si el cambio ocurre en tiempo de diseño (no durante la carga) y ya hay pestañas,
            // se actualiza la UI.
            if (!isDuringInitializationOrLoading && contentPane.getTabCount() > 0) {
                updateTabTitlesFromProperty();
                revalidate();
                repaint();
            }
            // En tiempo de ejecución o carga inicial, addNotify() se encargará de la actualización.

            firePropertyChange("tabTitles", oldValue, this.tabTitlesDesignTime);
        }
    }

    /**
     * Método privado que aplica los títulos de la propiedad {@code tabTitlesDesignTime}
     * a las pestañas del {@code contentPane}.
     * <p>
     * Parsea la cadena de títulos y los asigna a cada pestaña existente. Si hay más pestañas
     * que títulos en la cadena, se usan títulos por defecto ("Pestaña X").
     */
    private void updateTabTitlesFromProperty() {
        if (contentPane == null) {
            System.err.println("Error: contentPane es null en updateTabTitlesFromProperty.");
            return;
        }

        String[] titlesArray = this.tabTitlesDesignTime.isEmpty() ? new String[0] : this.tabTitlesDesignTime.split(",");
        List<String> titlesList = new ArrayList<>(Arrays.asList(titlesArray));

        int tabCount = contentPane.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            String newTitle = "Pestaña " + (i + 1); // Título por defecto

            if (i < titlesList.size()) {
                String titleFromProperty = titlesList.get(i).trim();
                if (!titleFromProperty.isEmpty()) {
                    newTitle = titleFromProperty; // Usa el título de la propiedad si no está vacío
                }
            }
            if (i < contentPane.getTabCount()) {
                contentPane.setTitleAt(i, newTitle);
            }
        }
    }

    /**
     * Sobrescribe {@code addNotify} para realizar la configuración final cuando el componente
     * es añadido a un contenedor visible.
     * <p>
     * Utiliza {@code SwingUtilities.invokeLater} para asegurar que la lógica se ejecuta en el
     * Event Dispatch Thread (EDT) después de que el componente haya sido completamente
     * inicializado. Esto resuelve problemas de timing al cargar formularios en el diseñador.
     * <p>
     * Las acciones principales son:
     * 1. Aplicar los títulos de las pestañas desde la propiedad {@code tabTitlesDesignTime}.
     * 2. Desactivar la bandera {@code isDuringInitializationOrLoading}.
     */
    @Override
    public void addNotify() {
        super.addNotify();

        SwingUtilities.invokeLater(() -> {
           
            // Actualiza los títulos de las pestañas, valida y repinta el componente.
            updateTabTitlesFromProperty();
            revalidate();
            repaint();
            
            // Finaliza el modo de inicialización.
            isDuringInitializationOrLoading = false;
        });
    }
    
    /**
     * Reconstruye la cadena de títulos {@code tabTitlesDesignTime} a partir de los títulos
     * actuales de las pestañas en el {@code contentPane}.
     * <p>
     * Es útil para mantener la propiedad sincronizada cuando las pestañas se modifican
     * interactivamente en el diseñador.
     *
     * @return Una cadena de texto con los títulos actuales, separados por comas.
     */
    private String rebuildTitulosPestanasStringFromTabs() {
        List<String> tempTitles = new ArrayList<>();
        for (int i = 0; i < contentPane.getTabCount(); i++) {
            String currentTitle = contentPane.getTitleAt(i);
            if (currentTitle == null || currentTitle.trim().isEmpty() || currentTitle.equals("Default Title")) {
                tempTitles.add("Pestaña " + (i + 1));
            } else {
                tempTitles.add(currentTitle);
            }
        }
        return String.join(",", tempTitles);
    }
    
    /**
     * Devuelve la instancia del panel de pestañas interno. 
     * Se utiliza en el BeanInfo para indicar que este componente es el que
     * se debe comportar como contenedor de los componentes.
     *
     * @return El {@link JTabbedPane} que gestiona las pestañas.
     */
    public JTabbedPane getContentPane() {
        return contentPane;
    }

    /**
     * Clase interna privada que extiende {@link JTabbedPane} para personalizar
     * el comportamiento de adición y eliminación de pestañas. 
     * <p>
     * Es el verdadero contenedor del componente. Solo permite añadir componentes que hereden de 
     * {@link BaseComponent}. Es decir, o son contenedores (heredan de {@link BaseContainer}) o
     * son componentes hoja (heredan de {@link es.uned.TFGDavidGiner.core.LeafComponent}).
     */
    private class CustomTab extends JTabbedPane {
        
        /**
        * Prepara un componente antes de ser añadido, aplicando reglas comunes.
        * En este caso, deshabilita el borde si el componente es un contenedor.
        * @param component El componente a preparar.
        */
       private void prepareComponentForAddition(Component component) {
           if (component instanceof BaseContainer) {
               ((BaseContainer) component).setShowBorder(false);
           }
       }
        
        /**
         * Sobrescribe el método {@code add} para controlar los componentes que se añaden
         * en tiempo de diseño (por ejemplo, al arrastrar y soltar).
         * <p>
         * Si el componente no es una instancia de {@link BaseComponent}, muestra un mensaje
         * de advertencia indicando al usuario que debe eliminarlo manualmente y evita añadirlo 
         * como una pestaña. Si es válido, lo añade con un título por defecto (que se actualiza
         * a posteriori)y actualiza la propiedad {@code tabTitlesDesignTime}.
         *
         * @param component el componente a añadir.
         * @param index la posición en la que se añadirá.
         * @return el componente que se ha añadido.
         */
        @Override
        public Component add(Component component, int index) {
            
            // Un ButtonPanelContainer nunca puede ser un componente hijo.
            if (component instanceof ButtonPanelContainer) {
                if (!TabContainer.this.isDuringInitializationOrLoading) {
                    JOptionPane.showMessageDialog(this,
                            "Un ButtonPanelContainer no puede ser añadido dentro de otro contenedor.",
                            "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                }
                return component; // Se devuelve sin añadirlo.
            }
            
            // Valida que solo se puedan añadir componentes de nuestro framework.
            if (!(component instanceof BaseComponent)) {
                // Muestra un mensaje de error solo si no estamos en fase de carga.
                if (!TabContainer.this.isDuringInitializationOrLoading) {
                    String errorMessage = "Solo se pueden arrastrar componentes de tipo BaseComponent a este contenedor. Elimine manualmente el componente en Navigator.";
                    JOptionPane.showMessageDialog(this,
                            errorMessage,
                            "Componente no válido",
                            JOptionPane.WARNING_MESSAGE);
                }
                // Devuelve el componente aunque sea inválido para que el diseñador lo maneje.
                return component;
            }
            
            // Llamar al método de ayuda ANTES de añadir la pestaña
            prepareComponentForAddition(component);
            
            super.addTab("Default Title", component);

            // Si la adición no es en la inicialización o en la carga, actualiza la propiedad de títulos y la UI.
            if (!TabContainer.this.isDuringInitializationOrLoading) {
                TabContainer.this.tabTitlesDesignTime = TabContainer.this.rebuildTitulosPestanasStringFromTabs();
                TabContainer.this.firePropertyChange("tabTitles", null, TabContainer.this.tabTitlesDesignTime);
                TabContainer.this.updateTabTitlesFromProperty();
                TabContainer.this.revalidate();
                TabContainer.this.repaint();
            }

            return component;
        }

        @Override
        public Component add(Component comp) {
            if (comp instanceof BaseComponent) {
                // Llama a la versión sobrecargada que maneja la lógica de adición de pestañas.
                return this.add(comp, -1);
            }
            // Si no es un BaseComponent, se añade como hijo normal, pero no como pestaña.
            return comp; //super.add(comp);
        }

        /**
         * Sobrescritura para sincronizar la propiedad de títulos cuando se añade una pestaña
         * explícitamente con este método.
         * {@inheritDoc}
         */
        @Override
        public void addTab(String title, Icon icon, Component component, String tip) {
            // Llamar al método de ayuda ANTES de añadir la pestaña
            prepareComponentForAddition(component);
            
            super.addTab(title, icon, component, tip);
            if (!TabContainer.this.isDuringInitializationOrLoading) {
                TabContainer.this.tabTitlesDesignTime = TabContainer.this.rebuildTitulosPestanasStringFromTabs();
                TabContainer.this.firePropertyChange("tabTitles", null, TabContainer.this.tabTitlesDesignTime);
                // No es necesario llamar a updateTabTitlesFromProperty aquí porque el título ya se ha establecido.
                TabContainer.this.revalidate();
                TabContainer.this.repaint();
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void addTab(String title, Component component) {
            addTab(title, null, component, null);
        }
        
        /**
         * Sobrescritura para sincronizar la propiedad de títulos cuando se inserta una pestaña.
         * {@inheritDoc}
         */
        @Override
        public void insertTab(String title, Icon icon, Component component, String tip, int index) {
            // Llamar al método de ayuda ANTES de añadir la pestaña
            prepareComponentForAddition(component);
            
            super.insertTab(title, icon, component, tip, index);
            if (!TabContainer.this.isDuringInitializationOrLoading) {
                TabContainer.this.tabTitlesDesignTime = TabContainer.this.rebuildTitulosPestanasStringFromTabs();
                TabContainer.this.firePropertyChange("tabTitles", null, TabContainer.this.tabTitlesDesignTime);
                TabContainer.this.revalidate();
                TabContainer.this.repaint();
            }
        }
        
        /**
         * Sobrescritura para sincronizar la propiedad de títulos cuando se elimina una pestaña.
         * {@inheritDoc}
         */
        @Override
        public void removeTabAt(int index) {
            super.removeTabAt(index);
            if (!TabContainer.this.isDuringInitializationOrLoading) {
                TabContainer.this.tabTitlesDesignTime = TabContainer.this.rebuildTitulosPestanasStringFromTabs();
                TabContainer.this.firePropertyChange("tabTitles", null, TabContainer.this.tabTitlesDesignTime);
                TabContainer.this.revalidate();
                TabContainer.this.repaint();
            }
        }
        
    }
}