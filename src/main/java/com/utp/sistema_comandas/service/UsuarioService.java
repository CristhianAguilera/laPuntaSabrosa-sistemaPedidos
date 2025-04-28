package com.utp.sistema_comandas.service;

import java.util.ArrayList;
import java.util.Optional;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public ArrayList<Usuario> obtenerUsuarios() {
        return (ArrayList<Usuario>) usuarioRepository.findAll();
    }

    public ArrayList<Usuario> obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public ArrayList<Usuario> obtenerPorCorreoYContrasena(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena);
    }

    public Usuario guardUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public boolean eliminarUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

}
