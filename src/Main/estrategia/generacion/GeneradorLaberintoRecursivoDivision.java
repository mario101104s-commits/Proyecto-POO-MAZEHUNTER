package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

// Estrategia de generación usando División Recursiva
// Genera laberintos con estructura de habitaciones - Dificultad DIFÍCIL
public class GeneradorLaberintoRecursivoDivision implements GeneradorLaberinto {
    private Random random;

    public GeneradorLaberintoRecursivoDivision() {
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

        // Inicializar todo como camino
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                celdas[i][j] = new Celda(TipoCelda.CAMINO, i, j);
            }
        }

        // Crear bordes
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

        return new Laberinto(celdas, filas, columnas);
    }

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
