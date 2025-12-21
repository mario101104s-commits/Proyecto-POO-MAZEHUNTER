package Main.estrategia.factory;

import Main.estrategia.generacion.GeneradorLaberintoDificultad;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.Random;

// Factory para crear estrategias de generación de laberintos
// Sistema de dificultades definitivo con generación aleatoria de dimensiones
public class GeneradorLaberintoFactory {

    private static Random random = new Random();

    // Tipos de dificultad disponibles
    public static final String FACIL = "FACIL";
    public static final String MEDIA = "MEDIA";
    public static final String DIFICIL = "DIFICIL";

    // Crea una estrategia de generación según la dificultad especificada
    public static GeneradorLaberinto crear(String dificultad) {
        if (dificultad == null) {
            return new GeneradorLaberintoDificultad(MEDIA); // Default
        }

        return new GeneradorLaberintoDificultad(dificultad.toUpperCase());
    }

    // Obtiene la descripción y rangos de una dificultad
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

    // Obtiene los rangos válidos de filas para una dificultad
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

    // Obtiene los rangos válidos de columnas para una dificultad
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

    // Genera un número aleatorio de filas según la dificultad
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

    // Genera un número aleatorio de columnas según la dificultad
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
