package com.xantrix.webapp.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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

	@Autowired
	private PriceClient priceClient;

	@Autowired
	private PromoClient promoClient;

	private Double getPriceArt(String codArt, Optional<String> idList, String authorizationHeader) {
		Double prezzo = idList.isPresent() 
				? priceClient.getPriceArt(authorizationHeader, codArt, idList.get()) 
				: priceClient.getDefaultPriceArt(authorizationHeader, codArt);
		logger.info("Prezzo Articolo {}: {}", codArt, prezzo);
		return prezzo;
	}

	private Double getPriceArt(String codArt, Optional<String> idList) {
		Double prezzo = idList.isPresent() 
				? priceClient.getPriceArt(codArt, idList.get()) 
				: priceClient.getDefaultPriceArt(codArt);
		logger.info("Prezzo Articolo {}: {}", codArt, prezzo);
		return prezzo;
	}

	private Double getPromoArt(String codArt, String header) {
		Double prezzo = promoClient.getPromoArt(header, codArt);
		logger.info("Prezzo Promo Articolo {}: {}", codArt, prezzo);
		return prezzo;
	}

	@GetMapping(value = "test", produces = "application/json")
	public ResponseEntity<?> testConnex() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("code", HttpStatus.OK.toString());
		responseNode.put("message", "Test Connessione Ok");

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}
	
	@GetMapping(value = "/cerca/ean/{barcode}", produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByEan(@PathVariable("barcode") String barcode, HttpServletRequest httpRequest)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con barcode {} *******", barcode);

		String authHeader = httpRequest.getHeader("Authorization");

		ArticoliDto articoliDto = articoliService.findByEan(barcode);
		if (articoliDto == null) {
			String errMsg = String.format("Non è stato trovato alcun articolo avente il barcode %s", barcode);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		} else {
			articoliDto.setPrezzo(this.getPriceArt(articoliDto.getCodArt(), Optional.empty(), authHeader));
			articoliDto.setPromo(this.getPromoArt(articoliDto.getCodArt(), authHeader));
		}

		return new ResponseEntity<>(articoliDto, HttpStatus.OK);
	}

	@GetMapping(value = "/cerca/codice/{codart}", produces = "application/json")
	public ResponseEntity<ArticoliDto> listArtByCodArt(@PathVariable("codart") String codArt, HttpServletRequest httpRequest)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con codice {} *******", codArt);

		String authHeader = httpRequest.getHeader("Authorization");

		ArticoliDto articoliDto = articoliService.selByCodArt(codArt);
		if (articoliDto == null) {
			String errMsg = String.format("L'articolo con codice %s non è stato trovato!", codArt);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		} else {
			articoliDto.setPrezzo(this.getPriceArt(codArt, Optional.empty(), authHeader));
			articoliDto.setPromo(this.getPromoArt(articoliDto.getCodArt(), authHeader));
		}

		return new ResponseEntity<>(articoliDto, HttpStatus.OK);
	}

	@GetMapping(value = "/noauth/cerca/codice/{codart}", produces = "application/json")
	public ResponseEntity<ArticoliDto> noAuthListArtByCodArt(@PathVariable("codart") String codArt)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con codice {} *******", codArt);
		
		ArticoliDto articoliDto = articoliService.selByCodArt(codArt);
		if (articoliDto == null) {
			String errMsg = String.format("L'articolo con codice %s non è stato trovato!", codArt);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		} else {
			articoliDto.setPrezzo(this.getPriceArt(codArt, Optional.empty()));
		}
		
		return new ResponseEntity<>(articoliDto, HttpStatus.OK);
	}

	@GetMapping(value = "/cerca/descrizione/{filter}", produces = "application/json")
	public ResponseEntity<List<ArticoliDto>> listArtByDesc(@PathVariable("filter") String filter, HttpServletRequest httpRequest)
			throws NotFoundException {
		logger.info("****** Otteniamo l'articolo con descrizione {} *******", filter);

		String authHeader = httpRequest.getHeader("Authorization");

		List<ArticoliDto> articoli = articoliService.selByDescrizione("%" + filter.toUpperCase() + "%");
		if (articoli == null) {
			String errMsg = String.format("Non è stato trovato alcun articolo avente descrizione %s", filter);
			logger.warn(errMsg);
			throw new NotFoundException(errMsg);
		} else {
			articoli.stream().forEach(a -> {
				a.setPrezzo(this.getPriceArt(a.getCodArt(), Optional.empty(), authHeader));
				a.setPromo(this.getPromoArt(a.getCodArt(), authHeader));
			});
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
