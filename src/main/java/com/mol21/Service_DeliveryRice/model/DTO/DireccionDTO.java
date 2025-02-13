package com.mol21.Service_DeliveryRice.model.DTO;

import com.mol21.Service_DeliveryRice.model.Direccion;

public class DireccionDTO {
    private long id;
    private String calle;
    private String numero;
    private String codPostal;
    private String ciudad;
    private boolean esPrincipal;
    private String emailUsuario;
    //De momento dejo el email, pero no me sirve para nada


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String email) {
        this.emailUsuario = email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public boolean isEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }

    public DireccionDTO() {
    }

    public DireccionDTO(Direccion direccion) {
        this.id = direccion.getDireccion_id();
        this.calle = direccion.getCalle();
        this.numero = direccion.getNumero();
        this.ciudad = direccion.getCiudad();
        this.codPostal = direccion.getCodPostal();
        this.esPrincipal = direccion.isEsPrincipal();
    }
}
