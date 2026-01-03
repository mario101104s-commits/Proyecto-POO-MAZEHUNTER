package Main.ui.consola;

import Main.modelo.Dominio.Celda;
import Main.modelo.Dominio.Jugador;
import Main.modelo.Dominio.Laberinto;

/**
 * Clase de utilidad para la Interfaz de Usuario (UI) encargada de la representación
 * visual del juego en la consola.
 * <p>
 * Transforma las entidades del dominio (Laberinto, Jugador, Celdas) en una matriz
 * de caracteres, gestionando dinámicamente la visibilidad del entorno mediante
 * el sistema de "Niebla de Guerra".
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class RenderizadorLaberinto {

    /**
     * Dibuja el estado actual del laberinto en la salida estándar.
     * <p>
     * El renderizado varía según la configuración de {@code nieblaDeGuerra}:
     * <ul>
     * <li>Si está activa: Solo muestra celdas en el radio de visión (?) o visitadas (.).</li>
     * <li>Si está desactivada: Revela la totalidad del mapa.</li>
     * </ul>
     * </p>
     *
     * @param laberinto      Estructura de datos del mapa a dibujar.
     * @param jugador        Instancia del jugador para posicionar el avatar '@'.
     * @param nieblaDeGuerra Interruptor lógico para el sistema de visibilidad limitada.
     */
    public void mostrarLaberinto(Laberinto laberinto, Jugador jugador, boolean nieblaDeGuerra) {
        System.out.println("\n=== 🗺️  LABERINTO ===");
        mostrarLeyenda();
        System.out.println("=====================================");

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);

                if (i == jugador.getPosX() && j == jugador.getPosY()) {
                    System.out.print("@ ");
                } else if (!nieblaDeGuerra) {
                    System.out.print(celda.getSimbolo() + " ");
                } else if (celda.isVisible()) {
                    System.out.print(celda.getSimbolo() + " ");
                } else if (celda.isVisitada()) {
                    System.out.print(". ");
                } else {
                    System.out.print("? ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Imprime la guía de referencia de caracteres para la vista con niebla.
     */
    private void mostrarLeyenda() {
        System.out.println("Leyenda: @ Tú | # Muro | % Muro Rojo | . Camino");
        System.out.println("         C Cristal | T Trampa | B Bomba | K Llave Exp");
        System.out.println("         L Llave | X Salida | E Energía | + Vida | ? No explorado");
    }

    /**
     * Muestra una ficha detallada con las estadísticas vitales y recursos del Hunter.
     * <p>
     * Incluye una representación gráfica de la salud mediante una barra de
     * caracteres Unicode para facilitar la lectura rápida del estado del jugador.
     * </p>
     *
     * @param jugador El objeto {@code Jugador} del cual extraer las métricas.
     */
    public void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\n=== 👤 ESTADO DEL JUGADOR ===");
        System.out.println("❤️  Vida: " + jugador.getVida() + "%");
        System.out.println("💎 Cristales: " + jugador.getCristales());
        System.out.println("🗝️  Llave: " + (jugador.isTieneLlave() ? "SÍ ✅" : "NO ❌"));
        System.out.println("📍 Posición: (" + jugador.getPosX() + ", " + jugador.getPosY() + ")");

        System.out.print("Salud: [");
        int barrasVida = jugador.getVida() / 10;
        for (int i = 0; i < 10; i++) {
            if (i < barrasVida) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + jugador.getVida() + "%");
    }

    /**
     * Renderiza el mapa completo ignorando cualquier restricción de visibilidad.
     * <p>
     * Este metodo se utiliza principalmente para la función de "Mapa Mágico" o
     * depuración, permitiendo al usuario ver la disposición total de muros y premios.
     * </p>
     *
     * @param laberinto El mapa completo a visualizar.
     */
    public void mostrarLaberintoCompleto(Laberinto laberinto) {
        System.out.println("\n=== 🗺️  VISTA COMPLETA DEL LABERINTO ===");
        mostrarLeyendaCompleta();
        System.out.println("===========================================");

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                System.out.print(celda.getSimbolo() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Imprime la guía de referencia técnica para la vista revelada del mapa.
     */
    private void mostrarLeyendaCompleta() {
        System.out.println("# Muro        % Muro Rojo   . Camino      @ Jugador");
        System.out.println("C Cristal     T Trampa      B Bomba       K Llave Exp");
        System.out.println("L Llave       X Salida      S Entrada     E Energía");
        System.out.println("+ Vida extra");
    }

    /**
     * Despliega el menú de acciones y controles del teclado disponibles en el juego.
     */
    public void mostrarControles() {
        System.out.println("\n=== 🎮 CONTROLES ===");
        System.out.println("W - Mover ↑ Arriba");
        System.out.println("A - Mover ← Izquierda");
        System.out.println("S - Mover ↓ Abajo");
        System.out.println("D - Mover → Derecha");
        System.out.println("M - Mostrar mapa completo");
        System.out.println("G - Guardar y salir");
        System.out.println("Q - Salir sin guardar");
    }
}