package com.xantrix.webapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.dtos.InfoMsg;
import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ArticoliService;

@RestController
@RequestMapping("api/articoli")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticoliController {

	private static final Logger logger = LoggerFactory.getLogger(ArticoliController.class);

	@Autowired
	private ArticoliService articoliService;

	@Autowired
	private ResourceBundleMessageSource errMessage;

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
	public ResponseEntity<ArticoliDto> listArtByCodArt(@PathVariable("codart") String codArt)
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

		List<ArticoliDto> articoli = articoliService.selByDescrizione("%" + filter.toUpperCase() + "%");
		if (articoli == null) {
			String errMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", filter);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(articoli, HttpStatus.OK);
	}

	// ------------------- INSERIMENTO ARTICOLO ------------------------------------
	@PostMapping(value = "/inserisci")
	public ResponseEntity<?> createArt(@Valid @RequestBody ArticoliDto articolo, BindingResult bindingResult)
		throws BindingException, DuplicateException	{
		logger.info("Salviamo l'articolo con codice {}", articolo.getCodArt());

		//controllo validità dati articolo
		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			logger.warn(msgErr);
			throw new BindingException(msgErr);
		}

		//Disabilitare se si vuole gestire anche la modifica 
		ArticoliDto checkArt =  articoliService.selByCodArt(articolo.getCodArt());
		if (checkArt != null) {
			String msgErr = String.format("Articolo %s presente in anagrafica! "
					+ "Impossibile utilizzare il metodo POST", articolo.getCodArt());
			logger.warn(msgErr);
			throw new DuplicateException(msgErr);
		}

		articoliService.insArticolo(articolo);
		return new ResponseEntity<>(createResponseNode(HttpStatus.OK.toString(), "Inserimento Articolo " + articolo.getCodArt() + " Eseguito Con Successo"), new HttpHeaders(), HttpStatus.CREATED);
	}

	// ------------------- MODIFICA ARTICOLO ------------------------------------
	@PutMapping(value = "/modifica")
	public ResponseEntity<InfoMsg> updateArt(@Valid @RequestBody ArticoliDto articolo, BindingResult bindingResult)
			throws BindingException, NotFoundException {
		logger.info("Modifichiamo l'articolo con codice {}", articolo.getCodArt());

		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			logger.warn(msgErr);
			throw new BindingException(msgErr);
		}

		ArticoliDto checkArt =  articoliService.selByCodArt(articolo.getCodArt());
		if (checkArt == null) {
			String msgErr = String.format("Articolo %s non presente in anagrafica! "
					+ "Impossibile utilizzare il metodo PUT", articolo.getCodArt());
			logger.warn(msgErr);
			throw new NotFoundException(msgErr);
		}

		articoliService.insArticolo(articolo);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Modifica Articolo " + articolo.getCodArt() + " Eseguita Con Successo"), new HttpHeaders(), HttpStatus.CREATED);
	}

	private ObjectNode createResponseNode(String code, String message) {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", code);
		responseNode.put("message", message);
		return responseNode;
	}
	
	// ------------------- ELIMINAZIONE ARTICOLO ------------------------------------
	@DeleteMapping(value = "/elimina/{codart}", produces = "application/json")
	public ResponseEntity<InfoMsg> deleteArt(@PathVariable("codart") String codArt) throws NotFoundException {
		logger.info("Eliminiamo l'articolo con codice {}", codArt);

		ArticoliDto articolo = articoliService.selByCodArt(codArt);
		if (articolo == null) {
			String msgErr = String.format("Articolo %s non presente in anagrafica!",codArt);
			logger.warn(msgErr);
			throw new NotFoundException(msgErr);
		}

		articoliService.delArticolo(articolo);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Eliminazione Articolo " + articolo.getCodArt() + " Eseguita Con Successo"), new HttpHeaders(), HttpStatus.OK);
	}
}
