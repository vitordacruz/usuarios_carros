package br.com.examplo.sistema.usuariocarros.domain.repository;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
public class CarroRepositoryImpl implements CustomCarroRepository {
    @PersistenceContext
    private EntityManager manager;

    public boolean placaExiste(String placa, Long usuarioId) {

        if (usuarioId == null) {

            String jpql = "select count(c) from Carro c where c.licensePlate = :placa";

            TypedQuery<Long> query = manager
                    .createQuery(jpql, Long.class);

            query.setParameter("placa", placa);

            Long quantidade = query.getSingleResult();

            return quantidade.longValue() > 1L;


        } else {

            String jpql = "select count(c) from Carro c where c.licensePlate = :placa and c.usuario.id = :usuarioId";

            TypedQuery<Long> query = manager
                    .createQuery(jpql, Long.class);

            query.setParameter("placa", placa);
            query.setParameter("usuarioId", usuarioId);

            Long quantidade = query.getSingleResult();

            return quantidade.longValue() > 1L;

        }


    }
}
