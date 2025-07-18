package com.utp.sistema_comandas.Controllers;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.sistema_comandas.model.Categoria;
import com.utp.sistema_comandas.model.Producto;
import com.utp.sistema_comandas.service.CategoriaService;
import com.utp.sistema_comandas.service.ProductoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/admin/carta")
    public String carta(HttpSession session, Model model) {
        List<Producto> Productos = productoService.listarPorTipo("Carta");
        model.addAttribute("productos", Productos);

        List<Categoria> Categorias = categoriaService.listarTodasCategorias();
        model.addAttribute("categorias", Categorias);

        return "admin/carta";
    }

    @PostMapping("/registro-plato-carta")
    @ResponseBody
    public ResponseEntity<?> registroplatocarta(@ModelAttribute Producto producto) {
        try {
            producto.setTipo("Carta");

            productoService.guardarProducto(producto);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar Plato en Carta"));
        }

    }

    @GetMapping("/editarProductoModal/{productoId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenerproductoporId(@PathVariable Long productoId, Model model) {

        Optional<Producto> ProductoOpt = productoService.obtenerPorId(productoId);

        // verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (ProductoOpt.isPresent()) {
            Producto producto = ProductoOpt.get();

            Map<String, Object> productoData = new HashMap<>();

            productoData.put("nombre", producto.getNombre());
            productoData.put("precio", producto.getPrecio());
            productoData.put("id", producto.getId());
            productoData.put("categoria", producto.getCategoria().getId());

            return ResponseEntity.ok(productoData);
        } else {
            // retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró esta categoria."));
        }
    }

    @PostMapping("/editar-plato-carta")
    @ResponseBody
    public ResponseEntity<?> editarCarta(@ModelAttribute Producto producto) {
        try {
            Optional<Producto> productoOpt = productoService.obtenerPorId(producto.getId());
            if (productoOpt.isPresent()) {
                Producto productoAct = productoOpt.get();

                productoAct.setNombre(producto.getNombre());
                productoAct.setPrecio(producto.getPrecio());
                productoAct.setCategoria(producto.getCategoria());

                productoService.guardarProducto(productoAct);
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Producto no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al editar Plato en Carta"));
        }
    }

    @PostMapping("/eliminar-Plato-carta")
    @ResponseBody
    public ResponseEntity<?> eliminarPlatocarta(@RequestParam("id") Long id) {
        try {
            productoService.eliminar(id);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al eliminar Plato de la Carta"));
        }
    }

}
