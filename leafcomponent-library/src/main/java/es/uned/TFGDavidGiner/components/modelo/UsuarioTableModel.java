package es.uned.TFGDavidGiner.components.modelo;

import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Un TableModel personalizado que trabaja directamente con una lista de objetos Usuario.
 * Esto asegura que la JTable siempre muestre los datos más actualizados.
 */
public class UsuarioTableModel extends AbstractTableModel {

    private final List<Usuario> usuarios;
    private final String[] columnNames = {"Nombre", "Apellido", "Nivel"};

    public UsuarioTableModel(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int getRowCount() {
        return usuarios.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Usuario usuario = usuarios.get(rowIndex);
        switch (columnIndex) {
            case 0: return usuario.getNombre();
            case 1: return usuario.getApellido();
            case 2: return usuario.getNivel();
            default: return null;
        }
    }
}