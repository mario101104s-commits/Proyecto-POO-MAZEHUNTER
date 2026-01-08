package Main.ui.gui.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

/**
 * Gestor centralizado para la música de fondo del juego.
 * Implementa el patrón Singleton para asegurar una única instancia de
 * reproducción.
 */
public class GestorAudio {
    private static GestorAudio instancia;
    private MediaPlayer mediaPlayer;
    private String trackActual = "";
    private double volumen = 0.5;
    private boolean silenciado = false;

    private GestorAudio() {
    }

    public static synchronized GestorAudio getInstancia() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    /**
     * Cambia la música de fondo según el track solicitado.
     * Solo reinicia si el track es diferente al que ya se está reproduciendo.
     * 
     * @param track Nombre del archivo (ej. "principal.mp3")
     */
    public void reproducirMusica(String track) {
        if (track == null || track.isEmpty() || trackActual.equals(track)) {
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        try {
            URL resource = getClass().getResource("/audio/" + track);
            if (resource == null) {
                System.err.println("⚠️ No se encontró el recurso de audio: " + track);
                return;
            }

            Media media = new Media(resource.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(silenciado ? 0 : volumen);
            mediaPlayer.play();
            trackActual = track;

            System.out.println("🎵 Reproduciendo: " + track);
        } catch (Exception e) {
            System.err.println("❌ Error al reproducir audio (" + track + "): " + e.getMessage());
        }
    }

    public void setVolumen(double v) {
        this.volumen = Math.max(0, Math.min(1, v));
        if (mediaPlayer != null && !silenciado) {
            mediaPlayer.setVolume(this.volumen);
        }
    }

    public void setSilenciado(boolean s) {
        this.silenciado = s;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(s ? 0 : volumen);
        }
    }

    public double getVolumen() {
        return volumen;
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    public String getTrackActual() {
        return trackActual;
    }
}
