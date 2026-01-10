package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Implementación de la estrategia de generación de laberintos mediante el algoritmo
 * Depth-First Search (DFS) con Backtracking.
 * <p>
 * Este generador crea laberintos perfectos (sin ciclos) caracterizados por tener
 * caminos largos, sinuosos y con muchos callejones sin salida. Es ideal para
 * niveles de dificultad media o alta debido a la complejidad de las rutas generadas.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class GeneradorLaberintoDFS implements GeneradorLaberinto {

    /**
     * Generador de aleatoriedad para la elección de direcciones.
     */
    private Random random;

    /**
     * Constructor por defecto. Inicializa el motor de aleatoriedad.
     */
    public GeneradorLaberintoDFS() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto utilizando el timestamp actual como semilla.
     *
     * @param filas Cantidad de filas del laberinto.
     * @param columnas Cantidad de columnas del laberinto.
     * @return Un objeto {@link Laberinto} con estructura sinuosa.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto basado en una semilla para garantizar la reproducibilidad.
     * <p>
     * El proceso inicia con una cuadrícula llena de muros y utiliza una pila (Stack)
     * para excavar caminos, asegurando que todas las áreas transitables estén conectadas.
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

        generarLaberintoDFS(celdas, filas, columnas);
        colocarElementosEspeciales(celdas, filas, columnas);

        // Verificar solubilidad y forzar si es necesario
        if (!esSoluble(celdas, filas, columnas)) {
            forzarCamino(celdas, filas, columnas);
        }

        return new Laberinto(celdas, filas, columnas);
    }

    /**
     * Algoritmo principal de DFS que talla el laberinto sobre la matriz de muros.
     * <p>
     * Selecciona una celda inicial y se mueve en pasos de dos unidades para mantener
     * muros divisorios, eliminando el muro intermedio para conectar las celdas de camino.
     * </p>
     *
     * @param celdas Matriz de celdas a procesar.
     * @param filas Límite vertical.
     * @param columnas Límite horizontal.
     */
    private void generarLaberintoDFS(Celda[][] celdas, int filas, int columnas) {
        Stack<int[]> pila = new Stack<>();
        int startX = 1;
        int startY = 1;

        celdas[startX][startY].setTipo(TipoCelda.CAMINO);
        pila.push(new int[] { startX, startY });

        // Direcciones en saltos de 2 celdas
        int[][] direcciones = { { -2, 0 }, { 0, 2 }, { 2, 0 }, { 0, -2 } };

        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            int x = actual[0];
            int y = actual[1];

            ArrayList<int[]> direccionesValidas = new ArrayList<>();

            for (int[] dir : direcciones) {
                int nuevoX = x + dir[0];
                int nuevoY = y + dir[1];

                if (esPosicionValida(nuevoX, nuevoY, filas, columnas) &&
                        celdas[nuevoX][nuevoY].getTipo() == TipoCelda.MURO) {
                    direccionesValidas.add(dir);
                }
            }

            if (!direccionesValidas.isEmpty()) {
                int[] dirElegida = direccionesValidas.get(random.nextInt(direccionesValidas.size()));
                int nuevoX = x + dirElegida[0];
                int nuevoY = y + dirElegida[1];

                // Romper el muro intermedio
                int muroX = x + dirElegida[0] / 2;
                int muroY = y + dirElegida[1] / 2;
                celdas[muroX][muroY].setTipo(TipoCelda.CAMINO);

                celdas[nuevoX][nuevoY].setTipo(TipoCelda.CAMINO);
                pila.push(new int[] { nuevoX, nuevoY });
            } else {
                pila.pop();
            }
        }
    }

    /**
     * Distribuye la entrada, salida y consumibles en las celdas transitables.
     * <p>
     * Utiliza un barajado aleatorio para asegurar que los elementos no se
     * concentren en una sola zona del mapa.
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

        // Distribución de cristales, trampas, llaves y energía
        int numCristales = Math.min(10, posicionesCaminos.size() / 10);
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
            }
        }

        int numTrampas = Math.min(8, posicionesCaminos.size() / 12);
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

        int numEnergia = Math.min(5, posicionesCaminos.size() / 20);
        for (int i = 0; i < numEnergia; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos,
                    numCristales + numTrampas + 1 + i);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
            }
        }
    }

    /**
     * Busca una posición libre de elementos especiales en la lista de caminos.
     *
     * @param celdas Matriz actual.
     * @param posiciones Lista de coordenadas de tipo CAMINO.
     * @param inicio Índice para evitar colisiones entre objetos.
     * @return Coordenadas [x,y] o {@code null}.
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