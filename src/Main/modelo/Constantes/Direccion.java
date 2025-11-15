package Main.modelo.Constantes;

/**
 * Esta clase representa los cuatro movimientos posibles dentro del laberinto
 * <p>
 * Cada constante almacena el cambio que debe aplicarse a la fila y columna
 * de la posicion del jugador para moverse en esa direccion
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public enum Direccion {
    /**Movimiento hacia arriba. */
    ARRIBA(-1, 0),
    /**Movimiento hacia abajo. */
    ABAJO(1, 0),
    /**Movimiento hacia la izquierda. */
    IZQUIERDA(0, -1),
    /**Movimiento hacia la derecha. */
    DERECHA(0, 1);

    /**
     * El cambio en la coordenada de fila causado por este movimiento.
     */
    private final int deltaFila;
    /**
     * El cambio en la coordenada de columna causado por este movimiento.
     */
    private final int deltaColumna;

    /**
     * Constructor que inicializa la direccion con sus respectivos deltas de fila y columna
     * @param deltaFila el valor de cambio para la coordenada de fila
     * @param deltaColumna el valor de cambio para la coordenada de columna
     */
    Direccion(int deltaFila, int deltaColumna) {
        this.deltaFila = deltaFila;
        this.deltaColumna = deltaColumna;
        }
    /**
     * @return El cambio en el índice de la fila para esta dirección.
     */
    public int getDeltaFila() { return deltaFila; }
    /**
     * @return El cambio en el índice de la columna para esta dirección.
     */
    public int getDeltaColumna() { return deltaColumna; }
}
