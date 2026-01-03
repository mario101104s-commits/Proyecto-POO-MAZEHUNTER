package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

/**
 * Estrategia de generación de laberintos basada en niveles de dificultad paramétricos.
 * <p>
 * Esta clase es responsable de crear laberintos equilibrados ajustando dinámicamente
 * la cantidad de trampas, energías, bombas y muros especiales (muros rojos) según el
 * nivel seleccionado (FÁCIL, MEDIA, DIFÍCIL). Además, garantiza que cada laberinto
 * generado sea resoluble mediante un algoritmo de búsqueda en anchura (BFS).
 * </p>
 *
 * @author Mario Sanchaz
 * @version 1.0
 * @since 22/12/25
 */
public class GeneradorLaberintoDificultad implements GeneradorLaberinto {

    /**
     *  Generador de aleatoriedad para la disposición de elementos.
     */
    private Random random;

    /**
     * Nivel de dificultad que rige los parámetros de generación
     */
    private String dificultad; // "FACIL", "MEDIA", "DIFICIL"

    /**
     * Construye un generador con una dificultad específica.
     *
     * @param dificultad Cadena que representa el nivel (ej. "FACIL").
     * Si es nula, se establece "MEDIA" por defecto.
     */
    public GeneradorLaberintoDificultad(String dificultad) {
        this.random = new Random();
        this.dificultad = dificultad != null ? dificultad : "MEDIA";
    }

    /**
     * Genera un laberinto utilizando el tiempo del sistema como semilla.
     *
     * @param filas Cantidad de filas solicitadas.
     * @param columnas Cantidad de columnas solicitadas.
     * @return Un objeto {@link Laberinto} configurado según la dificultad.
     */
    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

    /**
     * Genera un laberinto con una semilla específica para consistencia de resultados.
     * <p>
     * El flujo incluye:
     * 1. Validación estricta de dimensiones según la dificultad.
     * 2. Creación de una matriz con distribución de caminos y muros normales/rojos.
     * 3. Garantía de conectividad física mediante un algoritmo de camino directo.
     * 4. Colocación de recursos (Llaves, Bombas, Fósforos) calculados por dificultad.
     * 5. Prueba de solubilidad y corrección automática si el laberinto está bloqueado.
     * </p>
     *
     * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @param semilla Valor para controlar la aleatoriedad.
     * @return Un objeto {@link Laberinto} listo para jugar.
     * @throws IllegalArgumentException si las dimensiones no corresponden a la dificultad.
     */
    @Override
    public Laberinto generarConSemilla(int filas, int columnas, long semilla) {
        this.random = new Random(semilla);

        // Validar dimensiones según dificultad
        validarDimensiones(filas, columnas);

        Celda[][] celdas = new Celda[filas][columnas];

        // Generar patrón aleatorio de muros y caminos
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // 60% caminos, 40% muros
                if (random.nextDouble() < 0.6) {
                    celdas[i][j] = new Celda(TipoCelda.CAMINO, i, j);
                } else {
                    // Determinar si es muro normal o rojo (solo en interior)
                    boolean esBorde = (i == 0 || i == filas - 1 || j == 0 || j == columnas - 1);
                    if (esBorde) {
                        celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
                    } else {
                        // 30% muros rojos, 70% muros normales
                        TipoCelda tipoMuro = (random.nextDouble() < 0.3) ? TipoCelda.MURO_ROJO : TipoCelda.MURO;
                        celdas[i][j] = new Celda(tipoMuro, i, j);
                    }
                }
            }
        }

        // Asegurar bordes con muros normales
        for (int i = 0; i < filas; i++) {
            celdas[i][0].setTipo(TipoCelda.MURO);
            celdas[i][columnas - 1].setTipo(TipoCelda.MURO);
        }
        for (int j = 0; j < columnas; j++) {
            celdas[0][j].setTipo(TipoCelda.MURO);
            celdas[filas - 1][j].setTipo(TipoCelda.MURO);
        }

        // Asegurar conectividad básica
        asegurarConectividad(celdas, filas, columnas);

        // Colocar elementos según dificultad
        colocarElementosSegunDificultad(celdas, filas, columnas);

        // Verificar solubilidad y forzar si es necesario
        if (!esSoluble(celdas, filas, columnas)) {
            forzarCamino(celdas, filas, columnas);
        }

        return new Laberinto(celdas, filas, columnas);
    }

    /**
     * Verifica que el tamaño del laberinto sea coherente con la dificultad elegida.
     * * @param filas Número de filas.
     * @param columnas Número de columnas.
     * @throws IllegalArgumentException Si las dimensiones están fuera de rango.
     */
    private void validarDimensiones(int filas, int columnas) {
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                if (filas < 5 || filas > 15 || columnas < 10 || columnas > 25) {
                    throw new IllegalArgumentException(
                            "Dificultad FÁCIL: Filas debe ser 5-15, Columnas debe ser 10-25");
                }
                break;
            case "MEDIA":
                if (filas < 16 || filas > 25 || columnas < 26 || columnas > 35) {
                    throw new IllegalArgumentException(
                            "Dificultad MEDIA: Filas debe ser 16-25, Columnas debe ser 26-35");
                }
                break;
            case "DIFICIL":
                if (filas < 26 || filas > 45 || columnas < 36 || columnas > 65) {
                    throw new IllegalArgumentException(
                            "Dificultad DIFÍCIL: Filas debe ser 26-45, Columnas debe ser 36-65");
                }
                break;
        }
    }

    /**
     * Traza una ruta básica para asegurar que no existan áreas completamente aisladas.
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
     * Distribuye todos los elementos interactuables en el mapa utilizando
     * los cálculos de balanceo de dificultad.
     */
    private void colocarElementosSegunDificultad(Celda[][] celdas, int filas, int columnas) {
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

        // Entrada y Salida
        int[] entrada = posicionesCaminos.get(0);
        celdas[entrada[0]][entrada[1]].setTipo(TipoCelda.ENTRADA);

        int[] salida = posicionesCaminos.get(posicionesCaminos.size() - 1);
        celdas[salida[0]][salida[1]].setTipo(TipoCelda.SALIDA);

        // Cálculos de cantidades
        int numTrampas = calcularTrampas(filas);
        int numEnergias = calcularEnergias(filas);
        int numBombas = calcularBombas();
        int numFosforos = calcularFosforos();
        int numCristales = Math.max(5, posicionesCaminos.size() / 15);

        int contadorPos = 1;
        // Colocación secuencial de elementos en posiciones barajadas
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null) celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
        }

        for (int i = 0; i < numTrampas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null) celdas[pos[0]][pos[1]].setTipo(TipoCelda.TRAMPA);
        }

        int[] posLlave = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
        if (posLlave != null) celdas[posLlave[0]][posLlave[1]].setTipo(TipoCelda.LLAVE);

        for (int i = 0; i < numEnergias; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null) celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
        }

        for (int i = 0; i < numBombas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null) celdas[pos[0]][pos[1]].setTipo(TipoCelda.BOMBA);
        }

        for (int i = 0; i < numFosforos; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null) celdas[pos[0]][pos[1]].setTipo(TipoCelda.FOSFORO);
        }
    }

    /**
     * Determina el número de trampas basándose en el nivel de dificultad y la altura del laberinto.
     * @param filas Número de filas.
     * @return Cantidad de trampas.
     */
    private int calcularTrampas(int filas) {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return (filas >= 5 && filas <= 10) ? 2 : 3;
            case "MEDIA": return (filas >= 16 && filas <= 20) ? 4 : 5;
            case "DIFICIL":
                if (filas >= 26 && filas <= 30) return 6;
                if (filas >= 31 && filas <= 40) return 12;
                return 18;
            default: return 3;
        }
    }

    /**
     * Determina la cantidad de energías disponibles para el jugador.
     * @param filas Altura del mapa.
     * @return Número de celdas de energía.
     */
    private int calcularEnergias(int filas) {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return (filas >= 5 && filas <= 10) ? 2 : 3;
            case "MEDIA": return (filas >= 16 && filas <= 20) ? 4 : 5;
            case "DIFICIL":
                if (filas >= 26 && filas <= 30) return 6;
                if (filas >= 31 && filas <= 40) return 12;
                return 18;
            default: return 3;
        }
    }

    /**
     * Define el inventario inicial de bombas según la dificultad.
     * @return Cantidad de bombas.
     */
    private int calcularBombas() {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return 5;
            case "MEDIA": return 15;
            case "DIFICIL": return 20;
            default: return 15;
        }
    }

    /**
     * Define el inventario de fósforos (usados para la niebla).
     * @return Cantidad de fósforos.
     */
    private int calcularFosforos() {
        switch (dificultad.toUpperCase()) {
            case "FACIL": return 3;
            case "MEDIA": return 5;
            case "DIFICIL": return 6;
            default: return 5;
        }
    }

    /**
     * Busca una celda de tipo CAMINO en la lista de posiciones barajadas.
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
     * Utiliza un algoritmo de búsqueda en anchura (BFS) para determinar si
     * existe al menos un camino libre de muros entre la entrada y la salida.
     *
     * @return {@code true} si el laberinto es superable.
     */
    private boolean esSoluble(Celda[][] celdas, int filas, int columnas) {
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.ENTRADA) {
                    startX = i; startY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.SALIDA) {
                    endX = i; endY = j;
                }
            }
        }

        if (startX == -1 || endX == -1) return false;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startX, startY });
        boolean[][] visitado = new boolean[filas][columnas];
        visitado[startX][startY] = true;

        int[][] dirs = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            if (current[0] == endX && current[1] == endY) return true;

            for (int[] d : dirs) {
                int ni = current[0] + d[0];
                int nj = current[1] + d[1];

                if (ni >= 0 && ni < filas && nj >= 0 && nj < columnas &&
                        !visitado[ni][nj] &&
                        celdas[ni][nj].getTipo() != TipoCelda.MURO &&
                        celdas[ni][nj].getTipo() != TipoCelda.MURO_ROJO) {
                    visitado[ni][nj] = true;
                    queue.add(new int[] { ni, nj });
                }
            }
        }
        return false;
    }

    /**
     * En caso de que el laberinto sea insoluble, este método perfora muros
     * en línea recta para crear una vía de escape garantizada.
     */
    private void forzarCamino(Celda[][] celdas, int filas, int columnas) {
        int startX = -1, startY = -1;
        int endX = -1, endY = -1;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (celdas[i][j].getTipo() == TipoCelda.ENTRADA) {
                    startX = i; startY = j;
                } else if (celdas[i][j].getTipo() == TipoCelda.SALIDA) {
                    endX = i; endY = j;
                }
            }
        }

        if (startX == -1 || endX == -1) return;

        int currX = startX;
        int currY = startY;

        while (currX != endX || currY != endY) {
            if (currX < endX) currX++;
            else if (currX > endX) currX--;
            else if (currY < endY) currY++;
            else if (currY > endY) currY--;

            if (celdas[currX][currY].getTipo() == TipoCelda.MURO ||
                    celdas[currX][currY].getTipo() == TipoCelda.MURO_ROJO) {
                celdas[currX][currY].setTipo(TipoCelda.CAMINO);
            }
        }
    }
}