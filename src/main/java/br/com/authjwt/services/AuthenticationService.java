package br.com.authjwt.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.authjwt.dtos.LoginUserDto;
import br.com.authjwt.dtos.RegisterUserDto;
import br.com.authjwt.entities.Usuario;
import br.com.authjwt.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

/*
@Service: Indica que a classe é um serviço Spring, um componente especializado em lógica de negócios. Essa classe pode ser injetada em outros componentes do Spring.
AuthenticationService: Esta classe fornece funcionalidades de autenticação e gerenciamento de usuários, incluindo registro, login e listagem de todos os usuários.
signup: Método que registra um novo usuário codificando a senha e salvando o usuário no repositório.
authenticate: Método que autentica um usuário verificando as credenciais fornecidas e retornando o usuário correspondente.
allUsers: Método que retorna uma lista de todos os usuários presentes no banco de dados.
*/




@Service // Anotação que indica que esta classe é um serviço do Spring, sendo um componente gerenciado e adequado para lógica de negócios.
public class AuthenticationService { // Define uma classe de serviço chamada `AuthenticationService`.

    private final UserRepository userRepository; // Declara um campo final para o repositório de usuários.
    private final PasswordEncoder passwordEncoder; // Declara um campo final para o codificador de senhas.
    private final AuthenticationManager authenticationManager; // Declara um campo final para o gerenciador de autenticação.

    public AuthenticationService( // Construtor que injeta as dependências necessárias.
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager; // Atribui o gerenciador de autenticação ao campo correspondente.
        this.userRepository = userRepository; // Atribui o repositório de usuários ao campo correspondente.
        this.passwordEncoder = passwordEncoder; // Atribui o codificador de senhas ao campo correspondente.
    }

    public Usuario signup(RegisterUserDto input) { // Método para registrar um novo usuário.
        var user = new Usuario() // Cria uma nova instância de `Usuario`.
            .setnomeCompleto(input.getnomeCompleto()) // Define o nome completo a partir do DTO de entrada.
            .setEmail(input.getEmail()) // Define o e-mail a partir do DTO de entrada.
            .setPassword(passwordEncoder.encode(input.getPassword())); // Codifica a senha e a define para o usuário.

        return userRepository.save(user); // Salva o usuário no banco de dados e o retorna.
    }

    public Usuario authenticate(LoginUserDto input) { // Método para autenticar um usuário existente.
        authenticationManager.authenticate( // Usa o gerenciador de autenticação para autenticar as credenciais do usuário.
            new UsernamePasswordAuthenticationToken(
                input.getEmail(), // Usa o e-mail do DTO de entrada como nome de usuário.
                input.getPassword() // Usa a senha do DTO de entrada como credencial.
            )
        );

        return userRepository.findByEmail(input.getEmail()).orElseThrow(); // Busca o usuário pelo e-mail e lança uma exceção se não for encontrado.
    }

    public List<Usuario> allUsers() { // Método para listar todos os usuários.
        List<Usuario> usuarios = new ArrayList<>(); // Cria uma lista vazia para armazenar os usuários.

        userRepository.findAll().forEach(usuarios::add); // Itera sobre todos os usuários encontrados e os adiciona à lista.

        return usuarios; // Retorna a lista de usuários.
    }
}
