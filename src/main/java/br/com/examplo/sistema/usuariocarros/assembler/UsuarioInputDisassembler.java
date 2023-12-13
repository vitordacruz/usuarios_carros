package br.com.examplo.sistema.usuariocarros.assembler;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioInputDTO;
import br.com.examplo.sistema.usuariocarros.api.controller.dto.UsuarioInputDTOSemSenha;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toDomainObject(UsuarioInputDTO usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }

    public Usuario toDomainObject(UsuarioInputDTOSemSenha usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInputDTOSemSenha usuarioInput, Usuario usuario) {
        modelMapper.map(usuarioInput, usuario);
    }

    public void  copyToDomainObject(UsuarioInputDTO usuarioInput, Usuario usuario) {
        modelMapper.map(usuarioInput, usuario);
    }

}
