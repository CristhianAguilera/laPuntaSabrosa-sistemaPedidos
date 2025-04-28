package com.utp.sistema_comandas.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PedidosController {
    
    @GetMapping("/Pedidos")
    public String Pedidos() {
        return "Pedidos";
    }

}
