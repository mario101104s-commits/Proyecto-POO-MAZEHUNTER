package Main.servicio.Interfaces;
import Main.modelo.Dominio.Laberinto;
/**
 * Define el contrato para la generación de la estructura de un laberinto.
 * <p>
 * Las implementaciones de esta interfaz deben ser capaces de crear una nueva
 * instancia de {@code Laberinto} con sus celdas inicializadas (Muros, Caminos
 * y elementos especiales).
 * </p>
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */

public interface GeneradorLaberinto {
    /**
     * Genera un laberinto de las dimensiones especificadas utilizando un proceso aleatorio
     * (usualmente basado en la hora actual del sistema).
     *
     * @param filas El número de filas deseado para el laberinto.
     * @param columnas El número de columnas deseado para el laberinto.
     * @return El objeto {@code Laberinto} recién generado.
     */
    Laberinto generar(int filas, int columnas);
    /**
     * Genera un laberinto de las dimensiones especificadas utilizando una semilla fija.
     * <p>
     * Al utilizar una semilla, la generación del laberinto es determinista, lo que permite
     * recrear el mismo laberinto en diferentes ejecuciones.
     * </p>
     * @param filas El número de filas.
     * @param columnas El número de columnas.
     * @param semilla La semilla numérica para inicializar el generador de números aleatorios.
     * @return El objeto {@code Laberinto} generado.
     */
    Laberinto generarConSemilla(int filas, int columnas, long semilla);
}