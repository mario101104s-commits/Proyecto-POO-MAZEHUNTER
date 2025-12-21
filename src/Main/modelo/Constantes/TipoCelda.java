package Main.modelo.Constantes;

/**
 * Esta clase define los diferentes tipo de celdas que puede componer el
 * laberinto.
 * <p>
 * Cada constante almacena propiedades clave como el simbolo que representa,
 * si es transitable y una descripcion para el usuario.
 * </p>
 * 
 * @author Mario Sanchez
 * @version 1.0
 * @since 11/11/2025
 */
public enum TipoCelda {
    /** Una pared que bloquea el paso. */
    MURO('#', false, "Muro"),
    /** Espacio vacío por donde el jugador puede moverse libremente. */
    CAMINO('.', true, "Camino libre"),
    /** Celda transparente que podría tener propiedades especiales o efectos. */
    CRISTAL('C', true, "Cristal"),
    /** Una celda peligrosa que causa daño o aplica un efecto negativo. */
    TRAMPA('T', true, "Trampa"),
    /** El objetivo final del laberinto. */
    SALIDA('X', true, "Salida"),
    /** Objeto que el jugador debe recoger para desbloquear ciertas áreas. */
    LLAVE('L', true, "Llave"),
    /** Ítem que recupera o mejora la barra de energía del jugador. */
    ENERGIA('E', true, "Energía"),
    /** Ítem que otorga vida adicional al jugador. */
    VIDA('+', true, "Vida extra"),
    /** Una criatura que se mueve y ataca al jugador. */
    ENEMIGO('M', true, "Enemigo"),
    /** El punto de inicio del jugador en el laberinto. */
    ENTRADA('S', true, "Entrada"),
    /** La posición actual del jugador en el mapa. */
    JUGADOR('@', true, "Jugador"),
    /** Bomba que se puede recolectar para usar en explosiones. */
    BOMBA('B', true, "Bomba"),
    /** Muro rojo que puede ser destruido con explosiones. */
    MURO_ROJO('%', false, "Muro rojo"),
    /** Llave de explosión necesaria para activar bombas. */
    LLAVE_EXPLOSION('K', true, "Llave de explosión");

    /**
     * El carácter que se utiliza para dibujar la celda en la consola.
     */
    private final char simbolo;
    /**
     * Indica si el jugador puede moverse a esta celda (true) o si es un muro
     * (false).
     */
    private final boolean transitable;
    /**
     * Descripción larga para mostrar información al usuario.
     */
    private final String descripcion;

    /**
     * Constructor para inicializar un tipo de celda con sus propiedades.
     *
     * @param simbolo     El carácter de representación en el mapa.
     * @param transitable Indica si el jugador puede pasar sobre esta celda.
     * @param descripcion La explicación del tipo de celda.
     */
    TipoCelda(char simbolo, boolean transitable, String descripcion) {
        this.simbolo = simbolo;
        this.transitable = transitable;
        this.descripcion = descripcion;
    }

    /**
     * @return El carácter que representa esta celda en la consola.
     */
    public char getSimbolo() {
        return simbolo;
    }

    /**
     * @return {@code true} si la celda permite el paso del jugador, {@code false}
     *         si es un muro.
     */
    public boolean isTransitable() {
        return transitable;
    }

    /**
     * @return La descripción detallada del tipo de celda.
     */
    public String getDescripcion() {
        return descripcion;
    }
}
