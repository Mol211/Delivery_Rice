package com.mol21.Service_DeliveryRice.persistence;

import com.mol21.Service_DeliveryRice.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    @Query("SELECT u.email FROM Usuario u WHERE u.id = :id")
    String findEmailById(long id);

    Optional<Usuario> findByEmail(String email);
}
