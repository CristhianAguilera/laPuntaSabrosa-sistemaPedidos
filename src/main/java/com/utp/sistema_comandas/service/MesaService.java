package com.utp.sistema_comandas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.sistema_comandas.model.Mesa;
import com.utp.sistema_comandas.repository.MesaRepository;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public List<Mesa> listarTodas(){
        return mesaRepository.findAll();
    }

    public int obtenerUltimoNumeroMesa() {
        Integer ultimo = mesaRepository.encontrarUltimoNumeroMesa();
        return (ultimo != null) ? ultimo : 0;
    }

    public Optional<Mesa> obtenerPorId(Long id) {
        return mesaRepository.findById(id);
    }

    public Mesa guardar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    public void eliminar(Long id) {
        mesaRepository.deleteById(id);
    }

    public List<Mesa> listarPorEstado(boolean ocupada) {
        return mesaRepository.findByOcupada(ocupada);
    }

    public Optional<Mesa> buscarPorNumero(int numero) {
        return mesaRepository.findByNumero(numero);
    }
    
}
