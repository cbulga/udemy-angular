package com.xantrix.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.entity.FamAssort;
import com.xantrix.webapp.exception.NotFoundException;
import com.xantrix.webapp.repository.FAssortRepository;

import lombok.extern.java.Log;

@RestController
@RequestMapping("api/categoria")
@CrossOrigin(origins="http://localhost:4200")
@Log
public class FAssortController {

	@Autowired
	private FAssortRepository fassortRepository;
	
	@GetMapping(value = "/cerca/tutti", produces = "application/json")
	public ResponseEntity<List<FamAssort>> listFAssort()
			throws NotFoundException {
		log.info("****** Otteniamo gli elementi di classificazione *******");

		List<FamAssort> famAssorts = fassortRepository.findAll();
		if (famAssorts.isEmpty()) {
			String errMsg = "Nessun elemento della Famiglia Assortimento presente!";
			log.warning(errMsg);
			throw new NotFoundException(errMsg);
		}

		return new ResponseEntity<>(famAssorts, HttpStatus.OK);
	}
}
