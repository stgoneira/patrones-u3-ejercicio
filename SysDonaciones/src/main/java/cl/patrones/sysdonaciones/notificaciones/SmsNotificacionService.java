package cl.patrones.sysdonaciones.notificaciones;

public class SmsNotificacionService extends NotificacionServiceBase {

	public SmsNotificacionService(NotificacionService decorado) {
		super(decorado);
	}

	@Override
	public void notificar(String transaccionId) {
		super.notificar(transaccionId);
		System.out.println("Enviando SMS....");
	}
	
}
