package es.uned.TFGDavidGiner.demo;

import es.uned.TFGDavidGiner.components.containers.ButtonPanelContainer;
import es.uned.TFGDavidGiner.components.containers.ContainerFactory;
import es.uned.TFGDavidGiner.components.containers.SimpleContainer;
import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.core.SplitOrientation;
import es.uned.TFGDavidGiner.components.leafs.GraficoRendimiento;
import es.uned.TFGDavidGiner.components.leafs.PanelDatosUsuario;
import es.uned.TFGDavidGiner.components.leafs.TablaUsuarios;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Aplicación de demostración final que ensambla los componentes del framework
 * para crear un "Gestor de Actividad de Gimnasio".
 */
public class GimnasioDemoFrame extends JFrame {

    // Componentes hoja personalizados que forman la aplicación
    private TablaUsuarios tablaUsuarios;
    private PanelDatosUsuario panelDatos;
    private GraficoRendimiento graficoRendimiento;

    public GimnasioDemoFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Gestor de Actividad de Gimnasio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // --- 1. Creación de los Componentes Hoja ---
        tablaUsuarios = new TablaUsuarios();
        panelDatos = new PanelDatosUsuario();
        graficoRendimiento = new GraficoRendimiento();
        
        // --- 2. Ensamblado de la Interfaz con los Contenedores del Framework ---
        
        // Contenedor para el panel derecho (datos y gráfico)
        SimpleContainer panelDerecho = ContainerFactory.createSimpleContainer();
        panelDerecho.setOrientation(SplitOrientation.VERTICAL);
        panelDerecho.getContentPane().add(panelDatos);
        panelDerecho.getContentPane().add(graficoRendimiento);
        
        // Contenedor principal que une la tabla (izquierda) con el panel derecho
        SimpleContainer panelContenido = ContainerFactory.createSimpleContainer();
        panelContenido.setOrientation(SplitOrientation.HORIZONTAL);
        panelContenido.setDividerLocation(300); // Dar más espacio a la tabla
        panelContenido.getContentPane().add(tablaUsuarios);
        panelContenido.getContentPane().add(panelDerecho);
        
        // Contenedor final con la botonera de Aceptar/Cancelar
        ButtonPanelContainer panelPrincipal = new ButtonPanelContainer();
        panelPrincipal.getContentPane().add(panelContenido);
        
        // --- 3. Se añade el panel ensamblado al JFrame ---
        this.setLayout(new BorderLayout());
        this.add(panelPrincipal, BorderLayout.CENTER);
        
        // --- 4. Carga de Datos de Prueba ---
        tablaUsuarios.setUsuarios(crearDatosDePrueba());
        
        // NOTA: No es necesario añadir listeners manualmente. El framework se encarga
        // del data binding automático porque los 3 componentes hoja comparten la
        // propiedad "usuarioSeleccionado" y están dentro del mismo BaseContainer.
        
        // --- 5. Finalización ---
        this.pack();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Iniciar maximizado
        this.setLocationRelativeTo(null);
    }

    /**
     * Crea una lista de usuarios de ejemplo para poblar la tabla.
     * @return Una lista de objetos Usuario.
     */
    private List<Usuario> crearDatosDePrueba() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Ana", "García", "Intermedio", 60, 90));
        usuarios.add(new Usuario("Carlos", "Martínez", "Avanzado", 120, 160));
        usuarios.add(new Usuario("Elena", "Ruiz", "Principiante", 30, 40));
        return usuarios;
    }
    
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> {
            new GimnasioDemoFrame().setVisible(true);
        });
    }
}