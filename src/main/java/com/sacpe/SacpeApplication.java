package com.sacpe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sacpe.enums.PuestoEmpleado;
import com.sacpe.model.Empleado;
import com.sacpe.service.EmpleadoService;

@SpringBootApplication
public class SacpeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SacpeApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(EmpleadoService empleadoService, PasswordEncoder passwordEncoder) {
        return args -> {
            if (empleadoService.buscarTodos().isEmpty()) {
                Empleado admin = new Empleado();
                admin.setNombre("Admin");
                admin.setApellido("Principal");
                admin.setEmail("admin@sacpe.com");
                admin.setPuesto(PuestoEmpleado.GERENTE);
                // Cifra la contraseña antes de guardarla
                admin.setPassword(passwordEncoder.encode("admin")); 
                empleadoService.guardarEmpleado(admin);
            }
        };
    }
}