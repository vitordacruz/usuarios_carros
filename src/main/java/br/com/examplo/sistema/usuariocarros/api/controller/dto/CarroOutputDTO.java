package br.com.examplo.sistema.usuariocarros.api.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CarroOutputDTO {
    private Long id;
    private Integer year;
    private String licensePlate;
    private String model;
    private String color;
}
