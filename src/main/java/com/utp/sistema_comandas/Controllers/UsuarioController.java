package com.utp.sistema_comandas.Controllers;

import java.util.ArrayList;
import java.util.Optional;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/listar")
    public ArrayList<Usuario> obtenerUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @GetMapping("/correo/{correo}")
    public ArrayList<Usuario> obtenerPorCorreo(@PathVariable String correo) {
        return usuarioService.obtenerPorCorreo(correo);
    }

    @PostMapping("/guardar")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return this.usuarioService.guardUsuario(usuario);
    }

    @GetMapping("/id/{id}")
    public Optional<Usuario> obtenerPorId(@PathVariable Long id) {
        return this.usuarioService.obtenerPorId(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        boolean ok = this.usuarioService.eliminarUsuario(id);
        if (ok) {
            return "Se eliminó el usuario con id: " + id;
        } else {
            return "No se pudo eliminar el usuario con id: " + id;
        }

    }

}
