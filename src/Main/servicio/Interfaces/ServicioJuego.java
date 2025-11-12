package Main.servicio.Interfaces;

import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Laberinto;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Transferencia.ResultadoJuego;

public interface ServicioJuego {
    Juego iniciarNuevoJuego(int filas, int columnas, String usuario);
    Juego cargarJuegoGuardado(String usuario);
    boolean moverJugador(Juego juego, Direccion direccion);
    boolean guardarJuego(Juego juego);
    ResultadoJuego terminarJuego(Juego juego);
    boolean puedeSalir(Juego juego);
    ResultadoJuego guardarEstadisticasParciales(Juego juego);
}
