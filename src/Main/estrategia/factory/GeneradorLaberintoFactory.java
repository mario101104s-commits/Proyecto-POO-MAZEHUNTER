package Main.estrategia.factory;

import Main.estrategia.generacion.GeneradorLaberintoDificultad;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.Random;

/**
 * Fábrica encargada de la creación y configuración de las estrategias de generación de laberintos.
 * <p>
 * Implementa el patrón <b>Factory Method</b> para instanciar generadores según el nivel de dificultad.
 * Además, centraliza los parámetros de configuración como rangos de dimensiones, cantidad de trampas
 * y objetos de energía para cada nivel (FÁCIL, MEDIA, DIFÍCIL).
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class GeneradorLaberintoFactory {

    /**
     * Generador de números aleatorios para determinar dimensiones dinámicas.
     */
    private static Random random = new Random();

    /**
     *  Identificador para el nivel de dificultad fácil.
     */
    public static final String FACIL = "FACIL";

    /**
     *  Identificador para el nivel de dificultad media.
     */
    public static final String MEDIA = "MEDIA";

    /**
     * Identificador para el nivel de dificultad difícil.
     */
    public static final String DIFICIL = "DIFICIL";

    /**
     * Crea una instancia de un generador de laberintos basada en la dificultad solicitada.
     *
     * @param dificultad Cadena de texto con el nombre de la dificultad.
     * @return Una implementación de {@link GeneradorLaberinto}. Si la dificultad es nula,
     * retorna el generador de dificultad MEDIA por defecto.
     */
    public static GeneradorLaberinto crear(String dificultad) {
        if (dificultad == null) {
            return new GeneradorLaberintoDificultad(MEDIA); // Default
        }

        return new GeneradorLaberintoDificultad(dificultad.toUpperCase());
    }

    /**
     * Proporciona una descripción detallada de las características de una dificultad,
     * incluyendo rangos de tamaño y cantidad de elementos.
     *
     * @param dificultad Nivel de dificultad a consultar.
     * @return Una cadena formateada con la información del nivel.
     */
    public static String obtenerDescripcion(String dificultad) {
        if (dificultad == null)
            dificultad = MEDIA;

        switch (dificultad.toUpperCase()) {
            case FACIL:
                return "🟢 FÁCIL - Filas: 5-15, Columnas: 10-25 | Trampas: 2-3, Energías: 2-3";
            case MEDIA:
                return "🟡 MEDIA - Filas: 16-25, Columnas: 26-35 | Trampas: 4-5, Energías: 4-5";
            case DIFICIL:
                return "🔴 DIFÍCIL - Filas: 26-45, Columnas: 36-65 | Trampas: 6-18, Energías: 6-18";
            default:
                return "🟡 MEDIA - Filas: 16-25, Columnas: 26-35 | Trampas: 4-5, Energías: 4-5";
        }
    }

    /**
     * Obtiene el rango de filas permitido para una dificultad específica.
     *
     * @param dificultad Nivel de dificultad.
     * @return Cadena con el formato "mín-máx".
     */
    public static String getRangoFilas(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case FACIL:
                return "5-15";
            case MEDIA:
                return "16-25";
            case DIFICIL:
                return "26-45";
            default:
                return "16-25";
        }
    }

    /**
     * Obtiene el rango de columnas permitido para una dificultad específica.
     *
     * @param dificultad Nivel de dificultad.
     * @return Cadena con el formato "mín-máx".
     */
    public static String getRangoColumnas(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case FACIL:
                return "10-25";
            case MEDIA:
                return "26-35";
            case DIFICIL:
                return "36-65";
            default:
                return "26-35";
        }
    }

    /**
     * Calcula un número de filas aleatorio respetando los límites de la dificultad.
     *
     * @param dificultad Nivel que define el rango de filas.
     * @return Valor entero aleatorio entre los límites establecidos.
     */
    public static int generarFilasAleatorias(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case FACIL:
                return 5 + random.nextInt(11); // 5-15
            case MEDIA:
                return 16 + random.nextInt(10); // 16-25
            case DIFICIL:
                return 26 + random.nextInt(20); // 26-45
            default:
                return 16 + random.nextInt(10); // 16-25
        }
    }

    /**
     * Calcula un número de columnas aleatorio respetando los límites de la dificultad.
     *
     * @param dificultad Nivel que define el rango de columnas.
     * @return Valor entero aleatorio entre los límites establecidos.
     */
    public static int generarColumnasAleatorias(String dificultad) {
        switch (dificultad.toUpperCase()) {
            case FACIL:
                return 10 + random.nextInt(16); // 10-25
            case MEDIA:
                return 26 + random.nextInt(10); // 26-35
            case DIFICIL:
                return 36 + random.nextInt(30); // 36-65
            default:
                return 26 + random.nextInt(10); // 26-35
        }
    }
}