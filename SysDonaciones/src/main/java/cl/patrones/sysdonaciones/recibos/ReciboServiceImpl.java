package cl.patrones.sysdonaciones.recibos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class ReciboServiceImpl implements ReciboService {

	@Override
	public byte[] generarRecibo(String fileName, String transaccionId, String fecha, String nombre, String monto)
			throws IOException {
		String plantilla = "/static/plantillas/comprobante.pdf";
		var tmpDir = System.getProperty("java.io.tmpdir");
		var sysTmpDir = Paths.get(tmpDir, "SysDonaciones");
		var destPath = Paths.get(sysTmpDir.toString(), fileName + ".pdf");
		if (!sysTmpDir.toFile().exists())
			sysTmpDir.toFile().mkdirs();

		var resource = new ClassPathResource(plantilla);
		var reader = new PdfReader(resource.getInputStream());
		var writer = new PdfWriter(destPath.toString());
		PdfDocument pdf = new PdfDocument(reader, writer);

		var formulario = PdfAcroForm.getAcroForm(pdf, true);
		var campos = formulario.getAllFormFields();

		campos.get("transaccion").setValue(transaccionId);
		campos.get("nombre").setValue(nombre);
		campos.get("monto").setValue(monto);
		campos.get("fecha").setValue(fecha);

		formulario.flattenFields();
		pdf.close();

		var pdfBytes = Files.readAllBytes(destPath);
		Files.delete(destPath);
		return pdfBytes;
	}

}
