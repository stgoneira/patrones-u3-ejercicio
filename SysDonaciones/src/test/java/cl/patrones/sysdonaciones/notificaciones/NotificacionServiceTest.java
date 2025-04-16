package cl.patrones.sysdonaciones.notificaciones;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class NotificacionServiceTest {

	@Test
	public void notificar_decorador_ok() {
		// Arrange
		EmailNotificacionService emailMock 	= mock();
		SmsNotificacionService smsMock 		= mock();
		MobileAppNotificacionService appMock = mock();
		
		var emailAndSms = new EmailNotificacionService(smsMock);
		var smsAndApp = new SmsNotificacionService(appMock);
		var appAndEmail = new MobileAppNotificacionService(emailMock);
		
		var transactionId = UUID.randomUUID().toString();
		
		// Act
		emailAndSms.notificar(transactionId);
		smsAndApp.notificar(transactionId);
		appAndEmail.notificar(transactionId);
		
		// Assert
		verify(smsMock, times(1)).notificar(transactionId);
		verify(appMock, times(1)).notificar(transactionId);
		verify(emailMock, times(1)).notificar(transactionId);
	}
}
