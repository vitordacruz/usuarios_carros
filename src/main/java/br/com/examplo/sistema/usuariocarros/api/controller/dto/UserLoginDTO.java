package br.com.examplo.sistema.usuariocarros.api.controller.dto;

import javax.validation.constraints.NotBlank;

import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class UserLoginDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    public static Usuario convertToTO(UserLoginDTO dto) {
        Usuario user = new Usuario();
        user.setLogin(dto.getLogin());
        user.setPassword(dto.getPassword());
        return user;
    }
}
