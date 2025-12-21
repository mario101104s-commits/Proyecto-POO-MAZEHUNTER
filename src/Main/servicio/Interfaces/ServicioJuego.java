package Main.servicio.Interfaces;

import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Transferencia.ResultadoJuego;

import java.util.List;

// Define el contrato de la capa de servicio para gestión del juego
public interface ServicioJuego {

    Juego iniciarNuevoJuego(int filas, int columnas, String usuario);

    Juego cargarJuegoGuardado(String usuario);

    boolean existeJuegoGuardado(String usuario);

    boolean moverJugador(Juego juego, Direccion direccion);

    boolean guardarJuego(Juego juego);

    ResultadoJuego terminarJuego(Juego juego);

    boolean puedeSalir(Juego juego);

    ResultadoJuego guardarEstadisticasParciales(Juego juego);

    List<EstadisticasJuego> obtenerEstadisticas(String emailUsuario);
}
