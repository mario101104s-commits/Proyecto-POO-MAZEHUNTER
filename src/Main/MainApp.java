package Main;

import Main.ui.gui.VentanaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Clase principal de la aplicación JavaFX que gestiona el ciclo de vida de la interfaz.
 * <p>
 * Hereda de {@link Application} y se encarga de la configuración inicial del escenario (Stage),
 * la carga de recursos globales como el icono de la ventana y la instanciacion de la
 * {@link VentanaPrincipal} para iniciar el flujo del programa.
 * </p>
 * * @author Mario Sanchez
 * @version 1.2
 * @since 22/12/25
 */
public class MainApp extends Application {

    /**
     * Inicializa y despliega la ventana principal de la aplicación.
     * <p>
     * Configura el título, el icono decorativo, las dimensiones de la escena (1024x768)
     * y establece la propiedad de redimensionamiento a falso para mantener la integridad
     * visual del laberinto.
     * </p>
     *
     * @param primaryStage El escenario principal proporcionado por el runtime de JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Maze Hunter - The Lost Temple");

            // Intentar cargar icono
            InputStream iconStream = getClass().getResourceAsStream("/imagenes/jugador.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
            }

            // Iniciar con la ventana principal (Menú)
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(primaryStage);
            Scene scene = new Scene(ventanaPrincipal.getView(), 1024, 768);

            // Cargar estilos CSS si existieran (opcional por ahora)
            // scene.getStylesheets().add(getClass().getResource("/estilos.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lanza la aplicación JavaFX.
     * <p>
     * Este metodo es invocado por la clase {@link Main} mediante reflexión para
     * iniciar el ciclo de vida del toolkit gráfico.
     * </p>
     *
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}