package cl.patrones.sysdonaciones.notificaciones;

import cl.patrones.sysdonaciones.core.observers.DonacionObserver;

public interface NotificacionService extends DonacionObserver {

	public void notificar(String transaccionId);
}
