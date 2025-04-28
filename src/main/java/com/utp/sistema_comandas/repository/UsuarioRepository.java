package com.utp.sistema_comandas.repository;

import java.util.ArrayList;
import com.utp.sistema_comandas.model.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    
    public abstract ArrayList<Usuario> findByCorreo(String correo);

    public abstract ArrayList<Usuario> findByCorreoAndContrasena(String correo, String contrasena);
    
}
