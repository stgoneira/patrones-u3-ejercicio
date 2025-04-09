package cl.patrones.sysdonaciones.reportes;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class ReporteServiceImpl implements ReporteService {

	public byte[] generarReporteDonaciones() throws IOException {
		try {
			Thread.sleep(6_000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String reportePathStr = "/static/files/reporte.pdf";
		var resource = new ClassPathResource(reportePathStr);
		return resource.getContentAsByteArray();
	}
	
}
