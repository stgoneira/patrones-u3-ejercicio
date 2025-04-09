package cl.patrones.sysdonaciones.notificaciones;

import cl.patrones.sysdonaciones.core.entities.Donacion;

abstract public class NotificacionServiceBase implements NotificacionService {

	private NotificacionService decorado;

	public NotificacionServiceBase(NotificacionService decorado) {
		super();
		this.decorado = decorado;
	}

	@Override
	public void notificar(String transaccionId) {
		if( decorado != null)
			decorado.notificar(transaccionId);		
	}

	@Override
	public void donacionRegistrada(Donacion donacion) {
		notificar(donacion.getId().toString());		
	}
}
