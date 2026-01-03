package Main.servicio.Interfaces;

/**
 * Define el contrato para los servicios de seguridad y cifrado del sistema.
 * <p>
 * Proporciona métodos para proteger información sensible como contraseñas
 * y correos electrónicos mediante algoritmos de cifrado y descifrado.
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/25
 * </p>
 */
public interface Cifrador {

    /**
     * Cifra una contraseña en texto plano para su almacenamiento seguro.
     * @param contrasenia Texto original de la contraseña.
     * @return Cadena de texto cifrada.
     */
    public String cifrarContrasenia(String contrasenia);

    /**
     * Recupera la contraseña original a partir de su versión cifrada.
     * @param contraseniaCifrada Texto cifrado almacenado.
     * @return Contraseña en texto plano.
     */
    public String descifrarContrasenia(String contraseniaCifrada);

    /**
     * Cifra una dirección de correo electrónico para proteger la privacidad del usuario.
     * @param email Email en formato estándar.
     * @return Email transformado mediante cifrado.
     */
    public String cifrarEmail(String email);

    /**
     * Descifra un correo electrónico previamente cifrado.
     * @param emailCifrado Email en formato cifrado.
     * @return Email original en texto plano.
     */
    public String descifrarEmail(String emailCifrado);
}