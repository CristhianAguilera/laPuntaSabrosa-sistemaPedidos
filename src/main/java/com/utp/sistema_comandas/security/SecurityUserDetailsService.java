package com.utp.sistema_comandas.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
            
        if (usuario == null) {
            System.out.println("Usuario no encontrado con correo: " + correo);
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        System.out.println("Usuario encontrado: " + correo);
                
        return new SecurityUserDetails(usuario);
    }

}
