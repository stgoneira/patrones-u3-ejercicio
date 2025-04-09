package cl.patrones.sysdonaciones.core.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import cl.patrones.sysdonaciones.core.entities.Causa;
import cl.patrones.sysdonaciones.core.entities.Contribuyente;
import cl.patrones.sysdonaciones.core.entities.Contribuyente.TipoContribuyente;
import cl.patrones.sysdonaciones.core.entities.Donacion;
import cl.patrones.sysdonaciones.core.exceptions.SocioInexistenteException;
import cl.patrones.sysdonaciones.core.observers.DonacionObserver;
import cl.patrones.sysdonaciones.core.repositories.CausaRepository;
import cl.patrones.sysdonaciones.core.repositories.ContribuyenteRepository;
import cl.patrones.sysdonaciones.core.repositories.DonacionRepository;

@Service
public class DonacionServiceImpl implements DonacionService {

	private ContribuyenteRepository contribuyenteRepository;
	private DonacionRepository donacionRepository;
	private CausaRepository causaRepository;
	private List<DonacionObserver> observadores = new ArrayList<>();

	public DonacionServiceImpl(
			DonacionRepository donacionRepository,
			CausaRepository causaRepository,
			ContribuyenteRepository contribuyenteRepository
	) {
		this.donacionRepository = donacionRepository;
		this.causaRepository = causaRepository;
		this.contribuyenteRepository = contribuyenteRepository;
	}
	
	@Override
	public void registrarObservador(DonacionObserver observador) {
		this.observadores.add(observador);
	}
	
	/**
	 * 
	 * @param file
	 * @return extensión del archivo ej. jpg, jpeg, pdf, etc
	 */
	private String fileExtension(File file) {
		var fileName = file.toString();
		var indexDot = fileName.lastIndexOf('.');
		var ext = fileName.substring(indexDot+1);
		return ext;
	}
	
	private File moveFile(File tempFile, UUID uuid) {
		var extension 	= fileExtension(tempFile);
		var homeDir 	= System.getProperty("user.home");
		var sysDir	 	= Paths.get(homeDir, "SysDonaciones");
		var sysDirFile 	= sysDir.toFile();		
		var destPath 	= Paths.get(sysDir.toString(), uuid.toString()+"."+extension);
		if(!sysDirFile.exists())sysDirFile.mkdirs();
		
		try {
			Files.move(tempFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
			return destPath.toFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Optional<Donacion> obtenerDonacion(String transaccionId) {
		var id = UUID.fromString(transaccionId);
		return donacionRepository.findById(id);
	}
	
	public UUID registrarDonacionAnonima(File comprobante, Long monto) {		
		Optional<Causa> causa = causaRepository.findByNombre("Donaciones Anónimas");
		var donacion = new Donacion(monto, null, causa.get(), comprobante.toURI().toString());
		donacion = donacionRepository.save(donacion);
		
		var finalFile = moveFile(comprobante, donacion.getId());
		donacion.setComprobante( finalFile.getAbsolutePath() );
		donacionRepository.save(donacion);
		notificarObservadores(donacion);
		return donacion.getId();
	}
	
	private void notificarObservadores(Donacion donacion) {
		for(var observador : observadores) {
			observador.donacionRegistrada(donacion);
		}
	}

	public UUID registrarMensualidadSocio(File comprobante, Long monto, String rut) throws SocioInexistenteException {
		Optional<Causa> causa = causaRepository.findByNombre("Mensualidades Socios");
		var optContribuyente = contribuyenteRepository.findByRut(rut);
		Contribuyente contribuyente;
		
		if(optContribuyente.isEmpty()) {
			throw new SocioInexistenteException(rut);
		} else {
			contribuyente = optContribuyente.get();
		}
		var donacion = new Donacion(monto, contribuyente, causa.get(), comprobante.getAbsolutePath());
		donacionRepository.save(donacion);
		var finalFile = moveFile(comprobante, donacion.getId());
		donacion.setComprobante(finalFile.getAbsolutePath());
		donacionRepository.save(donacion);
		notificarObservadores(donacion);
		return donacion.getId();
	}
	
	public UUID registrarDonacionGeneral(File comprobante, Long monto, String rut, String nombre, String email, String telefono) {
		var optContribuyente = contribuyenteRepository.findByRut(rut);
		Contribuyente contribuyente;
		if(optContribuyente.isEmpty()) {
			contribuyente = new Contribuyente(rut, nombre, email, telefono, TipoContribuyente.NORMAL);
			contribuyente = contribuyenteRepository.save(contribuyente);
		} else {
			contribuyente = optContribuyente.get();	
		}		
		var causa = causaRepository.findByNombre("Donaciones Generales");
		var donacion = new Donacion(monto, contribuyente, causa.get(), comprobante.getAbsolutePath());
		donacion = donacionRepository.save(donacion);
		var finalFile = moveFile(comprobante, donacion.getId());
		donacion.setComprobante(finalFile.getAbsolutePath());
		donacionRepository.save(donacion);
		notificarObservadores(donacion);
		return donacion.getId();
	}
	
}
