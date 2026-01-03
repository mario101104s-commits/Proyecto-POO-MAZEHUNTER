package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

/**
 * Implementación concreta de la interfaz {@link GeneradorLaberinto} que utiliza el algoritmo
 * de Búsqueda en Profundidad (DFS) con Backtracking para generar laberintos perfectos.
 * <p>
 * El algoritmo asegura que todas las celdas de camino sean alcanzables. Tras generar
 * la estructura, se distribuyen aleatoriamente elementos como cristales, trampas,
 * llaves, bombas y fósforos.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.1
 * @since 11/11/2025
 */
public class GeneradorLaberintoImpl implements GeneradorLaberinto {

    /** Generador de números pseudoaleatorios. */
    private Random random;

    /**
     * Constructor por defecto.
     */
    public GeneradorLaberintoImpl() {
        this.random = new Random();
    }

    /**
     * Genera un laberinto utilizando el tiempo actual como semilla.
     *
     * @param filas Número de filas solicitadas.
     * @param columnas Número de columnas solicitadas.
     * @return Un objeto {@link Laberinto} procesado.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto determinista a partir de una semilla.
     * <p>
     * Se recomienda usar dimensiones impares para un resultado óptimo del algoritmo DFS.
     * </p>
     *
     * @param filas El número de filas.
     * @param columnas El número de columnas.
     * @param semilla La semilla para el generador aleatorio.
     * @return Un objeto {@link Laberinto}.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        // Ajuste opcional: asegurar que las dimensiones sean impares para el patrón de muros
        int f = (filas % 2 == 0) ? filas + 1 : filas;
        int c = (columnas % 2 == 0) ? columnas + 1 : columnas;

        Celda[][] celdas = new Celda[f][c];
        for (int i = 0; i < f; i++) {
            for (int j = 0; j < c; j++) {
                celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
            }
        }

        generarLaberintoDFS(celdas, f, c);
        colocarElementosEspeciales(celdas, f, c);

        return new Laberinto(celdas, f, c);
    }

    /**
     * Ejecuta el algoritmo de Backtracking para esculpir caminos en la matriz de muros.
     */
    private void generarLaberintoDFS(Celda[][] celdas, int filas, int columnas) {
        Stack<int[]> pila = new Stack<>();

        // Punto de inicio
        int startX = 1;
        int startY = 1;
        celdas[startX][startY].setTipo(TipoCelda.CAMINO);
        pila.push(new int[]{startX, startY});

        int[][] direcciones = {{-2, 0}, {0, 2}, {2, 0}, {0, -2}};

        while (!pila.isEmpty()) {
            int[] actual = pila.peek();
            List<int[]> validos = new ArrayList<>();

            for (int[] dir : direcciones) {
                int nx = actual[0] + dir[0];
                int ny = actual[1] + dir[1];

                if (esPosicionValida(nx, ny, filas, columnas) && celdas[nx][ny].getTipo() == TipoCelda.MURO) {
                    validos.add(dir);
                }
            }

            if (!validos.isEmpty()) {
                int[] d = validos.get(random.nextInt(validos.size()));
                int nx = actual[0] + d[0];
                int ny = actual[1] + d[1];

                // Romper el muro intermedio
                celdas[actual[0] + d[0]/2][actual[1] + d[1]/2].setTipo(TipoCelda.CAMINO);
                celdas[nx][ny].setTipo(TipoCelda.CAMINO);

                pila.push(new int[]{nx, ny});
            } else {
                pila.pop();
            }
        }
    }

    /**
     * Distribuye los elementos del juego sobre los caminos generados.
     */
    private void colocarElementosEspeciales(Celda[][] celdas, int filas, int columnas) {
        List<int[]> caminos = new ArrayList<>();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.CAMINO) {
                    caminos.add(new int[]{i, j});
                }
            }
        }

        if (caminos.size() < 15) return;

        Collections.shuffle(caminos, random);

        // Entrada y Salida
        celdas[caminos.get(0)[0]][caminos.get(0)[1]].setTipo(TipoCelda.ENTRADA);
        celdas[caminos.get(caminos.size()-1)[0]][caminos.get(caminos.size()-1)[1]].setTipo(TipoCelda.SALIDA);

        // Distribución de ítems (usando índices de la lista mezclada)
        int idx = 1;

        // Cristales (10%)
        int limite = Math.min(10, caminos.size() / 10);
        for(int i=0; i < limite; i++) celdas[caminos.get(idx++)[0]][caminos.get(idx)[1]].setTipo(TipoCelda.CRISTAL);

        // Trampas (8%)
        limite = Math.min(8, caminos.size() / 12);
        for(int i=0; i < limite; i++) celdas[caminos.get(idx++)[0]][caminos.get(idx)[1]].setTipo(TipoCelda.TRAMPA);

        // Llave (1)
        celdas[caminos.get(idx++)[0]][caminos.get(idx)[1]].setTipo(TipoCelda.LLAVE);

        // Bombas (Nuevas)
        celdas[caminos.get(idx++)[0]][caminos.get(idx)[1]].setTipo(TipoCelda.BOMBA);

        // Fósforos (Nuevos)
        celdas[caminos.get(idx++)[0]][caminos.get(idx)[1]].setTipo(TipoCelda.FOSFORO);
    }
    private boolean esPosicionValida(int x, int y, int f, int c) {
        return x > 0 && x < f - 1 && y > 0 && y < c - 1;
    }
}