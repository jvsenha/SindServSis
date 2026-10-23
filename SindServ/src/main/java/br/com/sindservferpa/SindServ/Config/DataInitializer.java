package br.com.sindservferpa.SindServ.Config;

import br.com.sindservferpa.SindServ.Entity.UserEntity;
import br.com.sindservferpa.SindServ.Enum.Role;
import br.com.sindservferpa.SindServ.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Verifica se o administrador já existe na base de dados
            if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

                // 2. Instancia o utilizador com os dados padrão
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@gmail.com");
                admin.setSenha(passwordEncoder.encode("admin")); // Encripta a palavra-passe usando o BCrypt do Spring Security
                admin.setRole(Role.ROLE_ADMIN); // Ajuste para Role.ROLE_ADMIN se for o nome no seu Enum
                admin.setNome("Administrador do Sistema");
                admin.setTelefone("(00) 00000-0000");
                // 3. Guarda a entidade na base de dados
                userRepository.save(admin);
                System.out.println(">>> Utilizador ADMIN padrão criado com sucesso: [login: admin | senha: admin] <<<");
            }
        };
    }
}