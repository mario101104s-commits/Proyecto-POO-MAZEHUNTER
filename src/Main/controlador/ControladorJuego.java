package Main.controlador;

import Main.estrategia.contexto.ConfiguracionJuego;
import Main.estrategia.factory.GeneradorLaberintoFactory;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.servicio.Interfaces.ServicioJuego;

import java.util.List;

// Controlador que maneja toda la lógica del juego
// Integrado con patrón Strategy para generación de laberintos
public class ControladorJuego {
    private ServicioJuego servicioJuego;
    private ConfiguracionJuego configuracionJuego;

    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
        this.configuracionJuego = new ConfiguracionJuego();
    }

    // ===== MÉTODOS PARA PATRÓN STRATEGY =====

    // Cambia la estrategia de generación de laberintos
    public void setEstrategiaGeneracion(String tipo) {
        configuracionJuego.setEstrategiaGeneracion(tipo);
    }

    // Obtiene la descripción de la estrategia actual
    public String getDescripcionEstrategia() {
        return configuracionJuego.getDescripcionActual();
    }

    // Obtiene el tipo actual de estrategia
    public String getTipoEstrategiaActual() {
        return configuracionJuego.getTipoActual();
    }

    // Obtiene todas las estrategias disponibles
    public String[] getEstrategiasDisponibles() {
        return new String[] {
                GeneradorLaberintoFactory.FACIL,
                GeneradorLaberintoFactory.MEDIA,
                GeneradorLaberintoFactory.DIFICIL
        };
    }

    // Obtiene los rangos de filas para una dificultad
    public String getRangoFilas(String dificultad) {
        return GeneradorLaberintoFactory.getRangoFilas(dificultad);
    }

    // Obtiene los rangos de columnas para una dificultad
    public String getRangoColumnas(String dificultad) {
        return GeneradorLaberintoFactory.getRangoColumnas(dificultad);
    }

    // ===== MÉTODOS DE JUEGO =====

    // Inicia un nuevo juego con las dimensiones especificadas usando la estrategia
    // actual
    public Juego iniciarNuevoJuego(int filas, int columnas, String emailUsuario) {
        return servicioJuego.iniciarNuevoJuego(filas, columnas, emailUsuario, configuracionJuego);
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
