package br.com.examplo.sistema.usuariocarros.assembler;

import br.com.examplo.sistema.usuariocarros.api.controller.dto.CarroInputDTO;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarroDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Carro toDomainObject(CarroInputDTO carroInput) {
        return modelMapper.map(carroInput, Carro.class);
    }

    public void copyToDomainObject(CarroInputDTO carroInput, Carro carro) {
        modelMapper.map(carroInput, carro);
    }

}
