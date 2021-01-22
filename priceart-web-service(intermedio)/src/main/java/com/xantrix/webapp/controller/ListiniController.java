package com.xantrix.webapp.controller;

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

import com.xantrix.webapp.entity.dto.InfoMsg;
import com.xantrix.webapp.entity.dto.ListiniDto;
import com.xantrix.webapp.exception.BindingException;
import com.xantrix.webapp.exception.DuplicateException;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.service.ListiniService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/listino")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class ListiniController {

	@Autowired
	private ResourceBundleMessageSource errMessage;
	@Autowired
	private ListiniService listiniService;

	// ------------------- INSERIMENTO LISTINO ------------------------------------
	@PostMapping(value = "/inserisci", produces = "application/json")
	public ResponseEntity<InfoMsg> createListino(@Valid @RequestBody ListiniDto listiniDto, BindingResult bindingResult) throws BindingException, DuplicateException {
		log.info("Salviamo il listino con id {}", listiniDto.getId());

		// controllo validità dati listino
		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warn(msgErr);
			throw new BindingException(msgErr);
		}

		//Disabilitare se si vuole gestire anche la modifica 
		ListiniDto checkListiniDto =  listiniService.findById(listiniDto.getId());
		if (checkListiniDto != null) {
			String msgErr = String.format("Listino %s presente in anagrafica! "
					+ "Impossibile utilizzare il metodo POST", listiniDto.getId());
			log.warn(msgErr);
			throw new DuplicateException(msgErr);
		}

		listiniService.insListini(listiniDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Inserimento Listino " + listiniDto.getId() + " Eseguito Con Successo"), new HttpHeaders(), HttpStatus.CREATED);
	}

	// ------------------- MODIFICA LISTINO ------------------------------------
	@PutMapping(value = "/modifica")
	public ResponseEntity<InfoMsg> updateListino(@Valid @RequestBody ListiniDto listiniDto, BindingResult bindingResult)
			throws BindingException, NotFoundException {
		log.info("Modifichiamo il listino con id {}", listiniDto.getId());

		if (bindingResult.hasErrors()) {
			String msgErr = errMessage.getMessage(bindingResult.getFieldError(), LocaleContextHolder.getLocale());
			log.warn(msgErr);
			throw new BindingException(msgErr);
		}

		ListiniDto checkListiniDto = listiniService.findById(listiniDto.getId());
		if (checkListiniDto == null) {
			String msgErr = String.format("Listino %s non presente in anagrafica! "
					+ "Impossibile utilizzare il metodo PUT", listiniDto.getId());
			log.warn(msgErr);
			throw new NotFoundException(msgErr);
		}

		listiniService.insListini(listiniDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), "Modifica Listino " + listiniDto.getId() + " Eseguita Con Successo"), new HttpHeaders(), HttpStatus.CREATED);
	}

	@GetMapping(value = "/cerca/id/{id}", produces = "application/json")
	public ResponseEntity<ListiniDto> listListiniById(@PathVariable("id") String id)
			throws NotFoundException {
		log.info("****** Otteniamo il listino con id {} *******", id);

		ListiniDto listiniDto = listiniService.findById(id);
		if (listiniDto == null) {
			String errMsg = String.format("Il listino con id %s non è stato trovato!", id);
			log.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(listiniDto, HttpStatus.OK);
	}

	// ------------------- ELIMINAZIONE LISTINO ------------------------------------
	@DeleteMapping(value = "/elimina/{id}", produces = "application/json")
	public ResponseEntity<InfoMsg> deleteListiniById(@PathVariable("id") String id) throws NotFoundException {
		log.info("****** Eliminiamo il listino con id {} *******", id);

		ListiniDto listiniDto = listiniService.findById(id);
		if (listiniDto == null) {
			String errMsg = String.format("Listino %s non presente in anagrafica!", id);
			log.warn(errMsg);
			throw new NotFoundException(errMsg);
		}

		listiniService.delListini(listiniDto);
		return new ResponseEntity<>(new InfoMsg(HttpStatus.OK.toString(), String.format("Eliminazione Listino %s Eseguita Con Successo", id)), HttpStatus.OK);
	}
}
