package cl.patrones.sysdonaciones.core.observers;

import cl.patrones.sysdonaciones.core.entities.Donacion;

public interface DonacionObserver {

	public void donacionRegistrada(Donacion donacion);
}
