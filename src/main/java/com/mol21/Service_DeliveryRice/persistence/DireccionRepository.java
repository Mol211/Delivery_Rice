package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Direccion;
import com.mol21.Service_DeliveryRice.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository extends CrudRepository<Direccion, Long> {
    //Obtener todas las direcciones de un usuario
    List<Direccion> findByUsuario_id(long usuarioId);

    Optional<Direccion> findByUsuario_idAndEsPrincipalTrue(long usuarioId);

    //Buscar la direccion principal de un usuario
    @Modifying
    @Query("UPDATE Direccion D SET D.esPrincipal = false WHERE D.usuario.id=:usuarioId")
    void resetPrincipal(@Param("usuarioId") long usuarioId);

    Optional<Direccion> findByUsuarioAndCalleAndNumeroAndCiudadAndCodPostal(Usuario usuario, String calle, String numero, String ciudad, String codPostal);
}
