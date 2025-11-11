package Main.modelo.Dominio;

public class Usuario {
    private String email;
    private String contraseniaCifrada;
    private String fechaRegistro;

    public Usuario(String email, String contraseniaCifrada, String fechaRegistro) {
        this.email = email;
        this.contraseniaCifrada = contraseniaCifrada;
        this.fechaRegistro = fechaRegistro;
    }
    //Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseniaCifrada() {
        return contraseniaCifrada;
    }

    public void setContraseniaCifrada(String contraseniaCifrada) {
        this.contraseniaCifrada = contraseniaCifrada;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    //metodos
    @Override
    public String toString() {
        return "Usuario{email='" + email + "', fechaRegistro='" + fechaRegistro + "'}";
    }

}
