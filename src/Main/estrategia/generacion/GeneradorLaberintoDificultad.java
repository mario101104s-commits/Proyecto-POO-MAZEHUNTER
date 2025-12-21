package Main.estrategia.generacion;

import Main.modelo.Dominio.Laberinto;
import Main.modelo.Dominio.Celda;
import Main.modelo.Constantes.TipoCelda;
import Main.servicio.Interfaces.GeneradorLaberinto;

import java.util.*;

// Estrategia única de generación con sistema de dificultades
// Genera laberintos aleatorios con parámetros específicos por dificultad
public class GeneradorLaberintoDificultad implements GeneradorLaberinto {
    private Random random;
    private String dificultad; // "FACIL", "MEDIA", "DIFICIL"

    public GeneradorLaberintoDificultad(String dificultad) {
        this.random = new Random();
        this.dificultad = dificultad != null ? dificultad : "MEDIA";
    }

    @Override
    public Laberinto generar(int filas, int columnas) {
        return generarConSemilla(filas, columnas, System.currentTimeMillis());
    }

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

        // Asegurar conectividad
        asegurarConectividad(celdas, filas, columnas);

        // Colocar elementos según dificultad
        colocarElementosSegunDificultad(celdas, filas, columnas);

        return new Laberinto(celdas, filas, columnas);
    }

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

        // Calcular cantidades según dificultad y filas
        int numTrampas = calcularTrampas(filas);
        int numEnergias = calcularEnergias(filas);
        int numBombas = calcularBombas();
        int numLlavesExplosion = calcularLlavesExplosion();
        int numCristales = Math.max(5, posicionesCaminos.size() / 15); // Cristales proporcionales

        // Colocar cristales
        int contadorPos = 1;
        for (int i = 0; i < numCristales; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null)
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.CRISTAL);
        }

        // Colocar trampas
        for (int i = 0; i < numTrampas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null)
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.TRAMPA);
        }

        // Colocar llave salida
        int[] posLlave = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
        if (posLlave != null)
            celdas[posLlave[0]][posLlave[1]].setTipo(TipoCelda.LLAVE);

        // Colocar energías
        for (int i = 0; i < numEnergias; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null)
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.ENERGIA);
        }

        // Colocar bombas
        for (int i = 0; i < numBombas; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null)
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.BOMBA);
        }

        // Colocar llaves de explosión
        for (int i = 0; i < numLlavesExplosion; i++) {
            int[] pos = encontrarPosicionValida(celdas, posicionesCaminos, contadorPos++);
            if (pos != null)
                celdas[pos[0]][pos[1]].setTipo(TipoCelda.LLAVE_EXPLOSION);
        }
    }

    private int calcularTrampas(int filas) {
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                return (filas >= 5 && filas <= 10) ? 2 : 3;
            case "MEDIA":
                return (filas >= 16 && filas <= 20) ? 4 : 5;
            case "DIFICIL":
                if (filas >= 26 && filas <= 30)
                    return 6;
                if (filas >= 31 && filas <= 40)
                    return 12;
                return 18; // 41-45
            default:
                return 3;
        }
    }

    private int calcularEnergias(int filas) {
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                return (filas >= 5 && filas <= 10) ? 2 : 3;
            case "MEDIA":
                return (filas >= 16 && filas <= 20) ? 4 : 5;
            case "DIFICIL":
                if (filas >= 26 && filas <= 30)
                    return 6;
                if (filas >= 31 && filas <= 40)
                    return 12;
                return 18; // 41-45
            default:
                return 3;
        }
    }

    private int calcularBombas() {
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                return 5;
            case "MEDIA":
                return 15;
            case "DIFICIL":
                return 20;
            default:
                return 15;
        }
    }

    private int calcularLlavesExplosion() {
        switch (dificultad.toUpperCase()) {
            case "FACIL":
                return 1;
            case "MEDIA":
                return 3;
            case "DIFICIL":
                return 4;
            default:
                return 3;
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
