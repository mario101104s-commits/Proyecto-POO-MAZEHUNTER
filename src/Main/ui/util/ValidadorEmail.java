package Main.ui.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ValidadorEmail {

    private Scanner scanner;

    public boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(regex, email);
    }

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
