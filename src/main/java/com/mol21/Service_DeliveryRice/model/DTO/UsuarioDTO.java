package com.mol21.Service_DeliveryRice.model.DTO;
import com.mol21.Service_DeliveryRice.model.Rol;
import com.mol21.Service_DeliveryRice.model.Usuario;
public class UsuarioDTO {

    private Long id;
    private String email;
    private String nombre;
    private String apellido;
    private String telefono;
    private Rol rol;


    public UsuarioDTO(Usuario usuario){
        if(usuario != null) {
            this.id = usuario.get_id();
            this.email = usuario.getEmail();
            this.nombre = usuario.getNombre();
            this.apellido = usuario.getApellido();
            this.rol = usuario.getRol();
            this.telefono = usuario.getTelefono();
        } else {
            this.id = null;
            this.nombre = "Desconocido";
            this.email = "Sin email";
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }



    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }
}
