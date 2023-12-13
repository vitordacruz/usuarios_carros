package br.com.examplo.sistema.usuariocarros.api.controller;


import br.com.examplo.sistema.usuariocarros.domain.CarroService;
import br.com.examplo.sistema.usuariocarros.domain.UsuarioService;
import br.com.examplo.sistema.usuariocarros.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarroService carroService;

    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long usuarioId) {
        usuarioService.excluir(usuarioId);
    }

}
