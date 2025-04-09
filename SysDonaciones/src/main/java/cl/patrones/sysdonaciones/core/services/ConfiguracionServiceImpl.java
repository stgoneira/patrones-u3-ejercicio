package cl.patrones.sysdonaciones.core.services;

import org.springframework.stereotype.Service;

import cl.patrones.sysdonaciones.core.repositories.ConfiguracionRepository;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

	private ConfiguracionRepository repository;
	
	public ConfiguracionServiceImpl(ConfiguracionRepository repository) {
		super();
		this.repository = repository;
	}

	public String obtenerConfiguración(String clave) {
		var configurationOpt = repository.findById(clave);
		if( configurationOpt.isPresent()) {
			return configurationOpt.get().getValor();
		}
		return "";
	}
	
	
}
