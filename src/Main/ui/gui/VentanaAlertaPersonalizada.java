package Main.ui.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.InputStream;
import java.net.URL;

/**
 * Ventana de diálogo personalizada con estética temática de Maze Hunter.
 * <p>
 * Proporciona diferentes tipos de alertas (información, confirmación, game over,
 * pausa, victoria) con fondos personalizados y botones estilizados consistentes
 * con el diseño del juego. Implementa ventanas modales que bloquean la interacción
 * con la ventana principal hasta ser cerradas.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class VentanaAlertaPersonalizada extends Stage {

    /**
     * Enumeración que define los tipos de alertas disponibles.
     * <p>
     * Cada tipo tiene asociado un color temático y un comportamiento específico
     * para los botones y el diseño de la ventana.
     * </p>
     */
    public enum Tipo { 
        /** Alerta informativa con botón de aceptación. */
        INFO, 
        /** Alerta de confirmación con botones sí/no. */
        CONFIRMACION, 
        /** Pantalla de fin de juego con botón de reinicio. */
        GAME_OVER, 
        /** Menú de pausa con opciones de reanudar/salir. */
        PAUSA, 
        /** Pantalla de victoria con botón de continuar. */
        VICTORIA 
    }

    /** Almacena el resultado de la interacción del usuario (para confirmaciones). */
    private String resultado = null;
    
    /** Coordenadas para permitir arrastrar la ventana. */
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Constructor que crea una ventana de alerta personalizada.
     * <p>
     * Configura la ventana como modal y sin decoraciones, aplica el fondo temático
     * según el tipo de alerta y establece los botones apropiados para cada caso.
     * </p>
     * 
     * @param owner Ventana principal que será bloqueada mientras esta alerta esté activa.
     * @param titulo Título que se mostrará en la parte superior de la alerta.
     * @param mensaje Mensaje descriptivo que se mostrará en el cuerpo de la alerta.
     * @param tipo Tipo de alerta que determina el estilo y los botones disponibles.
     */
    public VentanaAlertaPersonalizada(Stage owner, String titulo, String mensaje, Tipo tipo) {
        initOwner(owner);
        initModality(Modality.APPLICATION_MODAL);
        initStyle(StageStyle.UNDECORATED); 

        StackPane root = new StackPane();
        root.setMinWidth(600); // Un poco mas ancho para Papyrus
        root.setMinHeight(380);
        
        root.setOnMousePressed(e -> { xOffset = e.getSceneX(); yOffset = e.getSceneY(); });
        root.setOnMouseDragged(e -> { setX(e.getScreenX() - xOffset); setY(e.getScreenY() - yOffset); });

        ImageView bg = new ImageView();
        String p = switch(tipo) {
            case VICTORIA -> "/imagenes/victoria.png";
            case GAME_OVER -> "/imagenes/derrota.png";
            case PAUSA -> "/imagenes/mazmorra.png";
            default -> "/imagenes/advertencia.png";
        };
        
        try {
            InputStream is = getClass().getResourceAsStream(p);
            if (is != null) {
                Image img = new Image(is);
                bg.setImage(img);
                bg.setFitWidth(600);
                bg.setFitHeight(380);
                bg.setPreserveRatio(false);
            }
        } catch (Exception e) { }

        VBox overlay = new VBox(25);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(35));
        String style = (tipo == Tipo.PAUSA) ? "rgba(26,21,10,0.85)" : "rgba(0,0,0,0.5)";
        overlay.setStyle("-fx-background-color: " + style + "; -fx-background-radius: 12;");

        root.setStyle("-fx-border-color: #DAA520; -fx-border-width: 4; -fx-border-radius: 12;");

        Label lblT = new Label(titulo.toUpperCase());
        lblT.setStyle("-fx-font-family: 'Papyrus', serif; -fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 10, 0, 0, 3);");

        Label lblM = new Label(mensaje);
        lblM.setWrapText(true); lblM.setMaxWidth(520); lblM.setAlignment(Pos.CENTER);
        lblM.setStyle("-fx-font-family: 'Papyrus', serif; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox btnBox = new VBox(15); btnBox.setAlignment(Pos.CENTER);

        if (tipo == Tipo.INFO || tipo == Tipo.GAME_OVER || tipo == Tipo.VICTORIA) {
            Button btnEnter = new Button("ACEPTAR"); estilizar(btnEnter); btnEnter.setPrefWidth(250);
            btnEnter.setOnAction(e -> close()); btnBox.getChildren().add(btnEnter);
        } else if (tipo == Tipo.CONFIRMACION) {
            HBox hb = new HBox(25); hb.setAlignment(Pos.CENTER);
            Button btnSi = new Button("SÍ"); estilizar(btnSi); btnSi.setPrefWidth(120);
            btnSi.setOnAction(e -> { resultado = "SI"; close(); });
            Button btnNo = new Button("NO"); estilizar(btnNo); btnNo.setPrefWidth(120);
            btnNo.setOnAction(e -> { resultado = "NO"; close(); });
            hb.getChildren().addAll(btnSi, btnNo); btnBox.getChildren().add(hb);
        } else if (tipo == Tipo.PAUSA) {
            Button c = new Button("CONTINUAR"); estilizar(c); c.setPrefSize(300, 50); c.setOnAction(e -> { resultado = "CONTINUAR"; close(); });
            Button g = new Button("GUARDAR"); estilizar(g); g.setPrefSize(300, 50); g.setOnAction(e -> { resultado = "GUARDAR"; close(); });
            Button s = new Button("SALIR AL MENÚ"); estilizar(s); s.setPrefSize(300, 50); s.setOnAction(e -> { resultado = "SALIR"; close(); });
            btnBox.getChildren().addAll(c, g, s);
        }

        overlay.getChildren().addAll(lblT, lblM, btnBox);
        root.getChildren().addAll(bg, overlay);
        Scene scene = new Scene(root); scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public String getResultado() { return resultado; }

    private void estilizar(Button b) {
        String p = getClass().getResource("/imagenes/boton2.jpg").toExternalForm();
        String base = "-fx-background-image: url('" + p + "'); -fx-background-size: 100% 100%; -fx-text-fill: white; -fx-font-family: 'Papyrus', serif; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand; -fx-border-color: #DAA520; -fx-border-radius: 8; -fx-padding: 12 30;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(base + " -fx-text-fill: #FFD700; -fx-border-color: #FFD700; -fx-scale-x: 1.05;"));
        b.setOnMouseExited(e -> b.setStyle(base));
    }
}
