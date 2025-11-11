package Main.modelo.Dominio;

public class Laberinto {
    private Celda[][] celdas;
    private int filas;
    private int columnas;

    public Laberinto(Celda[][] celdas, int filas, int columnas) {
        this.celdas = celdas;
        this.filas = filas;
        this.columnas = columnas;
    }

    // Getters
    public Celda[][] getCeldas() { return celdas; }
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    public Celda getCelda(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            return celdas[fila][columna];
        }
        return null;
    }

    public void setCelda(int fila, int columna, Celda celda) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            celdas[fila][columna] = celda;
        }
    }

    public boolean esPosicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    public boolean esTransitable(int fila, int columna) {
        Celda celda = getCelda(fila, columna);
        return celda != null && celda.isTransitable();
    }
}
