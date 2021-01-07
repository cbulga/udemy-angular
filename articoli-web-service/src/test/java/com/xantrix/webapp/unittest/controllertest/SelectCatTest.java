package com.xantrix.webapp.unittest.controllertest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.xantrix.webapp.Application;
 
@ContextConfiguration(classes = Application.class)
@SpringBootTest
class SelectCatTest {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;

	@BeforeEach
	public void setup() throws JSONException, IOException {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(wac)
				.build();	
	}

	String JsonData =  "[\r\n"
			+ "    {\r\n"
			+ "        \"id\": -1,\r\n"
			+ "        \"descrizione\": \"NON DISPONIBILE\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 1,\r\n"
			+ "        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 10,\r\n"
			+ "        \"descrizione\": \"DROGHERIA CHIMICA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 15,\r\n"
			+ "        \"descrizione\": \"BANCO TAGLIO\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 16,\r\n"
			+ "        \"descrizione\": \"GASTRONOMIA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 17,\r\n"
			+ "        \"descrizione\": \"PASTICCERIA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 20,\r\n"
			+ "        \"descrizione\": \"LIBERO SERVIZIO\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 25,\r\n"
			+ "        \"descrizione\": \"PANE\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 40,\r\n"
			+ "        \"descrizione\": \"SURGELATI\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 50,\r\n"
			+ "        \"descrizione\": \"ORTOFRUTTA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 60,\r\n"
			+ "        \"descrizione\": \"MACELLERIA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 70,\r\n"
			+ "        \"descrizione\": \"PESCHERIA\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"id\": 90,\r\n"
			+ "        \"descrizione\": \"EXTRA ALIMENTARI\"\r\n"
			+ "    }\r\n"
			+ "]";

	@Test
	void listAllItems() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/categoria/cerca/tutti")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
}
