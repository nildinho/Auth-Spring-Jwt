package br.com.authjwt.services;

import org.springframework.stereotype.Service;

import br.com.authjwt.entities.Usuario;
import br.com.authjwt.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Usuario> allUsers() {
        List<Usuario> usuarios = new ArrayList<>();

        userRepository.findAll().forEach(usuarios::add);

        return usuarios;
    }
}
