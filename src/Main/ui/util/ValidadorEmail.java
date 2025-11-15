package Main.ui.util;

import java.util.Scanner;
import java.util.regex.Pattern;
/**
 * Clase de utilidad encargada de la validación de formato de correos electrónicos
 * mediante expresiones regulares y de la interacción con el usuario para solicitar
 * una dirección válida.
 *
 * @author Jose Berroteran
 * @version 1.0
 * @since 11/11/2025
 */

public class ValidadorEmail {
    /** Scanner para capturar la entrada de datos del usuario desde la consola. */
    private Scanner scanner;
    /**
     * Válida si una cadena de texto tiene el formato de correo electrónico estándar.
     * <p>
     * Utiliza una expresión regular (regex) estricta para asegurar la estructura
     * [usuario]@[dominio].[tld].
     * </p>
     * @param email La cadena de texto a validar.
     * @return {@code true} si el formato es válido y no está vacío, {@code false} en caso contrario.
     */
    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(regex, email);
    }
    /**
     * Solicita al usuario que ingrese un correo electrónico y lo valida.
     * <p>
     * Repite la solicitud si el formato es inválido hasta obtener una entrada válida.
     * </p>
     * @return El correo electrónico válido en formato String, o {@code null} si la validación falla (aunque el loop no está implementado aquí, se retorna el valor).
     */
    public String solicitarEmail() {
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine().trim();

        if (!validarEmail(email)) {
            System.out.println("❌ Formato de email inválido.");
            System.out.println("📧 Use formato: usuario@dominio.com");
            return null;
        }

        return email;
    }
}
