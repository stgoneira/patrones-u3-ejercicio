package cl.patrones.sysdonaciones.web;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cl.patrones.sysdonaciones.core.entities.Contribuyente;
import cl.patrones.sysdonaciones.core.services.DonacionService;
import cl.patrones.sysdonaciones.recibos.ReciboService;
import cl.patrones.sysdonaciones.reportes.ReporteService;
import cl.patrones.sysdonaciones.web.forms.DonacionAnonimaForm;
import cl.patrones.sysdonaciones.web.forms.DonacionGeneralForm;
import cl.patrones.sysdonaciones.web.forms.MensualidadSocioForm;

@Controller
public class AppWebController {
	
	private DonacionService donacionService;
	private ReciboService reciboService;	
	private ReporteService reporteService;
		
	public AppWebController(
			DonacionService donacionService, 
			ReciboService reciboService,
			ReporteService reporteService
	) {
		super();
		this.donacionService 	= donacionService;
		this.reciboService	 	= reciboService;		
		this.reporteService 	= reporteService;
	}
	
	@GetMapping("/reporte/donaciones")
	public String reporteDonacionesHtml() {
		return "reporte/reporte-donaciones";
	}
	
	@GetMapping("/reporte/raw")
	public ResponseEntity<byte[]> reporteDonacionesPdf() {
		try {
			var pdfBytes = reporteService.generarReporteDonaciones();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("inline", "reporte.pdf");
			
			return ResponseEntity
					.ok()
					.headers(headers)				
					.body(pdfBytes)
			;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity
				.internalServerError()
				.body(null)
		;
	}

	@GetMapping("/recibo/{transaccion}")
	public ResponseEntity<byte[]> recibo(@PathVariable("transaccion") String transaccionId) throws Exception {
		var fileName = transaccionId;
		var donacionOpt = donacionService.obtenerDonacion(transaccionId);
		if(donacionOpt.isEmpty()) {
			throw new Exception("Transaccion "+transaccionId+" NO Existe");
		} 
		
		var donacion = donacionOpt.get();
		Optional<Contribuyente> contribuyenteOpt = Optional.ofNullable(donacion.getContribuyente());
		var nombre = contribuyenteOpt.isPresent() ? contribuyenteOpt.get().getNombre() : "Anónimo";
		var monto = NumberFormat.getInstance().format(donacion.getMonto());
		var pdfBytes = reciboService.generarRecibo(fileName, transaccionId, donacion.getFecha().toString(), nombre , monto);	
				
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "recibo.pdf");
		
		return ResponseEntity
				.ok()
				.headers(headers)
				.body(pdfBytes)
		;
	}
	
	@GetMapping("/donacion/socio")
	public String formMensualidadSocio(Model model) {
		model.addAttribute("form", new MensualidadSocioForm());
		return "/donacion/donacion-socio";
	}
	@PostMapping("/donacion/socio")
	public String mensualidadSocio(
		@RequestParam("comprobante") MultipartFile comprobante,
		@ModelAttribute MensualidadSocioForm form,
		Model model
	) throws IllegalStateException, IOException {
		try {
			form.setComprobante(comprobante);
			var uuid = donacionService.registrarMensualidadSocio(form.getComprobanteAsFile(), form.getMonto(), form.getRut());					
			var transaccionId = uuid.toString();			
			var reciboUrl = "/recibo/"+transaccionId;
			model.addAttribute("reciboUrl", reciboUrl);
			return "/donacion/exito";	
		} catch (Exception e) {
			var mensajeErr = e.getMessage();
			model.addAttribute("error", "Error:"+ mensajeErr);			
			return "/donacion/error";
		}		
	}
	
	@GetMapping({"/","/donacion/general"})
	public String formDonacionGeneral(Model model) {
		model.addAttribute("form", new DonacionGeneralForm());
		return "/donacion/donacion";
	}
	@PostMapping({"/","/donacion/general"})
	public String donacionGeneral(
		@RequestParam("comprobante") MultipartFile comprobante,
		@ModelAttribute DonacionGeneralForm form,
		Model model
	) throws IllegalStateException, IOException {
		try {
			form.setComprobante(comprobante);
			var uuid = donacionService.registrarDonacionGeneral(form.getComprobanteAsFile(), form.getMonto(), form.getRut(), form.getNombre(), form.getEmail(), form.getTelefono());				
			var transaccionId = uuid.toString();			
			var reciboUrl = "/recibo/"+transaccionId;
			model.addAttribute("reciboUrl", reciboUrl);
			return "/donacion/exito";	
		} catch (Exception e) {
			var mensajeErr = e.getMessage();
			model.addAttribute("error", "Error:"+ mensajeErr);			
			return "/donacion/error";
		}
	} 
	
	@GetMapping("/donacion/anonima")
	public String formDonacionAnonima(Model model) {
		model.addAttribute("form", new DonacionAnonimaForm());
		return "/donacion/donacion-anonima";
	}		
	@PostMapping("/donacion/anonima")
	public String donacionAnonima(
			@RequestParam("comprobante") MultipartFile comprobante,
			@ModelAttribute DonacionAnonimaForm form,
			Model model
	) throws IOException {
		try {
			form.setComprobante(comprobante);		
			var uuid = donacionService.registrarDonacionAnonima(form.getComprobanteAsFile(), form.getMonto()).toString();
			var transaccionId = uuid.toString();
			var reciboUrl = "/recibo/"+transaccionId;
			model.addAttribute("reciboUrl", reciboUrl);
			return "/donacion/exito";
		} catch (Exception e) {
			var mensajeErr = e.getMessage();
			model.addAttribute("error", "Error:"+ mensajeErr);			
			return "/donacion/error";
		}
	}
		
}