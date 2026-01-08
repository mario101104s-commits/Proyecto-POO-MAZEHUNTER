package Main.ui.gui;

import Main.modelo.Dominio.EstadisticasJuego;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import Main.ui.gui.audio.ControladorAudioUI;

/**
 * Vista de estadísticas de Maze Hunter con controles de audio integrados.
 */
public class VentanaEstadisticasPartida extends BorderPane {

        private static final String BACKGROUND_IMAGE_PATH = "/imagenes/fondo4.jpg";
        private static final String BUTTON_IMAGE_PATH = "/imagenes/boton2.jpg";

        public VentanaEstadisticasPartida(EstadisticasJuego stats, Runnable onVolver) {
                inicializarGUI(stats, onVolver);
        }

        private void inicializarGUI(EstadisticasJuego stats, Runnable onVolver) {
                // Fondo
                try {
                        Image fondo = new Image(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH));
                        this.setBackground(new Background(new BackgroundImage(fondo,
                                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                        BackgroundPosition.CENTER,
                                        new BackgroundSize(100, 100, true, true, false, true))));
                } catch (Exception e) {
                        this.setStyle("-fx-background-color: #1a150a;");
                }
                this.setPadding(new Insets(40));

                // Cabecera: Título + Audio
                Label titulo = new Label("RESUMEN DE LA AVENTURA");
                titulo.setStyle("-fx-font-family: 'Papyrus'; -fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #FFD700; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.9), 15, 0, 0, 5);");

                ControladorAudioUI audioUI = new ControladorAudioUI();
                StackPane top = new StackPane(titulo, audioUI);
                StackPane.setAlignment(audioUI, Pos.TOP_RIGHT);
                this.setTop(top);

                // Grid de Stats
                GridPane grid = new GridPane();
                grid.setAlignment(Pos.CENTER);
                grid.setHgap(80);
                grid.setVgap(25);
                grid.setPadding(new Insets(30));
                grid.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-background-radius: 20; -fx-border-color: #DAA520; -fx-border-width: 2; -fx-border-radius: 20;");

                int r = 0;
                addStat(grid, "Estado:", stats.isGanado() ? "VICTORIA" : "DERROTA",
                                stats.isGanado() ? "#2ecc71" : "#e74c3c", r++, 0);

                long s = stats.getTiempoSegundos();
                addStat(grid, "Tiempo ⏱️:", String.format("%02d:%02d", s / 60, s % 60), "#FFFFFF", r++, 0);
                addStat(grid, "Tamaño 📏:", stats.getTamanioLaberinto(), "#FFFFFF", r++, 0);
                addStat(grid, "Niebla  :", stats.isNieblaDeGuerra() ? "SÍ" : "NO", "#FFFFFF", r++, 0);

                r = 0;
                addStat(grid, "Cristales 💎:", String.valueOf(stats.getCristalesRecolectados()), "#00FBFF", r++, 1);
                addStat(grid, "Bombas 💣:", String.valueOf(stats.getBombasRecolectadas()), "#FFFFFF", r++, 1);
                addStat(grid, "Fósforos 🔥:", String.valueOf(stats.getFosforosUsados()), "#FF4500", r++, 1);
                addStat(grid, "Trampas 💀:", String.valueOf(stats.getTrampasActivadas()), "#FF0000", r++, 1);

                this.setCenter(grid);

                // Botón
                Button btn = new Button("VOLVER AL MENÚ");
                estilizar(btn);
                btn.setOnAction(e -> onVolver.run());
                HBox bot = new HBox(btn);
                bot.setAlignment(Pos.CENTER);
                bot.setPadding(new Insets(30, 0, 0, 0));
                this.setBottom(bot);
        }

        private void addStat(GridPane grid, String lblTxt, String valTxt, String color, int row, int col) {
                Label l = new Label(lblTxt);
                l.setStyle("-fx-font-family: 'Papyrus'; -fx-font-size: 24px; -fx-text-fill: #DAA520;");
                Label v = new Label(valTxt);
                v.setStyle("-fx-font-family: 'Papyrus'; -fx-font-size: 24px; -fx-text-fill: " + color
                                + "; -fx-font-weight: bold;");
                HBox box = new HBox(15, l, v);
                box.setAlignment(Pos.CENTER_LEFT);
                grid.add(box, col, row);
        }

        private void estilizar(Button b) {
                String st = "-fx-background-image: url('" + BUTTON_IMAGE_PATH
                                + "'); -fx-background-size: 100% 100%; -fx-font-family: 'Papyrus'; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand; -fx-border-color: #DAA520; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 10 30;";
                b.setStyle(st);
                b.setOnMouseEntered(e -> b.setStyle(st
                                + " -fx-text-fill: #FFD700; -fx-scale-x: 1.1; -fx-scale-y: 1.1; -fx-border-color: #FFD700;"));
                b.setOnMouseExited(e -> b.setStyle(st));
        }
}
