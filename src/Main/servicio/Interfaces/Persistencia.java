package Main.servicio.Interfaces;

import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Usuario;

import java.util.List;
/**
 * Define el contrato de la capa de acceso a datos para todas las entidades del dominio.
 * <p>
 * Esta interfaz establece los métodos necesarios para la lectura, escritura y
 * actualización de Usuarios, Juegos y Estadísticas, independientemente de la
 * tecnología de almacenamiento subyacente (ej: archivos JSON, base de datos).
 * <p/>
 * @author Mario Sanchez y Niyerlin Muñoz
 * @version 1.0
 * @since 11/11/25
 */
public interface Persistencia {

    // Usuarios
    /**
     * Guarda un nuevo objeto Usuario en el almacén de datos.
     *
     * @param usuario El objeto Usuario a guardar.
     * @return {@code true} si el usuario fue guardado exitosamente, {@code false} si ya existía o falló.
     */
    boolean guardarUsuario(Usuario usuario);
    /**
     * Carga un usuario específico utilizando su correo electrónico como clave.
     *
     * @param email El correo electrónico del usuario a cargar.
     * @return El objeto {@code Usuario} si se encuentra, o {@code null} en caso contrario.
     */
    Usuario cargarUsuario(String email);
    /**
     * Carga la lista completa de todos los usuarios registrados en el sistema.
     *
     * @return Una {@code List} de todos los objetos {@code Usuario}.
     */
    List<Usuario> cargarTodosUsuarios();
    /**
     * Verifica la existencia de un usuario por su correo electrónico.
     *
     * @param email El correo electrónico a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    boolean existeUsuario(String email);
    /**
     * Actualiza la información de un usuario existente (usualmente la contraseña cifrada).
     *
     * @param usuario El objeto {@code Usuario} con los datos actualizados.
     * @throws Exception Si el usuario no se encuentra o si ocurre un error de I/O al guardar.
     */
    void actualizarUsuario(Usuario usuario) throws Exception;
    /**
     * Metodo auxiliar para cargar la lista de usuarios.
     *
     * @deprecated Este metodo no devuelve un valor y debe ser revisado; use {@link #cargarTodosUsuarios()} en su lugar.
     */
    void cargarUsuarios();
    List<EstadisticasJuego> cargarEstadisticas();

    // Juegos
    /**
     * Guarda el estado actual de un juego para permitir al usuario reanudar la partida.
     *
     * @param juego El objeto {@code Juego} con el estado actual del laberinto y jugador.
     * @return {@code true} si el juego fue guardado exitosamente.
     */
    boolean guardarJuego(Juego juego);
    /**
     * Carga el juego guardado de un usuario.
     *
     * @param usuario El correo electrónico del usuario.
     * @return El objeto {@code Juego} guardado, o {@code null} si no hay partida para ese usuario.
     */
    Juego cargarJuego(String usuario);
    /**
     * Verifica si existe una partida guardada para un usuario específico.
     *
     * @param usuario El correo electrónico del usuario.
     * @return {@code true} si existe una partida guardada.
     */
    boolean existeJuegoGuardado(String usuario);

    // Estadísticas
    /**
     * Guarda las estadísticas de una partida jugada.
     *
     * @param estadisticas El objeto {@code EstadisticasJuego} a guardar.
     * @return {@code true} si las estadísticas fueron guardadas exitosamente.
     */
    boolean guardarEstadisticas(EstadisticasJuego estadisticas);
    /**
     * Carga una lista de estadísticas asociadas a un usuario específico.
     *
     * @param usuario El correo electrónico del usuario.
     * @return Una lista de {@code EstadisticasJuego} jugadas por ese usuario.
     */
    List<EstadisticasJuego> cargarEstadisticas(String usuario);
    /**
     * Carga todas las estadísticas de juego de todos los usuarios.
     *
     * @return Una lista de todas las {@code EstadisticasJuego} registradas en el sistema.
     * @deprecated Este metodo es redundante con el siguiente; por favor, revise y unifique el nombre de los métodos.
     */
    List<EstadisticasJuego> cargarTodasEstadisticas();
    /**
     * Carga todas las estadísticas de juego de un usuario dado.
     *
     * @param usuario El correo electrónico del usuario.
     * @return Una lista de todas las {@code EstadisticasJuego} jugadas por el usuario.
     */
    List<EstadisticasJuego> cargarTodasEstadisticas(String usuario);
}