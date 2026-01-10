package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Implementación de la estrategia de generación de laberintos mediante el algoritmo de División Recursiva.
 * <p>
 * A diferencia de los algoritmos de "excavación" (como DFS o Prim), este algoritmo comienza con un
 * espacio completamente abierto y lo divide repetidamente mediante muros horizontales o verticales
 * que contienen un único pasaje aleatorio.
 * </p>
 * <p>
 * El resultado es un laberinto con una estructura similar a un conjunto de habitaciones interconectadas,
 * lo cual genera grandes áreas abiertas y pasillos largos, aumentando la dificultad de orientación.
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class GeneradorLaberintoRecursivoDivision implements GeneradorLaberinto {

    /**
     * Motor de aleatoriedad para determinar la orientación de la división y la posición de los pasajes.
     */
    private Random random;

    /**
     * Constructor por defecto. Inicializa el generador de números aleatorios.
     */
    public GeneradorLaberintoRecursivoDivision() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto utilizando el timestamp actual como semilla.
     *
     * @param filas Cantidad de filas del laberinto.
     * @param columnas Cantidad de columnas del laberinto.
     * @return Un objeto {@link Laberinto} con estructura de división recursiva.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto basado en una semilla para permitir la recreación de estructuras específicas.
     * <p>
     * El flujo de trabajo consiste en:
     * 1. Inicializar todas las celdas como {@link TipoCelda#CAMINO}.
     * 2. Construir muros perimetrales.
     * 3. Invocar el proceso recursivo de subdivisión de áreas.
     * 4. Distribuir elementos especiales en las áreas transitables.
     * </p>
     *
     * @param filas Número de filas del laberinto.
     * @param columnas Número de columnas del laberinto.
     * @param semilla Valor para inicializar el generador aleatorio.
     * @return Un objeto {@link Laberinto} estructurado.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        Celda[][] celdas = new Celda[filas][columnas];

        // Inicializar todo como camino
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(TipoCelda.CAMINO, i, j);
            }
        }

        for (int i = 0; i < filas; i++) {
            celdas[i][0].setTipo(TipoCelda.MURO);
            celdas[i][columnas - 1].setTipo(TipoCelda.MURO);
        }
        for (int j = 0; j < columnas; j++) {
            celdas[0][j].setTipo(TipoCelda.MURO);
            celdas[filas - 1][j].setTipo(TipoCelda.MURO);
        }

        // Dividir recursivamente
        dividir(celdas, 1, 1, filas - 2, columnas - 2);

        colocarElementosEspeciales(celdas, filas, columnas);

        // Verificar solubilidad y forzar si es necesario
        if (!esSoluble(celdas, filas, columnas)) {
            forzarCamino(celdas, filas, columnas);
        }

        return new Laberinto(celdas, filas, columnas);
    }

    /**
     * Metodo recursivo que subdivide una región del laberinto en dos partes menores.
     * <p>
     * El algoritmo decide si dividir horizontal o verticalmente basándose en la forma de
     * la región (ancho contra alto). Se coloca un muro con un único hueco (pasaje) para
     * garantizar que todas las "habitaciones" sigan estando conectadas.
     * </p>
     *
     * @param celdas Matriz de celdas.
     * @param x Coordenada inicial en filas.
     * @param y Coordenada inicial en columnas.
     * @param ancho Extensión horizontal de la región actual.
     * @param alto Extensión vertical de la región actual.
     */
    private void dividir(Celda[][] celdas, int x, int y, int ancho, int alto) {
        if (ancho < 2 || alto < 2)
            return;
        boolean horizontal = (alto > ancho);
        if (ancho == alto) {
            horizontal = random.nextBoolean();
        }

        if (horizontal) {
            int muroY = y + random.nextInt(alto);
            int pasajeX = x + random.nextInt(ancho);

            for (int i = x; i < x + ancho; i++) {
                if (i != pasajeX) {
                    celdas[muroY][i].setTipo(TipoCelda.MURO);
                }
            }

            dividir(celdas, x, y, ancho, muroY - y);
            dividir(celdas, x, muroY + 1, ancho, alto - (muroY - y) - 1);
        } else {
            int muroX = x + random.nextInt(ancho);
            int pasajeY = y + random.nextInt(alto);

            for (int j = y; j < y + alto; j++) {
                if (j != pasajeY) {
                    celdas[j][muroX].setTipo(TipoCelda.MURO);
                }
            }

            dividir(celdas, x, y, muroX - x, alto);
            dividir(celdas, muroX + 1, y, ancho - (muroX - x) - 1, alto);
        }
    }

    /**
     * Coloca los puntos de entrada, salida y consumibles en las áreas de camino.
     * <p>
     * Utiliza un barajado de las posiciones de camino disponibles para maximizar la
     * dispersión de los objetos especiales.
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

        int numCristales = Math.min(8, posicionesCaminos.size() / 12);
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
            }
        }

        int numTrampas = Math.min(10, posicionesCaminos.size() / 10);
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

        int numEnergia = Math.min(4, posicionesCaminos.size() / 25);
        for (int i = 0; i < numEnergia; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos,
                    numCristales + numTrampas + 1 + i);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
            }
        }
    }

    /**
     * Busca una posición libre en la lista barajada de caminos.
     *
     * @param celdas Matriz actual.
     * @param posiciones Lista de coordenadas transitables.
     * @param inicio Índice de partida en la búsqueda.
     * @return Arreglo de coordenadas [fila, columna] o {@code null}.
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

    /**
     * Valida que las coordenadas se encuentren dentro de los límites de la matriz.
     *
     * @param x Fila.
     * @param y Columna.
     * @param filas Total filas.
     * @param columnas Total columnas.
     * @return {@code true} si la posición es segura de acceder.
     */
    private boolean esPosicionValida(int x, int y, int filas, int columnas) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }
}