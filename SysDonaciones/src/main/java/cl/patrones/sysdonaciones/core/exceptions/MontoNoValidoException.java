package cl.patrones.sysdonaciones.core.exceptions;

public class MontoNoValidoException extends Exception {

	private static final long serialVersionUID = 5300382672183519399L;

	public MontoNoValidoException(Long donado, Long minimo, Long maximo) {		
		super(String.format("El monto donado: %d no está dentro del permitido %d - %d.", donado, minimo, maximo));
	}
	
}
