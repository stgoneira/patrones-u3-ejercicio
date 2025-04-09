package cl.patrones.sysdonaciones.reportes.proxy;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import cl.patrones.sysdonaciones.reportes.ReporteService;
import cl.patrones.sysdonaciones.reportes.ReporteServiceImpl;

@Primary
@Service
public class ReporteServiceProxyCache implements ReporteService {

	private ReporteServiceImpl reporteService;
	
	public ReporteServiceProxyCache(ReporteServiceImpl reporteService) {
		super();
		this.reporteService = reporteService;
	}

	private String todayFilename() {
		var hoy = LocalDate.now();
		var formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return formatter.format(hoy);
	}
	
	@Override
	public byte[] generarReporteDonaciones() throws IOException {
		var tmpDir = System.getProperty("java.io.tmpdir");
		var sysTmpDir = Paths.get(tmpDir, "SysDonaciones");
		var fileName = todayFilename() + ".pdf";
		var destPath = Paths.get(sysTmpDir.toString(), fileName);
		
		if( !sysTmpDir.toFile().exists()) sysTmpDir.toFile().mkdirs();
	
		if( destPath.toFile().exists() ) {
			return Files.readAllBytes(destPath);
		}
		
		var pdfBytes = reporteService.generarReporteDonaciones();
		try (var fos = new FileOutputStream(destPath.toFile())) {
			fos.write(pdfBytes);
		}
		return pdfBytes;
	}

}
