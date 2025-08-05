/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.uned.TFGDavidGiner.core.interfaces;

import es.uned.TFGDavidGiner.core.BaseComponent;

/**
 * Define un contrato para objetos que saben cómo construir su propia GUI reutilizable.
 * Implementa el requisito del TFG de un "ensamblado polimórfico".
 */
public interface IGuiAssembler {
    /**
     * Ensambla y devuelve el componente de GUI que representa a este objeto.
     * @return una instancia de BaseComponent configurada para este objeto.
     */
    BaseComponent assembleGui();
}