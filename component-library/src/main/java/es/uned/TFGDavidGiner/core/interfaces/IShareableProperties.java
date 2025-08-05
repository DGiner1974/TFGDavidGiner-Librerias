/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package es.uned.TFGDavidGiner.core.interfaces;

import java.util.Map;
import java.util.Set;

/**
 * Define un contrato para componentes que tienen la capacidad de exponer
 * y comparar un conjunto de propiedades compartidas.
 * <p>
 * Esta interfaz desacopla la lógica de data-binding de la jerarquía de clases base,
 * permitiendo que solo los componentes relevantes (como LeafComponent) la implementen.
 */
public interface IShareableProperties {

    /**
     * Devuelve el conjunto de propiedades que este componente puede compartir.
     * @return Un Set con los nombres de las propiedades compartidas.
     */
    Set<String> getSharedProperies();

    /**
     * Calcula la intersección entre las propiedades de este componente y las de otro.
     * @param set1 Un conjunto de propiedades a comparar.
     * @return Un nuevo Set con las propiedades en común.
     */
    Set<String> propertiesInCommon(Set<String> set1);     
    
     /**
     * Obtiene los tipos de datos de un conjunto de propiedades de este componente.
     * <p>
     * Utiliza reflexión para inspeccionar los campos de la clase y determinar
     * el tipo de dato de cada propiedad especificada por su nombre.
     *
     * @param propertiesName Un {@link Set} con los nombres de las propiedades a inspeccionar.
     * @return Un {@link Map} que asocia el nombre de cada propiedad con su tipo ({@link Class}).
     */
    Map<String, Class<?>> getPropertiesType(Set<String> propertiesName);
}
