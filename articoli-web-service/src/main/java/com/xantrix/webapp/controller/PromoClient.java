package com.xantrix.webapp.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PromoWebService", url = "localhost:8091")
public interface PromoClient {

	@GetMapping(value = "api/promo/prezzo/{codart}")
	public Double getPromoArt(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt);

	@GetMapping(value = "api/promo/prezzo/fidelity/{codart}")
	public Double getPromoArtFid(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt);

	@GetMapping(value = "api/promo/prezzo/{codart}/{codfid}")
	public Double getPromoOnlyFid(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt, @PathVariable("codfid") String codFid);
}