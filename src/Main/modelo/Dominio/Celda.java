package Main.modelo.Dominio;

import Main.modelo.Constantes.TipoCelda;

/**
 * Esta clase representa la posición individual dentro de la estructura del laberinto.
 * <p>
 * Cada celda almacena su tipo (muro, camino, cristal, etc.), su posición en la matriz
 * y su estado de visibilidad y descubrimiento para el jugador.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class Celda {
    /**
     * El tipo de contenido que tiene la celda (Muro, Camino, Enemigo, etc.).
     */
    private TipoCelda tipo;

    /**
     * La coordenada de la fila de la celda dentro de la matriz del laberinto.
     */
    private int fila;

    /**
     * La coordenada de la columna de la celda dentro de la matriz del laberinto.
     */
    private int columna;

    /**
     * Indica si la celda ha sido visitada por el jugador (utilizado para el seguimiento en el mapa).
     */
    private boolean visitada;

    /**
     * Indica si la celda es visible actualmente para el jugador (campo de visión).
     */
    private boolean visible;

    /**
     * Crea una nueva celda con su tipo y coordenadas.
     * <p>
     * Inicializa el estado de la celda como no visitada y no visible.
     * </p>
     *
     * @param tipo El tipo inicial de contenido de la celda.
     * @param fila La posición de la fila.
     * @param columna La posición de la columna.
     */
    public Celda(TipoCelda tipo, int fila, int columna) {
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        this.visitada = false;
        this.visible = false;
    }

    // --- Getters y Setters ---

    /**
     * Obtiene el tipo actual de la celda.
     * @return El {@link TipoCelda} que define el comportamiento de esta posición.
     */
    public TipoCelda getTipo() {
        return tipo;
    }

    /**
     * Define o cambia el tipo de la celda.
     * @param tipo El nuevo {@link TipoCelda} para esta ubicación.
     */
    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la coordenada de la fila.
     * @return Índice de la fila en la matriz.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Establece la coordenada de la fila.
     * @param fila Nuevo índice de fila.
     */
    public void setFila(int fila) {
        this.fila = fila;
    }

    /**
     * Obtiene la coordenada de la columna.
     * @return Índice de la columna en la matriz.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Establece la coordenada de la columna.
     * @param columna Nuevo índice de columna.
     */
    public void setColumna(int columna) {
        this.columna = columna;
    }

    /**
     * Indica si el jugador ya ha pasado por esta celda.
     * @return {@code true} si ha sido visitada, {@code false} en caso contrario.
     */
    public boolean isVisitada() {
        return visitada;
    }

    /**
     * Marca la celda como visitada o no visitada.
     * @param visitada Estado de exploración de la celda.
     */
    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }

    /**
     * Indica si la celda se encuentra dentro del rango de visión actual.
     * @return {@code true} si es visible, {@code false} si está bajo la "niebla de guerra".
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Actualiza el estado de visibilidad de la celda.
     * @param visible {@code true} para mostrar la celda, {@code false} para ocultarla.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Determina si el jugador puede moverse a esta celda.
     * <p>
     * Delega la verificación al {@link TipoCelda} asociado.
     * </p>
     * @return {@code true} si la celda permite el paso (no es muro), {@code false} en caso contrario.
     */
    public boolean isTransitable() {
        return tipo.isTransitable();
    }

    /**
     * Obtiene el carácter de representación de la celda.
     * <p>
     * Delega la obtención del símbolo al {@link TipoCelda} asociado.
     * </p>
     * @return El carácter que se utiliza para dibujar la celda en la interfaz.
     */
    public char getSimbolo() {
        return tipo.getSimbolo();
    }
}