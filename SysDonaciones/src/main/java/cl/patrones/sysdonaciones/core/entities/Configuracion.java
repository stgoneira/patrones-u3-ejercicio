package cl.patrones.sysdonaciones.core.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Configuracion {
	@Id
	private String clave;
	private String valor;
	
	public Configuracion() {
		super();
	}
	public Configuracion(String clave, String valor) {
		super();
		this.clave = clave;
		this.valor = valor;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

}
