package com.utp.sistema_comandas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utp.sistema_comandas.model.Mesa;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long>{

    List<Mesa> findByOcupada(boolean ocupada);

    Optional<Mesa> findByNumero(int numero);
    
    @Query("SELECT MAX(m.numero) FROM Mesa m")
    Integer encontrarUltimoNumeroMesa();
}
