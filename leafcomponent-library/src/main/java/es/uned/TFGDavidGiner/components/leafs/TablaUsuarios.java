package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.core.LeafComponent;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.components.modelo.UsuarioTableModel;
import java.awt.BorderLayout;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class TablaUsuarios extends LeafComponent {

    private JTable tablaUsuarios;
    private UsuarioTableModel tableModel;
    private List<Usuario> listaDeUsuarios;
    
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
        this.listaDeUsuarios = Collections.emptyList();
        tableModel = new UsuarioTableModel(this.listaDeUsuarios);
        tablaUsuarios = new JTable(tableModel);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

    public void setUsuarios(List<Usuario> usuarios) {
        this.listaDeUsuarios = usuarios;
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
    @Override
    public boolean configurar() { tablaUsuarios.clearSelection(); return true; }
    @Override
    public boolean validar() { return true; }
    @Override
    public String getError() { return ""; }
}