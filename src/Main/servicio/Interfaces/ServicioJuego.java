package Main.servicio.Interfaces;

import Main.estrategia.contexto.ConfiguracionJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Transferencia.ResultadoJuego;

import java.util.List;

/**
 * Define el contrato para la gestión de la lógica de negocio del juego.
 * <p>
 * Proporciona métodos para el control del ciclo de vida de las partidas,
 * movimientos del jugador, mecánicas especiales y persistencia de resultados.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/25
 */
public interface ServicioJuego {

    /**
     * Crea e inicializa una nueva partida con los parámetros especificados.
     * 
     * @param filas         Número de filas del laberinto.
     * @param columnas      Número de columnas del laberinto.
     * @param usuario       Identificador del jugador.
     * @param configuracion Estrategia y configuración para la generación.
     * @return Instancia de {@link Juego} inicializada.
     */
    Juego iniciarNuevoJuego(int filas, int columnas, String usuario, ConfiguracionJuego configuracion);

    /**
     * Recupera una partida guardada previamente para un usuario.
     * 
     * @param usuario Identificador del jugador.
     * @return El {@link Juego} recuperado o null si no existe.
     */
    Juego cargarJuegoGuardado(String usuario);

    /**
     * Verifica si el usuario tiene una partida pendiente de finalizar.
     * 
     * @param usuario Identificador del jugador.
     * @return {@code true} si existe un archivo de guardado.
     */
    boolean existeJuegoGuardado(String usuario);

    /**
     * Procesa el desplazamiento del jugador en el laberinto.
     * 
     * @param juego     Partida actual.
     * @param direccion Sentido del movimiento.
     * @return {@code true} si el movimiento fue válido y se ejecutó.
     */
    boolean moverJugador(Juego juego, Direccion direccion);

    /**
     * Activa la mecánica de explosión para destruir muros cercanos.
     * 
     * @param juego Partida actual.
     * @return {@code true} si se cumplieron los requisitos y se ejecutó la acción.
     */
    boolean activarExplosion(Juego juego);

    /**
     * Teletransporta al jugador a una posición específica del laberinto.
     * 
     * @param juego   Partida actual.
     * @param fila    Fila destino del teletransporte.
     * @param columna Columna destino del teletransporte.
     * @return {@code true} si el teletransporte fue exitoso.
     */
    boolean teletransportarJugador(Juego juego, int fila, int columna);

    /**
     * Almacena el estado actual de la partida.
     * 
     * @param juego Partida a guardar.
     * @return {@code true} si el guardado fue exitoso.
     */
    boolean guardarJuego(Juego juego);

    /**
     * Finaliza la partida formalmente y calcula los resultados finales.
     * 
     * @param juego Partida a terminar.
     * @return Objeto con el resumen y métricas de la sesión.
     */
    ResultadoJuego terminarJuego(Juego juego);

    /**
     * Evalúa si el jugador cumple los requisitos para cruzar la salida.
     * 
     * @param juego Partida actual.
     * @return {@code true} si tiene la llave y está en la posición de salida.
     */
    boolean puedeSalir(Juego juego);

    /**
     * Registra el progreso actual en el historial sin cerrar la sesión de juego.
     * 
     * @param juego Partida en curso.
     * @return Resumen del desempeño hasta el momento.
     */
    ResultadoJuego guardarEstadisticasParciales(Juego juego);

    /**
     * Obtiene el historial de rendimiento de un usuario específico.
     * 
     * @param emailUsuario Correo del usuario.
     * @return Lista de estadísticas de partidas previas.
     */
    List<EstadisticasJuego> obtenerEstadisticas(String emailUsuario);
}