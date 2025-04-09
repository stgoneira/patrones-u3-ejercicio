package cl.patrones.sysdonaciones.core.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Contribuyente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@Column(unique = true)	
	String rut;
	String nombre;
	String email;
	String telefono;
	@Enumerated(EnumType.STRING)
	TipoContribuyente tipo;
	
	public enum TipoContribuyente {
		SOCIO,
		NORMAL
	}
		
	public Contribuyente() {
		super();
	}
		
	public Contribuyente(String rut, String nombre, String email, String telefono, TipoContribuyente tipo) {
		super();
		this.rut = rut;
		this.nombre = nombre;
		this.email = email;
		this.telefono = telefono;
		this.tipo = tipo;
	}
	
	public static class Builder {			
		String rut;
		String nombre;
		String email;
		String telefono;		
		TipoContribuyente tipo;
		
		public Builder() {}
		
		public Builder rut(String rut) {
			this.rut = rut;
			return this;
		}
		public Builder nombre(String nombre) {
			this.nombre = nombre;
			return this;
		}
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		public Builder telefono(String telefono) {
			this.telefono = telefono;
			return this;
		}
		public Builder tipo(TipoContribuyente tipo) {
			this.tipo = tipo;
			return this;
		}
		public Contribuyente build() {
			return new Contribuyente(rut, nombre, email, telefono, tipo);
		}
	}
	
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoContribuyente getTipo() {
		return tipo;
	}

	public void setTipo(TipoContribuyente tipo) {
		this.tipo = tipo;
	}
	
}
