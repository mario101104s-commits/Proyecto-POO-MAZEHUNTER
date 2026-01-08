package Main.ui.gui.audio;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * Componente UI reutilizable para controlar el volumen y silenciar la música.
 */
public class ControladorAudioUI extends HBox {

    public ControladorAudioUI() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        // Estilo semi-transparente que encaja con el juego
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.6); " +
                "-fx-background-radius: 25; " +
                "-fx-padding: 8 15; " +
                "-fx-border-color: rgba(218, 165, 32, 0.5); " +
                "-fx-border-width: 1; " +
                "-fx-border-radius: 25;");

        GestorAudio gestor = GestorAudio.getInstancia();

        // Botón de Mute
        Button btnMute = new Button(gestor.isSilenciado() ? "🔇" : "🔊");
        btnMute.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 18px; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 0;");

        // Slider de Volumen
        Slider sliderVolumen = new Slider(0, 1, gestor.getVolumen());
        sliderVolumen.setPrefWidth(100);
        sliderVolumen.setStyle("-fx-cursor: hand;");

        // Lógica de interacción
        btnMute.setOnAction(e -> {
            boolean nuevoSilencio = !gestor.isSilenciado();
            gestor.setSilenciado(nuevoSilencio);
            btnMute.setText(nuevoSilencio ? "🔇" : "🔊");
        });

        sliderVolumen.valueProperty().addListener((obs, oldVal, newVal) -> {
            gestor.setVolumen(newVal.doubleValue());
            if (gestor.isSilenciado() && newVal.doubleValue() > 0) {
                gestor.setSilenciado(false);
                btnMute.setText("🔊");
            } else if (newVal.doubleValue() == 0) {
                gestor.setSilenciado(true);
                btnMute.setText("🔇");
            }
        });

        getChildren().addAll(btnMute, sliderVolumen);
    }
}
