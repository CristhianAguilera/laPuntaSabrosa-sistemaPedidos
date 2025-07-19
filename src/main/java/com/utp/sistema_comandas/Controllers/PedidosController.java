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
import com.utp.sistema_comandas.service.boletaService;

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

    @Autowired
    private boletaService boletaService;

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

    @PostMapping("/registar-detalle-pedido")
    public ResponseEntity<?> guardarDetallePedido(@RequestBody PedidoRequest request) {
        Optional<Pedido> optPedido = pedidoService.obtenerPorId(request.getPedidoId());
        if (optPedido.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error", "Pedido no encontrado"));

        Pedido pedido = optPedido.get();
        double total = 0;
        List<Long> nuevosIds = new ArrayList<>();

        for (DetalleRequest d : request.getDetalles()) {
            if (d.getProductoId() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto ID nulo en la solicitud"));
            }

            Optional<Producto> optproducto = productoService.obtenerPorId(d.getProductoId());
            if (optproducto.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto no encontrado"));
            }

            Producto producto = optproducto.get();

            // Busca si ya existe ese producto en el pedido
            DetallePedido existente = pedido.getDetalles().stream()
                    .filter(det -> det.getProducto().getId().equals(producto.getId()))
                    .findFirst()
                    .orElse(null);

            if (existente != null) {
                // Ya existe: actualiza cantidad y subtotal
                existente.setCantidad(d.getCantidad());
                existente.setSubtotal(d.getSubtotal());
                detallePedidoService.guardar(existente); // actualizar
                nuevosIds.add(existente.getId());
            } else {
                // No existe: crea uno nuevo
                DetallePedido nuevo = new DetallePedido();
                nuevo.setProducto(producto);
                nuevo.setCantidad(d.getCantidad());
                nuevo.setSubtotal(d.getSubtotal());
                nuevo.setPedido(pedido);
                DetallePedido guardado = detallePedidoService.guardar(nuevo);
                nuevosIds.add(guardado.getId());
            }

            total += d.getSubtotal();
        }

        Mesa mesa = pedido.getMesa();
        mesa.setMontoTotal(total);
        mesaService.guardar(mesa);

        return ResponseEntity.ok(Map.of("success", true, "nuevosIds", nuevosIds));
    }
    /*-------------------------------------------------Generar Boleta PDF--------------------------------------------------- */
    @GetMapping("/mozo/verificarPedido/{id}")
    @ResponseBody
    public Map<String, Object> verificarPedido(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.obtenerPorId(id);
        boolean hayDetalles = pedido != null && !pedido.get().getDetalles().isEmpty();

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("hayDetalles", hayDetalles);
        return resultado;
    }

    @GetMapping("/mozo/boleta/{id}")
    public void generarBoletaPDF(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Optional<Pedido> pedido = pedidoService.obtenerPorId(id);

        if (pedido == null || pedido.get().getDetalles().isEmpty()) {
            response.sendRedirect("/mozo/mesas"); // por seguridad
            return;
        }

        byte[] pdfBytes = boletaService.generarBoletaPDF(pedido.get());

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=boleta_mesa_" + pedido.get().getMesa().getNumero() + ".pdf");
        response.getOutputStream().write(pdfBytes);
        response.getOutputStream().flush();
    }

}