package com.utp.sistema_comandas.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.utp.sistema_comandas.model.Usuario;

public class SecurityUserDetails implements UserDetails {

    private final Usuario usuario;

    public SecurityUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRol()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getContrasena();
    }

    @Override
    public String getUsername() {
        return usuario.getCorreo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return usuario.getEstado().equalsIgnoreCase("Activo");
    }

    @Override
    public boolean isAccountNonLocked() {
        return usuario.getEstado().equalsIgnoreCase("Activo");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return usuario.getEstado().equalsIgnoreCase("Activo");
    }

    public Usuario getusuario() {
        return usuario;
    }
}
