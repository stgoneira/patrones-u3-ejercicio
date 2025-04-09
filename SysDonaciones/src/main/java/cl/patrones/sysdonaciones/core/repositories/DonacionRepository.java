package cl.patrones.sysdonaciones.core.repositories;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import cl.patrones.sysdonaciones.core.entities.Donacion;

public interface DonacionRepository extends ListCrudRepository<Donacion, UUID> {

}
