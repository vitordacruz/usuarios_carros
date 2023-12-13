package br.com.examplo.sistema.usuariocarros.assembler;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioOutputDTO;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public UsuarioOutputDTO toModel(Usuario usuario) {

        return modelMapper.map(usuario, UsuarioOutputDTO.class);

    }

    public List<UsuarioOutputDTO> toCollectionModel(Collection<Usuario> usuarios) {

        return usuarios.stream()
                .map(usuario -> toModel(usuario))
                .collect(Collectors.toList());

    }

}
