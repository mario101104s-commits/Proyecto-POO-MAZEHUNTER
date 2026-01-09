package Main.ui.gui.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Gestor centralizado para la música de fondo y efectos de sonido del juego.
 * <p>
 * Implementa el patrón Singleton para asegurar una única instancia de
 * reproducción y proporcionar control global sobre el audio. Maneja tanto
 * pistas de música en formato MP3/Media como efectos de sonido en formato WAV.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 22/12/25
 */
public class GestorAudio {
    /** Instancia única del gestor (Singleton). */
    private static GestorAudio instancia;
    
    /** Reproductor de JavaFX para música de fondo. */
    private MediaPlayer mediaPlayer;
    
    /** Nombre de la pista actualmente en reproducción. */
    private String trackActual = "";
    
    /** Nivel de volumen actual (0.0 a 1.0). */
    private double volumen = 0.5;
    
    /** Estado de silencio del audio. */
    private boolean silenciado = false;

    /**
     * Constructor privado para implementar el patrón Singleton.
     */
    private GestorAudio() {
    }

    /**
     * Obtiene la instancia única del gestor de audio.
     * <p>
     * Si no existe una instancia, la crea automáticamente. Este método es
     * thread-safe para evitar problemas de concurrencia.
     * </p>
     * 
     * @return La instancia única de {@link GestorAudio}.
     */
    public static synchronized GestorAudio getInstancia() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    /**
     * Cambia la música de fondo según el track solicitado.
     * <p>
     * Solo reinicia si el track es diferente al que ya se está reproduciendo.
     * Busca los archivos de audio en el directorio {@code /resources/audio/}.
     * </p>
     * 
     * @param track Nombre del archivo de audio (ej: "principal.mp3", "juego.mp3").
     */
    public void reproducirMusica(String track) {
        if (!track.equals(trackActual)) {
            try {
                URL resource = getClass().getResource("/audio/" + track);
                if (resource != null) {
                    Media media = new Media(resource.toString());
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(silenciado ? 0 : volumen);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    mediaPlayer.play();
                    trackActual = track;
                }
            } catch (Exception e) {
                System.err.println("Error al cargar música: " + track);
            }
        }
    }

    /**
     * Alterna el estado de silencio del audio.
     * <p>
     * Cuando se silencia, el volumen se establece en 0. Cuando se reactiva,
     * se restaura el volumen anterior.
     * </p>
     */
    public void toggleSilencio() {
        silenciado = !silenciado;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(silenciado ? 0 : volumen);
        }
    }

    /**
     * Verifica si el audio está actualmente silenciado.
     * 
     * @return {@code true} si el audio está silenciado, {@code false} en caso contrario.
     */
    public boolean isSilenciado() {
        return silenciado;
    }

    /**
     * Establece el nivel de volumen del audio.
     * <p>
     * El valor se aplica inmediatamente si hay música reproduciéndose.
     * No afecta el estado de silencio.
     * </p>
     * 
     * @param volumen Valor de volumen entre 0.0 (mínimo) y 1.0 (máximo).
     */
    public void setVolumen(double volumen) {
        this.volumen = Math.max(0.0, Math.min(1.0, volumen));
        if (mediaPlayer != null && !silenciado) {
            mediaPlayer.setVolume(this.volumen);
        }
    }

    /**
     * Obtiene el nivel de volumen actual.
     * 
     * @return Valor actual de volumen (0.0 a 1.0).
     */
    public double getVolumen() {
        return volumen;
    }

    /**
     * Detiene la reproducción actual y libera los recursos.
     * <p>
     * Útil para limpiar recursos cuando la aplicación se cierra o cambia
     * de escena.
     * </p>
     */
    public void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        trackActual = "";
    }

    /**
     * Obtiene el nombre de la pista de música actualmente en reproducción.
     * 
     * @return Nombre de la pista actual o cadena vacía si no hay música reproduciéndose.
     */
    public String getTrackActual() {
        return trackActual;
    }

    /**
     * Establece directamente el estado de silencio.
     * <p>
     * A diferencia de {@code toggleSilencio()}, este método permite establecer
     * explícitamente el estado sin alternarlo.
     * </p>
     * 
     * @param silenciado {@code true} para silenciar el audio, {@code false} para reactivarlo.
     */
    public void setSilenciado(boolean silenciado) {
        this.silenciado = silenciado;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(silenciado ? 0 : volumen);
        }
    }

    /**
     * Reproduce un efecto de sonido específico con mapeo predefinido.
     * <p>
     * Este método utiliza un sistema de mapeo para asociar nombres lógicos
     * con archivos de audio específicos y se ejecuta en un hilo separado
     * para no bloquear la interfaz de usuario.
     * </p>
     * 
     * @param efecto Nombre lógico del efecto (ej: "cristal", "explosion", "llave").
     */
    public void reproducirEfecto(String efecto) {
        if (silenciado) return;
        new Thread(() -> {
            try {
                String archivoWav = switch (efecto) {
                    case "cristal" -> "cristal.wav";
                    case "explosion" -> "explosion.wav";
                    case "llave" -> "llave.wav";
                    case "item" -> "recoger.wav"; // Para fosforos y bombas
                    case "trampa" -> "trampa.wav";
                    case "energia" -> "energia.wav";
                    default -> "recoger.wav";
                };
                
                URL resource = getClass().getResource("/audio/" + archivoWav);
                if (resource != null) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(resource);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.setFramePosition(0);
                    clip.start();
                    
                    // Esperar a que termine el sonido para cerrar recursos
                    Thread.sleep(clip.getMicrosecondLength() / 1000);
                    clip.close();
                    audioStream.close();
                } else {
                    System.err.println("⚠️ No se encontró el archivo de audio: " + archivoWav);
                }
            } catch (Exception e) {
                System.err.println("❌ Error al reproducir efecto (" + efecto + "): " + e.getMessage());
            }
        }, "SFX-Thread").start();
    }
}
