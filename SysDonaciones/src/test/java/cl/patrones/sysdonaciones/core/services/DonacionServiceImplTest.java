package cl.patrones.sysdonaciones.core.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cl.patrones.sysdonaciones.core.entities.Causa;
import cl.patrones.sysdonaciones.core.entities.Contribuyente;
import cl.patrones.sysdonaciones.core.entities.Contribuyente.TipoContribuyente;
import cl.patrones.sysdonaciones.core.entities.Donacion;
import cl.patrones.sysdonaciones.core.exceptions.SocioInexistenteException;
import cl.patrones.sysdonaciones.core.observers.DonacionObserver;
import cl.patrones.sysdonaciones.core.repositories.CausaRepository;
import cl.patrones.sysdonaciones.core.repositories.ContribuyenteRepository;
import cl.patrones.sysdonaciones.core.repositories.DonacionRepository;
import util.TestUtils;

public class DonacionServiceImplTest {
	
	@Test
	public void registrarMensualidadSocio_observadoresRegistrados_observadoresNotificados() {
		// Arrange		
		DonacionRepository donacionRepo = mock();
		CausaRepository causaRepo = mock();
		ContribuyenteRepository contribuyenteRepo = mock();
		DonacionObserver observer1 = mock();
		DonacionObserver observer2 = mock();
		DonacionServiceImpl service = new DonacionServiceImpl(donacionRepo, causaRepo, contribuyenteRepo);
		service.registrarObservador(observer1);
		service.registrarObservador(observer2);
		var file = TestUtils.getFileFromResources("comprobantes/comprobante.pdf");
		var monto = 10_000L;
		var rut = "13245678-5";
		var contribuyente = new Contribuyente.Builder()
				.email("aaa@123.cl")
				.nombre("AAAA BBBB")
				.rut(rut)
				.telefono("+56 9 8888 4444")
				.tipo(TipoContribuyente.SOCIO)
				.build();
		var causa = new Causa(1L, "Mensualidades Socios", "", "");
		var uuid = UUID.randomUUID();
		when(causaRepo.findByNombre("Mensualidades Socios")).thenReturn(Optional.of(causa));
		when(contribuyenteRepo.findByRut(rut)).thenReturn( Optional.of(contribuyente) );
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				var d = (Donacion) invocation.getArgument(0);
				d.setId(uuid);
				return d;
			}
		}).when(donacionRepo).save(any(Donacion.class));
		
		// Act
		try {
			service.registrarMensualidadSocio(file, monto, rut);
		} catch (SocioInexistenteException e) {
			e.printStackTrace();
		}
		
		// Assert
		verify(observer1, times(1)).donacionRegistrada(any());
		verify(observer2, times(1)).donacionRegistrada(any());
	}
		
}
