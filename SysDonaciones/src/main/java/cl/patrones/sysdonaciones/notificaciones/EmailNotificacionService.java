package cl.patrones.sysdonaciones.notificaciones;

public class EmailNotificacionService extends NotificacionServiceBase {

	public EmailNotificacionService(NotificacionService decorado) {
		super(decorado);
	}

	@Override
	public void notificar(String transaccionId) {
		super.notificar(transaccionId);
		System.out.println("Enviando email....");
	}
	
}
