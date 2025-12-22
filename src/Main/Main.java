package Main;

/**
 * Punto de entrada principal de la aplicación.
 * Redirige a la interfaz gráfica (MainApp).
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Usar reflexión para evitar que JavaFX detecte la clase Application
            // prematuramente y suelte la advertencia de "unnamed module".
            Class<?> mainAppClass = Class.forName("Main.MainApp");
            java.lang.reflect.Method mainMethod = mainAppClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}