package cl.patrones.sysdonaciones.core.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cl.patrones.sysdonaciones.core.entities.Causa;
import cl.patrones.sysdonaciones.core.entities.Donacion;
import cl.patrones.sysdonaciones.core.exceptions.MontoNoValidoException;
import cl.patrones.sysdonaciones.core.repositories.CausaRepository;
import cl.patrones.sysdonaciones.core.repositories.ContribuyenteRepository;
import cl.patrones.sysdonaciones.core.repositories.DonacionRepository;
import util.TestUtils;

public class DonacionServiceImplMontosTest {
	
	private DonacionRepository donacionRepo;
	private CausaRepository causaRepo;
	private ContribuyenteRepository contribuyenteRepo;
	
	
	@BeforeEach
	void setup() {
		donacionRepo 		= mock();
		causaRepo 			= mock();
		contribuyenteRepo 	= mock();
		
		Causa causaAnon = new Causa(1L, "Donaciones Anónimas", "", "");
		when(causaRepo.findByNombre("Donaciones Anónimas")).thenReturn(Optional.of(causaAnon));
		Causa causaGen = new Causa(2L, "Donaciones Generales", "", "");
		when(causaRepo.findByNombre("Donaciones Generales")).thenReturn(Optional.of(causaGen));
	}
	
	@ParameterizedTest
	@ValueSource(longs = {5000, 5001, 10_000, 4_999_999, 5_000_000})
	public void registrarDonacionAnonima_montosValidos_registroOk(long monto) {
		// Arrange		
		var uuid = UUID.randomUUID();
		
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				var d = (Donacion) invocation.getArgument(0);
				d.setId(uuid);
				return d;
			}
		}).when(donacionRepo).save(any(Donacion.class));
		
		var service = new DonacionServiceImpl(donacionRepo, causaRepo, contribuyenteRepo);
		File file = TestUtils.getFileFromResources("comprobantes/comprobante.pdf");
		UUID resultado = null;
		
		// Act
		resultado = assertDoesNotThrow(() -> service.registrarDonacionAnonima(file, monto));
		
		// Assert
		assertEquals(resultado, uuid);
		
	}
	
	@ParameterizedTest
	@ValueSource(longs = {1_000,4_999, 5_000_001, 6_000_000})
	public void registrarDonacionAnonima_montosInvalidos_generaException(long monto) {				
		var service = new DonacionServiceImpl(donacionRepo, causaRepo, contribuyenteRepo);
		File file = TestUtils.getFileFromResources("comprobantes/comprobante.pdf");		
		
		assertThrows(MontoNoValidoException.class ,() -> service.registrarDonacionAnonima(file, monto));
		
	}
		
}
