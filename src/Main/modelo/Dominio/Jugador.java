package Main.modelo.Dominio;

/**
 * Representa al personaje del jugador en el laberinto, manejando sus atributos
 * de juego y su posición actual.
 * <p>
 * Esta clase contiene la lógica para las interacciones básicas del jugador con
 * el entorno,
 * como moverse, recoger objetos y sufrir daño.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 2025-11-15
 */
public class Jugador {
    /**
     * Nivel de salud actual del jugador (0 a 100).
     */
    private int vida;
    /**
     * Cantidad de cristales recolectados por el jugador, utilizados para el
     * puntaje.
     */
    private int cristales;
    /**
     * Indica si el jugador ha recogido la llave necesaria para abrir la salida.
     */
    private boolean tieneLlave;
    /**
     * Cantidad de bombas recolectadas por el jugador.
     */
    private int bombas;
    /**
     * Cantidad de llaves de explosión recolectadas por el jugador.
     */
    private int fosforos;
    /**
     * La coordenada X (columna) actual del jugador en el laberinto.
     */
    private int posX;
    /**
     * La coordenada Y (fila) actual del jugador en el laberinto.
     */
    private int posY;

    /**
     * Construye un nuevo objeto Jugador.
     * <p>
     * Inicializa la posición en (0, 0) y establece los atributos iniciales del
     * juego.
     * </p>
     * 
     * @param vida       La vida inicial del jugador.
     * @param cristales  La cantidad inicial de cristales.
     * @param tieneLlave Indica si comienza la partida con la llave (normalmente
     *                   {@code false}).
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

    // Metodos
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
     * Aplica el daño de una trampa, reduciendo la vida en 20 puntos, sin que la
     * vida sea negativa.
     */
    public void activarTrampa() {
        this.vida = Math.max(0, this.vida - 20);
    }

    /**
     * Establece que el jugador ha recogido la llave.
     */
    public void recogerLlave() {
        this.tieneLlave = true;
    }

    /**
     * Incrementa en uno el contador de bombas recolectadas.
     */
    public void recolectarBomba() {
        this.bombas++;
    }

    /**
     * Decrementa en uno el contador de bombas.
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
     * Decrementa en uno el contador de fósforos.
     */
    public void decrementarFosforos() {
        if (this.fosforos > 0) {
            this.fosforos--;
        }
    }

    /**
     * Verifica si el jugador aún tiene vida.
     *
     * @return {@code true} si la vida es mayor a cero, {@code false} si el jugador
     *         ha muerto.
     */
    public boolean estaVivo() {
        return vida > 0;
    }

    // Getter y Setters:

    public int getVida() {
        return vida;
    }

    /**
     * Establece la vida del jugador, asegurando que el valor se mantenga en el
     * rango de 0 a 100.
     *
     * @param vida El nuevo valor de vida.
     */
    public void setVida(int vida) {
        this.vida = Math.max(0, Math.min(100, vida));
    }

    public int getCristales() {
        return cristales;
    }

    public void setCristales(int cristales) {
        this.cristales = cristales;
    }

    public boolean isTieneLlave() {
        return tieneLlave;
    }

    public void setTieneLlave(boolean tieneLlave) {
        this.tieneLlave = tieneLlave;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getBombas() {
        return bombas;
    }

    public void setBombas(int bombas) {
        this.bombas = bombas;
    }

    public int getFosforos() {
        return fosforos;
    }

    public void setFosforos(int fosforos) {
        this.fosforos = fosforos;
    }
}
