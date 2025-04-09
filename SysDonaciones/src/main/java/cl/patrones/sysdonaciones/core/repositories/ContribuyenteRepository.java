package cl.patrones.sysdonaciones.core.repositories;

import java.util.Optional;

import org.springframework.data.repository.ListCrudRepository;

import cl.patrones.sysdonaciones.core.entities.Contribuyente;

public interface ContribuyenteRepository extends ListCrudRepository<Contribuyente, Long> {

	Optional<Contribuyente> findByRut(String rut);
}
