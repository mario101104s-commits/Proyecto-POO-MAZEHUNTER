package Main.servicio.Implementaciones;

import Main.servicio.Interfaces.Persistencia;
import Main.servicio.Interfaces.ServicioUsuario;
public class ServicioUsuarioImpl implements ServicioUsuario {

    private final Persistencia persistencia;

    public ServicioUsuarioImpl(Persistencia persistencia) {
        this.persistencia = persistencia;
    }

}