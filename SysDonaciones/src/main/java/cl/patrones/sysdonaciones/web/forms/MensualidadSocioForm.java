package cl.patrones.sysdonaciones.web.forms;

public class MensualidadSocioForm extends DonacionFormBase {

	String rut;	
	
	public MensualidadSocioForm() {
		super();
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
	}
	
}
