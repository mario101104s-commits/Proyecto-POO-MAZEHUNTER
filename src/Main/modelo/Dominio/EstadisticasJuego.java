package Main.modelo.Dominio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EstadisticasJuego {

    private String usuario;
    private LocalDateTime fecha;
    private long tiempoSegundos;
    private int cristalesRecolectados;
    private int trampasActivadas;
    private int vidaRestante;
    private String tamanioLaberinto;
    private boolean ganado;

    public EstadisticasJuego(String usuario, LocalDateTime fecha) {
        this.usuario = usuario;
        this.fecha = fecha;
    }

    //Getters y Setters

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

    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return fecha.format(formatter);
    }

    @Override
    public String toString() {
        return String.format(
                "Estadísticas [%s] - %s - %s - %s",
                getFechaFormateada(),
                ganado ? "GANADO" : "PERDIDO",
                tamanioLaberinto,
                "Cristales: " + cristalesRecolectados
        );
    }
}
