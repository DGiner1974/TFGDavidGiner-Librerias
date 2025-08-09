package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.core.LeafComponent;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.components.modelo.UsuarioTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;


public class TablaUsuarios extends LeafComponent {

    private JTable tablaUsuarios;
    private UsuarioTableModel tableModel;

    /** La lista de usuarios que se está mostrando y editando actualmente. */
    private List<Usuario> listaDeUsuarios;
    
    /** Una copia de seguridad de la lista original para la operación "Cancelar". */
    private List<Usuario> listaDeUsuariosOriginal;
    
    /** Almacena los errores de validación, mapeando un índice de fila a su mensaje de error. */
    private Map<Integer, String> validationErrors = new HashMap<>();
    
    /**
     * Define el color que se usará para resaltar un error de validación.
     */
    private static final Color ERROR_COLOR = new Color(255, 51, 51); 
    
    // Propiedades internas para mantener el estado actual
    private String nombre = "";
    private String apellido = "";
    private String nivel = "";
    private int pesoMaxPressBanca = 0;
    private int pesoMaxSentadilla = 0;
    private int selectedUserIndex = -1;

    // Se añaden las nuevas propiedades al conjunto de propiedades compartidas
    private static final Set<String> sharedProperties = Set.of("nombre", "apellido", "nivel", "pesoMaxPressBanca", "pesoMaxSentadilla");

    public TablaUsuarios() {
        initComponents();
    }

    private void initComponents() {
        this.listaDeUsuarios = new ArrayList<>();
        this.listaDeUsuariosOriginal = new ArrayList<>();
        
        tableModel = new UsuarioTableModel(this.listaDeUsuarios);
        tablaUsuarios = new JTable(tableModel);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

         // Se asigna el renderizador personalizado para colorear las filas con errores.
        tablaUsuarios.setDefaultRenderer(Object.class, new ValidationTableCellRenderer());
        
        // Listener para cuando el USUARIO hace clic en una fila.
        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            
            int viewRow = tablaUsuarios.getSelectedRow();
            this.selectedUserIndex = (viewRow != -1) ? tablaUsuarios.convertRowIndexToModel(viewRow) : -1;
            
            if (this.selectedUserIndex != -1) {
                Usuario u = listaDeUsuarios.get(this.selectedUserIndex);
                // Actualiza TODAS las propiedades internas y notifica a los demás.
                setNombre(u.getNombre());
                setApellido(u.getApellido());
                setNivel(u.getNivel());
                setPesoMaxPressBanca(u.getPesoMaxPressBanca());
                setPesoMaxSentadilla(u.getPesoMaxSentadilla());
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(tablaUsuarios), BorderLayout.CENTER);
    }

    /**
     * Establece los datos iniciales de la tabla y crea la copia de seguridad.
     * @param usuarios
     */
    public void setUsuarios(List<Usuario> usuarios) {
        // Se clona cada usuario para asegurar que la lista original no sea modificada por referencia.
        this.listaDeUsuarios = usuarios.stream().map(Usuario::clone).collect(Collectors.toList());
        this.listaDeUsuariosOriginal = usuarios.stream().map(Usuario::clone).collect(Collectors.toList());
        
        this.tableModel = new UsuarioTableModel(this.listaDeUsuarios);
        this.tablaUsuarios.setModel(this.tableModel);
    }

    // --- Getters y Setters para las propiedades compartidas ---
    
    public String getNombre() { return nombre; }
    public void setNombre(String nuevoNombre) {
        String oldValue = this.nombre;
        if (nuevoNombre != null && !oldValue.equals(nuevoNombre)) {
            this.nombre = nuevoNombre;
            if (selectedUserIndex != -1) {
                listaDeUsuarios.get(selectedUserIndex).setNombre(nuevoNombre);
                SwingUtilities.invokeLater(() -> tableModel.fireTableRowsUpdated(selectedUserIndex, selectedUserIndex));
            }
            firePropertyChange("nombre", oldValue, nuevoNombre);
        }
    }

    public String getApellido() { return apellido; }
    public void setApellido(String nuevoApellido) {
        String oldValue = this.apellido;
        if (nuevoApellido != null && !oldValue.equals(nuevoApellido)) {
            this.apellido = nuevoApellido;
            if (selectedUserIndex != -1) {
                listaDeUsuarios.get(selectedUserIndex).setApellido(nuevoApellido);
                SwingUtilities.invokeLater(() -> tableModel.fireTableRowsUpdated(selectedUserIndex, selectedUserIndex));
            }
            firePropertyChange("apellido", oldValue, nuevoApellido);
        }
    }

    public String getNivel() { return nivel; }
    public void setNivel(String nuevoNivel) {
        String oldValue = this.nivel;
        if (nuevoNivel != null && !oldValue.equals(nuevoNivel)) {
            this.nivel = nuevoNivel;
            if (selectedUserIndex != -1) {
                listaDeUsuarios.get(selectedUserIndex).setNivel(nuevoNivel);
                SwingUtilities.invokeLater(() -> tableModel.fireTableRowsUpdated(selectedUserIndex, selectedUserIndex));
            }
            firePropertyChange("nivel", oldValue, nuevoNivel);
        }
    }
    
    public int getPesoMaxPressBanca() { return pesoMaxPressBanca; }
    public void setPesoMaxPressBanca(int nuevoPeso) {
        int oldValue = this.pesoMaxPressBanca;
        if (oldValue != nuevoPeso) {
            this.pesoMaxPressBanca = nuevoPeso;
            if (selectedUserIndex != -1) {
                listaDeUsuarios.get(selectedUserIndex).setPesoMaxPressBanca(nuevoPeso);
            }
            firePropertyChange("pesoMaxPressBanca", oldValue, nuevoPeso);
        }
    }

    public int getPesoMaxSentadilla() { return pesoMaxSentadilla; }
    public void setPesoMaxSentadilla(int nuevoPeso) {
        int oldValue = this.pesoMaxSentadilla;
        if (oldValue != nuevoPeso) {
            this.pesoMaxSentadilla = nuevoPeso;
            if (selectedUserIndex != -1) {
                listaDeUsuarios.get(selectedUserIndex).setPesoMaxSentadilla(nuevoPeso);
            }
            firePropertyChange("pesoMaxSentadilla", oldValue, nuevoPeso);
        }
    }

    @Override
    public Set<String> getSharedProperies() { return sharedProperties; }
    // --- Lógica de Validación y Configuración ---

    /**
     * Restaura la tabla a su estado original, descartando cualquier cambio.
     */
    @Override
    public boolean configurar() {
        // Se clona la lista original de vuelta a la lista de trabajo.
        this.listaDeUsuarios = this.listaDeUsuariosOriginal.stream()
                                    .map(Usuario::clone)
                                    .collect(Collectors.toList());
        
        tableModel = new UsuarioTableModel(this.listaDeUsuarios);
        tablaUsuarios.setModel(tableModel);
        validationErrors.clear(); // Se limpian los errores visuales.
        
        // Se notifica que la selección puede haber cambiado.
        if (tablaUsuarios.getRowCount() > 0) {
             tablaUsuarios.setRowSelectionInterval(0, 0);
        } else {
             firePropertyChange("nombre", null, "");
             firePropertyChange("apellido", null, "");
             firePropertyChange("nivel", null, "");
        }
        
        return true;
    }
    
    /**
     * Renderizador de celdas personalizado para colorear las filas con errores.
     */
    private class ValidationTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            // Si la fila actual está en nuestro mapa de errores, se pinta de rojo.
            if (validationErrors.containsKey(row)) {
                c.setBackground(ERROR_COLOR); 
            } else {
                // Si no, se usa el color por defecto (blanco o azul de selección).
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            }
            return c;
        }
    }

    /**
     * Valida todos los usuarios en la tabla. Si todos son válidos, consolida los cambios.
     * Si hay errores, los marca visualmente.
     */
    @Override
    public boolean validar() {
        validationErrors.clear();
        final List<String> NIVELES_VALIDOS = Arrays.asList("Principiante", "Intermedio", "Avanzado");

        for (int i = 0; i < listaDeUsuarios.size(); i++) {
            String error="";
            Usuario u = listaDeUsuarios.get(i);
//            if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
//                validationErrors.put(i, "El nombre no puede estar vacío.");
//            } else if (u.getApellido() == null || u.getApellido().trim().isEmpty()) {
//                validationErrors.put(i, "El apellido no puede estar vacío.");
//            } else if (!NIVELES_VALIDOS.contains(u.getNivel())) {
//                validationErrors.put(i, "El nivel '" + u.getNivel() + "' no es válido.");
//            } else if (u.getPesoMaxPressBanca() < 0 || u.getPesoMaxPressBanca() > 200) {
//                validationErrors.put(i, "El peso en Press Banca debe estar entre 0 y 200.");
//            } else if (u.getPesoMaxSentadilla() < 0 || u.getPesoMaxSentadilla() > 200) {
//                validationErrors.put(i, "El peso en Sentadilla debe estar entre 0 y 200.");
//            }
            
            if (u.getNombre() == null || u.getNombre().trim().isEmpty()) {
                error += "El nombre no puede estar vacío.";
            } 
            if (u.getApellido() == null || u.getApellido().trim().isEmpty()) {
                error += "El apellido no puede estar vacío.";
            } 
            if (!NIVELES_VALIDOS.contains(u.getNivel())) {
                error += "El nivel '" + u.getNivel() + "' no es válido.";
            } 
            if (u.getPesoMaxPressBanca() < 0 || u.getPesoMaxPressBanca() > 200) {
                error += "El peso en Press Banca debe estar entre 0 y 200.";
            } 
            if (u.getPesoMaxSentadilla() < 0 || u.getPesoMaxSentadilla() > 200) {
                error += "El peso en Sentadilla debe estar entre 0 y 200.";
            }
            
            if (error!="") validationErrors.put(i,error);
        }
        
        tablaUsuarios.repaint(); // Fuerza el redibujado para mostrar los errores (o limpiarlos).

        if (validationErrors.isEmpty()) {
            // Si no hay errores, se actualiza la copia de seguridad con los datos actuales.
            this.listaDeUsuariosOriginal = this.listaDeUsuarios.stream()
                                               .map(Usuario::clone)
                                               .collect(Collectors.toList());
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public String getError() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : validationErrors.entrySet()) {
            Usuario u = listaDeUsuarios.get(entry.getKey());
            sb.append("Fila ").append(entry.getKey() + 1).append(" (")
              .append(u.getNombre()).append("): ")
              .append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}