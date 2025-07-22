package com.sacpe.service;

import com.sacpe.model.Empleado;
import com.sacpe.repository.EmpleadoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmpleadoRepository empleadoRepository;

    public CustomUserDetailsService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Empleado empleado = empleadoRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró un empleado con el email: " + email));

        // El rol se basa en el puesto del empleado. El prefijo "ROLE_" es una convención de Spring Security.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + empleado.getPuesto().name());

        return new User(
                empleado.getEmail(),
                empleado.getPassword(),
                Collections.singletonList(authority)
        );
    }
}