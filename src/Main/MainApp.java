package Main;

import Main.ui.gui.VentanaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Punto de entrada para la versión gráfica de Maze Hunter.
 */
public class MainApp extends Application {

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

    public static void main(String[] args) {
        launch(args);
    }
}
