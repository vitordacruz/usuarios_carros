package br.com.examplo.sistema.usuariocarros.api.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarroInputDTO {

    @NotNull
    private Integer year;
    @NotBlank
    private String licensePlate;
    @NotBlank
    private String model;
    @NotBlank
    private String color;

}
