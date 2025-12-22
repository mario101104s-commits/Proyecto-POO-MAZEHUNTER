package Main.modelo.Dominio;

import Main.modelo.Constantes.EstadoJuego;

import java.time.LocalDateTime;

/**
 * Representa una partida activa o finalizada de Maze Hunter, encapsulando
 * el estado del laberinto, el jugador y los metadatos de la sesión.
 * <p>
 * Esta clase sirve como el modelo principal que se guarda y se recupera
 * para la persistencia del juego.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */

public class Juego {
    /**
     * La estructura actual del laberinto con sus celdas y contenido.
     */
    private Laberinto laberinto;
    /**
     * El estado del personaje del jugador (vida, posición, inventario).
     */
    private Jugador jugador;
    /**
     * La marca de tiempo que indica cuándo comenzó la partida.
     */
    private String usuario;
    /**
     * La marca de tiempo que indica cuándo inicio la partida (si no es
     * {@code null}).
     */
    private LocalDateTime inicio;
    /**
     * La marca de tiempo que indica cuándo finalizó la partida (si no es
     * {@code null}).
     */
    private LocalDateTime fin;
    /**
     * El estado actual de la partida (EN_CURSO, GANADO, PERDIDO, PAUSADO).
     */
    private EstadoJuego estado;
    /**
     * El contador de trampas que el jugador ha activado durante la partida.
     */
    private int trampasActivadas;
    /**
     * Indica si el juego tiene niebla de guerra activada (true) o no (false).
     */
    private boolean nieblaDeGuerra;

    // Estadísticas acumulativas
    private int bombasRecolectadasTotal;
    private int fosforosRecolectadosTotal;
    private int fosforosUsados;
    private int murosRojosDestruidos;

    /**
     * Construye una nueva instancia de Juego, inicializando los componentes
     * principales.
     * <p>
     * El estado inicial es {@code EN_CURSO} y el contador de trampas es cero.
     * </p>
     * 
     * @param laberinto La estructura del laberinto generada.
     * @param jugador   La instancia del jugador con sus atributos iniciales.
     * @param usuario   El correo electrónico del usuario que está jugando.
     * @param inicio    La marca de tiempo del inicio de la partida.
     */
    public Juego(Laberinto laberinto, Jugador jugador, String usuario, LocalDateTime inicio) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.usuario = usuario;
        this.inicio = inicio;
        this.estado = EstadoJuego.EN_CURSO;
        this.trampasActivadas = 0;
        this.trampasActivadas = 0;
        this.nieblaDeGuerra = true; // Por defecto activada

        this.bombasRecolectadasTotal = 0;
        this.fosforosRecolectadosTotal = 0;
        this.fosforosUsados = 0;
        this.murosRojosDestruidos = 0;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    public EstadoJuego getEstado() {
        return estado;
    }

    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }

    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    public boolean isNieblaDeGuerra() {
        return nieblaDeGuerra;
    }

    public void setNieblaDeGuerra(boolean nieblaDeGuerra) {
        this.nieblaDeGuerra = nieblaDeGuerra;
    }

    /**
     * Incrementa el contador de trampas activadas en uno.
     */
    public void incrementarTrampasActivadas() {

    }

    public int getBombasRecolectadasTotal() {
        return bombasRecolectadasTotal;
    }

    public void incrementarBombasRecolectadasTotal() {
        this.bombasRecolectadasTotal++;
    }

    public void setBombasRecolectadasTotal(int bombasRecolectadasTotal) {
        this.bombasRecolectadasTotal = bombasRecolectadasTotal;
    }

    public int getFosforosRecolectadosTotal() {
        return fosforosRecolectadosTotal;
    }

    public void incrementarFosforosRecolectadosTotal() {
        this.fosforosRecolectadosTotal++;
    }

    public void setFosforosRecolectadosTotal(int fosforosRecolectadosTotal) {
        this.fosforosRecolectadosTotal = fosforosRecolectadosTotal;
    }

    public int getFosforosUsados() {
        return fosforosUsados;
    }

    public void incrementarFosforosUsados() {
        this.fosforosUsados++;
    }

    public void setFosforosUsados(int fosforosUsados) {
        this.fosforosUsados = fosforosUsados;
    }

    public int getMurosRojosDestruidos() {
        return murosRojosDestruidos;
    }

    public void incrementarMurosRojosDestruidos() {
        this.murosRojosDestruidos++;
    }

    public void setMurosRojosDestruidos(int murosRojosDestruidos) {
        this.murosRojosDestruidos = murosRojosDestruidos;
    }
}
