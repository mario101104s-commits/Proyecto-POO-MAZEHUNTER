package Main.modelo.Dominio;

import Main.modelo.Constantes.EstadoJuego;
import java.time.LocalDateTime;

/**
 * Representa una partida activa o finalizada de Maze Hunter, encapsulando
 * el estado del laberinto, el jugador y los metadatos de la sesión.
 * <p>
 * Esta clase sirve como el modelo principal que se guarda y se recupera
 * para la persistencia del juego, actuando como un contenedor de toda la
 * información necesaria para reanudar una partida.
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.0
 * @since 11/11/2025
 */
public class Juego {
    /** La estructura actual del laberinto con sus celdas y contenido. */
    private Laberinto laberinto;
    /** El estado del personaje del jugador (vida, posición, inventario). */
    private Jugador jugador;
    /** El identificador (correo) del usuario propietario de la partida. */
    private String usuario;
    /** Marca de tiempo del inicio de la partida. */
    private LocalDateTime inicio;
    /** Marca de tiempo del fin de la partida. */
    private LocalDateTime fin;
    /** El estado actual de la partida (EN_CURSO, GANADO, PERDIDO, PAUSADO). */
    private EstadoJuego estado;
    /** Contador de trampas activadas durante la sesión. */
    private int trampasActivadas;
    /** Indica si la visibilidad está limitada por niebla de guerra. */
    private boolean nieblaDeGuerra;
    /** Tiempo jugado acumulado en segundos. */
    private long tiempoJugadoSegundos;
    /** El nivel de dificultad de la partida. */
    private String dificultad;

    // Estadísticas acumulativas
    /** Total de bombas recogidas en esta sesión. */
    private int bombasRecolectadasTotal;
    /** Total de fósforos recogidos en esta sesión. */
    private int fosforosRecolectadosTotal;
    /** Cantidad de fósforos consumidos por el jugador. */
    private int fosforosUsados;
    /** Contador de muros especiales eliminados. */
    private int murosRojosDestruidos;

    /**
     * Construye una nueva instancia de Juego, inicializando los componentes
     * principales.
     * <p>
     * Por defecto, la niebla de guerra se activa y los contadores de estadísticas
     * inician en cero. El estado inicial se establece como
     * {@link EstadoJuego#EN_CURSO}.
     * </p>
     * * @param laberinto La estructura del laberinto generada.
     * 
     * @param jugador La instancia del jugador con sus atributos iniciales.
     * @param usuario El correo electrónico del usuario que está jugando.
     * @param inicio  La marca de tiempo del inicio de la partida.
     */
    public Juego(Laberinto laberinto, Jugador jugador, String usuario, LocalDateTime inicio) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.usuario = usuario;
        this.inicio = inicio;
        this.estado = EstadoJuego.EN_CURSO;
        this.trampasActivadas = 0;
        this.nieblaDeGuerra = true;
        this.tiempoJugadoSegundos = 0;

        this.bombasRecolectadasTotal = 0;
        this.fosforosRecolectadosTotal = 0;
        this.fosforosUsados = 0;
        this.murosRojosDestruidos = 0;
    }

    // --- Getters y Setters ---

    /** @return El objeto laberinto asociado. */
    public Laberinto getLaberinto() {
        return laberinto;
    }

    /** @param laberinto El laberinto a establecer. */
    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
    }

    /** @return La instancia del jugador. */
    public Jugador getJugador() {
        return jugador;
    }

    /** @param jugador El estado del jugador a establecer. */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /** @return El correo del usuario. */
    public String getUsuario() {
        return usuario;
    }

    /** @param usuario El correo del usuario. */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /** @return Fecha de inicio. */
    public LocalDateTime getInicio() {
        return inicio;
    }

    /** @param inicio Fecha de inicio. */
    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    /** @return Fecha de finalización o null si sigue en curso. */
    public LocalDateTime getFin() {
        return fin;
    }

    /** @param fin Fecha de finalización. */
    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    /** @return El {@link EstadoJuego} actual. */
    public EstadoJuego getEstado() {
        return estado;
    }

    /** @param estado El nuevo estado del juego. */
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
    }

    /** @return Número de trampas activadas. */
    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    /** @param trampasActivadas Número de trampas activadas. */
    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    /** @return Si la niebla de guerra está activa. */
    public boolean isNieblaDeGuerra() {
        return nieblaDeGuerra;
    }

    /** @param nieblaDeGuerra Estado de la niebla de guerra. */
    public void setNieblaDeGuerra(boolean nieblaDeGuerra) {
        this.nieblaDeGuerra = nieblaDeGuerra;
    }

    /** @return Tiempo jugado acumulado en segundos. */
    public long getTiempoJugadoSegundos() {
        return tiempoJugadoSegundos;
    }

    /** @param tiempoJugadoSegundos Tiempo jugado acumulado en segundos. */
    public void setTiempoJugadoSegundos(long tiempoJugadoSegundos) {
        this.tiempoJugadoSegundos = tiempoJugadoSegundos;
    }

    /** @return Nivel de dificultad. */
    public String getDificultad() {
        return dificultad;
    }

    /** @param dificultad Nivel de dificultad. */
    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    // --- Métodos de estadísticas acumulativas ---

    /** @return Total de bombas recolectadas. */
    public int getBombasRecolectadasTotal() {
        return bombasRecolectadasTotal;
    }

    /** @param bombasRecolectadasTotal Valor acumulado de bombas. */
    public void setBombasRecolectadasTotal(int bombasRecolectadasTotal) {
        this.bombasRecolectadasTotal = bombasRecolectadasTotal;
    }

    /** @return Total de fósforos encontrados. */
    public int getFosforosRecolectadosTotal() {
        return fosforosRecolectadosTotal;
    }

    /** @param fosforosRecolectadosTotal Valor acumulado de fósforos. */
    public void setFosforosRecolectadosTotal(int fosforosRecolectadosTotal) {
        this.fosforosRecolectadosTotal = fosforosRecolectadosTotal;
    }

    /** @return Total de fósforos consumidos. */
    public int getFosforosUsados() {
        return fosforosUsados;
    }

    /** @param fosforosUsados Valor acumulado de uso. */
    public void setFosforosUsados(int fosforosUsados) {
        this.fosforosUsados = fosforosUsados;
    }

    /** @return Total de muros rojos destruidos. */
    public int getMurosRojosDestruidos() {
        return murosRojosDestruidos;
    }

    /** @param murosRojosDestruidos Valor acumulado de destrucción. */
    public void setMurosRojosDestruidos(int murosRojosDestruidos) {
        this.murosRojosDestruidos = murosRojosDestruidos;
    }

    // --- Métodos de Incremento ---

    /** Incrementa el contador de trampas activadas en una unidad. */
    public void incrementarTrampasActivadas() {
        this.trampasActivadas++;
    }

    /** Incrementa el contador total de bombas recolectadas. */
    public void incrementarBombasRecolectadasTotal() {
        this.bombasRecolectadasTotal++;
    }

    /** Incrementa el contador total de fósforos recolectados. */
    public void incrementarFosforosRecolectadosTotal() {
        this.fosforosRecolectadosTotal++;
    }

    /** Incrementa el contador de fósforos utilizados. */
    public void incrementarFosforosUsados() {
        this.fosforosUsados++;
    }

    /** Incrementa el contador de muros rojos destruidos mediante bombas. */
    public void incrementarMurosRojosDestruidos() {
        this.murosRojosDestruidos++;
    }
}