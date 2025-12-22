package Main.ui.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GeneradorAssets {

    public static void generarTodo(String rutaDestino) {
        File directorio = new File(rutaDestino);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        generarImagen(rutaDestino + "/jugador.png", Color.CYAN, "🤠");
        generarImagen(rutaDestino + "/muro.png", Color.GRAY, "🧱");
        generarImagen(rutaDestino + "/muro_rojo.png", Color.RED, "🟥");
        generarImagen(rutaDestino + "/suelo.png", Color.DARK_GRAY, "");
        generarImagen(rutaDestino + "/cristal.png", Color.MAGENTA, "💎");
        generarImagen(rutaDestino + "/bomba.png", Color.BLACK, "💣");
        generarImagen(rutaDestino + "/llave.png", Color.YELLOW, "🔑");
        generarImagen(rutaDestino + "/fosforo.png", Color.ORANGE, "🗝️");
        generarImagen(rutaDestino + "/salida.png", Color.GREEN, "🚪");
        generarImagen(rutaDestino + "/trampa.png", new Color(100, 0, 0), "💀");
        generarImagen(rutaDestino + "/energia.png", Color.BLUE, "⚡");
        generarImagen(rutaDestino + "/vida.png", Color.PINK, "❤️");
        generarImagen(rutaDestino + "/niebla.png", new Color(0, 0, 0, 240), "☁️");
    }

    private static void generarImagen(String ruta, Color colorFondo, String emoji) {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Fondo
        g2d.setColor(colorFondo);
        g2d.fillRect(0, 0, size, size);

        // Borde
        g2d.setColor(colorFondo.darker());
        g2d.drawRect(0, 0, size - 1, size - 1);

        // Emoji / Icono
        if (!emoji.isEmpty()) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
            FontMetrics fm = g2d.getFontMetrics();
            int x = (size - fm.stringWidth(emoji)) / 2;
            int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
            g2d.drawString(emoji, x, y);
        }

        g2d.dispose();

        try {
            ImageIO.write(image, "png", new File(ruta));
            System.out.println("Generado: " + ruta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Generar en src/resources/imagenes
        generarTodo("src/resources/imagenes");
    }
}
