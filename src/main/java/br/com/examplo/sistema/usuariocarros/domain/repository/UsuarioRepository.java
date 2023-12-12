package br.com.examplo.sistema.usuariocarros.domain.repository;

import br.com.examplo.sistema.usuariocarros.domain.Usuario;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByLogin(String login);

    @Query("SELECT u.password FROM Usuario u WHERE u.login = ?1")
    String findPassword(String username);

}
