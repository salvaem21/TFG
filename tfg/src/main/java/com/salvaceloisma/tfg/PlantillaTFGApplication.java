package com.salvaceloisma.tfg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.salvaceloisma.tfg.service.UsuarioService;
import com.salvaceloisma.tfg.enumerados.RolUsuario;

@SpringBootApplication(exclude=SecurityAutoConfiguration.class)
public class PlantillaTFGApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantillaTFGApplication.class, args);
	}

	@Bean
    CommandLineRunner initDatabase(UsuarioService repository) {
        return args -> {
			if (repository.count() == 0) {
            repository.save("Maria", "Cocera", "maria@educa.madrid.org", "", RolUsuario.PROFESOR);
            repository.save("Guillermo", "Sanz", "guillermo@educa.madrid.org", "", RolUsuario.JEFATURA);
            repository.save("Salvador", "Espejo", "salvador.espejo@educa.madrid.org", "", RolUsuario.DIRECTOR);
            repository.save("Admin", "Admin", "admin@educa.madrid.org", "admin", RolUsuario.ADMIN);
        }
    };
}

}