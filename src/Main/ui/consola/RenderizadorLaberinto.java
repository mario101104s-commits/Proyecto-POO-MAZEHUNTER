package Main.ui.consola;

import Main.modelo.Dominio.Celda;
import Main.modelo.Dominio.Jugador;
import Main.modelo.Dominio.Laberinto;

/**
 * Clase de utilidad para la Interfaz de Usuario (UI) que se encarga de renderizar
 * visualmente el estado del laberinto, la información del jugador y los controles
 * en la consola.
 * <p>
 * Utiliza los símbolos de las celdas y la posición del jugador para crear la vista
 * del juego, aplicando el concepto de campo de visión limitado.
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 2025-11-15
 */
public class RenderizadorLaberinto {
    /**
     * Muestra el laberinto en la consola, aplicando el campo de visión (solo celdas
     * visibles o visitadas) y marcando la posición actual del jugador (@).
     *
     * @param laberinto El objeto {@code Laberinto} a renderizar.
     * @param jugador El objeto {@code Jugador} para determinar su posición.
     */

    public void mostrarLaberinto(Laberinto laberinto, Jugador jugador) {
        System.out.println("\n=== 🗺️  LABERINTO ===");
        mostrarLeyenda();
        System.out.println("=====================================");

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);

                // Si el jugador está en esta celda, mostrar jugador
                if (i == jugador.getPosX() && j == jugador.getPosY()) {
                    System.out.print("@ ");
                } else if (celda.isVisible()) {
                    // Mostrar celda visible
                    System.out.print(celda.getSimbolo() + " ");
                } else if (celda.isVisitada()) {
                    // Mostrar celda visitada pero no visible actualmente
                    System.out.print(". ");
                } else {
                    // Celda no explorada
                    System.out.print("? ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Muestra la leyenda de símbolos para la vista de juego con visión limitada.
     * * Metodo auxiliar privado.
     */
    private void mostrarLeyenda() {
        System.out.println("Leyenda: @ Tú | # Muro | . Camino | C Cristal | T Trampa");
        System.out.println("         L Llave | X Salida | E Energía | + Vida | ? No explorado");
    }

    /**
     * Muestra el estado actual del jugador, incluyendo vida (con una barra visual),
     * cristales, posesión de la llave y posición.
     *
     * @param jugador El objeto {@code Jugador} cuyo estado se va a mostrar.
     */
    public void mostrarEstadoJugador(Jugador jugador) {
        System.out.println("\n=== 👤 ESTADO DEL JUGADOR ===");
        System.out.println("❤️  Vida: " + jugador.getVida() + "%");
        System.out.println("💎 Cristales: " + jugador.getCristales());
        System.out.println("🗝️  Llave: " + (jugador.isTieneLlave() ? "SÍ ✅" : "NO ❌"));
        System.out.println("📍 Posición: (" + jugador.getPosX() + ", " + jugador.getPosY() + ")");

        // Mostrar barra de vida visual
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
     * Muestra la vista completa del laberinto, sin aplicar restricciones de visibilidad.
     * * Útil para la función de "mapa completo".
     *
     * @param laberinto El objeto {@code Laberinto} a renderizar.
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
     * Muestra la leyenda de símbolos para la vista completa del laberinto.
     * * Metodo auxiliar privado.
     */
    private void mostrarLeyendaCompleta() {
        System.out.println("# Muro        . Camino      @ Jugador     C Cristal");
        System.out.println("T Trampa      L Llave       X Salida      S Entrada");
        System.out.println("E Energía     + Vida extra");
    }

    /**
     * Imprime en la consola la lista de comandos disponibles para el jugador.
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
