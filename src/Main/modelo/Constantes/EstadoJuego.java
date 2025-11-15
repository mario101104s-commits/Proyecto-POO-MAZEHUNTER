package Main.modelo.Constantes;

/**
 *Define los posibles estados en los que puede encontrarse una partida de Maze Hunter
 * <p>
 * Estas constantes son utilizadas para controlar el bucle principal del juego,
 * determinar la condicion de finalizacion y el estado actual de la partida.
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public enum EstadoJuego {
    /** El juego está activo y el jugador puede moverse. */
    EN_CURSO,
    /** La partida ha finalizado porque el jugador ha alcanzado la salida del laberinto y ha ganado. */
    GANADO,
    /** La partida ha finalizado porque el jugador fue atrapado o falló el objetivo. */
    PERDIDO,
    /** El estado de la partida está detenido temporalmente. */
    PAUSADO
}
