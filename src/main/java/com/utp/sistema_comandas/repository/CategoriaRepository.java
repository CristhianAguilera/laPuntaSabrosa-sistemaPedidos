package com.utp.sistema_comandas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.utp.sistema_comandas.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

    Optional<Categoria> findByNombre(String nombre);

    @Override
    List<Categoria> findAll();
    
    Optional findById(Long categoriaid);
}
