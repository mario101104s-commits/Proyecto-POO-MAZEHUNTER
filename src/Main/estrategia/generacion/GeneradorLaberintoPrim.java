package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

/**
 * Implementación de la estrategia de generación de laberintos basada en el Algoritmo de Prim.
 * <p>
 * A diferencia del DFS, el algoritmo de Prim expande el laberinto de manera uniforme desde
 * un punto central, seleccionando muros adyacentes al azar de una lista de candidatos.
 * Esto produce laberintos con rutas más cortas y una estructura más ramificada,
 * lo cual se asocia con una dificultad FÁCIL o de exploración inicial.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class GeneradorLaberintoPrim implements GeneradorLaberinto {

    /**
     *  Generador de aleatoriedad para la selección de muros candidatos.
     */
    private Random random;

    /**
     * Constructor por defecto. Inicializa el motor de aleatoriedad.
     */
    public GeneradorLaberintoPrim() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto utilizando el tiempo actual como semilla de aleatoriedad.
     *
     * @param filas Cantidad de filas del laberinto.
     * @param columnas Cantidad de columnas del laberinto.
     * @return Un objeto {@link Laberinto} con estructura ramificada.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto basado en una semilla para asegurar la reproducibilidad del mapa.
     * <p>
     * El proceso comienza con una cuadrícula llena de muros y expande el camino
     * seleccionando aleatoriamente muros de una lista de "frontera", asegurando que
     * el resultado sea un laberinto perfecto (un solo camino entre cualquier par de puntos).
     * </p>
     *
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @param semilla Valor para controlar la generación aleatoria.
     * @return Un objeto {@link Laberinto} completamente formado.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        Celda[][] celdas = new Celda[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
            }
        }

        generarLaberintoPrim(celdas, filas, columnas);
        colocarElementosEspeciales(celdas, filas, columnas);

        return new Laberinto(celdas, filas, columnas);
    }

    /**
     * Implementación central del algoritmo de Prim para la excavación de caminos.
     * <p>
     * 1. Comienza en una celda arbitraria y la marca como camino.<br>
     * 2. Añade los muros adyacentes a una lista de candidatos.<br>
     * 3. Mientras haya muros en la lista, elige uno al azar y lo conecta al camino
     * siempre que solo tenga un vecino que ya sea camino.
     * </p>
     *
     * @param celdas Matriz de celdas inicializada con muros.
     * @param filas Límite vertical.
     * @param columnas Límite horizontal.
     */
    private void generarLaberintoPrim(Celda[][] celdas, int filas, int columnas) {
        List<int[]> muros = new ArrayList<>();

        int startX = 1;
        int startY = 1;
        celdas[startX][startY].setTipo(TipoCelda.CAMINO);

        agregarMurosAdyacentes(celdas, muros, startX, startY, filas, columnas);

        int[][] direcciones = { { -2, 0 }, { 0, 2 }, { 2, 0 }, { 0, -2 } };

        while (!muros.isEmpty()) {
            int indiceAleatorio = random.nextInt(muros.isEmpty() ? 1 : muros.size());
            int[] muro = muros.remove(indiceAleatorio);
            int x = muro[0];
            int y = muro[1];

            int caminosAdyacentes = 0;
            int[] ultimaDireccion = null;

            for (int[] dir : direcciones) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (esPosicionValida(nx, ny, filas, columnas) &&
                        celdas[nx][ny].getTipo() == TipoCelda.CAMINO) {
                    caminosAdyacentes++;
                    ultimaDireccion = dir;
                }
            }

            if (caminosAdyacentes == 1 && ultimaDireccion != null) {
                celdas[x][y].setTipo(TipoCelda.CAMINO);

                int muroX = x + ultimaDireccion[0] / 2;
                int muroY = y + ultimaDireccion[1] / 2;
                if (esPosicionValida(muroX, muroY, filas, columnas)) {
                    celdas[muroX][muroY].setTipo(TipoCelda.CAMINO);
                }

                agregarMurosAdyacentes(celdas, muros, x, y, filas, columnas);
            }
        }
    }

    /**
     * Identifica y registra los muros candidatos a ser convertidos en camino
     * alrededor de una posición recién procesada.
     *
     * @param celdas Matriz actual del laberinto.
     * @param muros Lista de coordenadas de muros frontera.
     * @param x Fila de la celda actual.
     * @param y Columna de la celda actual.
     */
    private void agregarMurosAdyacentes(Celda[][] celdas, List<int[]> muros, int x, int y, int filas, int columnas) {
        int[][] direcciones = { { -2, 0 }, { 0, 2 }, { 2, 0 }, { 0, -2 } };

        for (int[] dir : direcciones) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (esPosicionValida(nx, ny, filas, columnas) &&
                    celdas[nx][ny].getTipo() == TipoCelda.MURO) {
                boolean yaExiste = false;
                for (int[] muro : muros) {
                    if (muro[0] == nx && muro[1] == ny) {
                        yaExiste = true;
                        break;
                    }
                }
                if (!yaExiste) {
                    muros.add(new int[] { nx, ny });
                }
            }
        }
    }

    /**
     * Distribuye elementos de juego (Entrada, Salida, Cristales, etc.)
     * sobre las celdas de tipo camino.
     * <p>
     * Las cantidades se calculan proporcionalmente al tamaño del laberinto generado
     * para mantener el balance del juego.
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

        // Colocación de cristales, trampas, llaves y energía
        int numCristales = Math.min(12, posicionesCaminos.size() / 8);
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
            }
        }

        int numTrampas = Math.min(6, posicionesCaminos.size() / 15);
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

        int numEnergia = Math.min(6, posicionesCaminos.size() / 18);
        for (int i = 0; i < numEnergia; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos,
                    numCristales + numTrampas + 1 + i);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
            }
        }
    }

    /**
     * Busca una coordenada disponible que no haya sido ocupada por otro elemento especial.
     *
     * @param celdas Matriz actual.
     * @param posiciones Lista de celdas transitables.
     * @param inicio Índice desde el cual iniciar la búsqueda en la lista barajada.
     * @return Arreglo de coordenadas [x, y] o {@code null}.
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

    /**
     * Valida que las coordenadas de una celda se encuentren dentro de la matriz.
     *
     * @param x Posición en filas.
     * @param y Posición en columnas.
     * @param filas Máximo de filas.
     * @param columnas Máximo de columnas.
     * @return {@code true} si la posición es segura de acceder.
     */
    private boolean esPosicionValida(int x, int y, int filas, int columnas) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }
}