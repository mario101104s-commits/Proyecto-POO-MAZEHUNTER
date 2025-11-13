package Main.servicio.Interfaces;

import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Usuario;

import java.util.List;

public interface Persistencia {

    // Usuarios
    boolean guardarUsuario(Usuario usuario);
    Usuario cargarUsuario(String email);
    List<Usuario> cargarTodosUsuarios();
    boolean existeUsuario(String email);

    void cargarUsuarios();
    void cargarEstadisticas();

    // Juegos
    boolean guardarJuego(Juego juego);
    Juego cargarJuego(String usuario);
    boolean existeJuegoGuardado(String usuario);

    // Estadísticas
    boolean guardarEstadisticas(EstadisticasJuego estadisticas);
    List<EstadisticasJuego> cargarEstadisticas(String usuario);
    List<EstadisticasJuego> cargarTodasEstadisticas();
}