/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Fichero: es/uned/TFGDavidGiner/core/SplitOrientationEditor.java
package es.uned.TFGDavidGiner.core;

import java.beans.PropertyEditorSupport;

/**
 * Editor de propiedades personalizado para el enumerado SplitOrientation.
 * El IDE de NetBeans descubrirá y usará esta clase automáticamente
 * para cualquier propiedad de tipo SplitOrientation.
 */
public class SplitOrientationEditor extends PropertyEditorSupport {

    /**
     * Devuelve las etiquetas de texto que se mostrarán en el menú desplegable.
     * Este es el método clave que crea el selector.
     * @return Un array de Strings con los valores a mostrar.
     */
    @Override
    public String[] getTags() {
        return new String[] { 
            SplitOrientation.HORIZONTAL.getDisplayName(), 
            SplitOrientation.VERTICAL.getDisplayName() 
        };
    }

    /**
     * Convierte el texto seleccionado en el desplegable (ej. "Horizontal")
     * de vuelta a un objeto del tipo correcto (SplitOrientation.HORIZONTAL).
     * @param text El texto seleccionado por el usuario.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        for (SplitOrientation so : SplitOrientation.values()) {
            if (so.getDisplayName().equals(text)) {
                setValue(so); // Almacena el valor del enumerado
                return;
            }
        }
    }

    /**
     * Convierte el valor actual del objeto (ej. SplitOrientation.HORIZONTAL)
     * a texto para mostrarlo en la ventana de propiedades.
     * @return El texto a mostrar.
     */
    @Override
    public String getAsText() {
        SplitOrientation so = (SplitOrientation) getValue();
        if (so == null) {
            return "";
        }
        return so.getDisplayName();
    }
}