package com.xantrix.webapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class SalutiRestContoller {
	@GetMapping(value = "/test")
	public String getGreetings() {
		return "Saluti, sono il tuo primo web services";
	}

	@GetMapping(value = "/saluti/{nome}")
	public String getSaluti2(@PathVariable("nome") String Nome) {
		return String.format("\"Saluti, %s hai usato il tuo primo web service\"", Nome);
	}
}
