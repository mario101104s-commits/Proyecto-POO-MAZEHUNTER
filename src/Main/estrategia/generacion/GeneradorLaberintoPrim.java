package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

// Estrategia de generación usando Algoritmo de Prim
// Genera laberintos más abiertos con múltiples caminos - Dificultad FÁCIL
public class GeneradorLaberintoPrim implements GeneradorLaberinto {
    private Random random;

    public GeneradorLaberintoPrim() {
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
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(TipoCelda.MURO, i, j);
            }
        }

        generarLaberintoPrim(celdas, filas, columnas);
        colocarElementosEspeciales(celdas, filas, columnas);

        return new Laberinto(celdas, filas, columnas);
    }

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

    private int[] encontrarPosicionValida(Celda[][] celdas, List<int[]> posiciones, int inicio) {
        for (int i = inicio; i < posiciones.size(); i++) {
            int[] pos = posiciones.get(i);
            if (celdas[pos[0]][pos[1]].getTipo() == TipoCelda.CAMINO) {
                return pos;
            }
        }
        return null;
    }

    private boolean esPosicionValida(int x, int y, int filas, int columnas) {
        return x >= 0 && x < filas && y >= 0 && y < columnas;
    }
}
