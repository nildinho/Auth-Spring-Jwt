package br.com.authjwt.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.authjwt.services.JwtService;

import java.io.IOException;


/*
JwtAuthenticationFilter: Este filtro intercepta cada solicitação HTTP para verificar se um token JWT está presente e é válido. Se o token for válido, ele autentica o usuário e o associa ao contexto de segurança do Spring.
OncePerRequestFilter: Garante que o filtro seja executado apenas uma vez por solicitação.
@Component: Marca a classe como um componente gerenciado pelo Spring, permitindo que o filtro seja registrado automaticamente no contexto de aplicação.
HandlerExceptionResolver: Trata as exceções que podem surgir durante o processamento do filtro.
SecurityContextHolder: Armazena o contexto de segurança, que contém informações de autenticação para a solicitação atual.
*/




@Component // Anotação que marca esta classe como um componente Spring, permitindo que seja gerenciada pelo Spring Framework e injetada em outros lugares.
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Define uma classe que estende `OncePerRequestFilter`, garantindo que o filtro seja executado uma vez por solicitação.

    private final HandlerExceptionResolver handlerExceptionResolver; // Declara uma variável final para resolver exceções durante o processo de filtragem.
    private final JwtService jwtService; // Declara uma variável final para o serviço que lida com tokens JWT.
    private final UserDetailsService userDetailsService; // Declara uma variável final para o serviço que carrega detalhes do usuário.

    public JwtAuthenticationFilter( // Construtor que injeta instâncias do `JwtService`, `UserDetailsService`, e `HandlerExceptionResolver`.
        JwtService jwtService,
        UserDetailsService userDetailsService,
        HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService; // Atribui a instância do `JwtService` ao campo `jwtService`.
        this.userDetailsService = userDetailsService; // Atribui a instância do `UserDetailsService` ao campo `userDetailsService`.
        this.handlerExceptionResolver = handlerExceptionResolver; // Atribui a instância do `HandlerExceptionResolver` ao campo `handlerExceptionResolver`.
    }

    @Override
    protected void doFilterInternal( // Método sobrescrito que executa a lógica de filtragem por solicitação.
        @NonNull HttpServletRequest request, // A solicitação HTTP atual.
        @NonNull HttpServletResponse response, // A resposta HTTP atual.
        @NonNull FilterChain filterChain // A cadeia de filtros que permite continuar o processamento da solicitação.
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Obtém o cabeçalho "Authorization" da solicitação.

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Verifica se o cabeçalho está presente e se começa com "Bearer ".
            filterChain.doFilter(request, response); // Se não estiver presente ou for inválido, continua com o próximo filtro na cadeia.
            return; // Encerra o processamento deste filtro.
        }

        try {
            final String jwt = authHeader.substring(7); // Extrai o token JWT do cabeçalho (removendo o prefixo "Bearer ").
            final String userEmail = jwtService.extractUsername(jwt); // Extrai o e-mail do usuário do token JWT.

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Obtém o contexto de autenticação atual.

            if (userEmail != null && authentication == null) { // Verifica se o e-mail de usuário está presente e se ainda não existe autenticação no contexto.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail); // Carrega os detalhes do usuário pelo e-mail.

                if (jwtService.isTokenValid(jwt, userDetails)) { // Verifica se o token JWT é válido para o usuário.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, // Detalhes do usuário.
                            null, // Credenciais, que não são necessárias aqui.
                            userDetails.getAuthorities() // Autoridades (roles) do usuário.
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Adiciona detalhes da solicitação ao token de autenticação.
                    SecurityContextHolder.getContext().setAuthentication(authToken); // Define o token de autenticação no contexto de segurança.
                }
            }

            filterChain.doFilter(request, response); // Continua com o próximo filtro na cadeia.
        } catch (Exception exception) { // Captura exceções que possam ocorrer durante a filtragem.
            handlerExceptionResolver.resolveException(request, response, null, exception); // Resolve a exceção usando o `HandlerExceptionResolver`.
        }
    }
}