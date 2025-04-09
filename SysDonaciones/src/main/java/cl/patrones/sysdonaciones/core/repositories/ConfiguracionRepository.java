package cl.patrones.sysdonaciones.core.repositories;

import org.springframework.data.repository.ListCrudRepository;

import cl.patrones.sysdonaciones.core.entities.Configuracion;

public interface ConfiguracionRepository extends ListCrudRepository<Configuracion, String> {

}
