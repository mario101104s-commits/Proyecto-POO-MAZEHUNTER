package Main.modelo.Dominio;

/**
 * Representa al personaje del jugador en el laberinto, manejando sus atributos
 * de juego y su posición actual.
 * <p>
 * Esta clase contiene la lógica para las interacciones básicas del jugador con
 * el entorno, como moverse, recoger objetos y sufrir daño. Mantiene el estado
 * del inventario y la salud vital para la progresión del juego.
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 15/11/2025
 */
public class Jugador {
    /** Nivel de salud actual del jugador (0 a 100). */
    private int vida;
    /** Cantidad de cristales recolectados por el jugador, utilizados para el puntaje. */
    private int cristales;
    /** Indica si el jugador ha recogido la llave necesaria para abrir la salida. */
    private boolean tieneLlave;
    /** Cantidad de bombas recolectadas para destruir muros. */
    private int bombas;
    /** Cantidad de fósforos disponibles para disipar la niebla de guerra. */
    private int fosforos;
    /** La coordenada X (columna) actual del jugador en el laberinto. */
    private int posX;
    /** La coordenada Y (fila) actual del jugador en el laberinto. */
    private int posY;

    /**
     * Construye un nuevo objeto Jugador.
     * <p>
     * Inicializa la posición en (0, 0) y establece los atributos iniciales del
     * juego. El inventario de herramientas comienza vacío.
     * </p>
     * * @param vida       La vida inicial del jugador.
     * @param cristales  La cantidad inicial de cristales.
     * @param tieneLlave Indica si comienza la partida con la llave (normalmente {@code false}).
     */
    public Jugador(int vida, int cristales, boolean tieneLlave) {
        this.vida = vida;
        this.cristales = cristales;
        this.tieneLlave = tieneLlave;
        this.bombas = 0;
        this.fosforos = 0;
        this.posX = 0;
        this.posY = 0;
    }

    // --- Métodos de Lógica ---

    /**
     * Mueve la posición del jugador sumando los deltas proporcionados a las
     * coordenadas actuales.
     *
     * @param deltaX El cambio en la coordenada X (columna).
     * @param deltaY El cambio en la coordenada Y (fila).
     */
    public void mover(int deltaX, int deltaY) {
        this.posX += deltaX;
        this.posY += deltaY;
    }

    /**
     * Incrementa en uno el contador de cristales recolectados.
     */
    public void recolectarCristal() {
        this.cristales++;
    }

    /**
     * Aplica el daño de una trampa, reduciendo la vida en 20 puntos.
     * La salud nunca descenderá por debajo de cero.
     */
    public void activarTrampa() {
        this.vida = Math.max(0, this.vida - 20);
    }

    /**
     * Establece que el jugador ha recogido la llave de la salida.
     */
    public void recogerLlave() {
        this.tieneLlave = true;
    }

    /**
     * Incrementa en uno el contador de bombas disponibles.
     */
    public void recolectarBomba() {
        this.bombas++;
    }

    /**
     * Reduce en uno el contador de bombas tras su uso, si existen disponibles.
     */
    public void decrementarBombas() {
        if (this.bombas > 0) {
            this.bombas--;
        }
    }

    /**
     * Incrementa en uno el contador de fósforos recolectados.
     */
    public void recolectarFosforo() {
        this.fosforos++;
    }

    /**
     * Reduce en uno el contador de fósforos tras su uso, si existen disponibles.
     */
    public void decrementarFosforos() {
        if (this.fosforos > 0) {
            this.fosforos--;
        }
    }

    /**
     * Verifica si el jugador aún tiene puntos de salud.
     *
     * @return {@code true} si la vida es mayor a cero, {@code false} en caso contrario.
     */
    public boolean estaVivo() {
        return vida > 0;
    }

    // --- Getters y Setters ---

    /** @return Nivel de vida actual. */
    public int getVida() {
        return vida;
    }

    /**
     * Establece la vida del jugador, asegurando que el valor se mantenga
     * en el rango lógico de [0, 100].
     *
     * @param vida El nuevo valor de vida.
     */
    public void setVida(int vida) {
        this.vida = Math.max(0, Math.min(100, vida));
    }

    /** @return Cantidad de cristales en posesión. */
    public int getCristales() {
        return cristales;
    }

    /** @param cristales Nuevo valor de cristales recolectados. */
    public void setCristales(int cristales) {
        this.cristales = cristales;
    }

    /** @return {@code true} si posee la llave, {@code false} si no. */
    public boolean isTieneLlave() {
        return tieneLlave;
    }

    /** @param tieneLlave Estado de posesión de la llave. */
    public void setTieneLlave(boolean tieneLlave) {
        this.tieneLlave = tieneLlave;
    }

    /** @return Coordenada X actual. */
    public int getPosX() {
        return posX;
    }

    /** @param posX Nueva posición en el eje X. */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /** @return Coordenada Y actual. */
    public int getPosY() {
        return posY;
    }

    /** @param posY Nueva posición en el eje Y. */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /** @return Número de bombas en inventario. */
    public int getBombas() {
        return bombas;
    }

    /** @param bombas Cantidad de bombas a establecer. */
    public void setBombas(int bombas) {
        this.bombas = bombas;
    }

    /** @return Número de fósforos en inventario. */
    public int getFosforos() {
        return fosforos;
    }

    /** @param fosforos Cantidad de fósforos a establecer. */
    public void setFosforos(int fosforos) {
        this.fosforos = fosforos;
    }
}