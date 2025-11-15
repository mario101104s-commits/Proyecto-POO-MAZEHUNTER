package Main.modelo.Dominio;
/**
 * Representa la estructura completa del laberinto como una matriz bidimensional de celdas.
 *<p>
 * Esta clase maneja las dimensiones del mapa y proporciona metodos de acceso y validación
 * para interactuar con las celdas individuales dentro de los límites del laberinto.
 *</p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class Laberinto {
    /**
     * La matriz bidimensional que contiene todas las {@code Celda} que componen el laberinto.
     * La estructura es [fila][columna].
     */
    private Celda[][] celdas;
    /**
     * El número total de filas en el laberinto.
     */
    private int filas;
    /**
     * El número total de columnas en el laberinto.
     */
    private int columnas;

    /**
     * Construye un nuevo objeto Laberinto con sus dimensiones y celdas.
     *
     * @param celdas La matriz de celdas que forman la estructura del laberinto.
     * @param filas El número de filas de la matriz.
     * @param columnas El número de columnas de la matriz.
     */
    public Laberinto(Celda[][] celdas, int filas, int columnas) {
        this.celdas = celdas;
        this.filas = filas;
        this.columnas = columnas;
    }

    // Getters
    public Celda[][] getCeldas() { return celdas; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    /**
     * Obtiene una celda específica en las coordenadas dadas.
     *
     * @param fila El índice de la fila (de 0 a filas-1).
     * @param columna El índice de la columna (de 0 a columnas-1).
     * @return La {@code Celda} en la posición especificada, o {@code null} si la posicion es invalida.
     */
    public Celda getCelda(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return celdas[fila][columna];
        }
        return null;
    }
    /**
     * Establece o reemplaza una celda específica en las coordenadas dadas.
     *
     * @param fila El indice de la fila.
     * @param columna El indice de la columna.
     * @param celda El objeto {@code Celda} a colocar en esa posicion.
     */

    public void setCelda(int fila, int columna, Celda celda) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            celdas[fila][columna] = celda;
        }
    }
    /**
     * Verifica si una posición dada está dentro de los límites del laberinto.
     *
     * @param fila El índice de la fila a verificar.
     * @param columna El índice de la columna a verificar.
     * @return {@code true} si la posición es válida (dentro de los límites), {@code false} en caso contrario.
     */
    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }
    /**
     * Verifica si una celda en las coordenadas dadas es transitable por el jugador.
     *
     * Implica verificar que la posición sea válida y que el tipo de celda no sea un muro.
     *
     * @param fila El índice de la fila.
     * @param columna El índice de la columna.
     * @return {@code true} si la posición es válida y la celda es transitable, {@code false} en caso contrario.
     */
    public boolean esTransitable(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        return celda != null && celda.isTransitable();
    }
}
