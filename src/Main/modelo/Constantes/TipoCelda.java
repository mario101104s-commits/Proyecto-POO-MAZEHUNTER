package Main.modelo.Constantes;

public enum TipoCelda {
    MURO('#', false, "Muro"),
    CAMINO('.', true, "Camino libre"),
    CRISTAL('C', true, "Cristal"),
    TRAMPA('T', true, "Trampa"),
    SALIDA('X', true, "Salida"),
    LLAVE('L', true, "Llave"),
    ENERGIA('E', true, "Energía"),
    VIDA('+', true, "Vida extra"),
    ENEMIGO('M', true, "Enemigo"),
    ENTRADA('S', true, "Entrada"),
    JUGADOR('@', true, "Jugador");

    private final char simbolo;
    private final boolean transitable;
    private final String descripcion;

    TipoCelda(char simbolo, boolean transitable, String descripcion) {
        this.simbolo = simbolo;
        this.transitable = transitable;
        this.descripcion = descripcion;
    }
    public char getSimbolo() { return simbolo; }
    public boolean isTransitable() { return transitable; }
    public String getDescripcion() { return descripcion; }
}

