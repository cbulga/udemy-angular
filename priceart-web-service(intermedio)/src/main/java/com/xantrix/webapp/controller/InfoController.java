package com.xantrix.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.appconf.AppConfig;

@RestController
@RequestMapping("")
@CrossOrigin(origins = "http://localhost:4200")
public class InfoController {

	@Autowired
	private AppConfig appConfig;
	
	@GetMapping(value = "info", produces = "application/json")
	public ResponseEntity<?> testConnex() {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode responseNode = mapper.createObjectNode();

		responseNode.put("listino", appConfig.getListino());

		return new ResponseEntity<>(responseNode, HttpStatus.OK);
	}
}
