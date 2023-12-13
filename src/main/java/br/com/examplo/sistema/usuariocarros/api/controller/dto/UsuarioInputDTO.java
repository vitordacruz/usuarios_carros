package br.com.examplo.sistema.usuariocarros.api.controller.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioInputDTO {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthday;
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String phone;

    private List<Carro> cars;
}
