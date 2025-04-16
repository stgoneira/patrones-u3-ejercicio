package cl.patrones.sysdonaciones.notificaciones;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import cl.patrones.sysdonaciones.core.services.ConfiguracionService;
import cl.patrones.sysdonaciones.core.services.DonacionService;

public class NotificacionServiceProducerTest {

	@Test
	public void notificacionService_todasActivadas_devuelve3Decorados() {
		// Arrange
		ConfiguracionService confService = mock();
		DonacionService donacionService = mock();
		var producer = new NotificacionServiceProducer(confService, donacionService);
		
		var claveNotiEmail 	= "notificacion-email";
		var claveNotiSms 	= "notificacion-sms";
		var claveNotiApp 	= "notificacion-app";
		when(confService.obtenerConfiguración(claveNotiApp)).thenReturn("true");
		when(confService.obtenerConfiguración(claveNotiSms)).thenReturn("true");
		when(confService.obtenerConfiguración(claveNotiEmail)).thenReturn("true");
		
		// Act 
		var service = (NotificacionServiceBase) producer.notificacionService();		
		
		// Assert 
		assertNotNull(service);
		assertInstanceOf(MobileAppNotificacionService.class, service);
		var decorado = (NotificacionServiceBase) service.getDecorado();
		assertNotNull( decorado );
		assertInstanceOf(SmsNotificacionService.class, decorado);
		assertNotNull( decorado.getDecorado() );
		assertInstanceOf(EmailNotificacionService.class, decorado.getDecorado());
	}
	
	@Test
	public void notificacionService_ningunaActivada_devuelveEmailPorDefecto() {
		// Arrange
		ConfiguracionService confService = mock();
		DonacionService donacionService = mock();
		var producer = new NotificacionServiceProducer(confService, donacionService);
		
		var claveNotiEmail 	= "notificacion-email";
		var claveNotiSms 	= "notificacion-sms";
		var claveNotiApp 	= "notificacion-app";
		when(confService.obtenerConfiguración(claveNotiApp)).thenReturn("false");
		when(confService.obtenerConfiguración(claveNotiSms)).thenReturn("false");
		when(confService.obtenerConfiguración(claveNotiEmail)).thenReturn("false");
		
		// Act 
		var service = producer.notificacionService();		
		
		// Assert 
		assertInstanceOf(EmailNotificacionService.class, service);
	}
	
}
