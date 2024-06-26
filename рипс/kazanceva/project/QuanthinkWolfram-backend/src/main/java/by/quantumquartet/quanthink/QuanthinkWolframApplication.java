package by.quantumquartet.quanthink;

import by.quantumquartet.quanthink.models.ERole;
import by.quantumquartet.quanthink.models.Role;
import by.quantumquartet.quanthink.rest.requests.users.RegisterRequest;
import by.quantumquartet.quanthink.services.RoleService;
import by.quantumquartet.quanthink.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QuanthinkWolframApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuanthinkWolframApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserService userService, RoleService roleService) {
        return args -> {
            roleService.createRole(new Role(ERole.ROLE_USER));
            roleService.createRole(new Role(ERole.ROLE_ADMIN));

            for (int i = 1; i <= 5; i++) {
                RegisterRequest registerRequest = new RegisterRequest(
                        "user" + i + "@example.com",
                        "user" + i,
                        "password" + i
                );
                userService.registerUser(registerRequest);
            }
        };
    }
}
