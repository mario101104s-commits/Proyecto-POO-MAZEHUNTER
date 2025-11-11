package Main.modelo.Transferencia;

public class ResultadoJuego {
    private long tiempoSegundos;
    private int cristalesRecolectados;
    private int trampasActivadas;
    private int vidaRestante;
    private String tamanioLaberinto;
    private boolean ganado;

    //Getters y Setters

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

    @Override
    public String toString() {
        return String.format(
                "Resultado del Juego:\n" +
                        "⏱️  Tiempo: %d segundos\n" +
                        "💎 Cristales: %d\n" +
                        "💀 Trampas: %d\n" +
                        "❤️  Vida: %d%%\n" +
                        "📏 Tamaño: %s\n" +
                        "🏆 Estado: %s",
                tiempoSegundos, cristalesRecolectados, trampasActivadas,
                vidaRestante, tamanioLaberinto, ganado ? "GANADO" : "PERDIDO"
        );
    }

    ;
}


