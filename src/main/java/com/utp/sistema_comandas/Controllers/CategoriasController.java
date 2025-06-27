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
import org.springframework.web.bind.annotation.ResponseBody;

import com.utp.sistema_comandas.model.Categoria;
import com.utp.sistema_comandas.service.CategoriaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CategoriasController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/admin/categorias")
    public String dotMozos(HttpSession session, Model model) {
        List<Categoria> Categorias = categoriaService.listarTodasCategorias();
        model.addAttribute("categorias", Categorias);

        return "admin/categorias";
    }

    @PostMapping("/registroCategoria")
    @ResponseBody
    public ResponseEntity<?> registroCategoria(@ModelAttribute Categoria categoria) {
        try {    
            categoria.setNombre(categoria.getNombre());
          
            categoriaService.guardarCategoria(categoria);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al registrar Categoria"));
        }

    }

    @GetMapping("/editarcategoriaModal/{categoriaId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> obtenercategoriaporId(@PathVariable Long categoriaId, Model model) {

        Optional<Categoria> CategoriaOpt = categoriaService.obtenerPorId(categoriaId);

        // verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (CategoriaOpt.isPresent()) {
            Categoria categoria = CategoriaOpt.get(); 

            Map<String, Object> categoriaData = new HashMap<>();

            categoriaData.put("nombre", categoria.getNombre());
            categoriaData.put("categoriaId", categoria.getId());

            return ResponseEntity.ok(categoriaData);
        } else {
            // retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró esta categoria."));
        }
    }

    @PostMapping("/editarCategoria")
    @ResponseBody
    public ResponseEntity<?> editarCategoria(@ModelAttribute Categoria categoria) {
        try {
            Optional<Categoria> categoriaOpt = categoriaService.obtenerPorId(categoria.getId());
            if (categoriaOpt.isPresent()) {
                Categoria categoriaAct = categoriaOpt.get();

                categoriaAct.setNombre(categoria.getNombre());

                categoriaService.guardarCategoria(categoriaAct);
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Categoria no encontrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al editar Categoria"));
        }
    }

    
}
