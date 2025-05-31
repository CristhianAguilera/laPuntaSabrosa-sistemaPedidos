package com.utp.sistema_comandas.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MozosController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/admin/registrarMozo")
    public String registrarMozos(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        return "admin/registrarMozo";
    }

    @PostMapping("/registroMozos")
    @ResponseBody
    public ResponseEntity<?> procesarRegistro(@ModelAttribute Usuario usuario) {
        try {
            usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            usuario.setCorreo(usuario.getCorreo());
            usuario.setNombre(usuario.getNombre());
            usuario.setApellido(usuario.getApellido());
            usuario.setTelefono(usuario.getTelefono());
            usuario.setDni(usuario.getDni());
            usuario.setRol("MOZO");
            usuario.setEstado("Activo");
            usuarioService.guardUsuario(usuario);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar mozo"));
        }

    }
    
}
