package Main.servicio.Interfaces;
import Main.modelo.Dominio.Laberinto;
public interface GeneradorLaberinto {
    Laberinto generar(int filas, int columnas);
    Laberinto generarConSemilla(int filas, int columnas, long semilla);
}