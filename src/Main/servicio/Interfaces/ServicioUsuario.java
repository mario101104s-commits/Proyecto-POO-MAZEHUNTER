package Main.servicio.Interfaces;

import Main.modelo.Dominio.Usuario;

import java.util.List;

/**
 * Define el contrato para la gestión de cuentas de usuario y administración.
 * <p>
 * Proporciona métodos para el registro, autenticación, consulta de perfiles
 * y generación de reportes estadísticos de los usuarios del sistema.
 * </p>
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/25
 */
public interface ServicioUsuario {

    /**
     * Registra un nuevo usuario en el sistema con sus credenciales básicas.
     * @param email Correo electrónico único del usuario.
     * @param contraseniaCifrada Contraseña previamente procesada por un cifrador.
     * @return {@code true} si el registro fue exitoso.
     */
    public boolean registrarUsuario(String email, String contraseniaCifrada);

    /**
     * Valida las credenciales de un usuario para permitir el acceso.
     * @param email Correo electrónico del usuario.
     * @param contraseniaCifrada Contraseña cifrada para comparar.
     * @return {@code true} si las credenciales son válidas.
     */
    public boolean autenticarUsuario(String email, String contraseniaCifrada);

    /**
     * Recupera la fecha de creación de la cuenta en formato crudo.
     * @param email Correo del usuario.
     * @return Cadena con la fecha de registro.
     */
    public String obtenerFechaRegistro(String email);

    /**
     * Recupera la fecha de registro con un formato legible para el usuario.
     * @param email Correo del usuario.
     * @return Fecha formateada (ej. dd/MM/yyyy).
     */
    public String obtenerFechaRegistroFormateada(String email);

    /**
     * Comprueba si un correo electrónico ya está registrado en el sistema.
     * @param email Correo a verificar.
     * @return {@code true} si el usuario existe.
     */
    public boolean existeUsuario(String email);

    /**
     * Recupera la lista completa de usuarios registrados.
     * @return Lista de objetos {@link Usuario}.
     */
    public List<Usuario> obtenerTodosUsuarios();

    /**
     * Obtiene el objeto de dominio completo de un usuario específico.
     * @param email Correo del usuario.
     * @return Objeto {@link Usuario} o null si no se encuentra.
     */
    public Usuario obtenerUsuario(String email);

    /**
     * Genera un resumen textual de los datos del usuario.
     * @param email Correo del usuario.
     * @return Información detallada en formato String.
     */
    public String obtenerInformacionUsuario(String email);

    /**
     * Genera un reporte consolidado de estadísticas globales de usuarios.
     * @return Resumen estadístico del sistema.
     */
    public String obtenerEstadisticasUsuarios();

    /**
     * Actualiza los datos de un usuario existente en la persistencia.
     * @param usuario Objeto con la información actualizada.
     * @throws Exception Si el usuario no existe o hay errores de integridad.
     */
    public void actualizarUsuario(Usuario usuario) throws Exception;
}