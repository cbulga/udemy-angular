package com.xantrix.webapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "dettlistini")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "listino")
public class DettListini implements Serializable {

	private static final long serialVersionUID = -2228572746734246102L;
	@Id
//	@Column(name = "id", updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotNull(message = "{NotNull.DettListini.codArt.Validation}")
	@Size(min = 5, max = 20, message = "{Size.DettListini.codArt.Validation}")
	@Column(name = "codart", nullable = false)
	private String codArt;
	@Min(value = (long) 0.1, message = "{Min.DettListini.prezzo.Validation}")
	private double prezzo;
	@ManyToOne
	@JoinColumn(name = "idlist", referencedColumnName = "id")
	@NotNull(message = "{Id Listino NON può essere Null}")
	private Listini listino;

	public DettListini(String codArt, double prezzo, Listini listino) {
		super();
		this.codArt = codArt;
		this.prezzo = prezzo;
		this.listino = listino;
	}
}
