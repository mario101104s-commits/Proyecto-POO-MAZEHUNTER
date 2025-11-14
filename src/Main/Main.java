package Main;

import Main.modelo.Constantes.Direccion;
import Main.modelo.Constantes.EstadoJuego;
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

public class Main {
    private static PersistenciaJASON persistencia = new PersistenciaJASON();
    private static Cifrador cifrador = new CifradorImpl();
    private static ServicioJuego servicioJuego = new ServicioJuegoImpl(persistencia);
    private static ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(persistencia);
    private static final RenderizadorLaberinto renderizador = new RenderizadorLaberinto();
    private static String usuarioAutenticadoEmail = null;

    public static void main(String[] args) {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== MAZE HUNTER, JUEGO EL LABERINTO ===");
        try {
            persistencia.cargarUsuarios();
            persistencia.cargarEstadisticas();

            boolean ejecutando = true;
            while (ejecutando) {
                if (usuarioAutenticadoEmail == null) {
                    mostrarMenuAutenticacion();
                    ejecutando = manejarMenuAutenticacion();
                } else {
                    ejecutando = mostraMenuPrincipal();
                }
            }
            ConsoleUtils.mostrarMensaje("Gracias por Jugar");
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en la aplicacion: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void mostrarMenuAutenticacion() {
        ConsoleUtils.limpiarConsola();
        System.out.println("===  ACCESO AL LABERINTO ===");
        System.out.println("1.  Iniciar seccion");
        System.out.println("2.  Registrar Nuevo Usuario");
        System.out.println("3.  Recuperar Contraseña");
        System.out.println("4.  Salir de la Aplicación");
        System.out.println("===============================");
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
                    ConsoleUtils.mostrarError("Opción inválida.");
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en la operación: " + e.getMessage());
        }
        ConsoleUtils.pausar();
        return true;
    }
    private static void iniciarSesion() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== INICIO DE SESIÓN ===");
        String email = ConsoleUtils.leerCadena("Ingrese email: ");
        String contrasenia = ConsoleUtils.leerCadena("Ingrese contraseña: ");

        try {
            Usuario usuario = persistencia.cargarUsuario(email);

            if (usuario == null) {
                ConsoleUtils.mostrarError("Credenciales inválidas. Usuario no encontrado.");
                return;
            }

            String contraseniaAlmacenadaCifrada = usuario.getContraseniaCifrada();
            String contraseniaDescifrada = cifrador.descifrarContrasenia(contraseniaAlmacenadaCifrada);

            if (contraseniaDescifrada == null) {
                ConsoleUtils.mostrarError("Error al validar credenciales.");
                return;
            }

            // Comparación de contraseña
            if (contraseniaDescifrada.equals(contrasenia)) {
                usuarioAutenticadoEmail = email;
                ConsoleUtils.mostrarExito("¡Inicio de sesión exitoso! Bienvenido.");
            } else {
                ConsoleUtils.mostrarError("Credenciales inválidas. Contraseña incorrecta.");
            }

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al iniciar sesión: " + e.getMessage());
        }
    }
    private static boolean validarContraseniaCompleta(String password) {
        boolean tieneMayuscula = false;
        boolean tieneCaracterEspecial = false;
        String caracteresEspeciales = "!@#$%^&*()-_+=<>?";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                tieneMayuscula = true;
            }
            if (caracteresEspeciales.indexOf(c) >= 0) {
                tieneCaracterEspecial = true;
            }
            if (tieneMayuscula && tieneCaracterEspecial) {
                break;
            }
        }
        if (!tieneMayuscula) {
            ConsoleUtils.mostrarError("Error: La contraseña debe contener al menos una letra mayúscula.");
            return false;
        }
        if (!tieneCaracterEspecial) {
            ConsoleUtils.mostrarError("Error: La contraseña debe contener al menos un caracter especial: " + caracteresEspeciales);
            return false;
        }

        return true;
    }
    private static void registrarUsuario() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== REGISTRO DE USUARIO ===");
        String email = ConsoleUtils.leerCadena("Ingrese nuevo email (será su ID): ");
        String password = ConsoleUtils.leerCadena("Ingrese nueva contraseña: ");
        String confirmPassword = ConsoleUtils.leerCadena("Repita la nueva contraseña: ");

        try {

            if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                ConsoleUtils.mostrarError("Error: Email es inválido. Falta o está mal colocado el '@'.");
                return;
            }
            int arrobaIndex = email.indexOf("@");
            if (!email.contains(".") || email.endsWith(".") || email.indexOf(".", arrobaIndex) == -1) {
                ConsoleUtils.mostrarError("Error: Email es inválido. Falta el punto o está mal colocado.");
                return;
            }
            int lastDotIndex = email.lastIndexOf(".");
            if (email.length() - 1 - lastDotIndex < 2) {
                ConsoleUtils.mostrarError("Error: debe de contener ej: (.com, .net, .co). Por favor, revisa el email.");
                return;
            }

            if (persistencia.existeUsuario(email)) {
                ConsoleUtils.mostrarError("Error: El email ya está registrado.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                ConsoleUtils.mostrarError("Error: Las contraseñas no coinciden. Intente de nuevo.");
                return;
            }

            if (password == null || password.length() < 8) {
                ConsoleUtils.mostrarError("Error: La contraseña debe tener un mínimo de 8 caracteres.");
                return;
            }

            if (!validarContraseniaCompleta(password)) {
                return;
            }

            String contraseniaCifrada = cifrador.cifrarContrasenia(password);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            String fechaRegistro = dtf.format(LocalDateTime.now());
            Usuario nuevoUsuario = new Usuario(email, contraseniaCifrada, fechaRegistro);
            persistencia.guardarUsuario(nuevoUsuario);

            ConsoleUtils.mostrarExito("¡Usuario registrado exitosamente! Ya puede iniciar sesión.");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al registrar: " + e.getMessage());
        }
    }

    private static void recuperarContrasenia() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== RECUPERACIÓN DE CONTRASEÑA ===");

        String email = ConsoleUtils.leerCadena("Ingrese su email de registro: ");

        try {
            Usuario usuario = persistencia.cargarUsuario(email);

            if (usuario == null) {
                ConsoleUtils.mostrarError("Error: El email no está registrado.");
                return;
            }

            ConsoleUtils.mostrarMensaje("Usuario encontrado. Por favor, ingrese su nueva contraseña.");
            String newPassword = ConsoleUtils.leerCadena("Ingrese nueva contraseña: ");
            String confirmNewPassword = ConsoleUtils.leerCadena("Repita la nueva contraseña: ");

            // 1. Validar que las contraseñas coincidan
            if (!newPassword.equals(confirmNewPassword)) {
                ConsoleUtils.mostrarError("Error: Las contraseñas no coinciden. Intente de nuevo.");
                return;
            }

            if (newPassword == null || newPassword.length() < 8) {
                ConsoleUtils.mostrarError("Error: La contraseña debe tener un mínimo de 8 caracteres.");
                return;
            }

            if (!validarContraseniaCompleta(newPassword)) {
                return;
            }
            String contraseniaCifrada = cifrador.cifrarContrasenia(newPassword);
            if (contraseniaCifrada == null) {
                ConsoleUtils.mostrarError("Error interno al cifrar la contraseña.");
                return;
            }
            usuario.setContraseniaCifrada(contraseniaCifrada);
            persistencia.actualizarUsuario(usuario);

            ConsoleUtils.mostrarExito("¡Contraseña restablecida exitosamente! Ya puede iniciar sesión.");

        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error al recuperar la contraseña: " + e.getMessage());
        }
    }
    private static boolean mostraMenuPrincipal() {
        try {
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
                    usuarioAutenticadoEmail = null;
                    return true;
                default:
                    ConsoleUtils.mostrarError("Opción inválida.");
                    ConsoleUtils.pausar();
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error inesperado en el menú principal: " + e.getMessage());
            e.printStackTrace();
        }
        ConsoleUtils.pausar();
        return true;
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
        System.out.println("1. 🆕 Juego nuevo");
        System.out.println("2. 📂 Cargar juego guardado");
        int opcionInicial = ConsoleUtils.leerEntero("Seleccione opción: ");

        Juego juego = null;

        try {
            String idUsuarioJuego = (usuarioAutenticadoEmail != null) ? usuarioAutenticadoEmail : "demo";

            if (opcionInicial == 2) {
                juego = servicioJuego.cargarJuegoGuardado(idUsuarioJuego);
                if (juego == null) {
                    ConsoleUtils.mostrarError("No hay juego guardado. Creando nuevo juego...");
                    opcionInicial = 1;
                } else {
                    ConsoleUtils.mostrarExito("¡Juego cargado exitosamente!");
                    ConsoleUtils.pausar();
                }
            }

            if (opcionInicial == 1) {
                int filas = ConsoleUtils.leerEntero("Filas del laberinto (recomendado 8-12): ");
                int columnas = ConsoleUtils.leerEntero("Columnas del laberinto (recomendado 8-12): ");
                juego = servicioJuego.iniciarNuevoJuego(filas, columnas, idUsuarioJuego);
                ConsoleUtils.mostrarExito("Laberinto generado exitosamente!");
                ConsoleUtils.pausar();
            }

            if (juego == null) {
                ConsoleUtils.mostrarError("No se pudo crear el juego");
                return;
            }
            boolean jugando = true;
            while (jugando && juego.getEstado() == EstadoJuego.EN_CURSO) {
                ConsoleUtils.limpiarConsola();
                renderizador.mostrarLaberinto(juego.getLaberinto(), juego.getJugador());
                renderizador.mostrarEstadoJugador(juego.getJugador());
                renderizador.mostrarControles();

                char input = ConsoleUtils.leerCaracter("Ingrese movimiento (W/A/S/D/G/Q/M): ");
                input = Character.toLowerCase(input);

                if (input == 'g') {
                    servicioJuego.guardarEstadisticasParciales(juego);
                    ConsoleUtils.mostrarExito("Juego guardado. Saliendo...");
                    jugando = false;
                    continue;
                } else if (input == 'q') {
                    ConsoleUtils.mostrarAdvertencia("Saliendo sin guardar...");
                    jugando = false;
                    continue;
                } else if (input == 'm') {
                    ConsoleUtils.limpiarConsola();
                    ConsoleUtils.mostrarMensaje("=== 🗺️  MAPA COMPLETO ===");
                    renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
                    ConsoleUtils.pausar();
                    continue;
                }

                Direccion direccion = null;
                switch (input) {
                    case 'w': direccion = Direccion.ARRIBA; break;
                    case 's': direccion = Direccion.ABAJO; break;
                    case 'a': direccion = Direccion.IZQUIERDA; break;
                    case 'd': direccion = Direccion.DERECHA; break;
                    default: ConsoleUtils.mostrarError("Movimiento inválido."); ConsoleUtils.pausar(); continue;
                }

                boolean movimientoExitoso = servicioJuego.moverJugador(juego, direccion);

                if (!movimientoExitoso) {
                    ConsoleUtils.mostrarError("¡Movimiento inválido! Hay un muro.");
                    ConsoleUtils.pausar();
                }

                if (juego.getEstado() != EstadoJuego.EN_CURSO) {
                    jugando = false;
                    ConsoleUtils.limpiarConsola();
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
            ConsoleUtils.mostrarExito("Laberinto generado exitosamente!");
            renderizador.mostrarLaberintoCompleto(juego.getLaberinto());
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