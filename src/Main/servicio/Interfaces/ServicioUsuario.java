package Main.servicio.Interfaces;

import Main.modelo.Dominio.Usuario;

import java.util.List;
/**
 * Define el contrato de la capa de servicio para la gestión de cuentas de usuario.
 * <p>
 * Esta interfaz abstrae la lógica de negocio relacionada con el registro,
 * la autenticación y la recuperación de información de los usuarios del sistema.
 * </p>
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */
public interface ServicioUsuario {
    /**
     * Intenta registrar un nuevo usuario en el sistema.
     *
     * @param email El correo electrónico del nuevo usuario (clave única).
     * @param contraseniaCifrada La contraseña del usuario ya procesada y cifrada.
     * @return {@code true} si el usuario fue registrado exitosamente, {@code false} si ya existía o falló el guardado.
     */
    public boolean registrarUsuario(String email, String contraseniaCifrada);
    /**
     * Verifica si las credenciales proporcionadas corresponden a un usuario válido.
     *
     * @param email El correo electrónico del usuario.
     * @param contraseniaCifrada La contraseña ingresada por el usuario, ya cifrada para comparación.
     * @return {@code true} si el usuario existe y la contraseña es correcta, {@code false} en caso contrario.
     */
    public boolean autenticarUsuario(String email, String contraseniaCifrada);
    /**
     * Obtiene la fecha y hora de registro de un usuario en su formato ISO original.
     *
     * @param email El correo electrónico del usuario.
     * @return La fecha de registro como String, o {@code null} si el usuario no existe.
     */
    public String obtenerFechaRegistro(String email);
    /**
     * Obtiene la fecha de registro de un usuario formateada para una presentación legible.
     *
     * @param email El correo electrónico del usuario.
     * @return La fecha de registro formateada.
     */
    public String obtenerFechaRegistroFormateada(String email);
    /**
     * Verifica si un usuario con el correo electrónico dado existe en la base de datos.
     *
     * @param email El correo electrónico a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    public boolean existeUsuario(String email);
    /**
     * Obtiene la lista completa de todos los usuarios registrados.
     *
     * @return Una {@code List} de todos los objetos {@code Usuario}.
     */
    public List<Usuario> obtenerTodosUsuarios();
    /**
     * Obtiene un usuario específico utilizando su correo electrónico.
     *
     * @param email El correo electrónico del usuario.
     * @return El objeto {@code Usuario} si se encuentra, o {@code null} si no existe.
     */
    public Usuario obtenerUsuario(String email);
    /**
     * Obtiene un resumen formateado de la información principal del usuario.
     *
     * @param email El correo electrónico del usuario.
     * @return Una cadena de texto con la información de la cuenta.
     */
    public String obtenerInformacionUsuario(String email);
    /**
     * Genera un reporte que incluye un resumen del total de usuarios y los últimos registrados.
     *
     * @return Una cadena de texto con las estadísticas de usuarios.
     */
    public String obtenerEstadisticasUsuarios();

}
