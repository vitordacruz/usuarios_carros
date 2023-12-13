package br.com.examplo.sistema.usuariocarros.domain.repository;

import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByLogin(String login);

    @Query("SELECT u.password FROM Usuario u WHERE u.login = ?1")
    String findPassword(String username);

}
