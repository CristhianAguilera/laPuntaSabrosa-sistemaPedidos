package com.utp.sistema_comandas.Controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.service.MesaService;

@Controller
public class MesasController {

    @Autowired
    private MesaService mesaService;

    @GetMapping("/admin/Mesas")
    public String Mesas(Model model) {
        List<Mesa> listaMesas = mesaService.listarTodas();
        model.addAttribute("mesas", listaMesas);
        return "/admin/Mesas";
    }

    @PostMapping("/agregarMesa")
    @ResponseBody
    public ResponseEntity<?> agregarMesa() {
        try {
            int ultimoNumero = mesaService.obtenerUltimoNumeroMesa();
            Mesa mesa = new Mesa();
            mesa.setNumero(ultimoNumero + 1);
            mesa.setCantidadPersonas(0);
            mesa.setMontoTotal(0.0);
            mesa.setNombreCliente("");
            mesa.setNombreMozo("");
            mesa.setOcupada(false);
            mesaService.guardar(mesa);
            return ResponseEntity.ok().body(mesa);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al Agregar mesa"));
        }
    }

    @GetMapping("/mozo/mesasMozo")
    public String mesasMozo(Model model) {
        List<Mesa> listaMesas = mesaService.listarTodas();
        model.addAttribute("mesasM", listaMesas);
        return "/mozo/mesasMozo"; 
    }
    @PostMapping("/eliminarMesa")
    @ResponseBody
    public ResponseEntity<?> eliminarMesa(@RequestParam("numeroMesa") int numeroMesa) {
        try {
            Optional<Mesa> mesa = mesaService.buscarPorNumero(numeroMesa);

            if (mesa.isPresent()) {
                mesaService.eliminar(mesa.get().getId());
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Mesa no encontrada"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar la Mesa"));
        }

    }
    
}
