package com.xantrix.webapp.controller;

import java.util.List;

import javax.validation.Valid;

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

import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.model.dtos.InfoMsg;
import com.xantrix.webapp.model.dtos.UtentiDto;
import com.xantrix.webapp.service.UtentiService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/utenti")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class UtentiController {

	@Autowired
	private UtentiService utentiService;

	@Autowired
	private ResourceBundleMessageSource errMessage;

	// ------------------- INSERIMENTO UTENTE ------------------------------------
	@PostMapping(value = "/inserisci", produces = "application/json")
	public ResponseEntity<InfoMsg> createUtenti(@Valid @RequestBody UtentiDto utenteDto, BindingResult bindingResult)
		throws BindingException, DuplicateException	{
		log.info("Salviamo l'utente con userId {}", utenteDto.getUserId());

		//controllo validità dati utente
		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warn(msgErr);
			throw new BindingException(msgErr);
		}

		//Disabilitare se si vuole gestire anche la modifica 
		UtentiDto checkUtenti =  utentiService.findByUserId(utenteDto.getUserId());
		if (checkUtenti != null) {
			String msgErr = String.format("Utente %s presente in anagrafica! "
					+ "Impossibile utilizzare il metodo POST", utenteDto.getUserId());
			log.warn(msgErr);
			throw new DuplicateException(msgErr);
		}

		utentiService.insUtente(utenteDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), String.format("Inserimento Utente %s Eseguito Con Successo", utenteDto.getUserId())), new HttpHeaders(), HttpStatus.CREATED);
	}

	// ------------------- MODIFICA UTENTE ------------------------------------
	@PutMapping(value = "/modifica", produces = "application/json")
	public ResponseEntity<InfoMsg> updateUtenti(@Valid @RequestBody UtentiDto utentiDto, BindingResult bindingResult)
			throws BindingException, NotFoundException {
		log.info("Modifichiamo l'utente con userId {}", utentiDto.getUserId());

		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warn(msgErr);
			throw new BindingException(msgErr);
		}

		UtentiDto checkUtenti =  utentiService.findByUserId(utentiDto.getUserId());
		if (checkUtenti == null) {
			String msgErr = String.format("Utente %s non presente in anagrafica! "
					+ "Impossibile utilizzare il metodo PUT", utentiDto.getUserId());
			log.warn(msgErr);
			throw new NotFoundException(msgErr);
		}

		utentiService.insUtente(utentiDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Modifica Utente " + utentiDto.getUserId() + " Eseguita Con Successo"), new HttpHeaders(), HttpStatus.CREATED);
	}

	@GetMapping(value = "/cerca/userid/{userId}", produces = "application/json")
	public ResponseEntity<UtentiDto> listUtentiByUserId(@PathVariable(value = "userId") String userId) throws NotFoundException {
		log.info("************** Otteniamo l'utente con userId {} ****************", userId);
		UtentiDto utentiDto =  utentiService.findByUserId(userId);
		if (utentiDto == null) {
			String message = String.format("L'utente con userId %s non è stato trovato!", userId);
			log.error(message);
			throw new NotFoundException(message);
		}
		return new ResponseEntity<>(utentiDto, HttpStatus.OK);
	}

	@GetMapping(value = "/cerca/tutti", produces = "application/json")
	public ResponseEntity<List<UtentiDto>> getAllUsers() {
		log.info("********* Otteniamo tutti gli utenti **************");
		return new ResponseEntity<>(utentiService.getAllUsers(), HttpStatus.OK);
	}

	@DeleteMapping(value = "/elimina/{userId}", produces = "application/json")
	public ResponseEntity<InfoMsg> deleteUtentiByUserId(@PathVariable(name = "userId") String userId) {
		log.info("******** Eliminiamo l'utente con userId {} *************", userId);
		utentiService.deleteUtentiByUserId(userId);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), String.format("Eliminazione Utente %s Eseguita Con Successo", userId)), HttpStatus.OK);
	}
}
