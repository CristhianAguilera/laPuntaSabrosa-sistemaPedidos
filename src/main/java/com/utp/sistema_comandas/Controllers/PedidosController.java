package com.utp.sistema_comandas.Controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.sistema_comandas.dto.DetalleRequest;
import com.utp.sistema_comandas.dto.PedidoRequest;
import com.utp.sistema_comandas.model.Categoria;
import com.utp.sistema_comandas.model.DetallePedido;
import com.utp.sistema_comandas.model.HistorialPedido;
import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.model.Pedido;
import com.utp.sistema_comandas.model.Producto;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.service.CategoriaService;
import com.utp.sistema_comandas.service.DetallePedidoService;
import com.utp.sistema_comandas.service.HistorialPedidoService;
import com.utp.sistema_comandas.service.MesaService;
import com.utp.sistema_comandas.service.PedidoService;
import com.utp.sistema_comandas.service.ProductoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PedidosController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetallePedidoService detallePedidoService;

    @Autowired
    private HistorialPedidoService historialPedidoService;

    @GetMapping("/mozo/detallePedido")
    public String detallePedido(@RequestParam("numeroMesa") int numeroMesa,
            HttpSession session, Model model) {

        Mesa mesa = mesaService.buscarPorNumero(numeroMesa).orElseThrow();

        Pedido pedido = pedidoService.pedidosActivosPorMesa(mesa.getId());

        List<DetallePedido> detalles = pedido.getDetalles(); // relación en Pedido

        List<Categoria> Categorias = categoriaService.listarTodasCategorias();

        model.addAttribute("mesa", mesa);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("categorias", Categorias);

        // monto total sumando los subtotales 
        double total = detalles.stream().mapToDouble(DetallePedido::getSubtotal).sum();
        model.addAttribute("totalPedido", total);

        return "/mozo/detallePedido";
    }

    @GetMapping("/platosPorCategoria")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> obtenerPlatosPorCategoria(@RequestParam("id") Long categoriaId) {
        List<Producto> productos = productoService.listarPorCategoria(categoriaId);

        List<Map<String, Object>> resultado = new ArrayList<>(); // para almacenar los resultados y devuelve

        for (Producto p : productos) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", p.getId());
            map.put("nombre", p.getNombre());
            map.put("precio", p.getPrecio());
            resultado.add(map);
        }

        return ResponseEntity.ok(resultado);
    }

}