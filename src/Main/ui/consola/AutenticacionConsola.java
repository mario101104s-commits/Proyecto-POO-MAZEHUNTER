package Main.ui.consola;

import Main.controlador.ControladorAutenticacion;
import Main.ui.util.ConsoleUtils;

// Vista para el sistema de autenticación
public class AutenticacionConsola {
    private ControladorAutenticacion controlador;

    public AutenticacionConsola(ControladorAutenticacion controlador) {
        this.controlador = controlador;
    }

    // Muestra el menú de autenticación
    public void mostrarMenu() {
        ConsoleUtils.limpiarConsola();
        System.out.println("=== 🔐 ACCESO AL TEMPLO PERDIDO ===");
        System.out.println("1. 🗝️  Iniciar sesión");
        System.out.println("2. 📝 Registrar nuevo Hunter");
        System.out.println("3. 🔑 Recuperar contraseña mágica");
        System.out.println("4. 🚪 Abandonar el templo");
        System.out.println("====================================");
    }

    // Maneja el menú de autenticación y retorna el email del usuario autenticado o
    // null
    public String manejarMenu() {
        int opcion = ConsoleUtils.leerEntero("Seleccione una opción: ");

        try {
            switch (opcion) {
                case 1:
                    return iniciarSesion();
                case 2:
                    registrarUsuario();
                    return null;
                case 3:
                    recuperarContrasenia();
                    return null;
                case 4:
                    return "SALIR"; // Señal especial para salir
                default:
                    ConsoleUtils.mostrarError("Opción inválida. El templo solo reconoce opciones del 1 al 4.");
                    ConsoleUtils.pausar();
                    return null;
            }
        } catch (Exception e) {
            ConsoleUtils.mostrarError("Error en la operación: " + e.getMessage());
            ConsoleUtils.pausar();
            return null;
        }
    }

    // Maneja el inicio de sesión
    public String iniciarSesion() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🗝️  INGRESO AL TEMPLO ===");

        String email = ConsoleUtils.leerCadena("Email del Hunter: ");
        String contrasenia = ConsoleUtils.leerCadena("Contraseña mágica: ");

        String emailAutenticado = controlador.iniciarSesion(email, contrasenia);

        if (emailAutenticado != null) {
            ConsoleUtils.mostrarExito("🎉 ¡Bienvenido de vuelta, Hunter " + email + "! El templo te recibe.");
            ConsoleUtils.pausar();
            return emailAutenticado;
        } else {
            ConsoleUtils.mostrarError("❌ Credenciales incorrectas. Las puertas del templo permanecen selladas.");
            ConsoleUtils.pausar();
            return null;
        }
    }

    // Maneja el registro de un nuevo usuario
    public void registrarUsuario() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 📝 REGISTRO DE NUEVO HUNTER ===");

        String email = ConsoleUtils.leerCadena("Email del nuevo Hunter: ");
        String password = ConsoleUtils.leerCadena("Contraseña mágica: ");
        String confirmPassword = ConsoleUtils.leerCadena("Confirmar contraseña mágica: ");

        // Validar email
        if (!controlador.validarEmail(email)) {
            ConsoleUtils.mostrarError("❌ Formato de email inválido. Ejemplo: hunter@templo.com");
            ConsoleUtils.pausar();
            return;
        }

        // Verificar si ya existe
        if (controlador.existeUsuario(email)) {
            ConsoleUtils.mostrarError("❌ Este Hunter ya está registrado en los anales del templo.");
            ConsoleUtils.pausar();
            return;
        }

        // Validar contraseñas coinciden
        if (!password.equals(confirmPassword)) {
            ConsoleUtils.mostrarError("❌ Las contraseñas mágicas no coinciden.");
            ConsoleUtils.pausar();
            return;
        }

        // Validar requisitos de contraseña
        if (!controlador.validarContrasenia(password)) {
            ConsoleUtils.mostrarError(
                    "❌ La contraseña debe tener al menos 8 caracteres, una mayúscula y un carácter especial.");
            ConsoleUtils.pausar();
            return;
        }

        // Registrar
        boolean exito = controlador.registrarUsuario(email, password);

        if (exito) {
            ConsoleUtils.mostrarExito("🎉 ¡Hunter registrado con éxito! Ya puede acceder al templo.");
        } else {
            ConsoleUtils.mostrarError("❌ Error al registrar el Hunter.");
        }

        ConsoleUtils.pausar();
    }

    // Maneja la recuperación de contraseña
    public void recuperarContrasenia() {
        ConsoleUtils.limpiarConsola();
        ConsoleUtils.mostrarMensaje("=== 🔑 RECUPERACIÓN DE CONTRASEÑA MÁGICA ===");

        String email = ConsoleUtils.leerCadena("Email del Hunter: ");

        if (!controlador.existeUsuario(email)) {
            ConsoleUtils.mostrarError("❌ Hunter no encontrado en los archivos del templo.");
            ConsoleUtils.pausar();
            return;
        }

        ConsoleUtils.mostrarMensaje("Hunter encontrado. Crea una nueva contraseña mágica.");
        String newPassword = ConsoleUtils.leerCadena("Nueva contraseña mágica: ");
        String confirmNewPassword = ConsoleUtils.leerCadena("Confirmar nueva contraseña: ");

        if (!newPassword.equals(confirmNewPassword)) {
            ConsoleUtils.mostrarError("❌ Las nuevas contraseñas no coinciden.");
            ConsoleUtils.pausar();
            return;
        }

        if (!controlador.validarContrasenia(newPassword)) {
            ConsoleUtils.mostrarError(
                    "❌ La contraseña debe tener al menos 8 caracteres, una mayúscula y un carácter especial.");
            ConsoleUtils.pausar();
            return;
        }

        boolean exito = controlador.recuperarContrasenia(email, newPassword);

        if (exito) {
            ConsoleUtils.mostrarExito("✅ ¡Contraseña restablecida! Las puertas del templo te esperan.");
        } else {
            ConsoleUtils.mostrarError("❌ Error al recuperar la contraseña.");
        }

        ConsoleUtils.pausar();
    }
}
