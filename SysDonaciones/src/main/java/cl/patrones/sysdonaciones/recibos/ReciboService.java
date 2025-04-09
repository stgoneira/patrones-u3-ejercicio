package cl.patrones.sysdonaciones.recibos;

import java.io.IOException;


public interface ReciboService {

	public byte[] generarRecibo(
			String fileName, 
			String transaccionId, 
			String fecha, 
			String nombre, 
			String monto)
		throws IOException;
}
