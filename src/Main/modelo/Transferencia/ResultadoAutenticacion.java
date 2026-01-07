package Main.modelo.Transferencia;

/**
 * Objeto de transferencia que encapsula el resultado de un intento de
 * autenticación.
 * <p>
 * Permite comunicar tanto el éxito como el motivo específico del fallo entre
 * el controlador y la vista, mejorando la experiencia de usuario con mensajes
 * claros.
 * </p>
 *
 * @author Mario Sanchez
 * @version 1.0
 * @since 06/01/26
 */
public class ResultadoAutenticacion {
    private boolean exitoso;
    private String email;
    private String mensajeError;

    /**
     * Constructor para un resultado exitoso.
     *
     * @param email El correo electrónico del usuario autenticado.
     */
    public ResultadoAutenticacion(String email) {
        this.exitoso = true;
        this.email = email;
        this.mensajeError = null;
    }

    /**
     * Constructor para un resultado fallido con mensaje de error específico.
     *
     * @param exitoso      Debe ser {@code false} para indicar fallo.
     * @param mensajeError Descripción del motivo del fallo.
     */
    public ResultadoAutenticacion(boolean exitoso, String mensajeError) {
        this.exitoso = exitoso;
        this.email = null;
        this.mensajeError = mensajeError;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getEmail() {
        return email;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
