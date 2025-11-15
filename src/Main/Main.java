package Main;

import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
import Main.modelo.Dominio.EstadisticasJuego;
import Main.modelo.Dominio.Juego;
import Main.modelo.Dominio.Usuario;
import Main.modelo.Transferencia.ResultadoJuego;
import Main.servicio.Implementaciones.CifradorImpl;
import Main.servicio.Implementaciones.PersistenciaJASON;
import Main.servicio.Implementaciones.ServicioJuegoImpl;
import Main.servicio.Implementaciones.ServicioUsuarioImpl;
import Main.servicio.Interfaces.Cifrador;
import Main.servicio.Interfaces.ServicioJuego;
import Main.servicio.Interfaces.ServicioUsuario;
import Main.ui.consola.RenderizadorLaberinto;
import Main.ui.util.ConsoleUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {
    private static PersistenciaJASON persistencia = new PersistenciaJASON();
    private static Cifrador cifrador = new CifradorImpl();
    private static ServicioJuego servicioJuego = new ServicioJuegoImpl(persistencia);
    private static ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(persistencia);
    private static final RenderizadorLaberinto renderizador = new RenderizadorLaberinto();
    private static String usuarioAutenticadoEmail = null;

    public static void main(String[] args) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🏰 MAZE HUNTER - EL TEMPLO PERDIDO ===");
        ConsoleUtils.mostrarMensaje("Bienvenido, valiente Hunter. El templo ancestral te espera...");

        try {
            persistencia.cargarUsuarios();
            persistencia.cargarEstadisticas();

            boolean ejecutando = true;
            while (ejecutando) {
                if (usuarioAutenticadoEmail == null) {
                    mostrarMenuAutenticacion();
                    ejecutando = manejarMenuAutenticacion();
                } else {
                    ejecutando = mostrarMenuPrincipal();
                }
            }
            ConsoleUtils.mostrarMensaje("🎮 ¡Que los cristales te guíen, Hunter! Hasta la próxima aventura.");
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error crítico en el sistema del templo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ===== SISTEMA DE AUTENTICACIÓN =====
    private static void mostrarMenuAutenticacion() {
        ConsoleUtils.limpiarConsola();
        System.out.println("=== 🔐 ACCESO AL TEMPLO PERDIDO ===");
        System.out.println("1. 🗝️  Iniciar sesión");
        System.out.println("2. 📝 Registrar nuevo Hunter");
        System.out.println("3. 🔑 Recuperar contraseña mágica");
        System.out.println("4. 🚪 Abandonar el templo");
        System.out.println("====================================");
    }

    private static boolean manejarMenuAutenticacion() {
        int opcion = ConsoleUtils.leerEntero("Seleccione una opción: ");
        try {
            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 2:
                    registrarUsuario();
                    break;
                case 3:
                    recuperarContrasenia();
                    break;
                case 4:
                    return false;
                default:
                    ConsoleUtils.mostrarError("Opción inválida. El templo solo reconoce opciones del 1 al 4.");
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en la operación: " + e.getMessage());
        }
        ConsoleUtils.pausar();
        return true;
    }

    private static void iniciarSesion() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🗝️  INGRESO AL TEMPLO ===");
        String email = ConsoleUtils.leerCadena("Email del Hunter: ");
        String contrasenia = ConsoleUtils.leerCadena("Contraseña mágica: ");

        try {
            Usuario usuario = persistencia.cargarUsuario(email);

            if (usuario == null) {
                ConsoleUtils.mostrarError("❌ Hunter no encontrado en los archivos ancestrales.");
                return;
            }

            String contraseniaAlmacenadaCifrada = usuario.getContraseniaCifrada();
            String contraseniaDescifrada = cifrador.descifrarContrasenia(contraseniaAlmacenadaCifrada);

            if (contraseniaDescifrada == null) {
                ConsoleUtils.mostrarError("❌ Error al validar las runas mágicas.");
                return;
            }

            if (contraseniaDescifrada.equals(contrasenia)) {
                usuarioAutenticadoEmail = email;
                ConsoleUtils.mostrarExito("🎉 ¡Bienvenido de vuelta, Hunter " + email + "! El templo te recibe.");
            } else {
                ConsoleUtils.mostrarError("❌ Contraseña incorrecta. Las puertas del templo permanecen selladas.");
            }

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error mágico al iniciar sesión: " + e.getMessage());
        }
    }

    private static boolean validarContrasenia(String password) {
        if (password == null || password.length() < 8) {
            ConsoleUtils.mostrarError("❌ La contraseña mágica debe tener al menos 8 caracteres.");
            return false;
        }

        boolean tieneMayuscula = password.matches(".*[A-Z].*");
        boolean tieneEspecial = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        if (!tieneMayuscula) {
            ConsoleUtils.mostrarError("❌ La contraseña debe contener al menos una letra mayúscula.");
            return false;
        }
        if (!tieneEspecial) {
            ConsoleUtils.mostrarError("❌ La contraseña debe contener al menos un carácter especial.");
            return false;
        }

        return true;
    }

    private static void registrarUsuario() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📝 REGISTRO DE NUEVO HUNTER ===");
        String email = ConsoleUtils.leerCadena("Email del nuevo Hunter: ");
        String password = ConsoleUtils.leerCadena("Contraseña mágica: ");
        String confirmPassword = ConsoleUtils.leerCadena("Confirmar contraseña mágica: ");

        try {
            // Validación de email mejorada
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                ConsoleUtils.mostrarError("❌ Formato de email inválido. Ejemplo: hunter@templo.com");
                return;
            }

            if (persistencia.existeUsuario(email)) {
                ConsoleUtils.mostrarError("❌ Este Hunter ya está registrado en los anales del templo.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                ConsoleUtils.mostrarError("❌ Las contraseñas mágicas no coinciden.");
                return;
            }

            if (!validarContrasenia(password)) {
                return;
            }

            String contraseniaCifrada = cifrador.cifrarContrasenia(password);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String fechaRegistro = dtf.format(LocalDateTime.now());

            Usuario nuevoUsuario = new Usuario(email, contraseniaCifrada, fechaRegistro);
            persistencia.guardarUsuario(nuevoUsuario);

            ConsoleUtils.mostrarExito("🎉 ¡Hunter registrado con éxito! Ya puede acceder al templo.");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en el registro: " + e.getMessage());
        }
    }

    private static void recuperarContrasenia() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🔑 RECUPERACIÓN DE CONTRASEÑA MÁGICA ===");

        String email = ConsoleUtils.leerCadena("Email del Hunter: ");

        try {
            Usuario usuario = persistencia.cargarUsuario(email);

            if (usuario == null) {
                ConsoleUtils.mostrarError("❌ Hunter no encontrado en los archivos del templo.");
                return;
            }

            ConsoleUtils.mostrarMensaje("Hunter encontrado. Crea una nueva contraseña mágica.");
            String newPassword = ConsoleUtils.leerCadena("Nueva contraseña mágica: ");
            String confirmNewPassword = ConsoleUtils.leerCadena("Confirmar nueva contraseña: ");

            if (!newPassword.equals(confirmNewPassword)) {
                ConsoleUtils.mostrarError("❌ Las nuevas contraseñas no coinciden.");
                return;
            }

            if (!validarContrasenia(newPassword)) {
                return;
            }

            String contraseniaCifrada = cifrador.cifrarContrasenia(newPassword);
            usuario.setContraseniaCifrada(contraseniaCifrada);
            persistencia.actualizarUsuario(usuario);

            ConsoleUtils.mostrarExito("✅ ¡Contraseña restablecida! Las puertas del templo te esperan.");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al recuperar la contraseña: " + e.getMessage());
        }
    }

    // ===== MENÚ PRINCIPAL Y SISTEMA DE JUEGO =====
    private static boolean mostrarMenuPrincipal() {
        try {
            ConsoleUtils.limpiarConsola();
            System.out.println("=== 🏰 SALA PRINCIPAL DEL TEMPLO ===");
            System.out.println("Hunter: " + usuarioAutenticadoEmail);
            System.out.println("1. 🎮 Iniciar nueva aventura");
            System.out.println("2. 📂 Cargar aventura guardada");
            System.out.println("3. 📊 Ver anales del templo (estadísticas)");
            System.out.println("4. 🚪 Cerrar sesión");
            System.out.println("======================================");

            int opcion = ConsoleUtils.leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    iniciarNuevaAventura();
                    break;
                case 2:
                    cargarAventuraExistente();
                    break;
                case 3:
                    mostrarEstadisticas();
                    break;
                case 4:
                    ConsoleUtils.mostrarMensaje("🔒 Sesión cerrada. ¡Vuelve pronto, Hunter!");
                    usuarioAutenticadoEmail = null;
                    break;
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

    private static void iniciarNuevaAventura() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🎮 NUEVA AVENTURA EN EL TEMPLO ===");

        try {
            ConsoleUtils.mostrarMensaje("🏗️  Configuración del laberinto mágico:");
            int filas = ConsoleUtils.leerEntero("Filas (8-15 recomendado): ");
            int columnas = ConsoleUtils.leerEntero("Columnas (8-15 recomendado): ");

            // Validar tamaño del laberinto
            if (filas < 5 || columnas < 5) {
                ConsoleUtils.mostrarError("❌ El laberinto debe ser de al menos 5x5.");
                ConsoleUtils.pausar();
                return;
            }

            if (filas > 20 || columnas > 20) {
                ConsoleUtils.mostrarError("❌ El laberinto no puede exceder 20x20.");
                ConsoleUtils.pausar();
                return;
            }

            Juego juego = servicioJuego.iniciarNuevoJuego(filas, columnas, usuarioAutenticadoEmail);
            ConsoleUtils.mostrarExito("🔮 ¡Laberinto mágico generado! El templo te espera...");
            ConsoleUtils.pausar();

            jugarPartida(juego);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al crear la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    private static void cargarAventuraExistente() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📂 CARGAR AVENTURA GUARDADA ===");

        try {
            Juego juego = servicioJuego.cargarJuegoGuardado(usuarioAutenticadoEmail);
            if (juego == null) {
                ConsoleUtils.mostrarError("❌ No hay aventuras guardadas. Inicia una nueva aventura.");
                ConsoleUtils.pausar();
                return;
            }

            ConsoleUtils.mostrarExito("✅ ¡Aventura cargada con éxito!");
            ConsoleUtils.mostrarMensaje("📍 Posición actual: (" + juego.getJugador().getPosX() + ", " + juego.getJugador().getPosY() + ")");
            ConsoleUtils.mostrarMensaje("💎 Cristales: " + juego.getJugador().getCristales());
            ConsoleUtils.mostrarMensaje("❤️  Vida: " + juego.getJugador().getVida() + "%");
            ConsoleUtils.mostrarMensaje("🗝️  Llave: " + (juego.getJugador().isTieneLlave() ? "SÍ" : "NO"));
            ConsoleUtils.pausar();

            jugarPartida(juego);

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al cargar la aventura: " + e.getMessage());
            ConsoleUtils.pausar();
        }
    }

    private static void jugarPartida(Juego juego) {
        boolean jugando = true;

        while (jugando && juego.getEstado() == EstadoJuego.EN_CURSO) {
            ConsoleUtils.limpiarConsola();

            // Mostrar interfaz de juego
            renderizador.mostrarLaberinto(juego.getLaberinto(), juego.getJugador());
            mostrarEstadoJugadorMejorado(juego);
            mostrarControlesJuego();

            char input = ConsoleUtils.leerCaracter("Ingrese su movimiento: ");
            input = Character.toLowerCase(input);

            switch (input) {
                case 'g': // ✅ GUARDAR Y SALIR
                    servicioJuego.guardarJuego(juego);
                    servicioJuego.guardarEstadisticasParciales(juego);
                    ConsoleUtils.mostrarExito("💾 Aventura guardada. ¡Hasta la próxima, Hunter!");
                    ConsoleUtils.mostrarMensaje("📊 Se registraron estadísticas parciales de tu progreso.");
                    jugando = false;
                    continue;

                case 'q': // ✅ SALIR SIN GUARDAR - CORREGIDO
                    ConsoleUtils.mostrarAdvertencia("🚪 Saliendo sin guardar el progreso...");
                    ConsoleUtils.mostrarMensaje("⚠️  Tu progreso actual se perderá.");
                    ConsoleUtils.mostrarMensaje("¿Estás seguro? (s/n)");
                    char confirmacion = ConsoleUtils.leerCaracter("");

                    if (Character.toLowerCase(confirmacion) == 's') {
                        // ✅ NO llamar a ningún método de guardado
                        ConsoleUtils.mostrarMensaje("❌ Progreso descartado. ¡Vuelve pronto, Hunter!");
                        jugando = false;
                    } else {
                        ConsoleUtils.mostrarMensaje("✅ Continuando la aventura...");
                        ConsoleUtils.pausar();
                    }
                    continue;

                case 'm':
                    ConsoleUtils.limpiarConsola();
                    ConsoleUtils.mostrarMensaje("=== 🗺️  VISTA COMPLETA DEL TEMPLO ===");
                    renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
                    ConsoleUtils.pausar();
                    continue;

                case 'w': case 's': case 'a': case 'd':
                    Direccion direccion = obtenerDireccion(input);
                    boolean movimientoExitoso = servicioJuego.moverJugador(juego, direccion);

                    if (!movimientoExitoso) {
                        ConsoleUtils.mostrarError("🧱 ¡Camino bloqueado! Hay un muro del templo.");
                        ConsoleUtils.pausar();
                    }
                    break;

                default:
                    ConsoleUtils.mostrarError("❌ Movimiento inválido. Use las teclas mágicas correctas.");
                    ConsoleUtils.pausar();
                    continue;
            }

            // Verificar fin del juego
            if (juego.getEstado() != EstadoJuego.EN_CURSO) {
                jugando = false;
                ConsoleUtils.limpiarConsola();
                mostrarFinDelJuego(juego);
            }
        }
    }

    private static void mostrarEstadoJugadorMejorado(Juego juego) {
        System.out.println("\n=== 👤 ESTADO DEL HUNTER ===");
        System.out.println("❤️  Vida: " + juego.getJugador().getVida() + "%");
        System.out.println("💎 Cristales: " + juego.getJugador().getCristales());
        System.out.println("🗝️  Llave: " + (juego.getJugador().isTieneLlave() ? "SÍ ✅" : "NO ❌"));
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

    private static void mostrarControlesJuego() {
        System.out.println("\n=== 🎮 CONTROLES MÁGICOS ===");
        System.out.println("W - ↑ Mover hacia arriba");
        System.out.println("A - ← Mover hacia izquierda");
        System.out.println("S - ↓ Mover hacia abajo");
        System.out.println("D - → Mover hacia derecha");
        System.out.println("M - 🗺️  Ver mapa completo del templo");
        System.out.println("G - 💾 Guardar y salir (guarda progreso)");
        System.out.println("Q - 🚪 Salir sin guardar (pierde progreso)");
        System.out.println("============================");
    }

    private static Direccion obtenerDireccion(char input) {
        switch (input) {
            case 'w': return Direccion.ARRIBA;
            case 's': return Direccion.ABAJO;
            case 'a': return Direccion.IZQUIERDA;
            case 'd': return Direccion.DERECHA;
            default: return Direccion.ARRIBA;
        }
    }

    private static void mostrarFinDelJuego(Juego juego) {
        ResultadoJuego resultado = servicioJuego.terminarJuego(juego);

        if (juego.getEstado() == EstadoJuego.GANADO) {
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

    private static void mostrarEstadisticas() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📊 ANALES DEL TEMPLO ===");
        ConsoleUtils.mostrarMensaje("Estadísticas de: " + usuarioAutenticadoEmail);
        ConsoleUtils.mostrarMensaje("=================================");

        try {

            List<EstadisticasJuego> estadisticas = persistencia.cargarTodasEstadisticas(usuarioAutenticadoEmail);;


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

                    if (stats.isGanado()) partidasGanadas++;
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
                ConsoleUtils.mostrarMensaje("⏱️  Tiempo promedio: " + String.format("%.1f", promedioTiempo) + " segundos");

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