package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

// Estrategia de generación completamente aleatoria
// Genera laberintos impredecibles - Dificultad VARIABLE
public class GeneradorLaberintoAleatorio implements GeneradorLaberinto {
    private Random random;

    public GeneradorLaberintoAleatorio() {
        this.random = new Random();
    }

    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

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

        // Asegurar bordes
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

    private void asegurarConectividad(Celda[][] celdas, int filas, int columnas) {
        // Crear camino garantizado desde esquina superior izquierda a inferior derecha
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
