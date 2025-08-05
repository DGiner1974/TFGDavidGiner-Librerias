/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.uned.TFGDavidGiner.core.interfaces;

/**
 * Define un contrato para los componentes que requieren lógica de validación y configuración.
 * <p>
 * Esta interfaz es implementada por los componentes del framework para asegurar que
 * puedan ser validados, reseteados a un estado inicial y que puedan comunicar
* mensajes de error de una manera estandarizada. Es utilizada por contenedores como
 * {@link es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer} para orquestar estas acciones sobre sus componentes hijos.
 *
 * @author david
 * @version 1.0
 * @since 13-07-2025
 */
public interface IValidation {

    /**
     * Restablece el componente a su estado inicial o por defecto.
     * <p>
     * Este método se utiliza para limpiar los datos del componente, quitar los
     * indicadores visuales de error y devolverlo a su configuración original.
     *
     * @return {@code true} si la configuración se realizó con éxito; {@code false} si ocurrió un error.
     */
    public boolean configurar();

    /**
     * Comprueba si el estado actual del componente es válido según sus reglas de negocio.
     * <p>
     * Este método es el núcleo de la lógica de validación. La implementación debe
     * verificar los datos internos y, si no son válidos, típicamente debería
     * preparar un mensaje de error y proporcionar alguna retroalimentación visual
     * (por ejemplo, cambiando el color de fondo).
     *
     * @return {@code true} si el estado del componente es válido; {@code false} en caso contrario.
     */
    public boolean validar();

    /**
     * Devuelve el mensaje de error correspondiente a la última validación fallida.
     * <p>
     * Este método debe ser llamado después de que una llamada a {@link #validar()}
     * haya devuelto {@code false}.
     *
     * @return Un {@link String} que describe el error de validación, o una cadena
     * vacía si no hay ningún error.
     */
    public String getError();
}