package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

import java.util.Queue;
import java.util.LinkedList;

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

        // Verificar solubilidad y forzar si es necesario
        if (!esSoluble(celdas, filas, columnas)) {
            forzarCamino(celdas, filas, columnas);
        }

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

    /**
     * Verifica si el laberinto tiene solución usando BFS para encontrar un camino
     * desde la entrada hasta la salida y asegurando que la llave sea accesible.
     *
     * @param celdas Matriz del laberinto.
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @return {@code true} si el laberinto es soluble.
     */
    private boolean esSoluble(Celda[][] celdas, int filas, int columnas) {
        // Encontrar entrada, salida y llave
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;
        int keyX = -1, keyY = -1;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.ENTRADA) {
                    startX = i;
                    startY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.SALIDA) {
                    endX = i;
                    endY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.LLAVE) {
                    keyX = i;
                    keyY = j;
                }
            }
        }

        if (startX == -1 || endX == -1 || keyX == -1) {
            return false;
        }

        // Verificar que se puede llegar desde entrada hasta la llave
        if (!hayCaminoBFS(celdas, filas, columnas, startX, startY, keyX, keyY)) {
            return false;
        }

        // Verificar que se puede llegar desde la llave hasta la salida
        return hayCaminoBFS(celdas, filas, columnas, keyX, keyY, endX, endY);
    }

    /**
     * Algoritmo BFS para verificar si existe un camino entre dos puntos.
     *
     * @param celdas Matriz del laberinto.
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @param startX Coordenada X inicial.
     * @param startY Coordenada Y inicial.
     * @param endX Coordenada X final.
     * @param endY Coordenada Y final.
     * @return {@code true} si hay camino.
     */
    private boolean hayCaminoBFS(Celda[][] celdas, int filas, int columnas, 
                                int startX, int startY, int endX, int endY) {
        boolean[][] visitado = new boolean[filas][columnas];
        Queue<int[]> cola = new LinkedList<>();
        cola.add(new int[]{startX, startY});
        visitado[startX][startY] = true;

        int[][] dirs = {{-1,0}, {1,0}, {0,-1}, {0,1}};

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int x = actual[0], y = actual[1];

            if (x == endX && y == endY) {
                return true;
            }

            for (int[] dir : dirs) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (esPosicionValida(nx, ny, filas, columnas) && !visitado[nx][ny]) {
                    TipoCelda tipo = celdas[nx][ny].getTipo();
                    if (tipo != TipoCelda.MURO) {
                        visitado[nx][ny] = true;
                        cola.add(new int[]{nx, ny});
                    }
                }
            }
        }

        return false;
    }

    /**
     * Fuerza la creación de un camino si el laberinto no tiene solución.
     * Crea un camino directo desde la entrada hasta la llave y desde la llave hasta la salida.
     *
     * @param celdas Matriz del laberinto.
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     */
    private void forzarCamino(Celda[][] celdas, int filas, int columnas) {
        // Encontrar entrada, salida y llave
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;
        int keyX = -1, keyY = -1;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.ENTRADA) {
                    startX = i;
                    startY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.SALIDA) {
                    endX = i;
                    endY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.LLAVE) {
                    keyX = i;
                    keyY = j;
                }
            }
        }

        if (startX != -1 && keyX != -1) {
            crearCaminoDirecto(celdas, startX, startY, keyX, keyY);
        }
        if (keyX != -1 && endX != -1) {
            crearCaminoDirecto(celdas, keyX, keyY, endX, endY);
        }
    }

    /**
     * Crea un camino directo entre dos puntos convirtiendo muros en caminos.
     *
     * @param celdas Matriz del laberinto.
     * @param x1 Coordenada X inicial.
     * @param y1 Coordenada Y inicial.
     * @param x2 Coordenada X final.
     * @param y2 Coordenada Y final.
     */
    private void crearCaminoDirecto(Celda[][] celdas, int x1, int y1, int x2, int y2) {
        // Camino horizontal primero
        int minX = Math.min(y1, y2);
        int maxX = Math.max(y1, y2);
        for (int y = minX; y <= maxX; y++) {
            if (celdas[x1][y].getTipo() == TipoCelda.MURO) {
                celdas[x1][y].setTipo(TipoCelda.CAMINO);
            }
        }

        // Camino vertical después
        int minY = Math.min(x1, x2);
        int maxY = Math.max(x1, x2);
        for (int x = minY; x <= maxY; x++) {
            if (celdas[x][y2].getTipo() == TipoCelda.MURO) {
                celdas[x][y2].setTipo(TipoCelda.CAMINO);
            }
        }
    }
}