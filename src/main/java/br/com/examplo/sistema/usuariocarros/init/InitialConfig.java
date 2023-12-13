package br.com.examplo.sistema.usuariocarros.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialConfig {

    @Autowired
    private DadosIniciais initialData;

    @Bean
    public boolean instantiateDatabase() {

        initialData.iniciarDadosDatabase();

        return true;
    }

}
