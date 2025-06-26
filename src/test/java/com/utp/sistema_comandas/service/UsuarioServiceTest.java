package com.utp.sistema_comandas.service;

import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarMozo_deberiaGuardarNuevoMozo() {
        Usuario nuevoMozo = new Usuario();
        nuevoMozo.setNombre("Luis");
        nuevoMozo.setApellido("Perez");
        nuevoMozo.setCorreo("luis.perez@example.com");
        nuevoMozo.setTelefono("123456789");
        nuevoMozo.setDni("76543210");
        nuevoMozo.setContrasena("1234");

        Usuario mozoGuardado = new Usuario();
        mozoGuardado.setId(1L);
        mozoGuardado.setNombre("Luis");
        mozoGuardado.setApellido("Perez");
        mozoGuardado.setCorreo("luis.perez@example.com");
        mozoGuardado.setTelefono("123456789");
        mozoGuardado.setDni("76543210");
        mozoGuardado.setContrasena("encryptedPassword");
        mozoGuardado.setRol("MOZO");
        mozoGuardado.setEstado("Activo");

        when(passwordEncoder.encode("1234")).thenReturn("encryptedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(mozoGuardado);

        // Simula lógica del controlador
        nuevoMozo.setContrasena(passwordEncoder.encode(nuevoMozo.getContrasena()));
        nuevoMozo.setRol("MOZO");
        nuevoMozo.setEstado("Activo");

        Usuario resultado = usuarioService.guardUsuario(nuevoMozo);

        assertNotNull(resultado);
        assertEquals("Luis", resultado.getNombre());
        assertEquals("encryptedPassword", resultado.getContrasena());
        assertEquals("MOZO", resultado.getRol());
        assertEquals("Activo", resultado.getEstado());

        verify(passwordEncoder, times(1)).encode("1234");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void editarMozo_deberiaActualizarDatos() {
        Usuario mozoExistente = new Usuario();
        mozoExistente.setId(1L);
        mozoExistente.setNombre("Carlos");
        mozoExistente.setApellido("Gomez");
        mozoExistente.setTelefono("111111");
        mozoExistente.setEstado("ACTIVO");

        Usuario mozoActualizado = new Usuario();
        mozoActualizado.setId(1L);
        mozoActualizado.setNombre("Carlos Editado");
        mozoActualizado.setApellido("Ramirez");
        mozoActualizado.setTelefono("222222");
        mozoActualizado.setEstado("INACTIVO");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(mozoExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(mozoActualizado);

        // Simular lo que hace tu controlador
        Optional<Usuario> opt = usuarioService.obtenerPorId(1L);
        if (opt.isPresent()) {
            Usuario existente = opt.get();
            existente.setNombre(mozoActualizado.getNombre());
            existente.setApellido(mozoActualizado.getApellido());
            existente.setTelefono(mozoActualizado.getTelefono());
            existente.setEstado(mozoActualizado.getEstado());

            Usuario resultado = usuarioService.guardUsuario(existente);

            assertEquals("Carlos Editado", resultado.getNombre());
            assertEquals("Ramirez", resultado.getApellido());
            assertEquals("222222", resultado.getTelefono());
            assertEquals("INACTIVO", resultado.getEstado());
        }

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

}
