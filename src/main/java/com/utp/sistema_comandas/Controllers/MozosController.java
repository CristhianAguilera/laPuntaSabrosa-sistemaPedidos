package com.utp.sistema_comandas.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @GetMapping("/admin/dotMozos")
    public String dotMozos(HttpSession session, Model model) {
        List<Usuario> usuarios = usuarioService.obtenerLosUsuarios();
        model.addAttribute("usuarios", usuarios);

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("usuario", usuario);
        return "admin/dotMozos";
    }

    @GetMapping("/editarusuarioModal/{usuarioId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerUsuarioporId(@PathVariable Long usuarioId, Model model) {
        // con el id del trabajdor buscamos su permiso ligado a el
        Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuarioId);

        // verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get(); // Extraemos el objeto Usuario

            Map<String, Object> usuarioData = new HashMap<>();
            usuarioData.put("id", usuario.getId()); // también es clave para edición
            usuarioData.put("nombre", usuario.getNombre());
            usuarioData.put("apellido", usuario.getApellido());
            usuarioData.put("correo", usuario.getCorreo());
            usuarioData.put("dni", usuario.getDni());
            usuarioData.put("telefono", usuario.getTelefono());
            usuarioData.put("estado", usuario.getEstado());

            return ResponseEntity.ok(usuarioData);
        } else {
            // retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró este usuario."));
        }
    }

    @PostMapping("/EditarMozo")
    @ResponseBody
    public ResponseEntity<?> editarMozo(@ModelAttribute Usuario usuario) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(usuario.getId());
            if (usuarioOpt.isPresent()) {
                Usuario usuarioAct = usuarioOpt.get();

                usuarioAct.setNombre(usuario.getNombre());
                usuarioAct.setApellido(usuario.getApellido());
                usuarioAct.setTelefono(usuario.getTelefono());
                usuarioAct.setEstado(usuario.getEstado());

                usuarioService.guardUsuario(usuarioAct);
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al editar el mozo"));
        }
    }
     @PostMapping("/ActualizarEstadoMozo")
    @ResponseBody
    public ResponseEntity<?> ActualizarEstadoMozo(@RequestParam("id") Long id) {

        try {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerPorId(id);
            if (usuarioOpt.isPresent()) {
                Usuario usuarioAct = usuarioOpt.get();

                usuarioAct.setEstado("Inactivo");

                usuarioService.guardUsuario(usuarioAct);
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Usuario no encontrado"));
            }
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al Modificar el estado del mozo"));
        }
    }
}
