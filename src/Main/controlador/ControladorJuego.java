package Main.controlador;

import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.servicio.Interfaces.ServicioJuego;

import java.util.List;

// Controlador que maneja toda la lógica del juego
public class ControladorJuego {
    private ServicioJuego servicioJuego;

    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
    }

    // Inicia un nuevo juego con las dimensiones especificadas
    public Juego iniciarNuevoJuego(int filas, int columnas, String emailUsuario) {
        return servicioJuego.iniciarNuevoJuego(filas, columnas, emailUsuario);
    }

    // Carga un juego guardado
    public Juego cargarJuegoGuardado(String emailUsuario) {
        return servicioJuego.cargarJuegoGuardado(emailUsuario);
    }

    // Verifica si existe un juego guardado
    public boolean existeJuegoGuardado(String emailUsuario) {
        return servicioJuego.existeJuegoGuardado(emailUsuario);
    }

    // Mueve al jugador en la dirección especificada
    public boolean moverJugador(Juego juego, Direccion direccion) {
        return servicioJuego.moverJugador(juego, direccion);
    }

    // Guarda el estado actual del juego
    public boolean guardarJuego(Juego juego) {
        return servicioJuego.guardarJuego(juego);
    }

    // Guarda estadísticas parciales del juego
    public void guardarEstadisticasParciales(Juego juego) {
        servicioJuego.guardarEstadisticasParciales(juego);
    }

    // Termina el juego y retorna el resultado
    public ResultadoJuego terminarJuego(Juego juego) {
        return servicioJuego.terminarJuego(juego);
    }

    // Obtiene el estado actual del juego
    public EstadoJuego obtenerEstadoJuego(Juego juego) {
        return juego.getEstado();
    }

    // Obtiene todas las estadísticas de un usuario
    public List<EstadisticasJuego> obtenerEstadisticas(String emailUsuario) {
        return servicioJuego.obtenerEstadisticas(emailUsuario);
    }

    // Valida las dimensiones del laberinto
    public boolean validarDimensiones(int filas, int columnas) {
        return filas >= 5 && columnas >= 5 && filas <= 20 && columnas <= 20;
    }
}
