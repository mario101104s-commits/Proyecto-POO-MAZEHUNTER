package Main.ui.consola;

import Main.controlador.ControladorJuego;
import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.ui.util.ConsoleUtils;

import java.io.File;

/**
 * Interfaz de usuario en consola para la ejecución del juego del laberinto.
 * <p>
 * Esta clase gestiona el bucle principal de la partida, procesando las entradas
 * del teclado para el movimiento del Hunter, el uso de habilidades especiales
 * y la interacción con el sistema de guardado.
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class ConsolaLaberinto {

    /** Controlador para delegar la lógica de negocio del juego. */
    private ControladorJuego controladorJuego;

    /** Componente encargado de la representación visual del mapa en texto. */
    private RenderizadorLaberinto renderizador;

    /**
     * Construye la vista de consola para el laberinto.
     * @param controladorJuego Controlador que maneja el estado y acciones del juego.
     */
    public ConsolaLaberinto(ControladorJuego controladorJuego) {
        this.controladorJuego = controladorJuego;
        this.renderizador = new RenderizadorLaberinto();
    }

    /**
     * Inicia y gestiona el ciclo de vida de una partida activa.
     * <p>
     * Mantiene el bucle de juego mientras el estado sea {@code EN_CURSO} o el
     * usuario no decida salir manualmente.
     * </p>
     * @param juego La instancia de la partida actual a ejecutar.
     */
    public void jugarPartida(Juego juego) {
        boolean jugando = true;

        while (jugando && controladorJuego.obtenerEstadoJuego(juego) == EstadoJuego.EN_CURSO) {
            ConsoleUtils.limpiarConsola();

            // Mostrar interfaz de juego
            renderizador.mostrarLaberinto(juego.getLaberinto(), juego.getJugador(), juego.isNieblaDeGuerra());
            mostrarEstadoJugador(juego);
            mostrarControles();

            char input = ConsoleUtils.leerCaracter("Ingrese su movimiento: ");
            input = Character.toLowerCase(input);

            switch (input) {
                case 'g': // Guardar y salir
                    controladorJuego.guardarJuego(juego);
                    controladorJuego.guardarEstadisticasParciales(juego);
                    ConsoleUtils.mostrarExito("💾 Aventura guardada. ¡Hasta la próxima, Hunter!");
                    ConsoleUtils.mostrarMensaje("📊 Se registraron estadísticas parciales de tu progreso.");
                    jugando = false;
                    continue;

                case 'q': // Salir sin guardar
                    ConsoleUtils.mostrarAdvertencia("🚪 Saliendo sin guardar...");
                    ConsoleUtils.mostrarMensaje("⚠️  Tu progreso actual se perderá.");
                    ConsoleUtils.mostrarMensaje("¿Estás seguro? (s/n)");
                    char confirmacion = ConsoleUtils.leerCaracter("");

                    if (Character.toLowerCase(confirmacion) == 's') {
                        eliminarJuegoGuardado(juego.getUsuario());
                        ConsoleUtils.mostrarMensaje("❌ Progreso descartado. ¡Vuelve pronto, Hunter!");
                        jugando = false;
                    } else {
                        ConsoleUtils.mostrarMensaje("✅ Continuando la aventura...");
                        ConsoleUtils.pausar();
                    }
                    continue;

                case 'm': // Ver mapa completo
                    ConsoleUtils.limpiarConsola();
                    ConsoleUtils.mostrarMensaje("=== 🗺️  VISTA COMPLETA DEL TEMPLO ===");
                    renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
                    ConsoleUtils.pausar();
                    continue;

                case 'w':
                case 's':
                case 'a':
                case 'd':
                    Direccion direccion = obtenerDireccion(input);
                    boolean movimientoExitoso = controladorJuego.moverJugador(juego, direccion);

                    if (!movimientoExitoso) {
                        ConsoleUtils.mostrarError("🧱 ¡Camino bloqueado! Hay un muro del templo.");
                        ConsoleUtils.pausar();
                    }
                    break;

                case 'k': // Activar explosión
                    boolean explosionExitosa = controladorJuego.activarExplosion(juego);

                    if (explosionExitosa) {
                        ConsoleUtils.mostrarExito("💥 ¡EXPLOSIÓN! Muros rojos destruidos");
                        ConsoleUtils.mostrarAdvertencia("⚡ -35 vida | -1 bomba | -1 llave");
                    } else {
                        if (juego.getJugador().getFosforos() < 1) {
                            ConsoleUtils.mostrarError("❌ Necesitas una llave de explosión");
                        } else if (juego.getJugador().getBombas() < 1) {
                            ConsoleUtils.mostrarError("❌ Necesitas una bomba");
                        }
                    }
                    ConsoleUtils.pausar();
                    continue;

                default:
                    ConsoleUtils.mostrarError("❌ Movimiento inválido. Use las teclas mágicas correctas.");
                    ConsoleUtils.pausar();
                    continue;
            }

            // Verificar fin del juego
            if (controladorJuego.obtenerEstadoJuego(juego) != EstadoJuego.EN_CURSO) {
                jugando = false;
                ConsoleUtils.limpiarConsola();
                mostrarFinDelJuego(juego);
            }
        }
    }

    /**
     * Imprime en consola los atributos actuales del Hunter, incluyendo una
     * representación gráfica de la salud.
     * @param juego Sesión actual de donde obtener los datos del jugador.
     */
    private void mostrarEstadoJugador(Juego juego) {
        System.out.println("\n=== 👤 ESTADO DEL HUNTER ===");
        System.out.println("❤️  Vida: " + juego.getJugador().getVida() + "%");
        System.out.println("💎 Cristales: " + juego.getJugador().getCristales());
        System.out.println("🗝️  Llave: " + (juego.getJugador().isTieneLlave() ? "SÍ ✅" : "NO ❌"));
        System.out.println("💣 Bombas: " + juego.getJugador().getBombas());
        System.out.println("🔑 Fósforos: " + juego.getJugador().getFosforos());
        System.out.println("💀 Trampas activadas: " + juego.getTrampasActivadas());

        // Barra de vida visual
        System.out.print("Salud: [");
        int barrasVida = juego.getJugador().getVida() / 10;
        for (int i = 0; i < 10; i++) {
            if (i < barrasVida) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + juego.getJugador().getVida() + "%");
    }

    /**
     * Imprime el manual de comandos disponibles para el usuario durante la partida.
     */
    private void mostrarControles() {
        System.out.println("\n=== 🎮 CONTROLES MÁGICOS ===");
        System.out.println("W - ↑ Mover hacia arriba");
        System.out.println("A - ← Mover hacia izquierda");
        System.out.println("S - ↓ Mover hacia abajo");
        System.out.println("D - → Mover hacia derecha");
        System.out.println("M - 🗺️  Ver mapa completo del templo");
        System.out.println("K - 💥 Activar explosión (requiere bomba + llave)");
        System.out.println("G - 💾 Guardar y salir (guarda progreso)");
        System.out.println("Q - 🚪 Salir sin guardar (pierde progreso)");
        System.out.println("============================");
    }

    /**
     * Traduce la entrada de carácter del usuario a una constante de dirección.
     * @param input Tecla presionada por el usuario.
     * @return La constante {@link Direccion} correspondiente.
     */
    private Direccion obtenerDireccion(char input) {
        switch (input) {
            case 'w':
                return Direccion.ARRIBA;
            case 's':
                return Direccion.ABAJO;
            case 'a':
                return Direccion.IZQUIERDA;
            case 'd':
                return Direccion.DERECHA;
            default:
                return Direccion.ARRIBA;
        }
    }

    /**
     * Elimina el archivo de persistencia física de una partida guardada.
     * @param usuario Email del usuario cuyo archivo de guardado será eliminado.
     */
    private void eliminarJuegoGuardado(String usuario) {
        try {
            String archivoJuego = "datos/juegos/" + usuario + ".json";
            File archivo = new File(archivoJuego);

            if (archivo.exists()) {
                boolean eliminado = archivo.delete();
                if (eliminado) {
                    System.out.println("✅ Juego guardado eliminado: " + archivoJuego);
                } else {
                    System.err.println("❌ No se pudo eliminar el juego guardado");
                }
            }
        } catch (Exception e) {
            System.err.println("Error eliminando juego guardado: " + e.getMessage());
        }
    }

    /**
     * Gestiona la pantalla de finalización de partida, mostrando si el usuario
     * ganó o perdió y el resumen detallado de su desempeño.
     * @param juego Sesión finalizada.
     */
    private void mostrarFinDelJuego(Juego juego) {
        ResultadoJuego resultado = controladorJuego.terminarJuego(juego);

        if (controladorJuego.obtenerEstadoJuego(juego) == EstadoJuego.GANADO) {
            ConsoleUtils.mostrarMensaje("=== 🏆 ¡VICTORIA GLORIOSA! ===");
            ConsoleUtils.mostrarMensaje("🎉 ¡Has escapado del Templo Perdido, valiente Hunter!");
            ConsoleUtils.mostrarMensaje("✨ Los cristales de poder brillan con tu éxito.");
        } else {
            ConsoleUtils.mostrarMensaje("=== 💀 FIN DE LA AVENTURA ===");
            ConsoleUtils.mostrarMensaje("El templo ha reclamado a otro Hunter...");
            ConsoleUtils.mostrarMensaje("💫 No te rindas, la próxima vez lo lograrás.");
        }

        ConsoleUtils.mostrarMensaje("\n📊 ESTADÍSTICAS FINALES:");
        ConsoleUtils.mostrarMensaje(resultado.toString());
        ConsoleUtils.pausar();
    }
}