package Main.modelo.Dominio;

import Main.modelo.Constantes.EstadoJuego;

import java.time.LocalDateTime;

public class Juego {
    private Laberinto laberinto;
    private Jugador jugador;
    private String usuario;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private EstadoJuego estado;
    private int trampasActivadas;

    public Juego(Laberinto laberinto, Jugador jugador, String usuario, LocalDateTime inicio) {
        this.laberinto = laberinto;
        this.jugador = jugador;
        this.usuario = usuario;
        this.inicio = inicio;
        this.estado = EstadoJuego.EN_CURSO;
        this.trampasActivadas = 0;
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

    public void incrementarTrampasActivadas() {

    }
}
