package com.xantrix.webapp.controller;

 
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xantrix.webapp.service.PrezzoService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping(value = "api/promo/prezzo")
@Slf4j
public class PrezzoController {

	@Autowired
	private PrezzoService promoService;

	// ------------------- SELEZIONE PREZZO PROMO ------------------------------------
	@GetMapping(value = {"/{codart}/{codfid}", "/{codart}"})
	public double getPricePromo(@PathVariable("codart") String codArt, @PathVariable("codfid") Optional<String> optCodFid) {
		double retVal = 0;

		if (optCodFid.isPresent()) {
			log.info("Cerchiamo promo riservata all fidelity {} dell'articolo {}", optCodFid, codArt);
			retVal = promoService.selByCodArtAndCodFid(codArt, optCodFid.get());
		} else {
			log.info("Cerchiamo Prezzo in promo articolo {}", codArt);
			retVal = promoService.selPromoByCodArt(codArt);
		}

		return retVal;
	}

	// ------------------- SELEZIONE PREZZO PROMO FIDELITY ------------------------------------
	@GetMapping(value = {"/fidelity/{codart}"})
	public double getPricePromoFid(@PathVariable("codart") String codArt) {
		double retVal = 0;
		log.info("Cerchiamo promo fidelity articolo {}", codArt);
		retVal = promoService.selPromoByCodArtAndFid(codArt);
		return retVal;
	}
}
