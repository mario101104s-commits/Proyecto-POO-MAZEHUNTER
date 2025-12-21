package Main.modelo.Dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Almacena los datos de rendimiento clave y el resultado final de una partida
 * de Maze Hunter.
 * <p>
 * Esta clase es utilizada por el sistema de persistencia para guardar un
 * registro
 * historico de las partidas jugadas por cada usuario.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */

public class EstadisticasJuego {
    /**
     * El correo electronico del usuario asociado a estas estadisticas.
     */
    private String usuario;
    /**
     * La fecha y hora exactas en que se guardaron estas estadisticas.
     */
    private LocalDateTime fecha;
    /**
     * El tiempo total que duró la partida, medido en segundos.
     */
    private long tiempoSegundos;
    /**
     * Número de items de cristal que el jugador logro recolectar durante la
     * partida.
     */
    private int cristalesRecolectados;
    /**
     * El número de veces que el jugador activo una trampa.
     */
    private int trampasActivadas;
    /**
     * La cantidad de vida que le restaba al jugador al finalizar la partida.
     */
    private int vidaRestante;

    /**
     * La dimension del laberinto.
     */
    private String tamanioLaberinto;
    /**
     * Indica si la partida termino en victoria ({@code true}) o derrota
     * ({@code false}).
     */
    private boolean ganado;

    // Nuevas estadísticas
    private int bombasRecolectadas;
    private int murosDestruidos;
    private int llavesExplosionUsadas;

    /**
     * Crea una nueva instancia de EstadisticasJuego, inicializando el usuario y la
     * fecha.
     *
     * Los demas campos de rendimiento deben ser establecidos posteriormente a
     * traves de sus setters.
     *
     * @param usuario El email del usuario que jugo la partida.
     * @param fecha   La marca de tiempo del inicio o fin del registro de
     *                estadísticas.
     */
    public EstadisticasJuego(String usuario, LocalDateTime fecha) {
        this.usuario = usuario;
        this.fecha = fecha;
    }

    // Getters y Setters

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public long getTiempoSegundos() {
        return tiempoSegundos;
    }

    public void setTiempoSegundos(long tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    public int getVidaRestante() {
        return vidaRestante;
    }

    public void setVidaRestante(int vidaRestante) {
        this.vidaRestante = vidaRestante;
    }

    public String getTamanioLaberinto() {
        return tamanioLaberinto;
    }

    public void setTamanioLaberinto(String tamanioLaberinto) {
        this.tamanioLaberinto = tamanioLaberinto;
    }

    public boolean isGanado() {
        return ganado;
    }

    public void setGanado(boolean ganado) {
        this.ganado = ganado;
    }

    public int getBombasRecolectadas() {
        return bombasRecolectadas;
    }

    public void setBombasRecolectadas(int bombasRecolectadas) {
        this.bombasRecolectadas = bombasRecolectadas;
    }

    public int getMurosDestruidos() {
        return murosDestruidos;
    }

    public void setMurosDestruidos(int murosDestruidos) {
        this.murosDestruidos = murosDestruidos;
    }

    public int getLlavesExplosionUsadas() {
        return llavesExplosionUsadas;
    }

    public void setLlavesExplosionUsadas(int llavesExplosionUsadas) {
        this.llavesExplosionUsadas = llavesExplosionUsadas;
    }

    /**
     * Formatea la fecha de la partida a un formato de cadena legible.
     *
     * @return La fecha y hora formateada como "yyyy-MM-dd HH:mm:ss".
     */
    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fecha.format(formatter);
    }

    /**
     * Proporciona una representacion en cadena concisa de las estadísticas
     * principales.
     *
     * @return Un string con el resumen de la fecha, el resultado, el tamaño del
     *         laberinto y los cristales recolectados.
     */
    @Override
    public String toString() {
        return String.format(
                "Estadísticas [%s] - %s - %s - %s",
                getFechaFormateada(),
                ganado ? "GANADO" : "PERDIDO",
                tamanioLaberinto,
                "Cristales: " + cristalesRecolectados + " | Bombas: " + bombasRecolectadas +
                        " | Muros: " + murosDestruidos);
    }
}
