package cl.patrones.sysdonaciones.core.exceptions;

public class SocioInexistenteException extends Exception {

	private static final long serialVersionUID = 5300382672183519399L;

	public SocioInexistenteException(String rut) {
		super("Socio con RUT "+rut+" inexistente");
	}
	
}
