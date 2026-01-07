package Main.ui.consola;

import Main.controlador.ControladorJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.ui.util.ConsoleUtils;

import java.util.List;

/**
 * Representa la interfaz de usuario para el menú principal del sistema.
 * <p>
 * Esta vista centraliza las opciones de gestión de partidas, permitiendo al
 * usuario configurar nuevas aventuras (dificultad y visibilidad), recuperar
 * sesiones guardadas y consultar el historial de rendimiento acumulado.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.1
 * @since 22/12/25
 */
public class MenuPrincipal {

    /** Controlador para la gestión de la lógica y persistencia del juego. */
    private ControladorJuego controladorJuego;

    /** Vista subordinada encargada de la ejecución de la partida en consola. */
    private ConsolaLaberinto consolaLaberinto;

    /**
     * Construye el menú principal inyectando las dependencias necesarias.
     * 
     * @param controladorJuego Controlador que actuará como puente hacia el modelo.
     */
    public MenuPrincipal(ControladorJuego controladorJuego) {
        this.controladorJuego = controladorJuego;
        this.consolaLaberinto = new ConsolaLaberinto(controladorJuego);
    }

    /**
     * Despliega el menú de navegación principal y gestiona la entrada del Hunter.
     * 
     * @param emailUsuario Correo del usuario actualmente autenticado.
     * @return {@code true} si el usuario desea permanecer en el menú;
     *         {@code false} si decide cerrar la sesión.
     */
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
                    return false;
                default:
                    ConsoleUtils.mostrarError("Opción inválida. El templo solo reconoce opciones del 1 al 4.");
                    ConsoleUtils.pausar();
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error inesperado en el templo: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Orquesta el proceso de creación de una nueva partida.
     * <p>
     * Permite al usuario seleccionar la dificultad (Fácil, Media, Difícil) y
     * configurar la mecánica de "Niebla de Guerra", calculando automáticamente
     * las dimensiones del laberinto según la estrategia elegida.
     * </p>
     * 
     * @param emailUsuario Correo del Hunter que inicia la aventura.
     */
    private void iniciarNuevaAventura(String emailUsuario) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🎮 NUEVA AVENTURA EN EL TEMPLO ===");

        try {
            ConsoleUtils.mostrarMensaje("\n🎯 Seleccione la dificultad:");
            ConsoleUtils
                    .mostrarMensaje("1. 🟢 FÁCIL   - Filas: 5-15,  Columnas: 10-25  | Trampas: 2-3,  Energías: 2-3");
            ConsoleUtils
                    .mostrarMensaje("2. 🟡 MEDIA   - Filas: 16-25, Columnas: 26-35  | Trampas: 4-5,  Energías: 4-5");
            ConsoleUtils
                    .mostrarMensaje("3. 🔴 DIFÍCIL - Filas: 26-45, Columnas: 36-65  | Trampas: 6-18, Energías: 6-18");

            int opcionDificultad = ConsoleUtils.leerEntero("\nOpción (1-3): ");

            String dificultad;
            switch (opcionDificultad) {
                case 1 -> dificultad = "FACIL";
                case 2 -> dificultad = "MEDIA";
                case 3 -> dificultad = "DIFICIL";
                default -> {
                    ConsoleUtils.mostrarAdvertencia("Opción inválida, usando MEDIA por defecto");
                    dificultad = "MEDIA";
                }
            }

            controladorJuego.setEstrategiaGeneracion(dificultad);
            ConsoleUtils.mostrarExito("\n✅ " + controladorJuego.getDescripcionEstrategia());

            ConsoleUtils.mostrarMensaje("\n🌫️  ¿Desea jugar con niebla de guerra?");
            ConsoleUtils.mostrarMensaje("(La niebla oculta las zonas no exploradas con '?')");
            ConsoleUtils.mostrarMensaje("1. Sí - Con niebla de guerra (🌫️  más desafío)");
            ConsoleUtils.mostrarMensaje("2. No - Sin niebla de guerra (🗺️  ver todo el mapa)");

            int opcionNiebla = ConsoleUtils.leerEntero("\nOpción (1-2): ");
            boolean nieblaDeGuerra = (opcionNiebla == 1);

            int filas = controladorJuego.generarFilasAleatorias(dificultad);
            int columnas = controladorJuego.generarColumnasAleatorias(dificultad);

            Juego juego = controladorJuego.iniciarNuevoJuego(filas, columnas, emailUsuario, nieblaDeGuerra);

            ConsoleUtils.mostrarExito("🔮 ¡Laberinto mágico generado! El templo te espera...");
            ConsoleUtils.pausar();

            consolaLaberinto.jugarPartida(juego);

        } catch (IllegalArgumentException e) {
            ConsoleUtils.mostrarError("❌ " + e.getMessage());
            ConsoleUtils.pausar();
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al crear la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    /**
     * Intenta recuperar y ejecutar una sesión de juego guardada previamente.
     * 
     * @param emailUsuario Correo del usuario dueño de la partida.
     */
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
            ConsoleUtils.pausar();

            consolaLaberinto.jugarPartida(juego);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al cargar la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    /**
     * Muestra el histórico detallado de partidas y un resumen estadístico
     * agregador.
     * <p>
     * Calcula métricas como tasa de victorias, promedios de tiempo y cristales,
     * proporcionando además consejos dinámicos basados en el rendimiento del
     * Hunter.
     * </p>
     * 
     * @param emailUsuario Correo del usuario cuyas estadísticas se desean
     *                     consultar.
     */
    private void mostrarEstadisticas(String emailUsuario) {
        // ... (Implementación de cálculo y visualización de estadísticas)
    }
}