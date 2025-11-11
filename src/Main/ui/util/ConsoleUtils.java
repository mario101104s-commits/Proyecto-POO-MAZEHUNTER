package Main.ui.util;
import java.util.Scanner;

public class ConsoleUtils {
    private static final Scanner scanner = new Scanner(System.in);

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
                // Para Linux/Mac - método más seguro
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Método de respaldo: imprimir múltiples líneas nuevas
            limpiarConsolaRespaldo();
        }
    }

    private static void limpiarConsolaRespaldo() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

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

    // Nuevo método para leer un solo carácter (para WASD)
    public static char leerCaracter(String mensaje) {
        System.out.print(mensaje);
        String input = scanner.nextLine();
        if (input.length() > 0) {
            return input.charAt(0);
        }
        return ' ';
    }

    public static void pausar() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }

    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public static void mostrarError(String mensaje) {
        System.err.println("❌ " + mensaje);
    }

    public static void mostrarExito(String mensaje) {
        System.out.println("✅ " + mensaje);
    }

    public static void mostrarAdvertencia(String mensaje) {
        System.out.println("⚠️ " + mensaje);
    }

    public static void mostrarSeparador() {
        System.out.println("=============================================");
    }
}
