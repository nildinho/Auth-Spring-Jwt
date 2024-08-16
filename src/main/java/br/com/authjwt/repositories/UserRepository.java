package br.com.authjwt.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.authjwt.entities.Usuario;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
