package Main.modelo.Dominio;
/**
 * Clase que representa un usuario del sistema.
 * <p>
 * Esta clase almacena la información básica de un usuario incluyendo su email,
 * contraseña cifrada y fecha de registro. La contraseña siempre se almacena
 * en formato cifrado por seguridad.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class Usuario {
    /**
     * Email único del usuario, utilizado como identificador principal.
     * Debe seguir el formato estándar de correo electrónico.
     */
    private String email;
    /**
     * Contraseña del usuario almacenada en formato cifrado..
     */
    private String contraseniaCifrada;
    /**
     * Fecha de registro del usuario en el sistema.
     */
    private String fechaRegistro;
    /**
     * Constructor para crear una nueva instancia de Usuario.
     *
     * @param email Email único del usuario
     * @param contraseniaCifrada Contraseña ya cifrada del usuario
     * @param fechaRegistro Fecha de registro en formato String
     */
    public Usuario(String email, String contraseniaCifrada, String fechaRegistro) {
        this.email = email;
        this.contraseniaCifrada = contraseniaCifrada;
        this.fechaRegistro = fechaRegistro;
    }
    //Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseniaCifrada() {
        return contraseniaCifrada;
    }

    public void setContraseniaCifrada(String contraseniaCifrada) {
        this.contraseniaCifrada = contraseniaCifrada;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    //metodos
    /**
     * Proporciona una representación concisa del objeto Usuario, mostrando el email y la fecha de registro.
     * @return Una cadena que representa los datos clave del usuario.
     */
    @Override
    public String toString() {
        return "Usuario{email='" + email + "', fechaRegistro='" + fechaRegistro + "'}";
    }

}
