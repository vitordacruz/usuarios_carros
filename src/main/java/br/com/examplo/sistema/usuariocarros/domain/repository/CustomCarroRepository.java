package br.com.examplo.sistema.usuariocarros.domain.repository;

public interface CustomCarroRepository {

    boolean placaExiste(String placa, Long usuarioId);

}
