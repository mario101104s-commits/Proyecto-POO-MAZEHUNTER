package Main.ui.consola;

import Main.controlador.ControladorAutenticacion;
import Main.ui.util.ConsoleUtils;

/**
 * Representa la interfaz de usuario basada en consola para el sistema de autenticación.
 * <p>
 * Esta vista se encarga de interactuar con el usuario para gestionar el acceso
 * al juego, incluyendo el inicio de sesión, registro de nuevos perfiles y
 * recuperación de credenciales, delegando la lógica al {@link ControladorAutenticacion}.
 * </p>
 * * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class AutenticacionConsola {

    /**
     * Controlador que gestiona la lógica de negocio para la autenticación.
     */
    private ControladorAutenticacion controlador;

    /**
     * Crea una nueva instancia de la vista de autenticación.
     * * @param controlador El controlador de autenticación que manejará las peticiones.
     */
    public AutenticacionConsola(ControladorAutenticacion controlador) {
        this.controlador = controlador;
    }

    /**
     * Despliega en consola el menú principal de autenticación con sus opciones.
     */
    public void mostrarMenu() {
        ConsoleUtils.limpiarConsola();
        System.out.println("=== 🔐 ACCESO AL TEMPLO PERDIDO ===");
        System.out.println("1. 🗝️  Iniciar sesión");
        System.out.println("2. 📝 Registrar nuevo Hunter");
        System.out.println("3. 🔑 Recuperar contraseña mágica");
        System.out.println("4. 🚪 Abandonar el templo");
        System.out.println("====================================");
    }

    /**
     * Captura la opción del usuario y ejecuta la acción correspondiente.
     * * @return El correo electrónico del usuario autenticado, "SALIR" si se elige abandonar,
     * o {@code null} si la operación no resultó en una sesión activa.
     */
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

    /**
     * Gestiona el flujo de inicio de sesión capturando credenciales por consola.
     * * @return El correo electrónico si la autenticación es exitosa, {@code null} en caso contrario.
     */
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

    /**
     * Gestiona el registro de un nuevo usuario, realizando validaciones de formato
     * y seguridad antes de persistir los datos.
     */
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

    /**
     * Gestiona la recuperación de cuenta permitiendo al usuario establecer
     * una nueva contraseña tras validar su existencia en el sistema.
     */
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