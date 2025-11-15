package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.Random;
import java.util.Stack;
/**
 * Implementación concreta de la interfaz {@code GeneradorLaberinto} que utiliza el algoritmo
 * de Búsqueda en Profundidad para generar laberintos perfectos.
 * <p>
 * Además de la generación de la estructura (caminos y muros), se encarga de la colocación
 * aleatoria de los elementos del juego: entrada, salida, cristales, trampas, llave y energía.
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */

public class GeneradorLaberintoImpl implements GeneradorLaberinto {
    /** Generador de números pseudoaleatorios utilizado para la selección de caminos y colocación de elementos. */
    private Random random;
    /**
     * Constructor. Inicializa el generador de números aleatorios.
     */
    public GeneradorLaberintoImpl() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto de las dimensiones especificadas utilizando una semilla de tiempo actual.
     *
     * @param filas El número de filas del laberinto.
     * @param columnas El número de columnas del laberinto.
     * @return Un objeto {@code Laberinto} recién generado.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }/**
     * Genera un laberinto determinista utilizando el algoritmo DFS a partir de una semilla específica.
     * * Esto asegura que el mismo laberinto puede ser recreado si se usa la misma semilla.
     *
     * @param filas El número de filas.
     * @param columnas El número de columnas.
     * @param semilla La semilla utilizada para el generador de números aleatorios.
     * @return Un objeto {@code Laberinto} recién generado.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        // Crear matriz inicial llena de muros
        Celda[][] celdas = new Celda[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
            }
        }

        // Generar laberinto usando Depth-First Search
        generarLaberintoDFS(celdas, filas, columnas);

        // Colocar elementos especiales
        colocarElementosEspeciales(celdas, filas, columnas);

        return new Laberinto(celdas, filas, columnas);
    }
    /**
     * Aplica el algoritmo de Búsqueda en Profundidad (DFS) para crear los caminos del laberinto.
     * * Trabaja en una cuadrícula donde las celdas son inicialmente muros, creando caminos
     * entre celdas no visitadas y eliminando los muros intermedios.
     *
     * @param celdas La matriz de celdas a modificar.
     * @param filas El límite de filas.
     * @param columnas El límite de columnas.
     */
    private void generarLaberintoDFS(Celda[][] celdas, int filas, int columnas) {
        Stack<int[]> pila = new Stack<>();

        // Empezar desde una posición aleatoria impar (para mantener patrón)
        int startX = 1;
        int startY = 1;

        celdas[startX][startY].setTipo(TipoCelda.CAMINO);
        pila.push(new int[]{startX, startY});

        // Direcciones: arriba, derecha, abajo, izquierda
        int[][] direcciones = {{-2, 0}, {0, 2}, {2, 0}, {0, -2}};

        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            int x = actual[0];
            int y = actual[1];

            // Obtener direcciones válidas no visitadas
            java.util.ArrayList<int[]> direccionesValidas = new java.util.ArrayList<>();

            for (int[] dir : direcciones) {
                int nuevoX = x + dir[0];
                int nuevoY = y + dir[1];

                if (esPosicionValida(nuevoX, nuevoY, filas, columnas) &&
                        celdas[nuevoX][nuevoY].getTipo() == TipoCelda.MURO) {
                    direccionesValidas.add(dir);
                }
            }

            if (!direccionesValidas.isEmpty()) {
                // Elegir dirección aleatoria
                int[] dirElegida = direccionesValidas.get(random.nextInt(direccionesValidas.size()));
                int nuevoX = x + dirElegida[0];
                int nuevoY = y + dirElegida[1];

                // Quitar el muro entre la celda actual y la nueva
                int muroX = x + dirElegida[0] / 2;
                int muroY = y + dirElegida[1] / 2;
                celdas[muroX][muroY].setTipo(TipoCelda.CAMINO);

                // Marcar la nueva celda como camino
                celdas[nuevoX][nuevoY].setTipo(TipoCelda.CAMINO);

                pila.push(new int[]{nuevoX, nuevoY});
            } else {
                pila.pop();
            }
        }
    }
    /**
     * Coloca los elementos especiales del juego (Entrada, Salida, Cristales, Trampas, Llave, Energía)
     * en posiciones aleatorias de los caminos generados.
     *
     * @param celdas La matriz de celdas del laberinto.
     * @param filas El límite de filas.
     * @param columnas El límite de columnas.
     */
    private void colocarElementosEspeciales(Celda[][] celdas, int filas, int columnas) {
        java.util.List<int[]> posicionesCaminos = new java.util.ArrayList<>();

        // Recolectar todas las posiciones que son caminos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.CAMINO) {
                    posicionesCaminos.add(new int[]{i, j});
                }
            }
        }

        if (posicionesCaminos.size() < 10) return; // Laberinto muy pequeño

        // Mezclar posiciones
        java.util.Collections.shuffle(posicionesCaminos, random);

        // Colocar entrada (posición 0)
        int[] entrada = posicionesCaminos.get(0);
        celdas[entrada[0]][entrada[1]].setTipo(TipoCelda.ENTRADA);

        // Colocar salida (última posición)
        int[] salida = posicionesCaminos.get(posicionesCaminos.size() - 1);
        celdas[salida[0]][salida[1]].setTipo(TipoCelda.SALIDA);

        // Colocar cristales (10% de los caminos, máximo 10)
        int numCristales = Math.min(10, posicionesCaminos.size() / 10);
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
            }
        }

        // Colocar trampas (8% de los caminos, máximo 8)
        int numTrampas = Math.min(8, posicionesCaminos.size() / 12);
        for (int i = 0; i < numTrampas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, i + numCristales + 1);
            if (pos != null) {
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.TRAMPA);
            }
        }


        // Colocar llave (solo 1)
        int[] posLlave = encontrarPosicionValida(celdas, posicionesCaminos, numCristales + numTrampas + 1);
        if (posLlave != null) {
            celdas[posLlave[0]][posLlave[1]].setTipo(TipoCelda.LLAVE);
        }

        // Colocar energía (5% de los caminos, máximo 5)
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
     * Busca la siguiente posición libre de {@code TipoCelda.CAMINO} dentro de la lista de posiciones.
     * * Utilizado para la colocación de elementos especiales después de mezclar la lista.
     *
     * @param celdas La matriz de celdas.
     * @param posiciones La lista de posiciones de camino disponibles.
     * @param inicio El índice a partir del cual comenzar la búsqueda.
     * @return Un array {@code int[]} con las coordenadas [fila, columna], o {@code null} si no se encuentra.
     */
    private int[] encontrarPosicionValida(Celda[][] celdas, java.util.List<int[]> posiciones, int inicio) {
        for (int i = inicio; i < posiciones.size(); i++) {
            int[] pos = posiciones.get(i);
            if (celdas[pos[0]][pos[1]].getTipo() == TipoCelda.CAMINO) {
                return pos;
            }
        }
        return null;
    }
    /**
     * Verifica si un par de coordenadas está dentro de los límites de la matriz del laberinto.
     *
     * @param x La coordenada de la fila a verificar.
     * @param y La coordenada de la columna a verificar.
     * @param filas El límite máximo de filas.
     * @param columnas El límite máximo de columnas.
     * @return {@code true} si la posición es válida, {@code false} en caso contrario.
     */
    private boolean esPosicionValida(int x, int y, int filas, int columnas) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }
}
