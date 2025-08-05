/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.components.containers;

import es.uned.TFGDavidGiner.core.BaseComponent;

/**
 * Implementa el patrón Builder para construir un ButtonPanelContainer.
 * Su orientación es siempre vertical para mantener un layout estándar de
 * contenido arriba y botonera abajo.
 */
public class ButtonPanelContainerBuilder {
    private BaseComponent content;

    /**
     * Constructor por defecto.
     * Inicializa una nueva instancia del builder.
     */
    public ButtonPanelContainerBuilder() {
        // Constructor vacío requerido para evitar el warning de Javadoc.
    }
    
    /**
     * Establece el componente de contenido que será envuelto por la botonera.
     * @param content El componente principal a mostrar sobre los botones.
     * @return La instancia del builder para encadenar llamadas.
     */
    public ButtonPanelContainerBuilder withContent(BaseComponent content) {
        this.content = content;
        return this;
    }

    /**
     * Construye y devuelve el ButtonPanelContainer final.
     * @return Una instancia de ButtonPanelContainer configurada.
     * @throws IllegalStateException si no se ha establecido un componente de contenido.
     */
    public ButtonPanelContainer build() {
        if (content == null) {
            throw new IllegalStateException("El componente de contenido no puede ser nulo para construir el contenedor.");
        }
        
        // Se instancia el contenedor, que ya establece su orientación a vertical por defecto.
        ButtonPanelContainer container = new ButtonPanelContainer(); 
        
        // Se añade el contenido principal.
        container.getContentPane().add(content); 
        
        return container;
    }
}