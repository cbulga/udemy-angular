package com.xantrix.webapp.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "PriceArtService", url = "localhost:5071") // , configuration = FeignClientConfiguration.class)
public interface PriceClient {

	@GetMapping(value = "/api/prezzi/{codart}")
	public Double getDefaultPriceArt(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt);

	@GetMapping(value = "/api/prezzi/{codart}/{idlist}")
	public Double getPriceArt(@RequestHeader("Authorization") String authHeader, @PathVariable("codart") String codArt, @PathVariable("idlist") String idList);
}