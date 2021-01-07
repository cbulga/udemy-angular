package com.xantrix.webapp.dtos;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.entity.FamAssort;
import com.xantrix.webapp.entity.Ingredienti;
import com.xantrix.webapp.entity.Iva;

import lombok.Data;

@Data
public class ArticoliDto {

	@Size(min = 5, max = 20, message = "{Size.Articoli.codArt.Validation}")
	@NotNull(message = "{NotNull.Articoli.codArt.Validation}")
	private String codArt;
	@Size(min = 6, max = 80, message = "{Size.Articoli.descrizione.Validation}")
	private String descrizione;
	private String um;
	private String codStat;
	@Max(value = 99, message = "{Max.Articoli.pzCart.Validation}")
	private Integer pzCart;
	@Min(value = (long) 0.01, message = "{Min.Articoli.pesoNetto.Validation}")
	@Max(value = 100, message = "{Max.Articoli.pesoNetto.Validation}")
	private double pesoNetto;
	private String idStatoArt;
	private Date dataCreaz;
	private double prezzo = 0;

	private Set<Barcode> barcode = new HashSet<>();
	private Ingredienti ingredienti;
	private FamAssort famAssort;
	private Iva iva;
}
