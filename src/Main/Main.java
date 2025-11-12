package Main;

import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Constantes.TipoCelda;
import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Laberinto;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.ui.consola.RenderizadorLaberinto;
import Main.ui.util.ConsoleUtils;
import Main.servicio.Interfaces.ServicioJuego;
import Main.servicio.Implementaciones.ServicioJuegoImpl;


public class Main {
    private static ServicioJuego servicioJuego = new ServicioJuegoImpl(new PersistenciaJASON());
    private static RenderizadorLaberinto renderizador = new RenderizadorLaberinto();

    public static void main(String[] args) {
        try {
            boolean ejecutando = true;
            while (ejecutando) {
                ConsoleUtils.limpiarConsola();
                mostrarMenuPrincipal();

                int opcion = ConsoleUtils.leerEntero("Seleccione una opción: ");

                switch (opcion) {
                    case 1:
                        jugarDemoCompleta();
                        break;
                    case 2:
                        probarGeneracionLaberinto();
                        break;
                    case 3:
                        verLaberintoCompleto();
                        break;
                    case 4:
                        ejecutando = false;
                        break;
                    default:
                        ConsoleUtils.mostrarError("Opción inválida");
                        ConsoleUtils.pausar();
                }
            }

            ConsoleUtils.mostrarMensaje("¡Gracias por jugar! 🎮");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("=== 🏰 MAZE HUNTER , LABERINTO MAGICO ===");
        System.out.println("1. 🎮 Jugar demo completa");
        System.out.println("2. 🔧 Probar generación de laberinto");
        System.out.println("3. 👀 Ver laberinto completo");
        System.out.println("4. 🚪 Salir");
        System.out.println("=============================================");
    }

    private static void jugarDemoCompleta() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🎮 DEMO COMPLETA DEL JUEGO ===");

        // PREGUNTAR SI QUIERE CARGAR JUEGO GUARDADO O NUEVO
        System.out.println("1. 🆕 Juego nuevo");
        System.out.println("2. 📂 Cargar juego guardado");
        int opcionInicial = ConsoleUtils.leerEntero("Seleccione opción: ");

        Juego juego = null;

        try {
            if (opcionInicial == 2) {
                // Cargar juego guardado
                juego = servicioJuego.cargarJuegoGuardado("demo");
                if (juego == null) {
                    ConsoleUtils.mostrarError("No hay juego guardado. Creando nuevo juego...");
                    opcionInicial = 1;
                } else {
                    ConsoleUtils.mostrarExito("¡Juego cargado exitosamente!");
                    ConsoleUtils.mostrarMensaje("Posición: (" + juego.getJugador().getPosX() + ", " + juego.getJugador().getPosY() + ")");
                    ConsoleUtils.mostrarMensaje("Cristales: " + juego.getJugador().getCristales() + ", Trampas: " + juego.getTrampasActivadas());
                    ConsoleUtils.pausar();
                }
            }

            if (opcionInicial == 1) {
                int filas = ConsoleUtils.leerEntero("Filas del laberinto (recomendado 8-12): ");
                int columnas = ConsoleUtils.leerEntero("Columnas del laberinto (recomendado 8-12): ");
                juego = servicioJuego.iniciarNuevoJuego(filas, columnas, "demo");
                ConsoleUtils.mostrarExito("Laberinto generado exitosamente!");

                // Mostrar laberinto completo una vez al inicio
                ConsoleUtils.mostrarMensaje("\n--- Vista completa inicial ---");
                renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
                ConsoleUtils.mostrarMensaje("\n¡Recuerda: necesitas la llave (L) para salir por la salida (X)!");
                ConsoleUtils.pausar();
            }

            if (juego == null) {
                ConsoleUtils.mostrarError("No se pudo crear el juego");
                return;
            }

            // Bucle principal del juego
            boolean jugando = true;
            while (jugando && juego.getEstado() == EstadoJuego.EN_CURSO) {
                ConsoleUtils.limpiarConsola();

                // Mostrar estado actual
                renderizador.mostrarLaberinto(juego.getLaberinto(), juego.getJugador());
                renderizador.mostrarEstadoJugador(juego.getJugador());
                ConsoleUtils.mostrarMensaje("💀 Trampas activadas: " + juego.getTrampasActivadas());
                renderizador.mostrarControles();

                char input = ConsoleUtils.leerCaracter("Ingrese movimiento (W/A/S/D): ");

                Direccion direccion = null;
                boolean salir = false;
                boolean mostrarMapa = false;

                switch (Character.toLowerCase(input)) {
                    case 'w':
                        direccion = Direccion.ARRIBA;
                        break;
                    case 's':
                        direccion = Direccion.ABAJO;
                        break;
                    case 'a':
                        direccion = Direccion.IZQUIERDA;
                        break;
                    case 'd':
                        direccion = Direccion.DERECHA;
                        break;
                    case 'm':
                        mostrarMapa = true;
                        break;
                    case 'g':
                        ResultadoJuego resultadoParcial = servicioJuego.guardarEstadisticasParciales(juego);
                        ConsoleUtils.mostrarExito("Juego guardado. Estadísticas parciales:");
                        ConsoleUtils.mostrarMensaje(resultadoParcial.toString());
                        salir = true;
                        break;
                    case 'q':
                        ConsoleUtils.mostrarAdvertencia("Saliendo sin guardar...");
                        salir = true;
                        break;
                    default:
                        ConsoleUtils.mostrarError("Movimiento inválido. Use W, A, S, D, M, G o Q");
                        ConsoleUtils.pausar();
                        continue;
                }

                if (salir) {
                    break;
                }

                if (mostrarMapa) {
                    ConsoleUtils.limpiarConsola();
                    ConsoleUtils.mostrarMensaje("=== 🗺️  MAPA COMPLETO ===");
                    renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
                    ConsoleUtils.pausar();
                    continue;
                }

                // Intentar mover al jugador
                boolean movimientoExitoso = servicioJuego.moverJugador(juego, direccion);

                if (!movimientoExitoso) {
                    ConsoleUtils.mostrarError("¡Movimiento inválido! Hay un muro en esa dirección.");
                    ConsoleUtils.pausar();
                }

                // Verificar si el juego terminó
                if (juego.getEstado() != EstadoJuego.EN_CURSO) {
                    jugando = false;
                    ConsoleUtils.limpiarConsola();
                    renderizador.mostrarLaberinto(juego.getLaberinto(), juego.getJugador());
                    renderizador.mostrarEstadoJugador(juego.getJugador());

                    ResultadoJuego resultado = servicioJuego.terminarJuego(juego);
                    ConsoleUtils.mostrarMensaje("\n=== 🏁 FIN DEL JUEGO ===");
                    ConsoleUtils.mostrarMensaje(resultado.toString());
                }
            }

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en el juego: " + e.getMessage());
            e.printStackTrace();
        }

        ConsoleUtils.pausar();
    }

    private static void probarGeneracionLaberinto() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🔧 PRUEBA DE GENERACIÓN ===");

        int filas = ConsoleUtils.leerEntero("Filas del laberinto: ");
        int columnas = ConsoleUtils.leerEntero("Columnas del laberinto: ");

        try {
            Juego juego = servicioJuego.iniciarNuevoJuego(filas, columnas, "test");

            ConsoleUtils.mostrarExito("Laberinto " + filas + "x" + columnas + " generado exitosamente!");

            // Mostrar información del laberinto
            ConsoleUtils.mostrarMensaje("Entrada en: (" + juego.getJugador().getPosX() +
                    ", " + juego.getJugador().getPosY() + ")");

            // Contar elementos
            Laberinto laberinto = juego.getLaberinto();
            int cristales = 0, trampas = 0, llaves = 0, energia = 0, vida = 0;

            for (int i = 0; i < laberinto.getFilas(); i++) {
                for (int j = 0; j < laberinto.getColumnas(); j++) {
                    TipoCelda tipo = laberinto.getCelda(i, j).getTipo();
                    switch (tipo) {
                        case CRISTAL: cristales++; break;
                        case TRAMPA: trampas++; break;
                        case LLAVE: llaves++; break;
                        case ENERGIA: energia++; break;
                        case VIDA: vida++; break;
                    }
                }
            }

            ConsoleUtils.mostrarMensaje("\n📊 Estadísticas del laberinto:");
            ConsoleUtils.mostrarMensaje("   💎 Cristales: " + cristales);
            ConsoleUtils.mostrarMensaje("   💀 Trampas: " + trampas);
            ConsoleUtils.mostrarMensaje("   🗝️  Llaves: " + llaves);
            ConsoleUtils.mostrarMensaje("   ⚡ Energía: " + energia);
            ConsoleUtils.mostrarMensaje("   ❤️  Vida extra: " + vida);

            // Mostrar el laberinto completo
            renderizador.mostrarLaberintoCompleto(laberinto);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error: " + e.getMessage());
        }

        ConsoleUtils.pausar();
    }

    private static void verLaberintoCompleto() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 👀 VISUALIZACIÓN DE LABERINTO ===");

        try {
            int filas = ConsoleUtils.leerEntero("Filas: ");
            int columnas = ConsoleUtils.leerEntero("Columnas: ");

            Juego juego = servicioJuego.iniciarNuevoJuego(filas, columnas, "visualizacion");
            renderizador.mostrarLaberintoCompleto(juego.getLaberinto());

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error: " + e.getMessage());
        }

        ConsoleUtils.pausar();
    }
}

