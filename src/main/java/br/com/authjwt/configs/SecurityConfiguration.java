package br.com.authjwt.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


/*
@Configuration: Indica que esta classe fornece configurações do Spring.
@EnableWebSecurity: Ativa a segurança baseada na Web, permitindo a personalização das configurações de segurança do Spring.
SecurityFilterChain: Define a cadeia de filtros que processa as solicitações HTTP e aplica as políticas de segurança configuradas.
CSRF: Desabilitado aqui, pois a aplicação usa tokens JWT, que não requerem CSRF.
SessionCreationPolicy.STATELESS: Indica que a aplicação não mantém estado de sessão, o que é comum quando se usa JWT para autenticação.
CorsConfigurationSource: Configura o CORS (Cross-Origin Resource Sharing), permitindo que a aplicação receba requisições de domínios diferentes.
*/


@Configuration // Indica que esta classe contém definições de configuração para o Spring.
@EnableWebSecurity // Habilita a configuração de segurança baseada em Web Security no Spring.
public class SecurityConfiguration { // Define a classe de configuração de segurança.

    private final AuthenticationProvider authenticationProvider; // Declara um campo para o provedor de autenticação.
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Declara um campo para o filtro de autenticação JWT.

    public SecurityConfiguration( // Construtor que injeta as dependências necessárias.
        JwtAuthenticationFilter jwtAuthenticationFilter,
        AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider; // Atribui o provedor de autenticação ao campo correspondente.
        this.jwtAuthenticationFilter = jwtAuthenticationFilter; // Atribui o filtro JWT ao campo correspondente.
    }

    @Bean // Define um bean gerenciado pelo Spring.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Configura a cadeia de filtros de segurança.
        http.csrf() // Configura a proteção contra CSRF (Cross-Site Request Forgery).
                .disable() // Desativa a proteção contra CSRF, já que tokens JWT são usados.
                .authorizeHttpRequests() // Inicia a configuração de autorização de requisições HTTP.
                .requestMatchers("/auth/**") // Define padrões de URL que não exigem autenticação.
                .permitAll() // Permite o acesso a todas as requisições que correspondem aos padrões especificados.
                .anyRequest() // Qualquer outra requisição que não corresponda ao padrão acima.
                .authenticated() // Exige autenticação para todas as outras requisições.
                .and() // Continuação da configuração HTTP.
                .sessionManagement() // Configura a gestão de sessões.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Define a política de criação de sessão como stateless, pois JWT está sendo usado.
                .and() // Continuação da configuração HTTP.
                .authenticationProvider(authenticationProvider) // Configura o provedor de autenticação personalizado.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona o filtro JWT antes do filtro de autenticação padrão.

        return http.build(); // Constrói e retorna a cadeia de filtros de segurança.
    }

    @Bean // Define um bean gerenciado pelo Spring para configuração de CORS.
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration(); // Cria uma nova configuração CORS.

        configuration.setAllowedOrigins(List.of("http://localhost:8005")); // Define as origens permitidas para requisições CORS.
        configuration.setAllowedMethods(List.of("GET","POST")); // Define os métodos HTTP permitidos.
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type")); // Define os cabeçalhos HTTP permitidos.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // Cria uma fonte de configuração CORS baseada em URL.
        source.registerCorsConfiguration("/**", configuration); // Registra a configuração CORS para todos os padrões de URL.

        return source; // Retorna a fonte de configuração CORS.
    }
}