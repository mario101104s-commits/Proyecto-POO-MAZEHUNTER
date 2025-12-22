package Main.modelo.Transferencia;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula el resultado final y las
 * métricas
 * de rendimiento de una partida de Maze Hunter.
 * <p>
 * Se utiliza para comunicar los datos resumidos de la partida a la capa de
 * presentación
 * y para crear objetos de tipo {@code EstadisticasJuego} para la persistencia.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class ResultadoJuego {
    /**
     * El tiempo total transcurrido en la partida, medido en segundos.
     */
    private long tiempoSegundos;
    /**
     * La cantidad de ítems de cristal recolectados por el jugador.
     */
    private int cristalesRecolectados;
    /**
     * El número de veces que el jugador activó una trampa.
     */
    private int trampasActivadas;
    /**
     * La cantidad de vida que le restaba al jugador al finalizar la partida.
     */
    private int vidaRestante;
    /**
     * La dimensión del laberinto en el que se jugó la partida ).
     */
    private String tamanioLaberinto;
    /**
     * Indica si la partida terminó en victoria ({@code true}) o derrota
     * ({@code false}).
     */
    private boolean ganado;

    // Nuevas estadísticas
    private int bombasRecolectadas;
    private int murosDestruidos;
    private int fosforosUsados;

    // Getters y Setters

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

    public int getFosforosUsados() {
        return fosforosUsados;
    }

    public void setFosforosUsados(int fosforosUsados) {
        this.fosforosUsados = fosforosUsados;
    }

    /**
     * Proporciona una representación legible y formateada del resultado del juego.
     *
     * @return Una cadena de texto que detalla las métricas clave de la partida.
     */
    @Override
    public String toString() {
        return String.format(
                "Resultado del Juego:\n" +
                        "⏱️  Tiempo: %d segundos\n" +
                        "💎 Cristales: %d\n" +
                        "💀 Trampas: %d\n" +
                        "❤️  Vida: %d%%\n" +
                        "📏 Tamaño: %s\n" +
                        "💣 Bombas: %d\n" +
                        "💥 Muros destruidos: %d\n" +
                        "🔑 Fósforos usados: %d\n" +
                        "🏆 Estado: %s",
                tiempoSegundos, cristalesRecolectados, trampasActivadas,
                vidaRestante, tamanioLaberinto, bombasRecolectadas, murosDestruidos, fosforosUsados,
                ganado ? "GANADO" : "PERDIDO");
    }

    ;
}
