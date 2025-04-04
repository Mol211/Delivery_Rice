package com.mol21.Service_DeliveryRice.model.DTO;
import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;

public class RegistrarUsuarioDTO {
    Usuario u;
    Direccion d;

    public  RegistrarUsuarioDTO(Usuario u, Direccion d) {
        this.u = u;
        this.d = d;
    }

    public Usuario getU() {
        return u;
    }

    public void setU(Usuario u) {
        this.u = u;
    }

    public Direccion getD() {
        return d;
    }

    public void setD(Direccion d) {
        this.d = d;
    }
}
