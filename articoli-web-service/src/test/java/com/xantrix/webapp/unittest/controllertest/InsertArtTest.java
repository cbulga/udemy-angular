package com.xantrix.webapp.unittest.controllertest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.Application;

@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class InsertArtTest {
	 
    private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@BeforeEach
	public void setup() throws JSONException, IOException {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}
	
	private String JsonData =  
					"{\n" + 
					"    \"codArt\": \"123Test\",\n" + 
					"    \"descrizione\": \"ARTICOLO TEST\",\n" + 
					"    \"um\": \"PZ\",\n" + 
					"    \"codStat\": \" TEST\",\n" + 
					"    \"pzCart\": 1,\n" + 
					"    \"pesoNetto\": 0,\n" + 
					"    \"idStatoArt\": \"1\",\n" + 
					"    \"dataCreaz\": \"2018-09-26\",\n" + 
					"	 \"famAssort\": {\n" + 
					"        \"id\": 1 \n" + 
					"    }\n" + 
					"}";
	
	@Test
	@Order(1)
	void testInsArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	@Order(2)
	void testErrInsArticolo1() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(jsonPath("$.codice").value(406))
				.andExpect(jsonPath("$.messaggio").value("Articolo 123Test presente in anagrafica! Impossibile utilizzare il metodo POST"))
				.andDo(print());
	}
	
	String ErrJsonData =  
					"{\n" + 
					"    \"codArt\": \"123Test1\",\n" + 
					"    \"descrizione\": \"\",\n" + 
					"    \"um\": \"PZ\",\n" + 
					"    \"codStat\": \" TEST\",\n" + 
					"    \"pzCart\": 1,\n" + 
					"    \"pesoNetto\": 0,\n" + 
					"    \"idStatoArt\": \"1\",\n" + 
					"    \"dataCreaz\": \"2018-09-26\",\n" + 
					"	 \"famAssort\": {\n" + 
					"        \"id\": 1 \n" + 
					"    }\n" + 
					"}";
	
	@Test
	@Order(3)
	void testErrInsArticolo2() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.post("/api/articoli/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(ErrJsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.codice").value(400))
				//.andExpect(jsonPath("$.messaggio").value("Articolo 123Test presente in anagrafica! Impossibile utilizzare il metodo POST"))
				.andDo(print());
	}
	
	@Test
	@Order(4)
	void testUpdArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.put("/api/articoli/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	@Test
	@Order(5)
	void testDelArticolo() throws Exception	{
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/articoli/elimina/123Test")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Articolo 123Test Eseguita Con Successo"))
				.andDo(print());
	}
}
