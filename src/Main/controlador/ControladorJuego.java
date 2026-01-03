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

/**
 * Controlador que orquesta la lógica de ejecución del juego y la generación de laberintos.
 * <p>
 * Esta clase actúa como mediador entre la interfaz de usuario y la lógica de negocio,
 * integrando el patrón de diseño Strategy para permitir diferentes algoritmos de
 * generación de laberintos según la dificultad seleccionada.
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class ControladorJuego {
    /**
     *  Servicio para la gestión de las reglas y estado del juego.
     */
    private ServicioJuego servicioJuego;

    /**
     * Contexto de configuración que gestiona la estrategia de generación actual.
     */
    private ConfiguracionJuego configuracionJuego;

    /**
     * Instancia de la partida que se está ejecutando actualmente.
     */
    private Juego juegoActual;

    /**
     * Constructor del controlador.
     *
     * @param servicioJuego Implementación del servicio de juego a utilizar.
     */
    public ControladorJuego(ServicioJuego servicioJuego) {
        this.servicioJuego = servicioJuego;
        this.configuracionJuego = new ConfiguracionJuego();
    }

    // ===== MÉTODOS PARA PATRÓN STRATEGY =====

    /**
     * Define la estrategia de generación de laberintos (Fácil, Media, Difícil).
     *
     * @param tipo Cadena que identifica el nivel de dificultad.
     */
    public void setEstrategiaGeneracion(String tipo) {
        configuracionJuego.setEstrategiaGeneracion(tipo);
    }

    /**
     * Obtiene el texto descriptivo de la estrategia de generación activa.
     *
     * @return Descripción de la dificultad actual.
     */
    public String getDescripcionEstrategia() {
        return configuracionJuego.getDescripcionActual();
    }

    /**
     * Retorna el tipo de estrategia que se encuentra configurado actualmente.
     *
     * @return Nombre del tipo de estrategia.
     */
    public String getTipoEstrategiaActual() {
        return configuracionJuego.getTipoActual();
    }

    /**
     * Proporciona la lista de todas las estrategias de generación disponibles en el sistema.
     *
     * @return Arreglo de Strings con las dificultades disponibles.
     */
    public String[] getEstrategiasDisponibles() {
        return new String[] {
                GeneradorLaberintoFactory.FACIL,
                GeneradorLaberintoFactory.MEDIA,
                GeneradorLaberintoFactory.DIFICIL
        };
    }

    /**
     * Consulta el rango de filas permitido para una dificultad específica.
     *
     * @param dificultad Nivel de dificultad a consultar.
     * @return Cadena que representa el rango de filas.
     */
    public String getRangoFilas(String dificultad) {
        return GeneradorLaberintoFactory.getRangoFilas(dificultad);
    }

    /**
     * Consulta el rango de columnas permitido para una dificultad específica.
     *
     * @param dificultad Nivel de dificultad a consultar.
     * @return Cadena que representa el rango de columnas.
     */
    public String getRangoColumnas(String dificultad) {
        return GeneradorLaberintoFactory.getRangoColumnas(dificultad);
    }

    /**
     * Genera de forma aleatoria un número de filas dentro del rango de la dificultad dada.
     *
     * @param dificultad Nivel de dificultad para determinar el límite.
     * @return Número de filas generado.
     */
    public int generarFilasAleatorias(String dificultad) {
        return GeneradorLaberintoFactory.generarFilasAleatorias(dificultad);
    }

    /**
     * Genera de forma aleatoria un número de columnas dentro del rango de la dificultad dada.
     *
     * @param dificultad Nivel de dificultad para determinar el límite.
     * @return Número de columnas generado.
     */
    public int generarColumnasAleatorias(String dificultad) {
        return GeneradorLaberintoFactory.generarColumnasAleatorias(dificultad);
    }

    // ===== MÉTODOS DE JUEGO =====

    /**
     * Inicializa una nueva partida con dimensiones personalizadas y estrategia actual.
     *
     * @param filas Cantidad de filas para el nuevo laberinto.
     * @param columnas Cantidad de columnas para el nuevo laberinto.
     * @param emailUsuario Correo del usuario que inicia la partida.
     * @return Objeto Juego con la nueva partida creada.
     */
    public Juego iniciarNuevoJuego(int filas, int columnas, String emailUsuario) {
        this.juegoActual = servicioJuego.iniciarNuevoJuego(filas, columnas, emailUsuario, configuracionJuego);
        return this.juegoActual;
    }

    /**
     * Recupera una partida previamente guardada desde el sistema de persistencia.
     *
     * @param emailUsuario Correo del usuario dueño de la partida.
     * @return Objeto Juego restaurado o {@code null} si no existe.
     */
    public Juego cargarJuegoGuardado(String emailUsuario) {
        this.juegoActual = servicioJuego.cargarJuegoGuardado(emailUsuario);
        return this.juegoActual;
    }

    /**
     * Retorna la instancia de la partida que se está controlando actualmente.
     *
     * @return Instancia actual de Juego.
     */
    public Juego getJuego() {
        return this.juegoActual;
    }

    /**
     * Comprueba si el usuario tiene una partida pendiente de reanudar.
     *
     * @param emailUsuario Correo del usuario a verificar.
     * @return {@code true} si existe un archivo de partida guardada.
     */
    public boolean existeJuegoGuardado(String emailUsuario) {
        return servicioJuego.existeJuegoGuardado(emailUsuario);
    }

    /**
     * Ejecuta la lógica de movimiento del jugador en el laberinto.
     *
     * @param juego Instancia del juego donde se realiza el movimiento.
     * @param direccion Hacia dónde se desea mover el jugador.
     * @return {@code true} si el movimiento se realizó con éxito.
     */
    public boolean moverJugador(Juego juego, Direccion direccion) {
        return servicioJuego.moverJugador(juego, direccion);
    }

    /**
     * Intenta detonar una bomba para limpiar obstáculos en el camino.
     *
     * @param juego Instancia del juego actual.
     * @return {@code true} si se logró destruir algún elemento.
     */
    public boolean activarExplosion(Juego juego) {
        return servicioJuego.activarExplosion(juego);
    }

    /**
     * Persiste el estado actual de la partida en el almacenamiento.
     *
     * @param juego Instancia del juego a guardar.
     * @return {@code true} si la operación fue exitosa.
     */
    public boolean guardarJuego(Juego juego) {
        return servicioJuego.guardarJuego(juego);
    }

    /**
     * Almacena estadísticas de progreso sin finalizar la partida.
     *
     * @param juego Instancia de la partida actual.
     */
    public void guardarEstadisticasParciales(Juego juego) {
        servicioJuego.guardarEstadisticasParciales(juego);
    }

    /**
     * Concluye la partida actual y procesa los resultados finales para los anales.
     *
     * @param juego Partida que llega a su fin.
     * @return Resumen detallado del resultado del juego.
     */
    public ResultadoJuego terminarJuego(Juego juego) {
        return servicioJuego.terminarJuego(juego);
    }

    /**
     * Consulta el estado en el que se encuentra la partida proporcionada.
     *
     * @param juego Instancia a consultar.
     * @return Enum representando el estado (EN_CURSO, GANADO, PERDIDO, PAUSADO).
     */
    public EstadoJuego obtenerEstadoJuego(Juego juego) {
        return juego.getEstado();
    }

    /**
     * Recupera el historial de partidas pasadas de un usuario específico.
     *
     * @param emailUsuario Correo del usuario.
     * @return Lista con las estadísticas de sus juegos anteriores.
     */
    public List<EstadisticasJuego> obtenerEstadisticas(String emailUsuario) {
        return servicioJuego.obtenerEstadisticas(emailUsuario);
    }

    /**
     * Valida si las dimensiones propuestas para un laberinto son aceptables.
     * * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @return {@code true} si están en el rango de 5 a 20.
     */
    public boolean validarDimensiones(int filas, int columnas) {
        return filas >= 5 && columnas >= 5 && filas <= 20 && columnas <= 20;
    }
}
