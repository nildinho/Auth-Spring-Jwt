package br.com.authjwt.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.authjwt.repositories.UserRepository;

/*
@Configuration: Marca esta classe como uma classe de configuração Spring, permitindo que ela seja usada para definir beans no contexto da aplicação.
@Bean: Indica que um método produz um bean gerenciado pelo contêiner do Spring. O Spring gerencia o ciclo de vida desses beans e os injeta onde necessário.
UserDetailsService: Um serviço usado pelo Spring Security para carregar detalhes de um usuário (como credenciais e autoridades) durante a autenticação.
BCryptPasswordEncoder: Um codificador de senha que aplica o algoritmo BCrypt, que é robusto e amplamente utilizado para segurança de senhas.
AuthenticationManager: Gerencia o processo de autenticação dentro do Spring Security.
AuthenticationProvider: Um componente que realiza a autenticação, que aqui utiliza o DaoAuthenticationProvider, um provedor de autenticação baseado em DAO (Data Access Object).
*/


@Configuration // Indica que esta classe contém configurações para o contexto da aplicação Spring.
public class ApplicationConfiguration { // Define uma classe de configuração chamada `ApplicationConfiguration`.

    private final UserRepository userRepository; // Declara uma variável final para armazenar a instância do repositório de usuários.

    public ApplicationConfiguration(UserRepository userRepository) { // Construtor que injeta uma instância do `UserRepository`.
        this.userRepository = userRepository; // Atribui a instância do repositório ao campo `userRepository`.
    }

    @Bean // Define um método que retorna um bean gerenciado pelo Spring.
    UserDetailsService userDetailsService() { // Cria um bean do tipo `UserDetailsService`.
        return username -> userRepository.findByEmail(username) // Implementa o método que busca um usuário pelo e-mail.
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Lança uma exceção se o usuário não for encontrado.
    }

    @Bean // Define outro bean gerenciado pelo Spring.
    BCryptPasswordEncoder passwordEncoder() { // Cria um bean do tipo `BCryptPasswordEncoder`.
        return new BCryptPasswordEncoder(); // Retorna uma nova instância do codificador de senhas `BCryptPasswordEncoder`.
    }

    @Bean // Define um bean gerenciado pelo Spring.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { // Cria um bean do tipo `AuthenticationManager`.
        return config.getAuthenticationManager(); // Retorna o gerenciador de autenticação a partir da configuração passada.
    }

    @Bean // Define um bean gerenciado pelo Spring.
    AuthenticationProvider authenticationProvider() { // Cria um bean do tipo `AuthenticationProvider`.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(); // Instancia um `DaoAuthenticationProvider`.

        authProvider.setUserDetailsService(userDetailsService()); // Configura o `UserDetailsService` para o provedor de autenticação.
        authProvider.setPasswordEncoder(passwordEncoder()); // Configura o codificador de senhas `BCryptPasswordEncoder` para o provedor de autenticação.

        return authProvider; // Retorna o `DaoAuthenticationProvider` configurado.
    }
}