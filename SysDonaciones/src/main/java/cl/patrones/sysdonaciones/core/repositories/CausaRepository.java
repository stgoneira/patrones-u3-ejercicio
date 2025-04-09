package cl.patrones.sysdonaciones.core.repositories;

import java.util.Optional;
import org.springframework.data.repository.ListCrudRepository;
import cl.patrones.sysdonaciones.core.entities.Causa;


public interface CausaRepository extends ListCrudRepository<Causa, Long> {
	
	Optional<Causa> findByNombre(String nombre);

}
