package br.com.examplo.sistema.usuariocarros.init;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.examplo.sistema.usuariocarros.domain.CarroService;
import br.com.examplo.sistema.usuariocarros.domain.UsuarioService;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DadosIniciais {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CarroService carroService;

    public void iniciarDadosDatabase() {

        Usuario usuario1 = new Usuario();

        usuario1.setLogin("usuario1");
        usuario1.setPassword("123");
        usuario1.setEmail("usuario1@email.com");
        usuario1.setBirthday(LocalDate.of(1950, 3, 20));
        usuario1.setFirstName("Teste");
        usuario1.setLastName("Testando");
        usuario1.setPhone("55+ 81 9.9999.5555");

        usuarioService.salvar(usuario1);

        Carro carro1 = new Carro();

        carro1.setColor("Vermelho");
        carro1.setModel("Ford Ka");
        carro1.setLicensePlate("GPI - 0000");
        carro1.setYear(2018);
        carro1.setUsuario(usuario1);

        Carro carro2 = new Carro();

        carro2.setColor("Azul");
        carro2.setModel("Sandero");
        carro2.setLicensePlate("LLL - 0081");
        carro2.setYear(2020);
        carro2.setUsuario(usuario1);

        carroService.salvar(carro1);
        carroService.salvar(carro2);

        Usuario usuario2 = new Usuario();

        usuario2.setLogin("usuario2");
        usuario2.setPassword("123");
        usuario2.setEmail("usuario2@email.com");
        usuario2.setBirthday(LocalDate.of(1980, 5, 15));
        usuario2.setFirstName("Teste 02");
        usuario2.setLastName("Testando 02");
        usuario2.setPhone("55+ 21 9.9999.8888");

        Carro carro3 = new Carro();

        carro3.setColor("Amarelo");
        carro3.setModel("Kombi");
        carro3.setLicensePlate("AAA - 3388");
        carro3.setYear(2019);
        carro3.setUsuario(usuario2);

        Carro carro4 = new Carro();

        carro4.setColor("Azul");
        carro4.setModel("Fusca");
        carro4.setLicensePlate("CCC - 5552");
        carro4.setYear(1960);
        carro4.setUsuario(usuario2);

        List<Carro> carros = new ArrayList<>();
        carros.add(carro3);
        carros.add(carro4);

        usuario2.setCars(carros);

        usuarioService.salvar(usuario2);

    }

}
