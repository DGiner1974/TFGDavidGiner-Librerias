package es.uned.TFGDavidGiner.components.modelo;

/**
 * Clase de modelo que representa a un usuario del gimnasio.
 * Contiene los datos y la lógica de negocio simple asociada a un usuario.
 */
public class Usuario {

    private String nombre;
    private String apellido;
    private String nivel; // Principiante, Intermedio, Avanzado
    private int pesoMaxPressBanca;
    private int pesoMaxSentadilla;

    public Usuario(String nombre, String apellido, String nivel, int pesoMaxPressBanca, int pesoMaxSentadilla) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nivel = nivel;
        this.pesoMaxPressBanca = pesoMaxPressBanca;
        this.pesoMaxSentadilla = pesoMaxSentadilla;
    }

    // --- Getters y Setters ---

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public int getPesoMaxPressBanca() {
        return pesoMaxPressBanca;
    }

    public void setPesoMaxPressBanca(int pesoMaxPressBanca) {
        this.pesoMaxPressBanca = pesoMaxPressBanca;
    }

    public int getPesoMaxSentadilla() {
        return pesoMaxSentadilla;
    }

    public void setPesoMaxSentadilla(int pesoMaxSentadilla) {
        this.pesoMaxSentadilla = pesoMaxSentadilla;
    }
    
    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
    
    /**
     * Crea y devuelve una copia de este objeto. Se declara público
     * para que pueda ser llamado desde otras clases.
     * @return un clon del objeto Usuario.
     */
    @Override
    public Usuario clone() { 
        try {
            return (Usuario) super.clone();
        } catch (CloneNotSupportedException e) {
            // Esto no debería ocurrir, ya que implementamos Cloneable
            return new Usuario(this.nombre, this.apellido, this.nivel, this.pesoMaxPressBanca, this.pesoMaxSentadilla);
        }
    }
}