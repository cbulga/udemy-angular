package com.xantrix.webapp.entities;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Articoli implements Serializable {
	private static final long serialVersionUID = 291353626011036772L;

	private String codArt;
	private String descrizione;
	private String um;
	private String codStat;
	private Integer pzCart;
	private double pesoNetto;
	private String idStatoArt;
	private Date dataCreaz;
	private Double prezzo;
}
