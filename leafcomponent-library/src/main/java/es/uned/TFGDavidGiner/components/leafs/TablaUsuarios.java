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
    private Usuario usuarioSeleccionado;
    //private boolean isUpdatingProgrammatically = false;
    private static final Set<String> sharedProperties = Set.of("usuarioSeleccionado");

    public TablaUsuarios() {
        initComponents();
    }

    private void initComponents() {
        this.listaDeUsuarios = Collections.emptyList();
        tableModel = new UsuarioTableModel(this.listaDeUsuarios);
        tablaUsuarios = new JTable(tableModel);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tablaUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return; //|| isUpdatingProgrammatically) return;
            int viewRow = tablaUsuarios.getSelectedRow();
            if (viewRow != -1) {
                int modelRow = tablaUsuarios.convertRowIndexToModel(viewRow);
                setUsuarioSeleccionado(this.listaDeUsuarios.get(modelRow));
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

    public Usuario getUsuarioSeleccionado() { return this.usuarioSeleccionado; }

    public void setUsuarioSeleccionado(Usuario nuevoUsuario) {
        Usuario oldValue = this.usuarioSeleccionado;
        System.out.println("Registro cambiado");
        int modelRow = listaDeUsuarios.indexOf(nuevoUsuario);
        System.out.println("Registro cambiado " + modelRow);
        // Si la notificación es sobre el mismo objeto, es una actualización de datos.
        if (oldValue != null && oldValue == nuevoUsuario) {
            //SwingUtilities.invokeLater(() -> tableModel.fireTableDataChanged());
            SwingUtilities.invokeLater(() -> {
            // Se le dice al modelo que sus datos han cambiado para que se repinte.
            tableModel.fireTableDataChanged();

        });
            return; // Se detiene para no notificar de nuevo.
        }

        // Si es un objeto diferente, es un cambio de selección.
        this.usuarioSeleccionado = nuevoUsuario;
        
                    // --- INICIO DE LA SOLUCIÓN ---
            // Después de refrescar, buscamos el índice del usuario modificado.
            

        
//        try {
//            isUpdatingProgrammatically = true;
//            if (nuevoUsuario == null) {
//                tablaUsuarios.clearSelection();
//            } else {
//                //int modelRow = listaDeUsuarios.indexOf(nuevoUsuario);
//                if (modelRow != -1) {
//                    int viewRow = tablaUsuarios.convertRowIndexToView(modelRow);
//                    if (viewRow != -1) {
//                        tablaUsuarios.setRowSelectionInterval(viewRow, viewRow);
//                    }
//                }
//            }
//        } finally {
//            isUpdatingProgrammatically = false;
//        }
        
        // Notificamos a otros componentes que la SELECCIÓN ha cambiado.
        firePropertyChange("usuarioSeleccionado", oldValue, nuevoUsuario);
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