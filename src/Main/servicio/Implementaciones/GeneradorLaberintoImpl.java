package Main.servicio.Implementaciones;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.Random;
import java.util.Stack;

public class GeneradorLaberintoImpl implements GeneradorLaberinto {
    private Random random;

    public GeneradorLaberintoImpl() {
        this.random = new Random();
    }

    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

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

    private int[] encontrarPosicionValida(Celda[][] celdas, java.util.List<int[]> posiciones, int inicio) {
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
