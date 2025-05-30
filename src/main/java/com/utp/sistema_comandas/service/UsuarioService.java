
package com.utp.sistema_comandas.service;


import java.util.List;
import java.util.Optional;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public List<Usuario> obtenerPorCorreoYContrasena(String correo, String contrasena) {
        return usuarioRepository.findByCorreoAndContrasena(correo, contrasena);
    }

    public Optional<Usuario> obtenerPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId);
    }

    @Transactional
    public Usuario guardUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public boolean eliminarUsuario(Long id) {
        try {
            usuarioRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

}
