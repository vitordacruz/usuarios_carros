package br.com.examplo.sistema.usuariocarros.domain;

import br.com.examplo.sistema.usuariocarros.domain.exception.NegocioException;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.repository.CarroRepository;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.util.Optional;

@SpringBootTest
public class CarroServiceTest extends TestCase {


    @Mock
    CarroRepository carroRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    CarroService carroService;

    @Test
    public void testPlacaExiste() {

        MockitoAnnotations.initMocks(this);

        // when
        when(carroRepository.placaExiste(anyString(), anyLong())).thenReturn(true);

        // then
        boolean result = carroService.placaExiste("AAA", 2L);

        assertTrue(result);

    }

    @Test
    public void testPlacaNaoExiste() {

        MockitoAnnotations.initMocks(this);

        // when
        when(carroRepository.placaExiste(anyString(), anyLong())).thenReturn(false);

        // then
        boolean result = carroService.placaExiste("AAA", 2L);

        assertFalse(result);

    }

    @Test
    public void salvarComSucesso() {

        MockitoAnnotations.initMocks(this);

        String licensePlate = "ABC 12345";


        // when

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setLicensePlate(licensePlate);

        Carro carro1 = new Carro();
        carro1.setId(1L);
        carro1.setLicensePlate(licensePlate);

        Carro carro2 = new Carro();
        carro2.setId(1L);
        carro2.setLicensePlate(licensePlate);

        when(carroRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(carro1));
        when(carroRepository.save(any(Carro.class))).thenReturn(carro2);

        // then

        Carro carro3 = carroService.salvar(carro);

        assertEquals(carro.getId(), carro3.getId());

    }

    @Test
    public void errorAoSalvar() {

        MockitoAnnotations.initMocks(this);
        String licensePlate = "ABC 12345";

        // when

        Carro carro = new Carro();
        carro.setId(1L);
        carro.setLicensePlate(licensePlate);

        Carro carro1 = new Carro();
        carro1.setId(2L);
        carro1.setLicensePlate(licensePlate);

        when(carroRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(carro1));
        when(messageSource.getMessage(anyString(), any(), any())).thenReturn("placa.carro.ja.existe");

        // then

        assertThrows(NegocioException.class, () -> carroService.salvar(carro));

    }
}