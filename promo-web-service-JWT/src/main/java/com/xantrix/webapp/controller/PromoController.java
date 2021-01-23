package com.xantrix.webapp.controller;

 
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.entities.Promo;
import com.xantrix.webapp.exception.PromoNotFoundException;
import com.xantrix.webapp.service.PromoService;

@RestController
@RequestMapping(value = "api/promo")
public class PromoController 
{
	private static final Logger logger = LoggerFactory.getLogger(PromoController.class);
	
	@Autowired
	private PromoService promoService;
	
	@Autowired
	ArticoliClient articoliClient;
		
	@GetMapping(produces = "application/json")
	public ResponseEntity<List<Promo>> listAllPromo() {
		logger.info("****** Otteniamo tutte le promozioni *******");

		List<Promo> promo = promoService.selTutti();

		if (promo.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		for (Promo promozione : promo) {
			promozione.getDettPromo().forEach(f -> f.setDescrizione(articoliClient.getArticolo(f.getCodart()).getDescrizione()));
			promozione.getDettPromo().forEach(f -> f.setPrezzo(articoliClient.getArticolo(f.getCodart()).getPrezzo()));
		}

		logger.info("Numero dei record: {}", promo.size());
		return new ResponseEntity<>(promo, HttpStatus.OK);
	}

	@GetMapping(value = "/id/{idPromo}", produces = "application/json")
	public ResponseEntity<Promo> listPromoById(@PathVariable("idPromo") String idPromo) throws PromoNotFoundException {
		logger.info("****** Otteniamo la promozione con Id: {} *******", idPromo);
		Promo promo = promoService.selByIdPromo(idPromo);
		if (promo == null) {
			throw new PromoNotFoundException("Promozione Assente o Id Errato");
			//return new ResponseEntity<Promo>(HttpStatus.NO_CONTENT);
		}
		promo.getDettPromo().forEach(f -> f.setDescrizione(articoliClient.getArticolo(f.getCodart()).getDescrizione()));
		promo.getDettPromo().forEach(f -> f.setPrezzo(articoliClient.getArticolo(f.getCodart()).getPrezzo()));

		return new ResponseEntity<Promo>(promo, HttpStatus.OK);
	}

	@GetMapping(value = "/codice", produces = "application/json")
	public ResponseEntity<Promo> listPromoByCodice(@RequestParam("anno") String anno,
			@RequestParam("codice") String codice) {
		logger.info("****** Otteniamo la promozione con Codice: {}*******", codice);
		Promo promo = promoService.selByAnnoCodice(anno, codice);
		if (promo == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		promo.getDettPromo().forEach(f -> f.setDescrizione(articoliClient.getArticolo(f.getCodart()).getDescrizione()));
		promo.getDettPromo().forEach(f -> f.setPrezzo(articoliClient.getArticolo(f.getCodart()).getPrezzo()));

		return new ResponseEntity<>(promo, HttpStatus.OK);
	}

	@GetMapping(value = "/active", produces = "application/json")
	public ResponseEntity<List<Promo>> listPromoActive() {
		logger.info("****** Otteniamo la Promozione Attive*******");
		List<Promo> promo = promoService.selPromoActive();
		if (promo == null) {
			 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			for (Promo promozione : promo) {
				promozione.getDettPromo().forEach(f -> f.setDescrizione(articoliClient.getArticolo(f.getCodart()).getDescrizione()));
				promozione.getDettPromo().forEach(f -> f.setPrezzo(articoliClient.getArticolo(f.getCodart()).getPrezzo()));
			}
		}

		return new ResponseEntity<>(promo, HttpStatus.OK);
	}

	@PostMapping(value = "/inserisci")
	public ResponseEntity<Promo> createPromo(@RequestBody Promo promo) {
		if (promo.getIdPromo().length() == 0) {
			UUID uuid = UUID.randomUUID();
		    String GUID = uuid.toString();
		    logger.info("***** Creiamo una Promo con id {} *****", GUID);
		    promo.setIdPromo(GUID);
		} else {
			 logger.warn("Impossibile modificare con il metodo POST ");
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		promoService.insPromo(promo);

		return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
	}

	@PutMapping(value = "/modifica")
	public ResponseEntity<Promo> updatePromo(@RequestBody Promo promo) {
		 logger.info("***** Modifichiamo la Promo con id {} *****", promo.getIdPromo());
		 if (promo.getIdPromo().length() > 0) {
			 promoService.insPromo(promo);
			 return new ResponseEntity<Promo>(new HttpHeaders(), HttpStatus.CREATED);
		 } else {
			 logger.warn("Impossibile modificare una promozione priva di id! ");
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		 }
	}

	@DeleteMapping(value = "/elimina/{idPromo}")
	public ResponseEntity<?> deletePromo(@PathVariable("idPromo") String idPromo) {
		logger.info("Eliminiamo la promo con id {}", idPromo);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();

		Promo promo = promoService.selByIdPromo(idPromo);

		if (promo == null) {
			logger.warn("Impossibile trovare la promo con id {}", idPromo);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		promoService.delPromo(promo);

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Eliminazione Promozione " + idPromo + " Eseguita Con Successo!");

		return new ResponseEntity<>(responseNode, headers, HttpStatus.OK);
	}
}
