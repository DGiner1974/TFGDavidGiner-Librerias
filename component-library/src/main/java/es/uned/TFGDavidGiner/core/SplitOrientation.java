/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.core;

import javax.swing.JSplitPane;

/**
 * Enumerado que representa las posibles orientaciones para un contenedor
 * que utilice un {@link JSplitPane}.
 * <p>
 * Este enumerado proporciona una abstracción segura y legible sobre las constantes
 * de entero de Swing ({@code JSplitPane.HORIZONTAL_SPLIT} y {@code JSplitPane.VERTICAL_SPLIT}),
 * facilitando su uso en el framework de componentes y en el editor de propiedades del IDE.
 *
 * @author david
 * @version 1.0
 * @since 13-07-2025
 */
public enum SplitOrientation {
    /**
     * Representa una división horizontal, donde los componentes se colocan uno al lado del otro.
     */
    HORIZONTAL("Horizontal", JSplitPane.HORIZONTAL_SPLIT),

    /**
     * Representa una división vertical, donde los componentes se colocan uno encima del otro.
     */
    VERTICAL("Vertical", JSplitPane.VERTICAL_SPLIT);

    /**
     * El nombre legible para mostrar en la interfaz de usuario.
     */
    private final String displayName;

    /**
     * La constante de entero correspondiente utilizada por {@link JSplitPane}.
     */
    private final int swingConstant;

    /**
     * Constructor privado para inicializar cada constante del enumerado.
     *
     * @param displayName   El nombre legible (ej. "Horizontal").
     * @param swingConstant La constante de Swing (ej. {@code JSplitPane.HORIZONTAL_SPLIT}).
     */
    SplitOrientation(String displayName, int swingConstant) {
        this.displayName = displayName;
        this.swingConstant = swingConstant;
    }

    /**
     * Devuelve el nombre legible de la orientación.
     *
     * @return El nombre para mostrar en la UI.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Devuelve la constante de entero de Swing correspondiente a la orientación.
     *
     * @return El valor entero que necesita {@link JSplitPane#setOrientation(int)}.
     */
    public int getSwingConstant() {
        return swingConstant;
    }

    /**
     * Devuelve una representación en cadena de texto del enumerado.
     * <p>
     * Al sobrescribir este método, se asegura que en los componentes de la UI
     * (como un JComboBox o el editor de propiedades del IDE) se muestre el
     * nombre legible en lugar del nombre de la constante en mayúsculas (ej. "HORIZONTAL").
     *
     * @return El nombre legible de la orientación.
     */
    @Override
    public String toString() {
        return displayName;
    }
}