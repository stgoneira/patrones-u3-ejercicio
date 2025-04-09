package cl.patrones.sysdonaciones.core.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Donacion {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	UUID id;
	LocalDateTime fecha = LocalDateTime.now();
	Long monto;
	@ManyToOne
	Contribuyente contribuyente;
	@ManyToOne
	Causa causa;
	String comprobante;
	
	public Donacion() {
		super();
	}

	public Donacion(Long monto, Contribuyente contribuyente, Causa causa, String comprobante) {
		super();
		this.monto = monto;
		this.contribuyente = contribuyente;
		this.causa = causa;
		this.comprobante = comprobante;
	}
	
	public static class Builder {
		
		private Long monto;		
		private Contribuyente contribuyente;		
		private Causa causa;
		private String comprobante;
		
		public Builder() {}
		
		public Builder monto(Long monto) {
			this.monto = monto;
			return this;
		}
		
		public Builder contribuyente(Contribuyente contribuyente) {
			this.contribuyente = contribuyente;
			return this;
		}
		
		public Builder causa(Causa causa) {
			this.causa = causa;
			return this;
		}
		
		public Builder comprobante(String comprobante) {
			this.comprobante = comprobante;
			return this;
		}
		
		public Donacion build() {
			return new Donacion(monto, contribuyente, causa, comprobante);
		}
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public Long getMonto() {
		return monto;
	}

	public void setMonto(Long monto) {
		this.monto = monto;
	}

	public Contribuyente getContribuyente() {
		return contribuyente;
	}

	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}

	public Causa getCausa() {
		return causa;
	}

	public void setCausa(Causa causa) {
		this.causa = causa;
	}
	
	public String getComprobante() {
		return comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
	
	
}
