package com.xantrix.webapp.dtos;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.xantrix.webapp.entity.Barcode;
import com.xantrix.webapp.entity.FamAssort;
import com.xantrix.webapp.entity.Ingredienti;
import com.xantrix.webapp.entity.Iva;

import lombok.Data;

@Data
public class ArticoliDto 
{
	private String codArt;
	private String descrizione;	
	private String um;
	private String codStat;
	private Integer pzCart;
	private double pesoNetto;
	private String idStatoArt;
	private Date dataCreaz;
	private double prezzo = 0;
	
	private Set<Barcode> barcode = new HashSet<>();
	private Ingredienti ingredienti;
	private FamAssort famAssort;
	private Iva iva;
}
