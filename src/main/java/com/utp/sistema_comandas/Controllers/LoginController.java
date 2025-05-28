package com.utp.sistema_comandas.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.utp.sistema_comandas.service.UsuarioService;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String correoelectronico,
            @RequestParam String contrasena, Model model) {

        if (usuarioService.obtenerPorCorreoYContrasena(correoelectronico, contrasena)
                .isEmpty()) {
            return "redirect:/?error=Usuario+no+encontrada";
        } else if (usuarioService.obtenerPorCorreo(correoelectronico).get(0).getContrasena().equals(contrasena)) {
            return "redirect:/Pedidos";
        } else {
            return "redirect:/?error=Contraseña+incorrecta";
        }

    }
}
