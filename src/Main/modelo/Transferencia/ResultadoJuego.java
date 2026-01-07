package Main.modelo.Transferencia;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula el resultado final y las
 * métricas de rendimiento de una partida de Maze Hunter.
 * <p>
 * Se utiliza para comunicar los datos resumidos de la partida a la capa de
 * presentación y para facilitar la creación de registros históricos en la
 * persistencia.
 * Al ser un DTO, no contiene lógica de negocio, solo contenedores de datos.
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.0
 * @since 11/11/2025
 */
public class ResultadoJuego {
    /** El tiempo total transcurrido en la partida, medido en segundos. */
    private long tiempoSegundos;
    /** La cantidad de ítems de cristal recolectados por el jugador. */
    private int cristalesRecolectados;
    /** El número de veces que el jugador activó una trampa. */
    private int trampasActivadas;
    /** La cantidad de vida que le restaba al jugador al finalizar la partida. */
    private int vidaRestante;
    /** La dimensión del laberinto en el que se jugó la partida. */
    private String tamanioLaberinto;
    /** Indica si la partida terminó en victoria (true) o derrota (false). */
    private boolean ganado;

    /** Cantidad de bombas obtenidas durante la exploración. */
    private int bombasRecolectadas;
    /** Cantidad de muros eliminados mediante el uso de bombas. */
    private int murosDestruidos;
    /** Cantidad de fósforos consumidos para iluminar el laberinto. */
    private int fosforosUsados;

    /** Referencia al objeto de estadísticas completo. */
    private Main.modelo.Dominio.EstadisticasJuego estadisticas;

    /** @return Segundos totales de duración. */
    public long getTiempoSegundos() {
        return tiempoSegundos;
    }

    /** @param tiempoSegundos Duración final de la partida. */
    public void setTiempoSegundos(long tiempoSegundos) {
        this.tiempoSegundos = tiempoSegundos;
    }

    /** @return Total de cristales obtenidos. */
    public int getCristalesRecolectados() {
        return cristalesRecolectados;
    }

    /** @param cristalesRecolectados Cantidad de cristales para el resumen. */
    public void setCristalesRecolectados(int cristalesRecolectados) {
        this.cristalesRecolectados = cristalesRecolectados;
    }

    /** @return Total de trampas accionadas. */
    public int getTrampasActivadas() {
        return trampasActivadas;
    }

    /** @param trampasActivadas Número de trampas para el resumen. */
    public void setTrampasActivadas(int trampasActivadas) {
        this.trampasActivadas = trampasActivadas;
    }

    /** @return Salud final del jugador. */
    public int getVidaRestante() {
        return vidaRestante;
    }

    /** @param vidaRestante Porcentaje de salud restante. */
    public void setVidaRestante(int vidaRestante) {
        this.vidaRestante = vidaRestante;
    }

    /** @return Nombre o dimensiones del laberinto. */
    public String getTamanioLaberinto() {
        return tamanioLaberinto;
    }

    /** @param tamanioLaberinto Descripción del mapa jugado. */
    public void setTamanioLaberinto(String tamanioLaberinto) {
        this.tamanioLaberinto = tamanioLaberinto;
    }

    /** @return true si fue victoria, false si fue derrota. */
    public boolean isGanado() {
        return ganado;
    }

    /** @param ganado Resultado final de la sesión. */
    public void setGanado(boolean ganado) {
        this.ganado = ganado;
    }

    /** @return Cantidad de bombas recogidas. */
    public int getBombasRecolectadas() {
        return bombasRecolectadas;
    }

    /** @param bombasRecolectadas Bombas encontradas en el laberinto. */
    public void setBombasRecolectadas(int bombasRecolectadas) {
        this.bombasRecolectadas = bombasRecolectadas;
    }

    /** @return Muros destruidos por el jugador. */
    public int getMurosDestruidos() {
        return murosDestruidos;
    }

    /** @param murosDestruidos Cantidad de muros rojos eliminados. */
    public void setMurosDestruidos(int murosDestruidos) {
        this.murosDestruidos = murosDestruidos;
    }

    /** @return Fósforos utilizados durante la partida. */
    public int getFosforosUsados() {
        return fosforosUsados;
    }

    /** @param fosforosUsados Cantidad de fósforos consumidos. */
    public void setFosforosUsados(int fosforosUsados) {
        this.fosforosUsados = fosforosUsados;
    }

    /**
     * @return El objeto de estadísticas completo asociado a este resultado.
     */
    public Main.modelo.Dominio.EstadisticasJuego getEstadisticas() {
        return estadisticas;
    }

    /**
     * @param estadisticas El objeto de estadísticas a establecer.
     */
    public void setEstadisticas(Main.modelo.Dominio.EstadisticasJuego estadisticas) {
        this.estadisticas = estadisticas;
    }

    /**
     * Proporciona una representación legible y formateada del resultado del juego.
     * <p>
     * Utiliza iconos decorativos para mejorar la legibilidad en interfaces de
     * consola.
     * </p>
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
}