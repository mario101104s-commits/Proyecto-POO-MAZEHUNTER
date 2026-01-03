package Main.ui.util;
import java.util.Scanner;
/**
 * Clase de utilidad estática que centraliza la interacción con la consola.
 * <p>
 * Proporciona métodos para leer datos con validación, limpiar la pantalla de la consola,
 * pausar la ejecución y mostrar mensajes con formatos estandarizados (éxito, error, advertencia).
 * </p>
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public class ConsoleUtils {
    /** Instancia estática de Scanner utilizada para la entrada de datos en todos los métodos. */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Intenta limpiar la pantalla de la consola ejecutando comandos específicos del sistema
     * operativo (Windows: 'cls', Unix/Linux/Mac: secuencia ANSI).
     * <p>
     * Si falla la ejecución, utiliza el metodo de respaldo.
     * </p>
     */
    public static void limpiarConsola() {
        try {
            final String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("windows")) {
                // Para Windows
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                // Para Linux/Mac - metodo más seguro
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Metodo de respaldo: imprimir múltiples líneas nuevas
            limpiarConsolaRespaldo();
        }
    }

    /**
     * Metodo de respaldo para limpiar la consola imprimiendo un gran número de líneas vacías.
     * * Metodo auxiliar privado.
     */
    private static void limpiarConsolaRespaldo() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    /**
     * Solicita al usuario que ingrese un número entero y válida la entrada.
     * * Repite la solicitud hasta que se ingrese un valor numérico válido.
     *
     * @param mensaje El mensaje a mostrar al usuario antes de la entrada.
     * @return El número entero ingresado por el usuario.
     */
    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido.");
            scanner.next(); // Limpiar entrada inválida
            System.out.print(mensaje);
        }
        int numero = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer del Enter
        return numero;
    }

    public static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    /**
     * Solicita y lee una línea completa de texto (String) ingresada por el usuario.
     *
     * @param mensaje El mensaje a mostrar al usuario antes de la entrada.
     * @return La cadena de texto ingresada.
     */
    public static char leerCaracter(String mensaje) {
        System.out.print(mensaje);
        String input = scanner.nextLine();
        if (input.length() > 0) {
            return input.charAt(0);
        }
        return ' ';
    }

    /**
     * Pausa la ejecución del programa hasta que el usuario presione la tecla Enter.
     */
    public static void pausar() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    /**
     * Muestra un mensaje estándar en la consola.
     *
     * @param mensaje La cadena de texto a mostrar.
     */
    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Muestra un mensaje de error utilizando {@code System.err} con un prefijo de advertencia (❌).
     *
     * @param mensaje El mensaje de error a mostrar.
     */
    public static void mostrarError(String mensaje) {
        System.err.println("❌ " + mensaje);
    }

    /**
     * Muestra un mensaje de éxito con un prefijo visual (✅).
     *
     * @param mensaje El mensaje de éxito a mostrar.
     */
    public static void mostrarExito(String mensaje) {
        System.out.println("✅ " + mensaje);
    }

    /**
     * Muestra un mensaje de advertencia con un prefijo visual (⚠️).
     *
     * @param mensaje El mensaje de advertencia a mostrar.
     */
    public static void mostrarAdvertencia(String mensaje) {
        System.out.println("⚠️ " + mensaje);
    }

    /**
     * Muestra una línea de separación estandarizada para estructurar la salida de la consola.
     */
    public static void mostrarSeparador() {
        System.out.println("=============================================");
    }
}
