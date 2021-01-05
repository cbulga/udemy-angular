package com.xantrix.webapp.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entity.Articoli;

public interface ArticoliService 
{
	public Iterable<Articoli> selTutti();
	
	public List<ArticoliDto> selByDescrizione(String descrizione);
		
	public List<Articoli> selByDescrizione(String descrizione, Pageable pageable);
	
	public ArticoliDto selByCodArt(String codArt);
	
	public void delArticolo(ArticoliDto articolo);
	
	public void insArticolo(ArticoliDto articolo);

	ArticoliDto findByEan(String ean);
}
