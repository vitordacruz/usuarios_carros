package br.com.examplo.sistema.usuariocarros.api.controller.dto;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioInputDTOSemSenha {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotNull
    private LocalDate birthday;
    @NotBlank
    private String login;
    @NotBlank
    private String phone;
}
