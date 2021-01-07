package com.xantrix.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.entity.Iva;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.repository.IvaRepository;

import lombok.extern.java.Log;

@RestController
@RequestMapping("api/iva")
@CrossOrigin(origins = "http://localhost:4200")
@Log
public class IvaController {

	@Autowired
	private IvaRepository ivaRepository;

	@GetMapping(value = "/cerca/tutti", produces = "application/json")
	public ResponseEntity<List<Iva>> listFAssort() throws NotFoundException {
		log.info("****** Otteniamo gli elementi dell'Iva *******");

		List<Iva> iva = ivaRepository.findAll();

		if (iva.isEmpty()) {
			String errMsg = "Nessun elemento dell'Iva presente!";
			log.warning(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(iva, HttpStatus.OK);
	}
}
