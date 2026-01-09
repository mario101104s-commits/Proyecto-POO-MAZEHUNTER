package Main.ui.gui.audio;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;

/**
 * Componente de interfaz de usuario para controlar el audio del juego.
 * <p>
 * Proporciona controles intuitivos para silenciar/desilenciar la música y
 * ajustar el volumen mediante un deslizador. El diseño es semi-transparente
 * y se integra visualmente con la estética del juego, mostrando iconos
 * representativos para el estado del audio.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class ControladorAudioUI extends HBox {

    /**
     * Constructor que inicializa los controles de audio.
     * <p>
     * Crea un botón de silencio/desilencio con iconos dinámicos (🔇/🔊),
     * un deslizador para ajustar el volumen y aplica el estilo temático
     * semi-transparente consistente con el diseño del juego.
     * </p>
     */
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
