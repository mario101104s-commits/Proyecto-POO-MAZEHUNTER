package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

/**
 * Implementación de la estrategia de generación de laberintos basada en algoritmos aleatorios.
 * <p>
 * Esta clase genera estructuras impredecibles utilizando una distribución probabilística
 * (60% caminos, 40% muros). Incluye mecanismos para garantizar la resolubilidad del laberinto
 * mediante la creación forzada de un camino entre los extremos y la distribución aleatoria
 * de objetos especiales.
 * </p>
 *
 * @author Jose Berroteran
 * @version 1.0
 */
public class GeneradorLaberintoAleatorio implements GeneradorLaberinto {

    /** Generador de números aleatorios para la toma de decisiones algorítmicas. */
    private Random random;

    /**
     * Constructor por defecto. Inicializa el generador de aleatoriedad.
     */
    public GeneradorLaberintoAleatorio() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto utilizando la hora actual del sistema como semilla.
     *
     * @param filas Cantidad de filas del laberinto.
     * @param columnas Cantidad de columnas del laberinto.
     * @return Un objeto {@link Laberinto} generado aleatoriamente.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto con una semilla específica para permitir la recreación de mapas.
     * <p>
     * El proceso de generación sigue estas etapas:
     * 1. Población inicial basada en probabilidades.
     * 2. Sellado de bordes perimetrales con muros.
     * 3. Garantía de conectividad entre entrada y salida.
     * 4. Colocación de elementos especiales (cristales, trampas, llaves, energía).
     * </p>
     *
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @param semilla Valor de inicialización para el generador aleatorio.
     * @return Un objeto {@link Laberinto} completamente configurado.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        Celda[][] celdas = new Celda[filas][columnas];

        // Generar patrón aleatorio de muros y caminos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // 60% caminos, 40% muros
                if (random.nextDouble() < 0.6) {
                    celdas[i][j] = new Celda(TipoCelda.CAMINO, i, j);
                } else {
                    celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
                }
            }
        }

        // Asegurar bordes (Muros perimetrales)
        for (int i = 0; i < filas; i++) {
            celdas[i][0].setTipo(TipoCelda.MURO);
            celdas[i][columnas - 1].setTipo(TipoCelda.MURO);
        }
        for (int j = 0; j < columnas; j++) {
            celdas[0][j].setTipo(TipoCelda.MURO);
            celdas[filas - 1][j].setTipo(TipoCelda.MURO);
        }

        // Asegurar que hay camino entre entrada y salida
        asegurarConectividad(celdas, filas, columnas);

        colocarElementosEspeciales(celdas, filas, columnas);

        return new Laberinto(celdas, filas, columnas);
    }

    /**
     * Crea un camino garantizado desde la esquina superior izquierda a la inferior derecha.
     * <p>
     * Este método asegura que, independientemente de la aleatoriedad inicial, el jugador
     * siempre tenga una ruta física para completar el nivel.
     * </p>
     *
     * @param celdas Matriz de celdas a modificar.
     * @param filas Filas totales.
     * @param columnas Columnas totales.
     */
    private void asegurarConectividad(Celda[][] celdas, int filas, int columnas) {
        int x = 1;
        int y = 1;

        while (x < filas - 2 || y < columnas - 2) {
            celdas[x][y].setTipo(TipoCelda.CAMINO);

            if (x < filas - 2 && (y >= columnas - 2 || random.nextBoolean())) {
                x++;
            } else if (y < columnas - 2) {
                y++;
            }
        }
        celdas[x][y].setTipo(TipoCelda.CAMINO);
    }

    /**
     * Identifica las celdas transitables y distribuye los objetos del juego.
     * <p>
     * Utiliza un sistema de barajado (shuffle) sobre la lista de posiciones de camino
     * para ubicar la entrada, la salida y los consumibles de forma dispersa.
     * </p>
     *
     * @param celdas Matriz del laberinto.
     * @param filas Dimensiones verticales.
     * @param columnas Dimensiones horizontales.
     */
    private void colocarElementosEspeciales(Celda[][] celdas, int filas, int columnas) {
        List<int[]> posicionesCaminos = new ArrayList<>();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.CAMINO) {
                    posicionesCaminos.add(new int[] { i, j });
                }
            }
        }

        if (posicionesCaminos.size() < 10)
            return;

        Collections.shuffle(posicionesCaminos, random);

        int[] entrada = posicionesCaminos.get(0);
        celdas[entrada[0]][entrada[1]].setTipo(TipoCelda.ENTRADA);

        int[] salida = posicionesCaminos.get(posicionesCaminos.size() - 1);
        celdas[salida[0]][salida[1]].setTipo(TipoCelda.SALIDA);

        // Cantidad variable de elementos
        int numCristales = Math.min(15, random.nextInt(posicionesCaminos.size() / 8) + 5);
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
            }
        }

        int numTrampas = Math.min(12, random.nextInt(posicionesCaminos.size() / 10) + 3);
        for (int i = 0; i < numTrampas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + numCristales + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.TRAMPA);
            }
        }

        int[] posLlave = encontrarPosicionValida(celdas, posicionesCaminos, numCristales + numTrampas + 1);
        if (posLlave != null) {
            celdas[posLlave[0]][posLlave[1]].setTipo(TipoCelda.LLAVE);
        }

        int numEnergia = Math.min(8, random.nextInt(posicionesCaminos.size() / 15) + 2);
        for (int i = 0; i < numEnergia; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos,
                    numCristales + numTrampas + 1 + i);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
            }
        }
    }

    /**
     * Busca una coordenada dentro de la lista de posiciones que aún sea de tipo CAMINO.
     *
     * @param celdas Matriz de celdas.
     * @param posiciones Lista de coordenadas disponibles.
     * @param inicio Índice de inicio para la búsqueda.
     * @return Arreglo [fila, columna] o {@code null} si no hay posiciones válidas.
     */
    private int[] encontrarPosicionValida(Celda[][] celdas, List<int[]> posiciones, int inicio) {
        for (int i = inicio; i < posiciones.size(); i++) {
            int[] pos = posiciones.get(i);
            if (celdas[pos[0]][pos[1]].getTipo() == TipoCelda.CAMINO) {
                return pos;
            }
        }
        return null;
    }
}