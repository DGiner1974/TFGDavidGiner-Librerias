package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.core.LeafComponent;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Componente hoja que actúa como la "fuente de la verdad" para un objeto Usuario.
 * Crea y gestiona su propia instancia de Usuario internamente y notifica
 * a otros componentes de los cambios a través de la propiedad compartida 'usuarioSeleccionado'.
 */
public class PanelUsuarioAutonomo extends LeafComponent {

    // --- Componentes Visuales ---
    private JLabel jLabelNombre;
    private JTextField jTextFieldNombre;
    private JLabel jLabelApellido;
    private JTextField jTextFieldApellido;
    private JLabel jLabelNivel;
    private JComboBox<String> jComboBoxNivel;

    /**
     * Instancia interna y única del Usuario. Este componente es el dueño de este objeto.
     */
    private Usuario usuarioSeleccionado;

    private static final Set<String> sharedProperties = Set.of("usuarioSeleccionado");

    public PanelUsuarioAutonomo() {
        // Se crea el objeto Usuario con valores por defecto al construir el componente.
        this.usuarioSeleccionado = new Usuario("Nuevo", "Usuario", "Principiante", 0, 0);
        initComponents();
    }

    private void initComponents() {
        jLabelNombre = new JLabel("Nombre:");
        jTextFieldNombre = new JTextField();
        jLabelApellido = new JLabel("Apellido:");
        jTextFieldApellido = new JTextField();
        jLabelNivel = new JLabel("Nivel:");
        jComboBoxNivel = new JComboBox<>(new String[]{"Principiante", "Intermedio", "Avanzado"});

        // Listeners para actualizar el objeto interno cuando el usuario edita la UI.
        FocusAdapter focusListener = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarUsuarioDesdeUI();
            }
        };
        jTextFieldNombre.addFocusListener(focusListener);
        jTextFieldApellido.addFocusListener(focusListener);
        jComboBoxNivel.addActionListener(e -> actualizarUsuarioDesdeUI());

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

        // Se inicializa la UI con los valores del objeto interno.
        actualizarUIDesdeUsuario();
    }

    /**
     * Actualiza los componentes visuales con los datos del objeto Usuario interno.
     */
    private void actualizarUIDesdeUsuario() {
        jTextFieldNombre.setText(usuarioSeleccionado.getNombre());
        jTextFieldApellido.setText(usuarioSeleccionado.getApellido());
        jComboBoxNivel.setSelectedItem(usuarioSeleccionado.getNivel());
    }

    /**
     * Actualiza el objeto Usuario interno y notifica a los demás componentes.
     */
    private void actualizarUsuarioDesdeUI() {
        System.out.println(usuarioSeleccionado.getNombre() + " - " + jTextFieldNombre.getText());
        usuarioSeleccionado.setNombre(jTextFieldNombre.getText());
        usuarioSeleccionado.setApellido(jTextFieldApellido.getText());
        usuarioSeleccionado.setNivel((String) jComboBoxNivel.getSelectedItem());
        System.out.println("después: " + usuarioSeleccionado.getNombre() + " - " + jTextFieldNombre.getText());
        
        // Notifica a los demás componentes que el estado de 'usuarioSeleccionado' ha cambiado.
        firePropertyChange("usuarioSeleccionado", null, this.usuarioSeleccionado);
    }

    /**
     * Devuelve la instancia del usuario que gestiona este componente.
     * El framework lo usa para la notificación inicial.
     * @return El objeto Usuario de este componente.
     */
    public Usuario getUsuarioSeleccionado() {
        return this.usuarioSeleccionado;
    }

    /**
     * Este método es llamado por el framework, pero como este componente es el
     * "maestro", no debe reaccionar a los cambios que él mismo origina.
     * @param nuevoUsuario El objeto usuario notificado.
     */
    public void setUsuarioSeleccionado(Usuario nuevoUsuario) {
        System.out.println("PanelUsuarioAutonomo usuarioSeleccionado: " +  this.usuarioSeleccionado.toString() + " - " + "nuevoUsuario: " + nuevoUsuario.toString());
        System.out.println("this.usuarioSeleccionado: " + this.usuarioSeleccionado + " - " + "nuevoUsuario: " + nuevoUsuario);
         //if (this.usuarioSeleccionado.toString() == nuevoUsuario.toString()) return;
         //if (this.usuarioSeleccionado.toString().equals(nuevoUsuario.toString())) return;
         System.out.println("YO SIGO PanelUsuarioAutonomo1 usuarioSeleccionado: " +  this.usuarioSeleccionado.toString() + " - " + "nuevoUsuario: " + nuevoUsuario.toString());
         //if (this.usuarioSeleccionado.equals(nuevoUsuario)) return;
        
        this.usuarioSeleccionado = nuevoUsuario;
        actualizarUIDesdeUsuario();
    }
    
    @Override
    public Set<String> getSharedProperies() {
        return sharedProperties;
    }
    
    @Override
    public boolean configurar() {
        // Resetea el usuario interno a un estado vacío y actualiza la UI.
        this.usuarioSeleccionado.setNombre("");
        this.usuarioSeleccionado.setApellido("");
        this.usuarioSeleccionado.setNivel("Principiante");
        actualizarUIDesdeUsuario();
        // Notifica del reseteo.
        firePropertyChange("usuarioSeleccionado", null, this.usuarioSeleccionado);
        return true;
    }

    @Override
    public boolean validar() {
        return !jTextFieldNombre.getText().trim().isEmpty() && !jTextFieldApellido.getText().trim().isEmpty();
    }

    @Override
    public String getError() {
        return validar() ? "" : "El nombre y el apellido no pueden estar vacíos.";
    }
}