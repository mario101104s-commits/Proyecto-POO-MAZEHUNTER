package Main.estrategia.contexto;

import Main.estrategia.factory.GeneradorLaberintoFactory;
import Main.modelo.Dominio.Laberinto;
import Main.servicio.Interfaces.GeneradorLaberinto;

// Context para la estrategia de generación de laberintos
// Permite cambiar la estrategia en tiempo de ejecución
public class ConfiguracionJuego {
    private GeneradorLaberinto estrategiaGeneracion;
    private String tipoActual;

    public ConfiguracionJuego() {
        // Estrategia por defecto: MEDIA
        this.tipoActual = GeneradorLaberintoFactory.MEDIA;
        this.estrategiaGeneracion = GeneradorLaberintoFactory.crear(tipoActual);
    }

    // Cambia la estrategia de generación
    public void setEstrategiaGeneracion(String tipo) {
        this.tipoActual = tipo;
        this.estrategiaGeneracion = GeneradorLaberintoFactory.crear(tipo);
    }

    // Obtiene el tipo actual de estrategia
    public String getTipoActual() {
        return tipoActual;
    }

    // Obtiene la descripción de la estrategia actual
    public String getDescripcionActual() {
        return GeneradorLaberintoFactory.obtenerDescripcion(tipoActual);
    }

    // Genera un laberinto usando la estrategia actual
    public Laberinto generarLaberinto(int filas, int columnas) {
        return estrategiaGeneracion.generar(filas, columnas);
    }

    // Genera un laberinto con semilla usando la estrategia actual
    public Laberinto generarLaberintoConSemilla(int filas, int columnas, long semilla) {
        return estrategiaGeneracion.generarConSemilla(filas, columnas, semilla);
    }
}
