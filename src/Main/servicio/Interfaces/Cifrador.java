package Main.servicio.Interfaces;
/**
 * Define el contrato para cualquier servicio de cifrado de contraseñas.
 * <p>
 * El propósito de esta interfaz es abstraer el algoritmo de cifrado,
 * permitiendo cambiar la implementación de seguridad sin afectar la lógica
 * de registro o autenticación.
 * </p>
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */
public interface Cifrador {
    /**
     * Cifra una cadena de texto plano (contraseña).
     * <p>
     * Se utiliza para almacenar las contraseñas de los usuarios de forma segura.
     * </p>
     * @param contrasenia La contraseña original en texto legible.
     * @return La contraseña cifrada como una cadena de texto, o {@code null} si falla el proceso.
     */
    public String cifrarContrasenia(String contrasenia);
    /**
     * Descifra una cadena de texto cifrado (contraseña).
     * <p>
     * Se utiliza para validar la contraseña ingresada por el usuario con la almacenada.
     * </p>
     * @param contraseniaCifrada La contraseña almacenada y cifrada.
     * @return La contraseña original en texto plano, o {@code null} si falla el proceso.
     */
    public String descifrarContrasenia(String contraseniaCifrada);
    /**
     * Recupera la contraseña cifrada almacenada para un usuario específico.
     * <p>
     * Este metodo interactúa con la capa de persistencia para obtener el hash de la contraseña.
     * </p>
     * @param email El correo electrónico del usuario.
     * @return La contraseña cifrada del usuario si existe, o {@code null} si el usuario no es encontrado.
     */
    public String recuperarContraseniaCifrada(String email);
}
