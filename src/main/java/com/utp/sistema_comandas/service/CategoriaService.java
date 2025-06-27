package com.utp.sistema_comandas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.sistema_comandas.model.Categoria;
import com.utp.sistema_comandas.repository.CategoriaRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodasCategorias() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> obtenerPorId(Long categoriaId) {
        return categoriaRepository.findById(categoriaId);
    }

    public void eliminarCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
}
