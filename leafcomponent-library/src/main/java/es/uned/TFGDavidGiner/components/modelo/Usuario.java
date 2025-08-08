package es.uned.TFGDavidGiner.components.modelo;

/**
 * Clase de modelo que representa a un usuario del gimnasio.
 * Implementa Cloneable para permitir la creación de copias "por valor".
 */
public class Usuario implements Cloneable {

    private String nombre;
    private String apellido;
    private String nivel;
    private int pesoMaxPressBanca;
    private int pesoMaxSentadilla;

    public Usuario(String nombre, String apellido, String nivel, int pesoMaxPressBanca, int pesoMaxSentadilla) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nivel = nivel;
        this.pesoMaxPressBanca = pesoMaxPressBanca;
        this.pesoMaxSentadilla = pesoMaxSentadilla;
    }
    
    public Usuario() {
        this.nombre = null;
        this.apellido = null;
        this.nivel = null;
        this.pesoMaxPressBanca = 0;
        this.pesoMaxSentadilla = 0;
    }

    // --- Getters y Setters (sin cambios) ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public int getPesoMaxPressBanca() { return pesoMaxPressBanca; }
    public void setPesoMaxPressBanca(int peso) { this.pesoMaxPressBanca = peso; }
    public int getPesoMaxSentadilla() { return pesoMaxSentadilla; }
    public void setPesoMaxSentadilla(int peso) { this.pesoMaxSentadilla = peso; }
    
    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
    
   
    public boolean equals(Usuario usuario) {
        return ((this.getNombre() == null ? usuario.getNombre() == null : this.getNombre().equals(usuario.getNombre())) && 
                (this.getApellido()== null ? usuario.getApellido() == null : this.getApellido().equals(usuario.getApellido())) && 
                (this.getNivel() == null ? usuario.getNivel() == null : this.getNivel().equals(usuario.getNivel())) && 
                (this.getPesoMaxPressBanca()==usuario.getPesoMaxPressBanca()) && 
                (this.getPesoMaxSentadilla()==usuario.getPesoMaxSentadilla()));
    }

    /**
     * Crea y devuelve una copia de este objeto.
     * @return un clon del objeto Usuario.
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // Esto no debería ocurrir, ya que implementamos Cloneable
            return new Usuario(this.nombre, this.apellido, this.nivel, this.pesoMaxPressBanca, this.pesoMaxSentadilla);
        }
    }
}