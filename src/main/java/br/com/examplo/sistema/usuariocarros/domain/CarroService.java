package br.com.examplo.sistema.usuariocarros.domain;

import java.util.List;
import java.util.Optional;

import br.com.examplo.sistema.usuariocarros.domain.exception.CarroDoUsuarioNaoEncontradoException;
import br.com.examplo.sistema.usuariocarros.domain.exception.CarroNaoEncontradoException;
import br.com.examplo.sistema.usuariocarros.domain.exception.EntidadeEmUsoException;
import br.com.examplo.sistema.usuariocarros.domain.exception.NegocioException;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import br.com.examplo.sistema.usuariocarros.domain.repository.CarroRepository;
import br.com.examplo.sistema.usuariocarros.domain.repository.UsuarioRepository;
import br.com.examplo.sistema.usuariocarros.util.ConstantesComum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CarroService {
    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MessageSource messageSource;

    private static final String MSG_CARRO_EM_USO = "Carro de código %d não pode ser removido, pois está em uso";

    @Transactional
    public Carro salvar(Carro carro) {

        Optional<Carro> carroExistente = carroRepository.findByLicensePlate(carro.getLicensePlate());

        if (carroExistente.isPresent() && !carroExistente.get().equals(carro)) {
            log.info("Já existe um carro com essa placa");
            throw new NegocioException(
                    messageSource.getMessage("placa.carro.ja.existe", null, null), ConstantesComum.ERROR_CODE_PLACA_CARRO_JA_EXISTE);
        }

        return carroRepository.save(carro);

    }

    public boolean placaExiste(String placa, Long usuarioId) {
        return carroRepository.placaExiste(placa, usuarioId);
    }

    public Carro buscarOuFalhar(Long carroId) {

        return carroRepository.findById(carroId).orElseThrow(
                () -> new CarroNaoEncontradoException(carroId));

    }

    public Carro buscarOuFalhar(Long carroId, String login) {

        return carroRepository.findOneByIdAndUsuarioLogin(carroId, login).orElseThrow(
                () -> new CarroDoUsuarioNaoEncontradoException(carroId));

    }

    @Transactional
    public void excluir(Long carroId) {
        try {
            Carro carro = buscarOuFalhar(carroId);
            Usuario usuarioAtual = carro.getUsuario();

            usuarioAtual.getCars().remove(carro);
            carro.setUsuario(null);

            usuarioRepository.save(usuarioAtual);
            carroRepository.deleteById(carroId);
            log.info("Carro excluido.");
        } catch (EmptyResultDataAccessException e) {
            throw new CarroNaoEncontradoException(carroId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CARRO_EM_USO, carroId));
        }
    }

    @Transactional
    public void excluir(Carro carro) {
        try {
            Usuario usuarioAtual = carro.getUsuario();

            for (int i = 0; i < usuarioAtual.getCars().size(); i++) {
                if (carro.getId().longValue() == usuarioAtual.getCars().get(i).getId().longValue()) {
                    usuarioAtual.getCars().remove(i);
                }
            }
            carro.setUsuario(null);

            usuarioRepository.save(usuarioAtual);
            carroRepository.deleteById(carro.getId());
            log.info("Carro excluido.");
        } catch (EmptyResultDataAccessException e) {
            throw new CarroNaoEncontradoException(carro.getId());
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CARRO_EM_USO, carro.getId()));
        }
    }


    @Transactional(readOnly = false)
    public void excluir(Long carroId, Usuario usuario) {
        try {

            Carro carro = null;

            carro = buscarOuFalhar(carroId, usuario.getLogin());

            Usuario usuarioAtual = carro.getUsuario();

            usuarioAtual.getCars().remove(carro);
            carro.setUsuario(null);

            usuarioRepository.save(usuarioAtual);
            carroRepository.deleteById(carro.getId());
            log.info("Carro excluido.");

        } catch (EmptyResultDataAccessException e) {
            throw new CarroNaoEncontradoException(carroId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CARRO_EM_USO, carroId));
        }
    }

    public List<Carro> findAllByUsuarioLogin(String login) {
        return carroRepository.findAllByUsuarioLogin(login);
    }

    @Transactional
    public void excluirTodos(List<Carro> carros) {
        for (Carro carro:carros) {
            excluir(carro);
        }

    }
}
