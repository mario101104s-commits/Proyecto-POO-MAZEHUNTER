package Main.ui.consola;

import Main.controlador.ControladorJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.ui.util.ConsoleUtils;

import java.util.List;

// Vista para el menú principal del juego
public class MenuPrincipal {
    private ControladorJuego controladorJuego;
    private ConsolaLaberinto consolaLaberinto;

    public MenuPrincipal(ControladorJuego controladorJuego) {
        this.controladorJuego = controladorJuego;
        this.consolaLaberinto = new ConsolaLaberinto(controladorJuego);
    }

    // Muestra el menú principal y retorna true para continuar, false para cerrar
    // sesión
    public boolean mostrarMenu(String emailUsuario) {
        try {
            ConsoleUtils.limpiarConsola();
            System.out.println("=== 🏰 SALA PRINCIPAL DEL TEMPLO ===");
            System.out.println("Hunter: " + emailUsuario);
            System.out.println("1. 🎮 Iniciar nueva aventura");
            System.out.println("2. 📂 Cargar aventura guardada");
            System.out.println("3. 📊 Ver anales del templo (estadísticas)");
            System.out.println("4. 🚪 Cerrar sesión");
            System.out.println("======================================");

            int opcion = ConsoleUtils.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    iniciarNuevaAventura(emailUsuario);
                    break;
                case 2:
                    cargarAventuraExistente(emailUsuario);
                    break;
                case 3:
                    mostrarEstadisticas(emailUsuario);
                    break;
                case 4:
                    ConsoleUtils.mostrarMensaje("🔒 Sesión cerrada. ¡Vuelve pronto, Hunter!");
                    return false; // Cerrar sesión
                default:
                    ConsoleUtils.mostrarError("Opción inválida. El templo solo reconoce opciones del 1 al 4.");
                    ConsoleUtils.pausar();
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error inesperado en el templo: " + e.getMessage());
            e.printStackTrace();
        }
        return true; // Continuar en el menú
    }

    // Inicia una nueva aventura
    private void iniciarNuevaAventura(String emailUsuario) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🎮 NUEVA AVENTURA EN EL TEMPLO ===");

        try {
            ConsoleUtils.mostrarMensaje("🏗️  Configuración del laberinto mágico:");
            int filas = ConsoleUtils.leerEntero("Filas (8-15 recomendado): ");
            int columnas = ConsoleUtils.leerEntero("Columnas (8-15 recomendado): ");

            // Validar dimensiones
            if (!controladorJuego.validarDimensiones(filas, columnas)) {
                ConsoleUtils.mostrarError("❌ El laberinto debe ser entre 5x5 y 20x20.");
                ConsoleUtils.pausar();
                return;
            }

            Juego juego = controladorJuego.iniciarNuevoJuego(filas, columnas, emailUsuario);
            ConsoleUtils.mostrarExito("🔮 ¡Laberinto mágico generado! El templo te espera...");
            ConsoleUtils.pausar();

            consolaLaberinto.jugarPartida(juego);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al crear la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    // Carga una aventura guardada
    private void cargarAventuraExistente(String emailUsuario) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📂 CARGAR AVENTURA GUARDADA ===");

        try {
            Juego juego = controladorJuego.cargarJuegoGuardado(emailUsuario);
            if (juego == null) {
                ConsoleUtils.mostrarError("❌ No hay aventuras guardadas. Inicia una nueva aventura.");
                ConsoleUtils.pausar();
                return;
            }

            ConsoleUtils.mostrarExito("✅ ¡Aventura cargada con éxito!");
            ConsoleUtils.mostrarMensaje(
                    "📍 Posición actual: (" + juego.getJugador().getPosX() + ", " + juego.getJugador().getPosY() + ")");
            ConsoleUtils.mostrarMensaje("💎 Cristales: " + juego.getJugador().getCristales());
            ConsoleUtils.mostrarMensaje("❤️  Vida: " + juego.getJugador().getVida() + "%");
            ConsoleUtils.mostrarMensaje("🗝️  Llave: " + (juego.getJugador().isTieneLlave() ? "SÍ" : "NO"));
            ConsoleUtils.pausar();

            consolaLaberinto.jugarPartida(juego);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al cargar la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    // Muestra las estadísticas del jugador
    private void mostrarEstadisticas(String emailUsuario) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📊 ANALES DEL TEMPLO ===");
        ConsoleUtils.mostrarMensaje("Estadísticas de: " + emailUsuario);
        ConsoleUtils.mostrarMensaje("=================================");

        try {
            List<EstadisticasJuego> estadisticas = controladorJuego.obtenerEstadisticas(emailUsuario);

            if (estadisticas.isEmpty()) {
                ConsoleUtils.mostrarMensaje("📝 Aún no hay aventuras registradas en tu nombre.");
                ConsoleUtils.mostrarMensaje("🎮 ¡Completa tu primera aventura para dejar tu marca en el templo!");
                ConsoleUtils.mostrarMensaje("💎 Recolecta cristales, evita trampas y encuentra la llave para escapar.");
            } else {
                int partidasGanadas = 0;
                int totalCristales = 0;
                int totalTrampas = 0;
                long totalTiempo = 0;

                ConsoleUtils.mostrarMensaje("📜 HISTORIAL DE AVENTURAS:");
                ConsoleUtils.mostrarMensaje("==========================");

                for (int i = 0; i < estadisticas.size(); i++) {
                    EstadisticasJuego stats = estadisticas.get(i);
                    String resultado = stats.isGanado() ? "🏆 VICTORIA" : "💀 DERROTA";
                    String emoji = stats.isGanado() ? "✅" : "❌";

                    ConsoleUtils.mostrarMensaje(emoji + " Aventura " + (i + 1) + " - " + resultado);
                    ConsoleUtils.mostrarMensaje("   📏 Laberinto: " + stats.getTamanioLaberinto());
                    ConsoleUtils.mostrarMensaje("   💎 Cristales: " + stats.getCristalesRecolectados());
                    ConsoleUtils.mostrarMensaje("   💀 Trampas: " + stats.getTrampasActivadas());
                    ConsoleUtils.mostrarMensaje("   ❤️  Vida final: " + stats.getVidaRestante() + "%");
                    ConsoleUtils.mostrarMensaje("   ⏱️  Tiempo: " + stats.getTiempoSegundos() + " segundos");
                    ConsoleUtils.mostrarMensaje("   📅 Fecha: " + stats.getFechaFormateada());
                    ConsoleUtils.mostrarMensaje("   ─────────────────────────");

                    if (stats.isGanado())
                        partidasGanadas++;
                    totalCristales += stats.getCristalesRecolectados();
                    totalTrampas += stats.getTrampasActivadas();
                    totalTiempo += stats.getTiempoSegundos();
                }

                // Calcular promedios
                double promedioCristales = (double) totalCristales / estadisticas.size();
                double promedioTrampas = (double) totalTrampas / estadisticas.size();
                double promedioTiempo = (double) totalTiempo / estadisticas.size();
                double tasaVictorias = (partidasGanadas * 100.0) / estadisticas.size();

                ConsoleUtils.mostrarMensaje("\n📈 RESUMEN DEL HUNTER:");
                ConsoleUtils.mostrarMensaje("======================");
                ConsoleUtils.mostrarMensaje("🎯 Partidas totales: " + estadisticas.size());
                ConsoleUtils.mostrarMensaje("✅ Victorias: " + partidasGanadas);
                ConsoleUtils.mostrarMensaje("❌ Derrotas: " + (estadisticas.size() - partidasGanadas));
                ConsoleUtils.mostrarMensaje("📊 Tasa de victorias: " + String.format("%.1f%%", tasaVictorias));
                ConsoleUtils.mostrarMensaje("💎 Cristales totales: " + totalCristales);
                ConsoleUtils.mostrarMensaje("📦 Cristales por partida: " + String.format("%.1f", promedioCristales));
                ConsoleUtils.mostrarMensaje("💀 Trampas totales: " + totalTrampas);
                ConsoleUtils.mostrarMensaje("⚡ Tiempo total: " + totalTiempo + " segundos");
                ConsoleUtils
                        .mostrarMensaje("⏱️  Tiempo promedio: " + String.format("%.1f", promedioTiempo) + " segundos");

                // Consejos basados en el desempeño
                ConsoleUtils.mostrarMensaje("\n💡 CONSEJOS DEL TEMPLO:");
                if (tasaVictorias >= 80) {
                    ConsoleUtils.mostrarMensaje("🌟 ¡Eres un Maestro Hunter! El templo teme tu nombre.");
                } else if (tasaVictorias >= 50) {
                    ConsoleUtils.mostrarMensaje("💪 Buen desempeño. Sigue así, Hunter experimentado.");
                } else if (tasaVictorias > 0) {
                    ConsoleUtils.mostrarMensaje("📚 Aprendiendo los caminos del templo. Sigue practicando.");
                } else {
                    ConsoleUtils.mostrarMensaje("🎯 El templo es traicionero. Enfócate en encontrar la llave primero.");
                }
            }

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error inesperado: " + e.getMessage());
            ConsoleUtils.mostrarMensaje("🔧 El sistema de estadísticas se está inicializando...");
            ConsoleUtils.mostrarMensaje("💡 Juega una partida para generar tus primeras estadísticas.");
        }

        ConsoleUtils.mostrarMensaje("\n🎮 ¿Listo para otra aventura?");
        ConsoleUtils.pausar();
    }
}
