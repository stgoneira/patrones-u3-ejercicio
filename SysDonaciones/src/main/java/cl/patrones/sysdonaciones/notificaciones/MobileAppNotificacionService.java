package cl.patrones.sysdonaciones.notificaciones;

public class MobileAppNotificacionService extends NotificacionServiceBase {

	public MobileAppNotificacionService(NotificacionService decorado) {
		super(decorado);
	}

	@Override
	public void notificar(String transaccionId) {
		super.notificar(transaccionId);
		System.out.println("Enviando push notification....");
	}

}
