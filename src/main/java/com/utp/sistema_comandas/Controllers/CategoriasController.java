package com.utp.sistema_comandas.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    
}
