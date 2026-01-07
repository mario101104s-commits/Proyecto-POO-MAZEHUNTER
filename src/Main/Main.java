package Main;

/**
 * Clase de lanzamiento de la aplicación Maze Hunter.
 * <p>
 * Esta clase actúa como un intermediario técnico para iniciar el entorno de
 * JavaFX.
 * Su propósito principal es separar el punto de entrada estático {@code main}
 * de la
 * clase que extiende {@code Application}, evitando así advertencias de
 * configuración
 * relacionadas con la modularidad y el entorno de ejecución de Java.
 * </p>
 * * @author Mario Sanchez
 * 
 * @version 1.1
 * @since 22/12/25
 */
public class Main {
    /**
     * Punto de entrada principal del sistema.
     * <p>
     * Utiliza reflexión para cargar dinámicamente {@link MainApp} y ejecutar su
     * metodo
     * principal, asegurando una inicialización limpia del toolkit gráfico.
     * </p>
     * * @param args Argumentos de la línea de comandos pasados durante la
     * ejecución.
     */
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