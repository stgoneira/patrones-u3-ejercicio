package cl.patrones.sysdonaciones.reportes;

import java.io.IOException;

public interface ReporteService {

	public byte[] generarReporteDonaciones() throws IOException;
	
}
