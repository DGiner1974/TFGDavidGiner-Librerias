package es.uned.TFGDavidGiner.components.leafs;

import javax.swing.*;
import java.awt.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Un panel de interfaz de usuario diseñado para actuar como un editor de propiedades personalizado.
 * <p>
 * Muestra una lista de propiedades de un componente específico (en este caso, {@code ComponenteAComp})
 * como una serie de casillas de verificación ({@link JCheckBox}). El panel utiliza un
 * {@link JScrollPane} para asegurar que la lista sea navegable incluso si es muy larga.
 * Está pensado para ser utilizado dentro de un {@link java.beans.PropertyEditor} para
 * permitir la selección múltiple de propiedades en un entorno de desarrollo integrado (IDE).
 *
 * @author david
 * @version 1.0
 * @since 12-07-2025
 */
public class EditorPanel extends JPanel {

    /**
     * Lista de todas las casillas de verificación generadas para las propiedades del componente.
     */
    private final List<JCheckBox> checkBoxes;

    /**
     * Construye el panel del editor.
     * <p>
     * Crea una lista desplazable de casillas de verificación, donde cada una representa una
     * propiedad del componente de destino. Las casillas correspondientes al {@code valorActual}
     * se marcan como seleccionadas.
     *
     * @param valorActual Una cadena de texto con los nombres de las propiedades actualmente
     * seleccionadas, separadas por comas (ej: "propiedad1,propiedad2").
     */
    public EditorPanel(String valorActual) {
        // Se configura el layout principal del panel. BorderLayout es ideal para un JScrollPane.
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Obtiene la lista de propiedades del componente y crea un JCheckBox para cada una.
        this.checkBoxes = getPropiedadesDelComponente().stream()
                .map(JCheckBox::new)
                .collect(Collectors.toList());

        // Se crea un panel intermedio que contendrá todas las casillas de verificación.
        // BoxLayout con Y_AXIS apilará los componentes verticalmente.
        JPanel panelDeChecks = new JPanel();
        panelDeChecks.setLayout(new BoxLayout(panelDeChecks, BoxLayout.Y_AXIS));

        // Parsea la cadena de entrada para saber qué casillas deben estar marcadas inicialmente.
        Set<String> propiedadesMarcadas = new HashSet<>();
        if (valorActual != null && !valorActual.isEmpty()) {
            propiedadesMarcadas.addAll(Arrays.asList(valorActual.split(",")));
        }

        // Itera sobre las casillas creadas, las marca si es necesario y las añade al panel intermedio.
        for (JCheckBox cb : checkBoxes) {
            if (propiedadesMarcadas.contains(cb.getText())) {
                cb.setSelected(true);
            }
            panelDeChecks.add(cb);
        }

        // Se crea el JScrollPane y se le asigna el panel con las casillas.
        JScrollPane scrollPane = new JScrollPane(panelDeChecks);
        // Se define un tamaño preferido para evitar que el diálogo se haga demasiado grande.
        scrollPane.setPreferredSize(new Dimension(350, 400));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Propiedades del Componente"));

        // Finalmente, se añade el JScrollPane al centro del panel principal.
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Devuelve el resultado de la selección del usuario.
     * <p>
     * Concatena los nombres de todas las propiedades cuyas casillas de verificación
     * están seleccionadas en una única cadena de texto separada por comas.
     *
     * @return Una cadena con las propiedades seleccionadas (ej: "color,textoBoton").
     */
    public String getResultado() {
        // Usa Streams para filtrar las casillas seleccionadas, obtener su texto y unirlas con una coma.
        return checkBoxes.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .collect(Collectors.joining(","));
    }

    /**
     * Obtiene dinámicamente la lista de nombres de las propiedades del componente {@code ComponenteAComp}.
     * <p>
     * Utiliza el mecanismo de introspección de JavaBeans para analizar la clase
     * y extraer sus propiedades, excluyendo las que no son relevantes para el usuario
     * (como "class").
     *
     * @return Una lista ordenada de nombres de propiedades.
     */
    private List<String> getPropiedadesDelComponente() {
        try {
            // Obtiene la información del Bean para la clase especificada.
            BeanInfo beanInfo = Introspector.getBeanInfo(ComponenteAComp.class);
            // Extrae los descriptores de todas sus propiedades (basado en getters/setters).
            PropertyDescriptor[] descriptores = beanInfo.getPropertyDescriptors();

            // Convierte el array de descriptores en un Stream para procesarlo.
            return Stream.of(descriptores)
                    // Extrae el nombre de cada propiedad.
                    .map(PropertyDescriptor::getName)
                    // Filtra propiedades genéricas o no deseadas.
                    .filter(nombre -> !nombre.equals("propiedadesSeleccionadas") && !nombre.equals("class"))
                    // Ordena la lista alfabéticamente para una mejor presentación.
                    .sorted()
                    // Recopila los resultados en una lista.
                    .collect(Collectors.toList());
        } catch (IntrospectionException e) {
            // En caso de error durante la introspección, imprime el error y devuelve una lista vacía.
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}