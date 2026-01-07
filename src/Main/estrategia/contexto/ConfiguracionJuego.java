package Main.estrategia.contexto;

import Main.estrategia.factory.GeneradorLaberintoFactory;
import Main.modelo.Dominio.Laberinto;
import Main.servicio.Interfaces.GeneradorLaberinto;

/**
 * Clase que actúa como el Contexto dentro del patrón de diseño Strategy.
 * <p>
 * Su función principal es gestionar la estrategia de generación de laberintos
 * activa,
 * permitiendo cambiar el algoritmo de creación (Fácil, Medio o Difícil) de
 * forma
 * dinámica en tiempo de ejecución.
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.0
 * @since 22/12/25
 */
public class ConfiguracionJuego {
    /**
     * La interfaz de la estrategia que se está utilizando actualmente.
     */
    private GeneradorLaberinto estrategiaGeneracion;

    /**
     * Nombre identificador de la dificultad actual.
     */
    private String tipoActual;

    /**
     * Indica si la niebla de guerra está activa.
     */
    private boolean nieblaDeGuerra = true;

    /**
     * Constructor por defecto.
     * Inicializa el juego con la estrategia MEDIA como configuración base.
     */
    public ConfiguracionJuego() {
        // Estrategia por defecto: MEDIA
        this.tipoActual = GeneradorLaberintoFactory.MEDIA;
        this.estrategiaGeneracion = GeneradorLaberintoFactory.crear(tipoActual);
    }

    public boolean isNieblaDeGuerra() {
        return nieblaDeGuerra;
    }

    public void setNieblaDeGuerra(boolean nieblaDeGuerra) {
        this.nieblaDeGuerra = nieblaDeGuerra;
    }

    /**
     * Modifica la estrategia de generación de laberintos.
     * <p>
     * Utiliza la {@code GeneradorLaberintoFactory} para instanciar la nueva
     * implementación basada en el tipo proporcionado.
     * </p>
     *
     * @param tipo El identificador de la nueva dificultad (Fácil, Media o Difícil).
     */
    public void setEstrategiaGeneracion(String tipo) {
        this.tipoActual = tipo;
        this.estrategiaGeneracion = GeneradorLaberintoFactory.crear(tipo);
    }

    /**
     * Retorna el identificador del nivel de dificultad configurado.
     *
     * @return El nombre de la estrategia actual.
     */
    public String getTipoActual() {
        return tipoActual;
    }

    /**
     * Recupera una descripción detallada sobre el comportamiento de la estrategia
     * actual.
     *
     * @return Texto descriptivo obtenido desde la fábrica de generadores.
     */
    public String getDescripcionActual() {
        return GeneradorLaberintoFactory.obtenerDescripcion(tipoActual);
    }

    /**
     * Ejecuta el algoritmo de la estrategia configurada para crear un nuevo
     * laberinto.
     *
     * @param filas    Número de filas para el laberinto.
     * @param columnas Número de columnas para el laberinto.
     * @return Un objeto {@link Laberinto} generado aleatoriamente.
     */
    public Laberinto generarLaberinto(int filas, int columnas) {
        return estrategiaGeneracion.generar(filas, columnas);
    }

    /**
     * Ejecuta la generación de un laberinto utilizando una semilla específica.
     * <p>
     * Esto permite recrear laberintos idénticos si se utiliza la misma semilla.
     * </p>
     *
     * @param filas    Número de filas deseado.
     * @param columnas Número de columnas deseado.
     * @param semilla  Valor numérico para controlar la aleatoriedad.
     * @return Un objeto {@link Laberinto} consistente con la semilla dada.
     */
    public Laberinto generarLaberintoConSemilla(int filas, int columnas, long semilla) {
        return estrategiaGeneracion.generarConSemilla(filas, columnas, semilla);
    }
}