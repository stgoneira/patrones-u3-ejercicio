package cl.patrones.sysdonaciones.otras;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cl.patrones.sysdonaciones.core.entities.Causa;
import cl.patrones.sysdonaciones.core.entities.Contribuyente;
import cl.patrones.sysdonaciones.core.entities.Donacion;
import cl.patrones.sysdonaciones.core.entities.Contribuyente.TipoContribuyente;
import cl.patrones.sysdonaciones.core.repositories.CausaRepository;
import cl.patrones.sysdonaciones.core.repositories.ContribuyenteRepository;
import cl.patrones.sysdonaciones.core.repositories.DonacionRepository;
import cl.patrones.sysdonaciones.core.services.DonacionServiceImpl;

public class Otras {
	

	/**
	 * Copia el archivo desde resources a una carpeta temporal y lo devuelve como File
	 * @param filename
	 * @return
	 */
	private File getFileFromResources(String filename) {
		var resource = getClass().getClassLoader().getResource(filename);
		if( resource != null) {
			try {				
				var tmpDirPath = Files.createTempDirectory(null);
				var source = Paths.get(resource.toURI());
				var fArr = filename.split("/");
				var f = fArr[ fArr.length-1 ];
				var target = tmpDirPath.resolve(f);
				Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
				return target.toFile();
			} catch (URISyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Archivo "+filename+" NO encontrado.");
	}
	

	// Happy Path
		@ParameterizedTest
		@MethodSource("factory_registrarDonacionGeneral_datosValidos")
		public void registrarDonacionGeneral_datosValidos_donacionCreada(
			String comprobante,
			Long monto,
			String rut,
			String nombre,
			String email,
			String telefono
		) {	
			// Arrange
			DonacionRepository donacionRepository 			= mock();
			CausaRepository causaRepository 				= mock();
			ContribuyenteRepository contribuyenteRepository = mock();		
			var service = new DonacionServiceImpl(
					donacionRepository,
					causaRepository,
					contribuyenteRepository
			);		
			
			var donacion = new Donacion();		
			var uuid = UUID.randomUUID();
			donacion.setId( uuid );
			
			var contribuyente = new Contribuyente(rut, nombre, email, telefono, TipoContribuyente.NORMAL);
			contribuyente.setId(100L);
			
			var causa = new Causa(2L, "Donaciones Generales", "", "");
			
			
			when( contribuyenteRepository.findByRut(rut) ).thenReturn(Optional.empty());
			when( contribuyenteRepository.save(any()) ).thenReturn(contribuyente);
			when( donacionRepository.save(any(Donacion.class)) ).thenReturn(donacion);
			when( causaRepository.findByNombre("Donaciones Generales") ).thenReturn(Optional.of(causa));
			
			// Act
			var file = getFileFromResources(comprobante);
			var result = service.registrarDonacionGeneral(
					file
					, monto
					, rut
					, nombre
					, email
					, telefono);		
			
			// Assert
			verify(contribuyenteRepository, times(1)).save(any(Contribuyente.class));
			verify(donacionRepository, times(2)).save(any(Donacion.class));
			assertEquals(uuid, result);
		}
		static Stream<Arguments> factory_registrarDonacionGeneral_datosValidos() {
			return Stream.of(
				Arguments.arguments(
						"comprobantes/comprobante.pdf"
						, 1_000L
						, "123456785"
						, "Juan Pérez"
						, "jperez@123.cl"
						, "+56 9 8888 4444")
				,Arguments.arguments(
						"comprobantes/comprobante.pdf"
						, 10_000L
						, "123456785"
						, "Juan Pérez"
						, "jperez@123.cl"
						, "+56 9 8888 4444")
				,Arguments.arguments(
						"comprobantes/comprobante.pdf"
						, 5_000_000L
						, "123456785"
						, "Juan Pérez"
						, "jperez@123.cl"
						, "+56 9 8888 4444")
			);
		}
	
}
