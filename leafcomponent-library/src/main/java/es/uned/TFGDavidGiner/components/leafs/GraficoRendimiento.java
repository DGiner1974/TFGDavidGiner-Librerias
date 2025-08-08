package es.uned.TFGDavidGiner.components.leafs;

import es.uned.TFGDavidGiner.components.modelo.Usuario;
import es.uned.TFGDavidGiner.core.LeafComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Componente hoja que muestra un gráfico de barras simple.
 * Se actualiza consumiendo propiedades simples compartidas como 'pesoMaxPressBanca'
 * y 'pesoMaxSentadilla' a través del mecanismo de data binding del framework.
 */
public class GraficoRendimiento extends LeafComponent {

    private ChartPanel chartPanel;

    // --- Campos para las propiedades compartidas ---
    private int pesoMaxPressBanca = 0;
    private int pesoMaxSentadilla = 0;
    private Usuario usuarioSeleccionado;

    private static final Set<String> sharedProperties = Set.of("usuarioSeleccionado");

    public GraficoRendimiento() {
        initComponents();
    }

    private void initComponents() {
        chartPanel = new ChartPanel();
        this.setLayout(new java.awt.BorderLayout());
        this.add(chartPanel, java.awt.BorderLayout.CENTER);
    }

    // --- Getters y Setters para las propiedades compartidas ---

    public int getPesoMaxPressBanca() {
        return pesoMaxPressBanca;
    }

    public void setPesoMaxPressBanca(int nuevoPeso) {
        if (this.pesoMaxPressBanca != nuevoPeso) {
            this.pesoMaxPressBanca = nuevoPeso;
            repaint(); // Pide al panel que se redibuje con el nuevo dato.
        }
    }

    public int getPesoMaxSentadilla() {
        return pesoMaxSentadilla;
    }

    public void setPesoMaxSentadilla(int nuevoPeso) {
        if (this.pesoMaxSentadilla != nuevoPeso) {
            this.pesoMaxSentadilla = nuevoPeso;
            repaint(); // Pide al panel que se redibuje con el nuevo dato.
        }
    }
    
    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario nuevoUsuario) {
        // La lógica para prevenir bucles está aquí: solo se actualiza la UI, no se vuelve a notificar.
        //Lo cambiamos porque al comparar las clases la dirección de memoria es la misma
        //if (this.usuarioSeleccionado == nuevoUsuario) return;
        System.out.println("GraficoRendimiento setUsuarioSeleccionado");
        this.usuarioSeleccionado = nuevoUsuario;
        this.pesoMaxSentadilla = usuarioSeleccionado.getPesoMaxSentadilla();
        this.pesoMaxPressBanca = usuarioSeleccionado.getPesoMaxPressBanca();
        repaint(); // Pide al panel que se redibuje con el nuevo dato.
    }
    
    @Override
    public Set<String> getSharedProperies() {
        return sharedProperties;
    }

    @Override
    public boolean configurar() {
        setUsuarioSeleccionado(usuarioSeleccionado);
        repaint();
        return true;
    }

    @Override
    public boolean validar() {
        // Este componente es solo de visualización, siempre es válido.
        return true;
    }

    @Override
    public String getError() {
        return "";
    }
    
    /**
     * Clase interna que extiende JPanel y sobrescribe su método de pintado
     * para dibujar el gráfico de barras personalizado.
     */
    private class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Si no hay datos, muestra un mensaje.
            if (pesoMaxPressBanca == 0 && pesoMaxSentadilla == 0) {
                g.drawString("Seleccione un usuario para ver su rendimiento", 10, 20);
                return;
            }

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int barWidth = 50;
            int spacing = 30;
            int bottomMargin = 20;
            int topMargin = 30;

            // Dibuja la barra para Press de Banca
            g.setColor(new Color(79, 129, 189)); // Azul
            int pressBancaHeight = (int) ((pesoMaxPressBanca / 200.0) * (panelHeight - bottomMargin - topMargin)); // Se asume un máximo de 200kg
            g.fillRect(spacing, panelHeight - pressBancaHeight - bottomMargin, barWidth, pressBancaHeight);
            g.setColor(Color.BLACK);
            g.drawString("Press Banca", spacing, panelHeight - 5);
            g.drawString(pesoMaxPressBanca + " kg", spacing + 5, panelHeight - pressBancaHeight - bottomMargin - 5);

            // Dibuja la barra para Sentadilla
            g.setColor(new Color(192, 80, 77)); // Rojo
            int sentadillaHeight = (int) ((pesoMaxSentadilla / 200.0) * (panelHeight - bottomMargin - topMargin));
            g.fillRect(spacing + barWidth + spacing, panelHeight - sentadillaHeight - bottomMargin, barWidth, sentadillaHeight);
            g.setColor(Color.BLACK);
            g.drawString("Sentadilla", spacing + barWidth + spacing, panelHeight - 5);
            g.drawString(pesoMaxSentadilla + " kg", spacing + barWidth + spacing + 5, panelHeight - sentadillaHeight - bottomMargin - 5);
        }
    }
}