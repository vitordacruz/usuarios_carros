package br.com.examplo.sistema.usuariocarros.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioInputDTO;
import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioOutputDTO;
import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioSenhaInput;
import br.com.examplo.sistema.usuariocarros.assembler.UsuarioAssembler;
import br.com.examplo.sistema.usuariocarros.assembler.UsuarioInputDisassembler;
import br.com.examplo.sistema.usuariocarros.domain.UsuarioService;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import br.com.examplo.sistema.usuariocarros.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @Autowired
    private UsuarioAssembler usuarioAssembler;

    @GetMapping
    public List<UsuarioOutputDTO> listarTodos(@RequestParam("login") Optional<String> login) {
        if (login.isEmpty()) {
            return usuarioAssembler.toCollectionModel(usuarioRepository.findAll());
        } else {
            return usuarioAssembler.toCollectionModel(usuarioRepository.findByLoginIgnoreCase(login.get()));
        }
    }

    @GetMapping("/{usuarioId}")
    public UsuarioOutputDTO buscar(@PathVariable Long usuarioId) {
        return usuarioAssembler.toModel(usuarioService.buscarOuFalhar(usuarioId));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioOutputDTO adicionar(@RequestBody @Valid UsuarioInputDTO usuarioInput) {

        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);

        return usuarioAssembler.toModel(usuarioService.salvar(usuario));

    }

    @PutMapping("/{usuarioId}")
    public UsuarioOutputDTO atualizar(@RequestBody @Valid UsuarioInputDTO usuarioInput, @PathVariable Long usuarioId) {

        Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioInput);

        usuario.setId(usuarioId);

        return usuarioAssembler.toModel(usuarioService.salvar(usuario));


    }

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {
        usuarioService.excluir(usuarioId);
    }

    @PutMapping("/{usuarioId}/senha")
    @ResponseStatus(HttpStatus.OK)
    public void alterarSenha(@PathVariable Long usuarioId, @RequestBody @Valid UsuarioSenhaInput usuarioSenhaInput) {

        usuarioService.alterarSenha(usuarioId, usuarioSenhaInput.getSenhaAtual(), usuarioSenhaInput.getNovaSenha());

    }

}
