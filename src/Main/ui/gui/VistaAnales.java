package Main.ui.gui;

import Main.modelo.Dominio.EstadisticasJuego;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class VistaAnales extends BorderPane {

    private Runnable onBack;

    public VistaAnales(List<EstadisticasJuego> estadisticas, Runnable onBack) {
        this.onBack = onBack;
        inicializarGUI(estadisticas);
    }

    private void inicializarGUI(List<EstadisticasJuego> estadisticas) {
        this.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #332b1a, #1a150a); -fx-border-color: #DAA520; -fx-border-width: 3;");
        this.setPadding(new Insets(20));

        Label title = new Label("📜 ANALES DEL TEMPLO");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: gold;");
        BorderPane.setAlignment(title, Pos.CENTER);
        this.setTop(title);

        TableView<EstadisticasJuego> table = new TableView<>();
        table.setStyle("-fx-background-color: #1a150a; " +
                "-fx-control-inner-background: #1a150a; " +
                "-fx-text-fill: #DAA520; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 1; " +
                "-fx-font-family: 'Serif';");

        TableColumn<EstadisticasJuego, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaFormateada"));
        colFecha.setPrefWidth(150);

        TableColumn<EstadisticasJuego, String> colResultado = new TableColumn<>("Resultado");
        colResultado.setCellValueFactory(cellData -> {
            boolean ganado = cellData.getValue().isGanado();
            return new javafx.beans.property.SimpleStringProperty(ganado ? "GANADO" : "PERDIDO");
        });
        colResultado.setPrefWidth(100);

        TableColumn<EstadisticasJuego, Long> colTiempo = new TableColumn<>("Tiempo (s)");
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoSegundos"));

        TableColumn<EstadisticasJuego, Integer> colCristales = new TableColumn<>("💎");
        colCristales.setCellValueFactory(new PropertyValueFactory<>("cristalesRecolectados"));

        TableColumn<EstadisticasJuego, Integer> colBombas = new TableColumn<>("💣");
        colBombas.setCellValueFactory(new PropertyValueFactory<>("bombasRecolectadas"));

        TableColumn<EstadisticasJuego, Integer> colFosforos = new TableColumn<>("🔥");
        colFosforos.setCellValueFactory(new PropertyValueFactory<>("fosforosUsados"));

        TableColumn<EstadisticasJuego, Integer> colMuros = new TableColumn<>("💥");
        colMuros.setCellValueFactory(new PropertyValueFactory<>("murosDestruidos"));

        TableColumn<EstadisticasJuego, String> colTamanio = new TableColumn<>("Tamaño");
        colTamanio.setCellValueFactory(new PropertyValueFactory<>("tamanioLaberinto"));

        @SuppressWarnings("unchecked")
        TableColumn<EstadisticasJuego, ?>[] columns = new TableColumn[] {
                colFecha, colResultado, colTiempo, colCristales, colBombas, colFosforos, colMuros, colTamanio
        };
        table.getColumns().addAll(columns);

        ObservableList<EstadisticasJuego> data = FXCollections.observableArrayList(estadisticas);
        table.setItems(data);

        this.setCenter(table);

        Button btnVolver = new Button("Volver");
        btnVolver.setStyle("-fx-background-color: linear-gradient(to bottom, #555, #222); " +
                "-fx-text-fill: #DAA520; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-min-width: 150px; " +
                "-fx-border-color: #DAA520; " +
                "-fx-border-width: 1; " +
                "-fx-cursor: hand;");
        btnVolver.setOnMouseEntered(e -> btnVolver.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #777, #444); -fx-text-fill: #FFD700; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 150px; -fx-border-color: #FFD700; -fx-border-width: 2; -fx-cursor: hand;"));
        btnVolver.setOnMouseExited(e -> btnVolver.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #555, #222); -fx-text-fill: #DAA520; -fx-font-size: 14px; -fx-font-weight: bold; -fx-min-width: 150px; -fx-border-color: #DAA520; -fx-border-width: 1; -fx-cursor: hand;"));
        btnVolver.setOnAction(e -> onBack.run());

        HBox bottom = new HBox(btnVolver);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(20, 0, 0, 0));
        this.setBottom(bottom);
    }
}
