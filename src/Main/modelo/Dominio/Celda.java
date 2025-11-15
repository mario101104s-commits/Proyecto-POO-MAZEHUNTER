package Main.modelo.Dominio;

import Main.modelo.Constantes.TipoCelda;

/**
 * Esta clase representa la posicion individual dentro de la estructura del laberinto.
 *
 * Cada celda almacena su tipo (muro, camino,cristal, etc), su posicion en la matriz
 * y su estado de visibilidad y descubrimiento para el jugador
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
     *
     * Inicializa el estado de la celda como no visitada y no visible.
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
    //Getter y Setters

    public TipoCelda getTipo() {
        return tipo;
    }

    public void setTipo(TipoCelda tipo) {
        this.tipo = tipo;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public boolean isVisitada() {
        return visitada;
    }

    public void setVisitada(boolean visitada) {
        this.visitada = visitada;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    //Metodos

    /**
     * Determina si el jugador puede moverse a esta celda.
     *
     * Delega la verificación al {@code TipoCelda} asociado.
     *
     * @return {@code true} si la celda permite el paso (no es muro), {@code false} en caso contrario.
     */
    public boolean isTransitable() {
        return tipo.isTransitable();
    }

    /**
     * Obtiene el carácter de representación de la celda.
     * <p>
     * Delega la obtención del símbolo al {@code TipoCelda} asociado.
     * </p>
     * @return El carácter que se utiliza para dibujar la celda en la consola.
     */
    public char getSimbolo() {
        return tipo.getSimbolo();
    }

}

