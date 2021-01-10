package com.xantrix.webapp.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.repository.UtentiRepository;
 
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UtentiControllerTest {
    private MockMvc mockMvc;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UtentiRepository utentiRepository;

	@Autowired
	private WebApplicationContext wac;

	@BeforeEach
	public void setup() throws JSONException, IOException {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}
	
	String jsonData =  
			"{\n" + 
			"    \"userId\": \"Nicola\",\n" + 
			"    \"password\": \"123Stella\",\n" + 
			"    \"attivo\": \"Si\",\n" + 
			"    \"ruoli\": [\n" + 
			"            \"USER\"\n" + 
			"        ]\n" + 
			"}";

	String jsonNotExistentUserData =  
			"{\n" + 
					"    \"userId\": \"NOT_EXISTENT\",\n" + 
					"    \"password\": \"123Stella\",\n" + 
					"    \"attivo\": \"Si\",\n" + 
					"    \"ruoli\": [\n" + 
					"            \"USER\"\n" + 
					"        ]\n" + 
					"}";
	
	@Test
	@Order(1)
	void testInsUtente1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	@Order(2)
	void testListUserByUserId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/utenti/cerca/userid/Nicola")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				  
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.userId").exists())
				.andExpect(jsonPath("$.userId").value("Nicola"))
				.andExpect(jsonPath("$.password").exists())
				.andExpect(jsonPath("$.attivo").exists())
				.andExpect(jsonPath("$.attivo").value("Si"))
				  
				.andExpect(jsonPath("$.ruoli[0]").exists())
				.andExpect(jsonPath("$.ruoli[0]").value("USER")) 
				.andDo(print());
		
				assertThat(passwordEncoder.matches("123Stella", 
						utentiRepository.findByUserId("Nicola").getPassword()))
				.isTrue();
	}
	
	String jsonData2 = 
			"{\n" + 
			"    \"userId\": \"Admin\",\n" + 
			"    \"password\": \"VerySecretPwd\",\n" + 
			"    \"attivo\": \"Si\",\n" + 
			"    \"ruoli\": [\n" + 
			"            \"USER\",\n" + 
			"            \"ADMIN\"\n" + 
			"        ]\n" + 
			"}";

	@Test
	@Order(3)
	void testInsUtente2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	String jsonDataUsers = 
			"[\n" + 
			"	{\n" + 
			"	    \"userId\": \"Nicola\",\n" + 
			"	    \"password\": \"123Stella\",\n" + 
			"	    \"attivo\": \"Si\",\n" + 
			"	    \"ruoli\": [\n" + 
			"		    \"USER\"\n" + 
			"		]\n" + 
			"	},\n" + 
			"	{\n" + 
			"	    \"userId\": \"Admin\",\n" + 
			"	    \"password\": \"VerySecretPwd\",\n" + 
			"	    \"attivo\": \"Si\",\n" + 
			"	    \"ruoli\": [\n" + 
			"		    \"USER\",\n" + 
			"		    \"ADMIN\"\n" + 
			"		]\n" + 
			"	}\n" + 
			"]";
	
	@Test
	@Order(4)
	void testGetAllUser() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/utenti/cerca/tutti")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				 //UTENTE 1
				.andExpect(jsonPath("$[0].id").exists())
				.andExpect(jsonPath("$[0].userId").exists())
				.andExpect(jsonPath("$[0].userId").value("Nicola"))
				.andExpect(jsonPath("$[0].password").exists())
				.andExpect(jsonPath("$[0].attivo").exists())
				.andExpect(jsonPath("$[0].attivo").value("Si"))
				.andExpect(jsonPath("$[0].ruoli[0]").exists())
				.andExpect(jsonPath("$[0].ruoli[0]").value("USER")) 
				 //UTENTE 2
				.andExpect(jsonPath("$[1].id").exists())
				.andExpect(jsonPath("$[1].userId").exists())
				.andExpect(jsonPath("$[1].userId").value("Admin"))
				.andExpect(jsonPath("$[1].password").exists())
				.andExpect(jsonPath("$[1].attivo").exists())
				.andExpect(jsonPath("$[1].attivo").value("Si"))
				.andExpect(jsonPath("$[1].ruoli[0]").exists())
				.andExpect(jsonPath("$[1].ruoli[0]").value("USER")) 
				.andExpect(jsonPath("$[1].ruoli[1]").exists())
				.andExpect(jsonPath("$[1].ruoli[1]").value("ADMIN")) 
				.andReturn();
		
				assertThat(passwordEncoder.matches("VerySecretPwd", 
						utentiRepository.findByUserId("Admin").getPassword()))
				.isEqualTo(true);
	}

	@Test
	@Order(5)
	void insUtente_DuplicatedUtenti_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotAcceptable())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.codice").exists())
				.andExpect(jsonPath("$.codice").value(HttpStatus.NOT_ACCEPTABLE.value()))
				.andExpect(jsonPath("$.messaggio").exists())
				.andExpect(jsonPath("$.messaggio").value("Utente Nicola presente in anagrafica! Impossibile utilizzare il metodo POST"))
				.andDo(print());
	}

	@Test
	@Order(6)
	void updUtente_UserNotFound_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/utenti/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonNotExistentUserData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.codice").exists())
				.andExpect(jsonPath("$.codice").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.messaggio").exists())
				.andExpect(jsonPath("$.messaggio").value("Utente NOT_EXISTENT non presente in anagrafica! Impossibile utilizzare il metodo PUT"))
				.andDo(print());
	}

	@Test
	@Order(7)
	void updUtente_NullUserId_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/utenti/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonNullUserIdData)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

		.andExpect(jsonPath("$.data").exists())
		.andExpect(jsonPath("$.codice").exists())
		.andExpect(jsonPath("$.codice").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.messaggio").exists())
		.andExpect(jsonPath("$.messaggio").value("La UserId NON può essere Null"))
		.andDo(print());
	}

	String jsonData3 = 
			"{\n" + 
			"    \"userId\": \"Cristian\",\n" + 
			"    \"password\": \"amalaPazzaInter\",\n" + 
			"    \"attivo\": \"Si\",\n" + 
			"    \"ruoli\": [\n" + 
			"            \"USER\",\n" + 
			"            \"ADMIN\"\n" + 
			"        ]\n" + 
			"}";

	@Test
	@Order(8)
	void insUtente3() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonData3)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	String jsonUpdateUtentiData3 = 
			"{\n" + 
			"    \"userId\": \"Cristian\",\n" + 
			"    \"password\": \"amalaPazzaInter\",\n" + 
			"    \"attivo\": \"Si\",\n" + 
			"    \"ruoli\": [\n" + 
			"            \"USER\"\n" +  
			"        ]\n" + 
			"}";

	@Order(9)
	void updUtente3() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.put("/api/utenti/modifica")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonUpdateUtentiData3)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	@Order(10)
	void listUserByUserId_Utente3_Found() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/utenti/cerca/userid/Cristian")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.userId").exists())
				.andExpect(jsonPath("$.userId").value("Cristian"))
				.andExpect(jsonPath("$.password").exists())
				.andExpect(jsonPath("$.attivo").exists())
				.andExpect(jsonPath("$.attivo").value("Si"))
				  
				.andExpect(jsonPath("$.ruoli[0]").exists())
				.andExpect(jsonPath("$.ruoli[0]").value("USER")) 
				.andDo(print());
		
				assertThat(passwordEncoder.matches("amalaPazzaInter", 
						utentiRepository.findByUserId("Cristian").getPassword()))
				.isTrue();
	}

	@Test
	@Order(11)
	void testDelUtente1() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/utenti/elimina/Nicola")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Utente Nicola Eseguita Con Successo"))
				.andDo(print());
	}
	
	@Test
	@Order(12)
	void testDelUtente2() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/utenti/elimina/Admin")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Utente Admin Eseguita Con Successo"))
				.andDo(print());
	}

	@Test
	@Order(13)
	void testDelUtente3() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/utenti/elimina/Cristian")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.code").value("200 OK"))
				.andExpect(jsonPath("$.message").value("Eliminazione Utente Cristian Eseguita Con Successo"))
				.andDo(print());
	}

	@Test
	@Order(14)
	void testListUserByUserId_UserNotFound_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/utenti/cerca/userid/NOT_EXISTING_USER")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.codice").exists())
				.andExpect(jsonPath("$.codice").value(HttpStatus.NOT_FOUND.value()))
				.andExpect(jsonPath("$.messaggio").exists())
				.andExpect(jsonPath("$.messaggio").value(String.format("L'utente con userId %s non è stato trovato!", "NOT_EXISTING_USER")))
				.andDo(print());
	}

	String jsonEmptyUserIdData =  
			"{\n" + 
			"    \"userId\": \"\",\n" + 
			"    \"password\": \"123Stella\",\n" + 
			"    \"attivo\": \"Si\",\n" + 
			"    \"ruoli\": [\n" + 
			"            \"USER\"\n" + 
			"        ]\n" + 
			"}";

	@Test
	@Order(15)
	void insUtente_EmptyUserId_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonEmptyUserIdData)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

		.andExpect(jsonPath("$.data").exists())
		.andExpect(jsonPath("$.codice").exists())
		.andExpect(jsonPath("$.codice").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.messaggio").exists())
		.andExpect(jsonPath("$.messaggio").value("La UserId deve essere compresa fra 6 e 80 caratteri"))
		.andDo(print());
	}

	String jsonNullUserIdData =  
			"{\n" + 
					"    \"userId\": null,\n" + 
					"    \"password\": \"123Stella\",\n" + 
					"    \"attivo\": \"Si\",\n" + 
					"    \"ruoli\": [\n" + 
					"            \"USER\"\n" + 
					"        ]\n" + 
					"}";

	@Test
	@Order(16)
	void insUtente_NullUserId_Exception() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/utenti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonNullUserIdData)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

		.andExpect(jsonPath("$.data").exists())
		.andExpect(jsonPath("$.codice").exists())
		.andExpect(jsonPath("$.codice").value(HttpStatus.BAD_REQUEST.value()))
		.andExpect(jsonPath("$.messaggio").exists())
		.andExpect(jsonPath("$.messaggio").value("La UserId NON può essere Null"))
		.andDo(print());
	}
}


