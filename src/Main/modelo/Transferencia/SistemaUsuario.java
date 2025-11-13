package Main.modelo.Transferencia;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.regex.Pattern;
import java.util.Base64;

public class SistemaUsuario {
    private Map<String, String> usuarios; // email -> contraseña cifrada
    private static final String CLAVE_CIFRADO = "MiClaveSecreta12"; // 16 caracteres para AES
    private Scanner scanner;

    // Constructor
    public SistemaUsuario() {
        this.usuarios = new HashMap<>();
        this.scanner = new Scanner(System.in);
    }

    // Constructor con scanner externo
    public SistemaUsuario(Scanner scanner) {
        this.usuarios = new HashMap<>();
        this.scanner = scanner;
    }

    // MÉTODO PRINCIPAL - Registro de usuario
    public boolean registrarUsuario() {
        System.out.println("\n=== REGISTRO DE USUARIO ===");

        // 1. Validar email
        String email = solicitarEmail();
        if (email == null) return false;

        // 2. Verificar si el email ya existe
        if (usuarios.containsKey(email)) {
            System.out.println("❌ Este correo ya está registrado.");
            System.out.println("¿Desea recuperar su contrasenia? (s/n)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                recuperarContrasenia(email);
            }
            return false;
        }

        // 3. Validar contraseña
        String contrasenia = solicitarContrasenia();
        if (contrasenia == null) return false;

        // 4. Cifrar contraseña
        String contraseniaCifrada = cifrarContrasenia(contrasenia);
        if (contraseniaCifrada == null) {
            System.out.println("❌ Error al cifrar la contrasenia.");
            return false;
        }

        // 5. Guardar usuario
        usuarios.put(email, contraseniaCifrada);
        System.out.println("✅ ¡Usuario registrado exitosamente!");
        System.out.println("📧 Email: " + email);
        return true;
    }

    // MÉTODO PARA AUTENTICAR
    public boolean autenticarUsuario() {
        System.out.println("\n=== INICIAR SESIÓN ===");

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Contrasenia: ");
        String contrasenia = scanner.nextLine();

        if (!usuarios.containsKey(email)) {
            System.out.println("❌ Usuario no encontrado.");
            return false;
        }

        // Descifrar contraseña almacenada y comparar
        String contraseniaCifrada = usuarios.get(email);
        String contraseniaAlmacenada = descifrarContrasenia(contraseniaCifrada);

        if (contrasenia.equals(contraseniaAlmacenada)) {
            System.out.println("✅ ¡Autenticación exitosa!");
            return true;
        } else {
            System.out.println("❌ Contrasenia incorrecta.");
            return false;
        }
    }

    // MÉTODO PARA RECUPERAR CONTRASEÑA
    public void recuperarContrasenia() {
        System.out.println("\n=== RECUPERAR CONTRASENIA ===");
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine().trim();
        recuperarContrasenia(email);
    }

    private void recuperarContrasenia(String email) {
        if (!usuarios.containsKey(email)) {
            System.out.println("❌ Email no registrado en el sistema.");
            return;
        }

        String contraseniaCifrada = usuarios.get(email);
        String contrasenia = descifrarContrasenia(contraseniaCifrada);

        if (contrasenia != null) {
            System.out.println("✅ Contrasenia recuperada:");
            System.out.println("📧 Email: " + email);
            System.out.println("🔑 Contrasenia: " + contrasenia);
        } else {
            System.out.println("❌ Error al recuperar la contrasenia.");
        }
    }

    // MÉTODOS DE VALIDACIÓN
    private String solicitarEmail() {
        System.out.print("Ingrese su email: ");
        String email = scanner.nextLine().trim();

        if (!validarEmail(email)) {
            System.out.println("❌ Formato de email inválido.");
            System.out.println("📧 Use formato: usuario@dominio.com");
            return null;
        }

        return email;
    }

    private String solicitarContrasenia() {
        String contrasenia, repetirContrasenia;

        while (true) {
            System.out.print("Ingrese su contrasenia: ");
            contrasenia = scanner.nextLine();

            if (!validarContrasenia(contrasenia)) {
                System.out.println("❌ La contrasenia no cumple los requisitos.");
                System.out.println("💡 Debe tener: mínimo 6 caracteres, 1 mayúscula y 1 carácter especial");
                continue;
            }

            System.out.print("Repita su contrasenia: ");
            repetirContrasenia = scanner.nextLine();

            if (!contrasenia.equals(repetirContrasenia)) {
                System.out.println("❌ Las contrasenias no coinciden.");
            } else {
                break;
            }
        }

        return contrasenia;
    }

    private boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(regex, email);
    }

    private boolean validarContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.length() < 6) return false;
        boolean tieneMayuscula = contrasenia.matches(".*[A-Z].*");
        boolean tieneEspecial = contrasenia.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        return tieneMayuscula && tieneEspecial;
    }

    // MÉTODOS DE CIFRADO
    private String cifrarContrasenia(String contrasenia) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            byte[] contraseniaCifrada = cipher.doFinal(contrasenia.getBytes());
            return Base64.getEncoder().encodeToString(contraseniaCifrada);
        } catch (Exception e) {
            return null;
        }
    }

    private String descifrarContrasenia(String contraseniaCifrada) {
        try {
            SecretKeySpec clave = new SecretKeySpec(CLAVE_CIFRADO.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, clave);
            byte[] contraseniaBytes = Base64.getDecoder().decode(contraseniaCifrada);
            byte[] contraseniaDescifrada = cipher.doFinal(contraseniaBytes);
            return new String(contraseniaDescifrada);
        } catch (Exception e) {
            return null;
        }
    }

    // MÉTODOS ADICIONALES ÚTILES
    public boolean existeUsuario(String email) {
        return usuarios.containsKey(email);
    }

    public int getTotalUsuarios() {
        return usuarios.size();
    }

    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

    // Para debugging
    public void mostrarUsuarios() {
        System.out.println("\n=== USUARIOS REGISTRADOS ===");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        int i = 1;
        for (String email : usuarios.keySet()) {
            System.out.println(i + ". " + email);
            i++;
        }
    }
}