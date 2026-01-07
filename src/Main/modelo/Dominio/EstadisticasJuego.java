package Main.modelo.Dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Almacena los datos de rendimiento clave y el resultado final de una partida
 * de Maze Hunter.
 * <p>
 * Esta clase es utilizada por el sistema de persistencia para guardar un
 * registro histórico de las partidas jugadas por cada usuario, permitiendo
 * el seguimiento de su progreso y logros.
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.0
 * @since 11/11/2025
 */
public class EstadisticasJuego {
    /** El correo electrónico del usuario asociado a estas estadísticas. */
    private String usuario;
    /** La fecha y hora exactas en que se guardaron estas estadísticas. */
    private LocalDateTime fecha;
    /** El tiempo total que duró la partida, medido en segundos. */
    private long tiempoSegundos;
    /** Número de ítems de cristal recolectados durante la partida. */
    private int cristalesRecolectados;
    /** El número de veces que el jugador activó una trampa. */
    private int trampasActivadas;
    /** La cantidad de vida que le restaba al jugador al finalizar la partida. */
    private int vidaRestante;
    /** La dimensión o nombre del tamaño del laberinto (ej. "15x20"). */
    private String tamanioLaberinto;
    /** Indica si la partida terminó en victoria (true) o derrota (false). */
    private boolean ganado;

    /** Cantidad de ítems de bomba recogidos. */
    private int bombasRecolectadas;
    /** Cantidad de muros eliminados usando bombas. */
    private int murosDestruidos;
    /** Cantidad de fósforos consumidos para iluminar el área. */
    private int fosforosUsados;
    /** Indica si la partida se jugó con niebla de guerra. */
    private boolean nieblaDeGuerra;

    /**
     * Crea una nueva instancia de EstadisticasJuego, inicializando el usuario y la
     * fecha.
     *
     * @param usuario El email del usuario que jugó la partida.
     * @param fecha   La marca de tiempo del registro de las estadísticas.
     */
    public EstadisticasJuego(String usuario, LocalDateTime fecha) {
        this.usuario = usuario;
        this.fecha = fecha;
    }

    // --- Getters y Setters ---

    /** @return El identificador del usuario. */
    public String getUsuario() {
        return usuario;
    }

    /** @param usuario El identificador del usuario a establecer. */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /** @return La fecha de la partida. */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /** @param fecha La fecha de la partida a establecer. */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /** @return Duración en segundos. */
    public long getTiempoSegundos() {
        return tiempoSegundos;
    }

    /** @param tiempoSegundos Segundos transcurridos en la partida. */
    public void setTiempoSegundos(long tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    /** @return Total de cristales obtenidos. */
    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    /** @param cristalesRecolectados Cantidad de cristales a registrar. */
    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    /** @return Total de trampas accionadas. */
    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    /** @param trampasActivadas Número de trampas activadas. */
    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    /** @return Puntos de vida al finalizar. */
    public int getVidaRestante() {
        return vidaRestante;
    }

    /** @param vidaRestante Puntos de vida restantes. */
    public void setVidaRestante(int vidaRestante) {
        this.vidaRestante = vidaRestante;
    }

    /** @return Descripción del tamaño del mapa. */
    public String getTamanioLaberinto() {
        return tamanioLaberinto;
    }

    /** @param tamanioLaberinto Nombre o dimensiones del laberinto. */
    public void setTamanioLaberinto(String tamanioLaberinto) {
        this.tamanioLaberinto = tamanioLaberinto;
    }

    /** @return true si el jugador llegó a la salida, false de lo contrario. */
    public boolean isGanado() {
        return ganado;
    }

    /** @param ganado Resultado final de la partida. */
    public void setGanado(boolean ganado) {
        this.ganado = ganado;
    }

    /** @return Cantidad de bombas recogidas. */
    public int getBombasRecolectadas() {
        return bombasRecolectadas;
    }

    /** @param bombasRecolectadas Total de bombas en la partida. */
    public void setBombasRecolectadas(int bombasRecolectadas) {
        this.bombasRecolectadas = bombasRecolectadas;
    }

    /** @return Número de muros derribados. */
    public int getMurosDestruidos() {
        return murosDestruidos;
    }

    /** @param murosDestruidos Cantidad de muros destruidos por bombas. */
    public void setMurosDestruidos(int murosDestruidos) {
        this.murosDestruidos = murosDestruidos;
    }

    /** @return Cantidad de fósforos usados. */
    public int getFosforosUsados() {
        return fosforosUsados;
    }

    /** @param fosforosUsados Cantidad de fósforos usados. */
    public void setFosforosUsados(int fosforosUsados) {
        this.fosforosUsados = fosforosUsados;
    }

    /** @return Si hubo niebla de guerra. */
    public boolean isNieblaDeGuerra() {
        return nieblaDeGuerra;
    }

    /** @param nieblaDeGuerra Estado de la niebla. */
    public void setNieblaDeGuerra(boolean nieblaDeGuerra) {
        this.nieblaDeGuerra = nieblaDeGuerra;
    }

    // --- Métodos de Utilidad ---

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
     * Proporciona una representación en cadena concisa de las estadísticas
     * principales.
     *
     * @return Un string con el resumen de la fecha, el resultado y el rendimiento.
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