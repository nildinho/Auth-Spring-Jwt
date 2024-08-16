package br.com.authjwt.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/*
JwtService: Esta classe lida com a geração, validação e extração de informações dos tokens JWT, que são amplamente usados para autenticação em aplicações sem estado.
@Value: Injeta valores do arquivo de configuração do Spring, permitindo personalização de chave secreta e tempo de expiração do token.
extractUsername: Obtém o nome de usuário armazenado no token.
generateToken: Gera um token JWT para um usuário, podendo adicionar reivindicações extras.
isTokenValid: Verifica se o token é válido, confirmando que o nome de usuário corresponde e que o token não expirou.
getSignInKey: Converte a chave secreta em um objeto Key adequado para assinar tokens JWT usando HMAC.
*/

@Service // Indica que esta classe é um serviço do Spring, usado para lógica de negócios.
public class JwtService { // Define uma classe de serviço para gerenciar operações relacionadas a JWT (JSON Web Tokens).

    @Value("${security.jwt.secret-key}") // Injeta o valor da chave secreta do JWT a partir do arquivo de configuração.
    private String secretKey;

    @Value("${security.jwt.expiration-time}") // Injeta o valor do tempo de expiração do JWT a partir do arquivo de configuração.
    private long jwtExpiration;

    public String extractUsername(String token) { // Extrai o nome de usuário do token JWT.
        return extractClaim(token, Claims::getSubject); // Chama o método `extractClaim` para obter o sujeito (nome de usuário) do token.
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // Extrai uma reivindicação específica do token.
        final Claims claims = extractAllClaims(token); // Extrai todas as reivindicações (claims) do token.
        return claimsResolver.apply(claims); // Aplica o resolvedor de reivindicações fornecido para obter a reivindicação desejada.
    }

    public String generateToken(UserDetails userDetails) { // Gera um token JWT para o usuário especificado.
        return generateToken(new HashMap<>(), userDetails); // Chama o método sobrecarregado `generateToken` com reivindicações extras vazias.
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) { // Gera um token JWT com reivindicações extras.
        return buildToken(extraClaims, userDetails, jwtExpiration); // Constrói o token usando as reivindicações extras, detalhes do usuário e tempo de expiração.
    }

    public long getExpirationTime() { // Retorna o tempo de expiração configurado para o JWT.
        return jwtExpiration;
    }

    private String buildToken( // Constrói e retorna um token JWT com base nas informações fornecidas.
            Map<String, Object> extraClaims, // Mapa de reivindicações extras.
            UserDetails userDetails, // Detalhes do usuário.
            long expiration // Tempo de expiração do token.
    ) {
        return Jwts
                .builder() // Cria um builder para construir o token JWT.
                .setClaims(extraClaims) // Define as reivindicações do token.
                .setSubject(userDetails.getUsername()) // Define o nome de usuário como o sujeito do token.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Define a data de emissão do token.
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Define a data de expiração do token.
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta usando o algoritmo HS256.
                .compact(); // Compacta e retorna o token JWT como uma string.
    }

    public boolean isTokenValid(String token, UserDetails userDetails) { // Verifica se o token JWT é válido para o usuário fornecido.
        final String username = extractUsername(token); // Extrai o nome de usuário do token.
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Verifica se o nome de usuário corresponde e se o token não expirou.
    }

    private boolean isTokenExpired(String token) { // Verifica se o token JWT expirou.
        return extractExpiration(token).before(new Date()); // Compara a data de expiração do token com a data atual.
    }

    private Date extractExpiration(String token) { // Extrai a data de expiração do token JWT.
        return extractClaim(token, Claims::getExpiration); // Usa o método `extractClaim` para obter a data de expiração.
    }

    private Claims extractAllClaims(String token) { // Extrai todas as reivindicações (claims) do token JWT.
        return Jwts
                .parserBuilder() // Cria um builder para o parser do JWT.
                .setSigningKey(getSignInKey()) // Define a chave de assinatura para o parser.
                .build() // Constrói o parser.
                .parseClaimsJws(token) // Faz o parsing do token e extrai as reivindicações.
                .getBody(); // Retorna o corpo das reivindicações.
    }

    private Key getSignInKey() { // Retorna a chave secreta usada para assinar o token JWT.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodifica a chave secreta da base64 para bytes.
        return Keys.hmacShaKeyFor(keyBytes); // Gera uma chave HMAC usando os bytes decodificados.
    }
}
