package com.xantrix.webapp.unittest.controllertest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class InsertArtTest {
	 
    private MockMvc mockMvc;
    
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	ArticoliRepository articoliRepository;

	@BeforeEach
	public void setup() throws JSONException, IOException {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}
	
	String jsonData =  
			"{\r\n" + 
			"    \"codArt\": \"123Test\",\r\n" + 
			"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
			"    \"um\": \"PZ\",\r\n" + 
			"    \"codStat\": \"TESTART\",\r\n" + 
			"    \"pzCart\": 6,\r\n" + 
			"    \"pesoNetto\": 1.75,\r\n" + 
			"    \"idStatoArt\": \"1 \",\r\n" + 
			"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
			"    \"barcode\": [\r\n" + 
			"        {\r\n" + 
			"            \"barcode\": \"12345678\",\r\n" + 
			"            \"idTipoArt\": \"CP\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"ingredienti\": null,\r\n" + 
			"    \"iva\": {\r\n" + 
			"        \"idIva\": 22,\r\n" + 
			"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" + 
			"        \"aliquota\": 22\r\n" + 
			"    },\r\n" + 
			"    \"famAssort\": {\r\n" + 
			"        \"id\": 1,\r\n" + 
			"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" + 
			"    }\r\n" + 
			"}";
	
	@Test
	@Order(1)
	void testInsArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Inserimento Articolo 123Test Eseguito Con Successo"))
				.andDo(print());
		assertThat(articoliRepository.findByCodArt("123Test"))
				.extracting(Articoli::getCodArt)
				.isEqualTo("123Test");
	}

	@Test
	@Order(2)
	void testErrInsArticolo1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.codice").value(406))
				.andExpect(jsonPath("$.messaggio").value("Articolo 123Test presente in anagrafica! Impossibile utilizzare il metodo POST"))
				.andDo(print());
	}
	
	String errJsonData =  
					"{\r\n" + 
					"    \"codArt\": \"123Test\",\r\n" + 
					"    \"descrizione\": \"\",\r\n" +  //<<< Articolo privo di descrizione
					"    \"um\": \"PZ\",\r\n" + 
					"    \"codStat\": \"TESTART\",\r\n" + 
					"    \"pzCart\": 6,\r\n" + 
					"    \"pesoNetto\": 1.75,\r\n" + 
					"    \"idStatoArt\": \"1 \",\r\n" + 
					"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
					"    \"barcode\": [\r\n" + 
					"        {\r\n" + 
					"            \"barcode\": \"12345678\",\r\n" + 
					"            \"idTipoArt\": \"CP\"\r\n" + 
					"        }\r\n" + 
					"    ],\r\n" + 
					"    \"ingredienti\": null,\r\n" + 
					"    \"iva\": {\r\n" + 
					"        \"idIva\": 22,\r\n" + 
					"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" + 
					"        \"aliquota\": 22\r\n" + 
					"    },\r\n" + 
					"    \"famAssort\": {\r\n" + 
					"        \"id\": 1,\r\n" + 
					"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" + 
					"    }\r\n" + 
					"}";
	
	@Test
	@Order(3)
	void testErrInsArticolo2() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(errJsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codice").value(400))
				.andExpect(jsonPath("$.messaggio").value("Il campo Descrizione deve avere un numero di caratteri compreso tra 6 e 80"))
				.andDo(print());
	}

	String jsonDataMod =  
			"{\r\n" + 
			"    \"codArt\": \"123Test\",\r\n" + 
			"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
			"    \"um\": \"PZ\",\r\n" + 
			"    \"codStat\": \"TESTART\",\r\n" + 
			"    \"pzCart\": 6,\r\n" + 
			"    \"pesoNetto\": 1.75,\r\n" + 
			"    \"idStatoArt\": \"2 \",\r\n" + //<<< Modifica Stato Articolo a 2
			"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
			"    \"barcode\": [\r\n" + 
			"        {\r\n" + 
			"            \"barcode\": \"12345678\",\r\n" + 
			"            \"idTipoArt\": \"CP\"\r\n" + 
			"        }\r\n" + 
			"    ],\r\n" + 
			"    \"ingredienti\": null,\r\n" + 
			"    \"iva\": {\r\n" + 
			"        \"idIva\": 22,\r\n" + 
			"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" + 
			"        \"aliquota\": 22\r\n" + 
			"    },\r\n" + 
			"    \"famAssort\": {\r\n" + 
			"        \"id\": 1,\r\n" + 
			"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" + 
			"    }\r\n" + 
			"}";
	String jsonNotExistingArticolo =  
			"{\r\n" + 
					"    \"codArt\": \"NOT_EXISTING_ART\",\r\n" + 
					"    \"descrizione\": \"Articoli Unit Test Inserimento\",\r\n" + 
					"    \"um\": \"PZ\",\r\n" + 
					"    \"codStat\": \"TESTART\",\r\n" + 
					"    \"pzCart\": 6,\r\n" + 
					"    \"pesoNetto\": 1.75,\r\n" + 
					"    \"idStatoArt\": \"2 \",\r\n" + //<<< Modifica Stato Articolo a 2
					"    \"dataCreaz\": \"2019-05-14\",\r\n" + 
					"    \"barcode\": [\r\n" + 
					"        {\r\n" + 
					"            \"barcode\": \"12345678\",\r\n" + 
					"            \"idTipoArt\": \"CP\"\r\n" + 
					"        }\r\n" + 
					"    ],\r\n" + 
					"    \"ingredienti\": null,\r\n" + 
					"    \"iva\": {\r\n" + 
					"        \"idIva\": 22,\r\n" + 
					"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" + 
					"        \"aliquota\": 22\r\n" + 
					"    },\r\n" + 
					"    \"famAssort\": {\r\n" + 
					"        \"id\": 1,\r\n" + 
					"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" + 
					"    }\r\n" + 
					"}";

	@Test
	@Order(4)
	void testUpdArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/articoli/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonDataMod)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Modifica Articolo 123Test Eseguita Con Successo"))
				.andDo(print());
		assertThat(articoliRepository.findByCodArt("123Test"))
				.extracting(Articoli::getIdStatoArt)
				.isEqualTo("2 ");
		
	}

	@Test
	@Order(5)
	void testErrUpdArticolo_DescriptionNotCompliant_Exception() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.put("/api/articoli/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(errJsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codice").value(400))
				.andExpect(jsonPath("$.messaggio").value("Il campo Descrizione deve avere un numero di caratteri compreso tra 6 e 80"))
				.andDo(print());
	}

	@Test
	@Order(6)
	void testErrUpdArticolo_NotExistingArticolo_Exception() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.put("/api/articoli/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonNotExistingArticolo)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andDo(print())
				.andExpect(jsonPath("$.codice").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.messaggio").value(String.format("Articolo %s non presente in anagrafica! Impossibile utilizzare il metodo PUT", "NOT_EXISTING_ART")))
				.andDo(print());
	}

	@Test
	@Order(7)
	void testDelArticolo() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/articoli/elimina/123Test")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Articolo 123Test Eseguita Con Successo"))
				.andDo(print());
	}

	@Test
	@Order(8)
	void testDelArticolo_NotExistingArticolo_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/articoli/elimina/NOT_EXISTING_ARTICOLO")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.codice").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.messaggio").value(String.format("Articolo %s non presente in anagrafica!", "NOT_EXISTING_ARTICOLO")))
				.andDo(print());
	}
}
