package cl.patrones.sysdonaciones.reportes.proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import cl.patrones.sysdonaciones.reportes.ReporteService;
import cl.patrones.sysdonaciones.reportes.ReporteServiceImpl;

public class ReporteServiceProxyCacheTest {

	@Test
	public void generarReporteDonaciones_desde2doLlamado_devuelveDesdeCache() {
		// Arrange
		ReporteServiceImpl serviceMock = mock();
		ReporteServiceImpl realService = new ReporteServiceImpl();
		var proxyService = new ReporteServiceProxyCache(serviceMock);
		try {
			when(serviceMock.generarReporteDonaciones()).thenReturn( new byte[0] );	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// Act
		try {
			proxyService.generarReporteDonaciones();
			proxyService.generarReporteDonaciones();
			proxyService.generarReporteDonaciones();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Assert
		try {
			verify(serviceMock, times(1)).generarReporteDonaciones();
			
			// Clean
			var tmpDir = System.getProperty("java.io.tmpdir");
			var sysTmpDir = Paths.get(tmpDir, "SysDonaciones");
			var fileName = proxyService.todayFilename() + ".pdf";
			var cacheFilePath = Paths.get(sysTmpDir.toString(), fileName);
			Files.deleteIfExists(cacheFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
