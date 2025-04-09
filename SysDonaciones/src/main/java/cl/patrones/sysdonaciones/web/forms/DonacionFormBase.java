package cl.patrones.sysdonaciones.web.forms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.web.multipart.MultipartFile;

public abstract class DonacionFormBase {

	protected Long monto;
	protected MultipartFile comprobante;
	
	public DonacionFormBase() {
		super();
	}

	protected DonacionFormBase(Long monto, MultipartFile comprobante) {
		super();
		this.monto = monto;
		this.comprobante = comprobante;
	}

	public File getComprobanteAsFile() throws IllegalStateException, IOException {
		var fileName = comprobante.getOriginalFilename();
		var tmpDir = System.getProperty("java.io.tmpdir");
		var path = Paths.get(tmpDir, fileName);		
		comprobante.transferTo(path);
		return path.toFile();
	}
	
	public Long getMonto() {
		return monto;
	}

	public void setMonto(Long monto) {
		this.monto = monto;
	}

	public MultipartFile getComprobante() {
		return comprobante;
	}

	public void setComprobante(MultipartFile comprobante) {
		this.comprobante = comprobante;
	}
	
	
	
}
