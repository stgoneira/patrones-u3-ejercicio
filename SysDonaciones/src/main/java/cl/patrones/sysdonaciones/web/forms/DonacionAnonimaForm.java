package cl.patrones.sysdonaciones.web.forms;

import org.springframework.web.multipart.MultipartFile;

public class DonacionAnonimaForm extends DonacionFormBase {
	
	public DonacionAnonimaForm() {
		super();
	}

	protected DonacionAnonimaForm(Long monto, MultipartFile comprobante) {
		super(monto, comprobante);
	}
		
}
