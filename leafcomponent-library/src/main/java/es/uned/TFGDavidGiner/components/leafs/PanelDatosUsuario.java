package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.core.LeafComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

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

    private static final Set<String> sharedProperties = Set.of("nombre", "apellido", "nivel");

    public PanelDatosUsuario() {
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
    
    private void actualizarUI() {
        jTextFieldNombre.setText(this.nombre);
        jTextFieldApellido.setText(this.apellido);
        jComboBoxNivel.setSelectedItem(this.nivel);
        
        boolean enabled = this.nombre != null && !this.nombre.isEmpty();
        jTextFieldNombre.setEnabled(enabled);
        jTextFieldApellido.setEnabled(enabled);
        jComboBoxNivel.setEnabled(enabled);
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
    @Override
    public boolean configurar() { /* Lógica de reseteo si es necesaria */ return true; }
    @Override
    public boolean validar() { return true; /* Validación si es necesaria */ }
    @Override
    public String getError() { return ""; }
}