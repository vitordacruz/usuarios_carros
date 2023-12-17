package br.com.examplo.sistema.usuariocarros.api.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.CarroInputDTO;
import br.com.examplo.sistema.usuariocarros.api.controller.dto.CarroOutputDTO;
import br.com.examplo.sistema.usuariocarros.assembler.CarroAsssembler;
import br.com.examplo.sistema.usuariocarros.assembler.CarroDisassembler;
import br.com.examplo.sistema.usuariocarros.domain.CarroService;
import br.com.examplo.sistema.usuariocarros.domain.UsuarioService;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CarroController {

    @Autowired
    private CarroService carroService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarroAsssembler carroAssembler;

    @Autowired
    private CarroDisassembler carroDisassembler;

    @GetMapping
    public List<CarroOutputDTO> findAllCarros(Principal principal) {

        List<Carro> carros = carroService.findAllByUsuarioLogin(principal.getName());

        return carroAssembler.toCollectionModel(carros);

    }

    @GetMapping("/{carroId}")
    public CarroOutputDTO findById(@PathVariable Long carroId, Principal principal) {
        return carroAssembler.toModel(carroService.buscarOuFalhar(carroId, principal.getName()));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarroOutputDTO adicionar(@RequestBody @Valid CarroInputDTO carroInput, @ApiIgnore Principal principal) {

        Usuario usuario = usuarioService.findBySession(principal);

        Carro carro = carroDisassembler.toDomainObject(carroInput);

        carro.setUsuario(usuario);

        return carroAssembler.toModel(carroService.salvar(carro));

    }

    @PutMapping("/{carroId}")
    public CarroOutputDTO atualizar(@RequestBody @Valid CarroInputDTO carroInput, @PathVariable Long carroId,  Principal principal) {

        Carro carroAtual = carroService.buscarOuFalhar(carroId);

        carroDisassembler.copyToDomainObject(carroInput, carroAtual);

        Usuario usuario = usuarioService.findBySession(principal);

        carroAtual.setUsuario(usuario);

        return carroAssembler.toModel(carroService.salvar(carroAtual));

    }

    @DeleteMapping("/{carroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long carroId, Principal principal) {

        Usuario usuario = usuarioService.findBySession(principal);

        carroService.excluir(carroId, usuario);

    }
}
