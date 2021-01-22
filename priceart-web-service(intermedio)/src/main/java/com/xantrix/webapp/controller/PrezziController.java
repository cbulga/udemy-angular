package com.xantrix.webapp.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.appconf.AppConfig;
import com.xantrix.webapp.entity.dto.DettListiniDto;
import com.xantrix.webapp.entity.dto.InfoMsg;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.PrezziService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/prezzi")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class PrezziController {

	@Autowired
	private PrezziService prezziService;

	@Autowired
	private AppConfig appConfig;

	@GetMapping(value = {"/{codArt}/{listinoId}", "/{codArt}"}, produces = "application/json")
	public ResponseEntity<Double> getPrezzoArticoloFromListinoAttivoByCodArt(@PathVariable("codArt") String codArt,
			@PathVariable("listinoId") Optional<String> listinoIdOpt)
			throws NotFoundException {
		log.info("****** Otteniamo il prezzo del listino attivo per l'articolo {} *******", codArt);
		String listinoId = listinoIdOpt.orElse(appConfig.getListino());
		log.info("Listino di riferimento: " + listinoId);

		Double prezzo = prezziService.findPrezzoArticoloByListinoIdAndCodArt(listinoId, codArt);
		if (prezzo == null) {
			String errMsg = String.format("Il prezzo del listino %s per l'articolo %s non è stato trovato!", listinoId, codArt);
			log.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(prezzo, HttpStatus.OK);
	}

	// ------------------- ELIMINAZIONE DETTAGLIO LISTINO ------------------------------------
	@DeleteMapping(value = "/elimina/{codArt}/{listinoId}", produces = "application/json")
	public ResponseEntity<InfoMsg> deleteDettListiniByCodArtAndListinoId(@PathVariable("codArt") String codArt, 
			@PathVariable("listinoId") String listinoId) throws NotFoundException {
		log.info("****** Eliminiamo il dettaglio listino per l'articolo con codArt {} and listino con id {} *******", codArt, listinoId);

		DettListiniDto dettListiniDto = prezziService.findByCodArtAndListinoId(codArt, listinoId);
		if (dettListiniDto == null) {
			String errMsg = String.format("Dettaglio listino per articolo %s e listino %s non presente in anagrafica!", codArt, listinoId);
			log.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		prezziService.delDettListini(dettListiniDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Eliminazione Prezzo Eseguita Con Successo"), HttpStatus.OK);
	}
}
