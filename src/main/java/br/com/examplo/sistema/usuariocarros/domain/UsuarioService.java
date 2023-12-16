package br.com.examplo.sistema.usuariocarros.domain;


import br.com.examplo.sistema.usuariocarros.domain.exception.AuthorizationException;
import br.com.examplo.sistema.usuariocarros.domain.exception.EntidadeEmUsoException;
import br.com.examplo.sistema.usuariocarros.domain.exception.NegocioException;
import br.com.examplo.sistema.usuariocarros.domain.exception.UsuarioNaoEncontradoException;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import br.com.examplo.sistema.usuariocarros.domain.repository.UsuarioRepository;
import br.com.examplo.sistema.usuariocarros.security.JWTUtil;
import br.com.examplo.sistema.usuariocarros.util.ConstantesComum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CarroService carroService;

    @Autowired
    private JWTUtil jwtUtil;

    private static final String MSG_USUARIO_EM_USO = "Usuario de código %d não pode ser removido, pois está em uso";

    /**
     * Metodo que contem a regra de negócio para salvar o usuario
     * Serve tanto para inserir como para atualizar
     * @param usuario
     * O usuario passado deve conter a senha sem criptografia
     * Esse método criptorafa a senha antes de salvar
     * @return
     */
    @Transactional
    public Usuario salvar(Usuario usuario) {

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        validaEmailJaExiste(usuario, usuarioExistente);

        Optional<Usuario> usuarioExistenteLogin = usuarioRepository.findByLogin(usuario.getLogin());

        validaLoginJaExiste(usuario, usuarioExistenteLogin);
        log.info("usuario.getId(): " + usuario.getId());
        if (usuario.getId() != null) {

            Optional<Usuario> usuarioAtual = usuarioRepository.findById(usuario.getId());

            if(usuarioAtual.isPresent()) {

                // usuarioRepository.detach(usuarioAtual.get());

                List<Carro> listaCarros = new ArrayList<>();
                listaCarros.addAll(usuarioAtual.get().getCars());
                carroService.excluirTodos(listaCarros);
                usuarioAtual.get().setCars(new ArrayList<>());

                usuario.setCreatedAt(usuarioAtual.get().getCreatedAt());

            }


        } else {
            usuario.setCreatedAt(LocalDateTime.now());
        }

        if (usuario.getCars() != null) {

            var listaPlacaCarros = new ArrayList<String>();

            usuario.getCars().forEach(carro ->{

                validaPlacaExiste(usuario, listaPlacaCarros, carro);

            });
        }

        usuario.setPassword(JWTUtil.encodePassword(usuario.getPassword()));
        return usuarioRepository.save(usuario);


    }

    private void validaPlacaExiste(Usuario usuario, ArrayList<String> listaPlacaCarros, Carro carro) {
        if (!carroService.placaExiste(carro.getLicensePlate(), usuario.getId()) &&
                !listaPlacaCarros.contains(carro.getLicensePlate())) {

            carro.setUsuario(usuario);
            listaPlacaCarros.add(carro.getLicensePlate());

        } else {
            log.info("Já existe um carro com essa placa");
            throw new NegocioException(
                    messageSource.getMessage("placa.carro.ja.existe", null, null), ConstantesComum.ERROR_CODE_PLACA_CARRO_JA_EXISTE);
        }
    }

    private void validaLoginJaExiste(Usuario usuario, Optional<Usuario> usuarioExistenteLogin) {
        if (usuarioExistenteLogin.isPresent() && !usuarioExistenteLogin.get().equals(usuario)) {
            log.info("Usuário tentou salvar um usuário com login já existente.");
            throw new NegocioException(
                    messageSource.getMessage("login.ja.existe", null, null), ConstantesComum.ERROR_CODE_LOGIN_JA_EXISTENTE);
        }
    }

    private void validaEmailJaExiste(Usuario usuario, Optional<Usuario> usuarioExistente) {
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            log.info("Usuário tentou salvar um usuário com e-mail já existente.");
            throw new NegocioException(
                    messageSource.getMessage("email.ja.existe", null, null), ConstantesComum.ERROR_CODE_EMAIL_JA_EXISTENTE);
        }
    }

    @Transactional
    public void excluir(Long usuarioId) {
        try {
            var usuarioAtual  = this.buscarOuFalhar(usuarioId);
            var carros = new ArrayList<Carro>();
            carros.addAll(usuarioAtual.getCars());
            carroService.excluirTodos(carros);
            usuarioRepository.deleteById(usuarioId);
            usuarioRepository.flush();
            log.info("Usuário excluido.");
        } catch (EmptyResultDataAccessException e) {
            throw new UsuarioNaoEncontradoException(usuarioId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_USUARIO_EM_USO, usuarioId));
        }
    }

    /**
     * Busca o usuário pela chave primaria
     * Se não achar o usuário ele levanta uma Exceção do tipo UsuarioNaoEncontradoException
     * @param usuarioId
     * @return
     */
    public Usuario buscarOuFalhar(Long usuarioId) {

        return usuarioRepository.findById(usuarioId).orElseThrow(
                () -> new UsuarioNaoEncontradoException(usuarioId));

    }

    /**
     * Método usado para alterar senha do usuario
     * Se não achar o usuário levanta uma exceção do tipo UsuarioNaoEncontradoException
     * Se a senha atual não coincidir levanta uma Exceção do tipo NegocioException
     * @param usuarioId
     * @param senhaAtual
     * A senha atual deve ser passada sem criptografia
     * @param novaSenha
     * Nova senha que deve ser passada sem criptografia
     */
    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {

        Usuario usuario = this.buscarOuFalhar(usuarioId);

        senhaAtual = JWTUtil.encodePassword(senhaAtual);

        if (usuario.senhaNaoCoincideCom(senhaAtual)) {
            log.info("Senha atual não coincide.");
            throw new NegocioException(messageSource.getMessage("senha.atual.nao.coincide.com.senha.usuario", null, null));
        }

        usuario.setPassword(JWTUtil.encodePassword(usuario.getPassword()));

        usuarioRepository.save(usuario);

    }

    public String signin(Usuario user) {
        String passwordRecoverd = usuarioRepository.findPassword(user.getUsername());

        if (StringUtils.isEmpty(passwordRecoverd)) {
            throw new AuthorizationException("Invalid e-mail or password.");
        }

        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

        if (!bCrypt.matches(user.getPassword(), passwordRecoverd)) {
            throw new AuthorizationException("Invalid e-mail or password.");
        }

        saveCurrentLogin(user.getUsername());

        return jwtUtil.generateToken(user.getUsername());
    }

    @Transactional
    public void saveCurrentLogin(String username) {
        Usuario user = findByLogin(username);

        user.setLastLogin(LocalDateTime.now());

        usuarioRepository.save(user);
    }

    public Usuario findByLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public Usuario findBySession(Principal principal) {
        return findByLogin(principal.getName());
    }

}
