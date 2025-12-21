package Main.servicio.Interfaces;

// Define el contrato para servicios de cifrado de contraseñas
public interface Cifrador {

    // Cifra una contraseña en texto plano
    public String cifrarContrasenia(String contrasenia);

    // Descifra una contraseña cifrada
    public String descifrarContrasenia(String contraseniaCifrada);
}
