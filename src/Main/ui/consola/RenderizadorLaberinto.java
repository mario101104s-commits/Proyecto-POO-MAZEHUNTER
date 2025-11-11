package Main.ui.consola;

import Main.modelo.Dominio.Celda;
import Main.modelo.Dominio.Jugador;
import Main.modelo.Dominio.Laberinto;

public class RenderizadorLaberinto {

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

    private void mostrarLeyenda() {
        System.out.println("Leyenda: @ Tú | # Muro | . Camino | C Cristal | T Trampa");
        System.out.println("         L Llave | X Salida | E Energía | + Vida | ? No explorado");
    }

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

    private void mostrarLeyendaCompleta() {
        System.out.println("# Muro        . Camino      @ Jugador     C Cristal");
        System.out.println("T Trampa      L Llave       X Salida      S Entrada");
        System.out.println("E Energía     + Vida extra");
    }

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
