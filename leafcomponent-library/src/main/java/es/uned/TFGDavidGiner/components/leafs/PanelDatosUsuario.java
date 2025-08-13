package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.core.LeafComponent;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Componente hoja que encapsula un formulario para editar los datos de un Usuario.
 * Se sincroniza con otros componentes a través de propiedades compartidas y
 * proporciona una lógica de validación para los datos introducidos.
 */
public class PanelDatosUsuario extends LeafComponent {

    // --- Componentes Visuales ---
    private JLabel jLabelNombre;
    private JTextField jTextFieldNombre;
    private JLabel jLabelApellido;
    private JTextField jTextFieldApellido;
    private JLabel jLabelNivel;
    private JComboBox<String> jComboBoxNivel;

    // --- Campos para las propiedades compartidas ---
    private String nombre = "";
    private String apellido = "";
    private String nivel = "Principiante";

    /**
     * Almacena el color de fondo por defecto del componente para poder restaurarlo.
     */
    private final Color defaultBackgroundColor;

    /**
     * Define el color que se usará para resaltar un error de validación.
     */
    private static final Color ERROR_COLOR = new Color(255, 51, 51); 
    
    private static final Set<String> sharedProperties = Set.of("nombre", "apellido", "nivel");

    public PanelDatosUsuario() {
        // Se captura el color por defecto ANTES de que se inicialicen los componentes.
        this.defaultBackgroundColor = UIManager.getColor("Panel.background");
        initComponents();
        actualizarUI(); // Se asegura de que los campos estén desactivados al inicio.
    }

    private void initComponents() {
        jLabelNombre = new JLabel("Nombre:");
        jTextFieldNombre = new JTextField();
        jLabelApellido = new JLabel("Apellido:");
        jTextFieldApellido = new JTextField();
        jLabelNivel = new JLabel("Nivel:");
        jComboBoxNivel = new JComboBox<>(new String[]{"Principiante", "Intermedio", "Avanzado"});

        // Listeners para detectar cuando el usuario termina de editar.
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                // Al perder el foco, se actualizan las propiedades internas y se notifica.
                setNombre(jTextFieldNombre.getText());
                setApellido(jTextFieldApellido.getText());
            }
        };
        jTextFieldNombre.addFocusListener(focusListener);
        jTextFieldApellido.addFocusListener(focusListener);
        jComboBoxNivel.addActionListener(e -> setNivel((String) jComboBoxNivel.getSelectedItem()));

        // --- Código de GroupLayout ---
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelNombre)
                    .addComponent(jLabelApellido)
                    .addComponent(jLabelNivel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldNombre)
                    .addComponent(jTextFieldApellido)
                    .addComponent(jComboBoxNivel, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNombre)
                    .addComponent(jTextFieldNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelApellido)
                    .addComponent(jTextFieldApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNivel)
                    .addComponent(jComboBoxNivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
    
    /**
    * Actualiza los componentes visuales del formulario (JTextFields, JComboBox)
    * para que reflejen los valores actuales de las propiedades internas
    * (nombre, apellido, nivel). También gestiona la habilitación/deshabilitación
    * de los campos.
    */
    private void actualizarUI() {
        jTextFieldNombre.setText(this.nombre);
        jTextFieldApellido.setText(this.apellido);
        jComboBoxNivel.setSelectedItem(this.nivel);
        
//        boolean enabled = (this.nombre != null && !this.nombre.isEmpty()) || (this.apellido != null && !this.apellido.isEmpty());
//        jTextFieldNombre.setEnabled(enabled);
//        jTextFieldApellido.setEnabled(enabled);
//        jComboBoxNivel.setEnabled(enabled);
    }

    // --- Getters y Setters para las propiedades compartidas ---

    public String getNombre() { return nombre; }
    public void setNombre(String nuevoNombre) {
        String oldValue = this.nombre;
        if (nuevoNombre != null && !oldValue.equals(nuevoNombre)) {
            this.nombre = nuevoNombre;
            actualizarUI();
            firePropertyChange("nombre", oldValue, nuevoNombre);
        }
    }

    public String getApellido() { return apellido; }
    public void setApellido(String nuevoApellido) {
        String oldValue = this.apellido;
        if (nuevoApellido != null && !oldValue.equals(nuevoApellido)) {
            this.apellido = nuevoApellido;
            actualizarUI();
            firePropertyChange("apellido", oldValue, nuevoApellido);
        }
    }

    public String getNivel() { return nivel; }
    public void setNivel(String nuevoNivel) {
        String oldValue = this.nivel;
        if (nuevoNivel != null && !oldValue.equals(nuevoNivel)) {
            this.nivel = nuevoNivel;
            actualizarUI();
            firePropertyChange("nivel", oldValue, nuevoNivel);
        }
    }

    @Override
    public Set<String> getSharedProperies() { return sharedProperties; }
    
    /**
     * {@inheritDoc}
     * <p>
     * Restaura los campos del formulario al último estado válido conocido y
     * limpia cualquier indicador visual de error.
     */
    @Override
    public boolean configurar() { 
        actualizarUI();
        setBackground(defaultBackgroundColor);
        return true;
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Comprueba si los datos del formulario son válidos. Las reglas son:
     * <ul>
     * <li>El nombre no puede estar vacío.</li>
     * <li>El apellido no puede estar vacío.</li>
     * <li>El nivel debe ser uno de los valores predefinidos.</li>
     * </ul>
     * Si la validación falla, el fondo del componente se pinta de color rojo.
     * @return {@code true} si todos los campos son válidos, {@code false} en caso contrario.
     */
    @Override
    public boolean validar() {
        // Solo se valida si el componente está activo (tiene un usuario cargado).
        if (!jTextFieldNombre.isEnabled()) {
            return true;
        }

        if (getError().isEmpty()) {
            setBackground(defaultBackgroundColor);
            return true;
        } else {
            setBackground(ERROR_COLOR); 
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Devuelve una cadena con todos los errores de validación encontrados en el formulario,
     * uno por línea.
     * @return Una cadena con los errores, o una cadena vacía si los datos son válidos.
     */
    @Override
    public String getError() {
        StringBuilder errors = new StringBuilder();
        final List<String> NIVELES_VALIDOS = Arrays.asList("Principiante", "Intermedio", "Avanzado");

        if (getNombre() == null || getNombre().trim().isEmpty()) {
            errors.append("El nombre no puede estar vacío.\n");
        }
        if (getApellido() == null || getApellido().trim().isEmpty()) {
            errors.append("El apellido no puede estar vacío.\n");
        }
        if (!NIVELES_VALIDOS.contains(getNivel())) {
            errors.append("El nivel seleccionado no es válido.\n");
        }
        
        return errors.toString().trim();    
    }
}