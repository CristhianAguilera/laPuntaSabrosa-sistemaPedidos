package com.utp.sistema_comandas.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.model.Pedido;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.service.MesaService;
import com.utp.sistema_comandas.service.PedidoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MesasController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/admin/Mesas")
    public String Mesas(Model model,HttpSession session) {
        List<Mesa> listaMesas = Optional.ofNullable(mesaService.listarTodas()).orElse(new ArrayList<>());
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

    /*-------------------------------------------------------Mozo------------------------------------------------- */
    @GetMapping("/mozo/mesasMozo")
    public String mesasMozo(Model model) {
        List<Mesa> listaMesas = mesaService.listarTodas();
        model.addAttribute("mesasM", listaMesas);
        return "/mozo/mesasMozo";
    }

    @PostMapping("/verificarMozoMesa")
    @ResponseBody
    public ResponseEntity<?> verificarMozoMesa(@RequestBody Map<String, Object> payload, HttpSession session) {
        int numeroMesa = Integer.parseInt(payload.get("numeroMesa").toString());
        Optional<Mesa> optMesa = mesaService.buscarPorNumero(numeroMesa);

        if (optMesa.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mesa no encontrada"));
        }

        Mesa mesa = optMesa.get();
        Usuario mozo = (Usuario) session.getAttribute("usuario");

        boolean pertenece = mesa.getNombreMozo().equalsIgnoreCase(mozo.getNombre() + " " + mozo.getApellido());

        if (!pertenece) {
            return ResponseEntity.ok(Map.of("autorizado", false, "mensaje", "Esta mesa no te pertenece"));
        }

        return ResponseEntity.ok(Map.of("autorizado", true));
    }


    @PostMapping("/aperturarMesa")
    @ResponseBody
    public ResponseEntity<?> aperturarMesa(@RequestBody Map<String, Object> payload, HttpSession session) {
        int numero = Integer.parseInt(payload.get("numero").toString());
        int cantidadPersonas = Integer.parseInt(payload.get("cantidadPersonas").toString());
        String nombreCliente = payload.get("nombreCliente").toString();

        Optional<Mesa> optMesa = mesaService.buscarPorNumero(numero);
        if (optMesa.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Mesa no encontrada"));
        }

        Mesa mesa = optMesa.get();
        mesa.setOcupada(true);
        mesa.setCantidadPersonas(cantidadPersonas);
        mesa.setNombreCliente(nombreCliente);

        // Obtener mozo desde sesión
        Usuario mozo = (Usuario) session.getAttribute("usuario");
        mesa.setNombreMozo(mozo.getNombre() + " " + mozo.getApellido());

        mesaService.guardar(mesa);

        Pedido pedido = new Pedido();
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setMesa(mesa);
        pedido.setMozo(mozo);
        pedido.setFinalizado(false);

        pedidoService.crearPedido(pedido);

        return ResponseEntity.ok(Map.of("success", true, "mesaId", mesa.getId()));

    }
    
}
