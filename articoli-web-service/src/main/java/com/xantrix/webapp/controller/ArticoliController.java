package com.xantrix.webapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ArticoliService;

@RestController
@RequestMapping("api/articoli")
public class ArticoliController 
{
	private static final Logger logger = LoggerFactory.getLogger(ArticoliController.class);
	
	@Autowired
	private ArticoliService articoliService;
	
	@GetMapping(value = "/cerca/ean/{barcode}", produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByEan(@PathVariable("barcode") String barcode)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con barcode {} *******", barcode);

		ArticoliDto articoliDto = articoliService.findByEan(barcode);

		if (articoliDto == null) {
			String errMsg = String.format("Non è stato trovato alcun articolo avente il barcode %s", barcode);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(articoliDto, HttpStatus.OK);
		
	}

	@GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByCodice(@PathVariable("codart") String codArt)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con codice {} *******", codArt);

		ArticoliDto articoliDto = articoliService.selByCodArt(codArt);
		
		if (articoliDto == null) {
			String errMsg = String.format("L'articolo con codice %s non è stato trovato!", codArt);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(articoliDto, HttpStatus.OK);
	}

	@GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
	public ResponseEntity<List<ArticoliDto>> listArtByDesc(@PathVariable("filter") String filter)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con descrizione {} *******", filter);
		
		List<ArticoliDto> articoli = articoliService.selByDescrizione(filter.toUpperCase() + "%");

		if (articoli == null) {
			String errMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", filter);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(articoli, HttpStatus.OK);
	}
}
