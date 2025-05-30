package com.utp.sistema_comandas.repository;


import java.util.List;
import java.util.Optional;

import com.utp.sistema_comandas.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    
    Usuario findByCorreo(String correo);

    List<Usuario> findByCorreoAndContrasena(String correo, String contrasena);

    List<Usuario> findByRol(String rol);

    @Override
    List<Usuario> findAll();

    Optional findById(Long usuarioid);
    
}
