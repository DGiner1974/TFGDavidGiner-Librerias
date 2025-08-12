package es.uned.TFGDavidGiner.demo.ensamblado;

import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.TreeContainer;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.core.BaseComponent;
import es.uned.TFGDavidGiner.core.interfaces.IGuiAssembler;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Implementa la interfaz {@link IGuiAssembler} para actuar como el ensamblador
 * principal de la aplicación de demostración.
 * <p>
 * Su responsabilidad es orquestar la construcción de la interfaz de usuario
 * completa, delegando la creación de las subvistas a otros ensambladores
 * especializados ({@link GestionSociosViewAssembler} y {@link CapacidadesFisicasViewAssembler}).
 * <p>
 * Este enfoque demuestra el principio de **ensamblado polimórfico**, donde un
 * objeto de alto nivel puede construir una estructura compleja invocando el
 * mismo método (`assembleGui`) en sus componentes, sin necesidad de conocer
 * los detalles de implementación de cada uno.
 *
 * @author David Giner
 * @version 1.0
 * @see IGuiAssembler
 * @see GestionSociosViewAssembler
 * @see CapacidadesFisicasViewAssembler
 */
public class MainViewAssembler implements IGuiAssembler {
    
    /**
     * El ensamblador para la vista de "Gestión de Socios".
     */
    private final IGuiAssembler gestionSociosAssembler;

    /**
     * El ensamblador para la vista de "Capacidades Físicas".
     */
    private final IGuiAssembler capacidadesFisicasAssembler;

    /**
     * Constructor que inicializa el ensamblador principal.
     * <p>
     * Crea las instancias de los ensambladores de subvistas, pasándoles los
     * datos necesarios para su construcción.
     *
     * @param datosDePrueba La lista de objetos {@link Usuario} que se utilizará
     * en la vista de gestión de socios.
     */
    public MainViewAssembler(List<Usuario> datosDePrueba) {
        this.gestionSociosAssembler = new GestionSociosViewAssembler(datosDePrueba);
        this.capacidadesFisicasAssembler = new CapacidadesFisicasViewAssembler();
    }

        /**
     * Ensambla y devuelve la interfaz gráfica principal de la aplicación.
     * <p>
     * El proceso de ensamblado consiste en:
     * <ol>
     * <li>Crear el contenedor principal, un {@link TreeContainer}.</li>
     * <li>Definir la estructura de navegación del árbol.</li>
     * <li>Invocar de forma polimórfica el método `assembleGui()` de cada
     * ensamblador de subvistas.</li>
     * <li>Añadir las GUIs resultantes al {@code TreeContainer} en el orden correcto.</li>
     * </ol>
     *
     * @return un {@link BaseComponent} que representa la GUI completa y ensamblada.
     */
    @Override
    public BaseComponent assembleGui() {
        // Se crea el contenedor principal que usará un árbol para la navegación.
        TreeContainer vistaPrincipal = ContainerFactory.createTreeContainer();
        
        // Se define la estructura del árbol de navegación.
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Gimnasio");
        root.add(new DefaultMutableTreeNode("Gestión de Socios"));
        root.add(new DefaultMutableTreeNode("Capacidades Físicas"));
        vistaPrincipal.setEstructuraArbol(new DefaultTreeModel(root));
        
        // --- Ensamblado Polimórfico ---
        // Se pide a cada ensamblador que construya su propia vista y se añade al contenedor.
        vistaPrincipal.getContentPane().add(gestionSociosAssembler.assembleGui());
        vistaPrincipal.getContentPane().add(capacidadesFisicasAssembler.assembleGui());
        
        // Se establece el nodo que se mostrará por defecto en el diseñador.
        vistaPrincipal.setDesignTimeSelectionPath("Gimnasio, Gestión de Socios");
        
        return vistaPrincipal;
    }
}