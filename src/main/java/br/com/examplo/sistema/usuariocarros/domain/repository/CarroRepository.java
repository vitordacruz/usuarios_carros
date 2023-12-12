package br.com.examplo.sistema.usuariocarros.domain.repository;

import br.com.examplo.sistema.usuariocarros.domain.Carro;
import br.com.examplo.sistema.usuariocarros.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Long>, CustomCarroRepository {

    List<Carro> findByUsuario(Usuario usuario);
    Optional<Carro> findByLicensePlate(String licensePlate);
    List<Carro> findAllByUsuarioLogin(String login);
    Optional<Carro> findOneByIdAndUsuarioLogin(Long id, String login);

}
