package Main.servicio.Interfaces;

import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Laberinto;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Transferencia.ResultadoJuego;
/**
 * Define el contrato de la capa de servicio para todas las operaciones relacionadas
 * con la gestión y el progreso de una partida del juego Maze Hunter.
 * <p>
 * Esta interfaz abstrae la lógica de negocio central, incluyendo la inicialización,
 * el movimiento del jugador, la persistencia del estado del juego y la finalización.
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 2025-11-15
 */
public interface ServicioJuego {
    /**
     * Inicializa y prepara un nuevo juego de laberinto para un usuario.
     *
     * @param filas El número de filas del laberinto a generar.
     * @param columnas El número de columnas del laberinto a generar.
     * @param usuario El correo electrónico del usuario que inicia la partida.
     * @return Un objeto {@code Juego} recién inicializado.
     */
    Juego iniciarNuevoJuego(int filas, int columnas, String usuario);
    /**
     * Carga el estado de un juego guardado previamente para un usuario.
     *
     * @param usuario El correo electrónico del usuario cuyo juego se desea cargar.
     * @return El objeto {@code Juego} guardado, o {@code null} si no existe una partida para ese usuario.
     */
    Juego cargarJuegoGuardado(String usuario);
    /**
     * Intenta mover al jugador en el juego actual en la dirección especificada.
     * <p>
     * Este metodo contiene la lógica de validación de movimiento y el procesamiento
     * de la celda destino (recolección de objetos, activación de trampas, etc.).
     * </p>
     * @param juego El objeto {@code Juego} actual.
     * @param direccion La {@code Direccion} del movimiento.
     * @return {@code true} si el movimiento fue válido y exitoso, {@code false} en caso contrario.
     */
    boolean moverJugador(Juego juego, Direccion direccion);
    /**
     * Persiste el estado actual del juego.
     *
     * @param juego El objeto {@code Juego} a guardar.
     * @return {@code true} si el guardado fue exitoso.
     */
    boolean guardarJuego(Juego juego);
    /**
     * Finaliza la partida y calcula todas las métricas
     * de rendimiento para el registro de estadísticas.
     *
     * @param juego El objeto {@code Juego} terminado.
     * @return Un objeto {@code ResultadoJuego} con todas las métricas finales.
     */
    ResultadoJuego terminarJuego(Juego juego);
    /**
     * Verifica si el jugador se encuentra en la celda de salida y ha recolectado la llave.
     *
     * @param juego El objeto {@code Juego} actual.
     * @return {@code true} si se cumplen las condiciones de escape.
     */
    boolean puedeSalir(Juego juego);
    /**
     * Guarda el estado del juego y las estadísticas parciales, típicamente cuando el usuario
     * decide salir de la partida en curso.
     *
     * @param juego El objeto {@code Juego} en curso.
     * @return Un objeto {@code ResultadoJuego} con las métricas parciales registradas.
     */
    ResultadoJuego guardarEstadisticasParciales(Juego juego);
}
