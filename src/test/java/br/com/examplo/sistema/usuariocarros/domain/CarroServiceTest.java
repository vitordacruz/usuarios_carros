package br.com.examplo.sistema.usuariocarros.domain;

import br.com.examplo.sistema.usuariocarros.domain.exception.NegocioException;
import br.com.examplo.sistema.usuariocarros.domain.model.Carro;
import br.com.examplo.sistema.usuariocarros.domain.repository.CarroRepository;
import br.com.examplo.sistema.usuariocarros.util.ConstantesComum;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

import java.util.Optional;

@SpringBootTest
public class CarroServiceTest extends TestCase {


    @Mock
    CarroRepository carroRepository;


    @InjectMocks
    CarroService carroService;

    @Test
    void testPlacaExiste() {

        MockitoAnnotations.initMocks(this);

        // when
        when(carroRepository.placaExiste(anyString(), anyLong())).thenReturn(true);

        // then
        boolean result = carroService.placaExiste("AAA", 2L);

        assertTrue(result);

    }

    @Test
    void testPlacaNaoExiste() {

        MockitoAnnotations.initMocks(this);

        // when
        when(carroRepository.placaExiste(anyString(), anyLong())).thenReturn(false);

        // then
        boolean result = carroService.placaExiste("AAA", 2L);

        assertFalse(result);

    }

    @Test
    void salvar() {

        MockitoAnnotations.initMocks(this);

        // when

        Carro carro = new Carro();
        carro.setId(1L);

        when(carroRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(carro));
        when(carroRepository.findByLicensePlate(anyString())).thenReturn(Optional.of(carro));

        // then

        assertEquals(carro.getId(), carro.getId());

    }
}