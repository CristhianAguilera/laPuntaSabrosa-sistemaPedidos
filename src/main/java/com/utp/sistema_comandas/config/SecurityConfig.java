package com.utp.sistema_comandas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.utp.sistema_comandas.model.Usuario;
import com.utp.sistema_comandas.service.UsuarioService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Collection;
import org.springframework.security.core.Authentication;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/**", "/css/**", "/image/**", "/error").permitAll()
                        .requestMatchers("/admin/**", "/admin/Mesas/**",
                        "/admin/registrarMozo/**",
                        "/admin/carta/**",
                        "/admin/menuDiario/**",
                        "/admin/categorias/**").hasRole("ADMIN")
                        .requestMatchers("/mozo/**",
                        "/mozo/mesasMozo/**").hasRole("MOZO")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/")
                        .failureUrl("/?error=true")
                        .loginProcessingUrl("/login")
                        .usernameParameter("correoelectronico")
                        .passwordParameter("contrasena")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }

    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {

            // Obtener el correo del usuario autenticado
            String correo = authentication.getName();

            Usuario usuario = usuarioService.obtenerPorCorreo(correo);

            if (usuario != null) {

                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
            }

            // Redirige según el rol
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    response.sendRedirect("/admin/registrarMozo");
                    return;
                } else if (authority.getAuthority().equals("ROLE_MOZO")) {
                    response.sendRedirect("/mozo/mesasMozo");
                    return;
                }
            }

            // Si no se encuentra el usuario o no tiene un rol válido
            response.sendRedirect("/");
        }
    }
}
