package com.example.busdevelop.buses;

/**
<<<<<<< HEAD
 *
=======
 * Created by jossue on 19/09/14.
>>>>>>> 138fb32b243a3b86f0b7927d3f00d8270547f160
 * Clase que guardara los datos de un usuario
 * para poder enviar objetos JSON o recibirlos
 * y manejarlos de mejor manera
 */
public class Usuario {
    private String email;
    private String nombre;
    private String encrypted_password;
    private String fechaNac;
    private String ciudad;

    //Constructor por omision
    public Usuario(){

    }

    public Usuario(String email,String nombre, String password,
                   String fechaNac, String ciudad){
        this.email = email;
        this.nombre = nombre;
        this.encrypted_password = password;
        this.fechaNac = fechaNac;
        this.ciudad = ciudad;
    }

    public String getEncrypted_password() {
        return encrypted_password;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEncrypted_password(String encrypted_password) {
        this.encrypted_password = encrypted_password;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

<<<<<<< HEAD
}
=======
}
>>>>>>> 138fb32b243a3b86f0b7927d3f00d8270547f160
