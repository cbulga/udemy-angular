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
class SelectIvaTest{

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
			+ "        \"idIva\": 0,\r\n"
			+ "        \"descrizione\": \"IVA ESENTE\",\r\n"
			+ "        \"aliquota\": 0\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"idIva\": 4,\r\n"
			+ "        \"descrizione\": \"IVA RIVENDITA 4%\",\r\n"
			+ "        \"aliquota\": 4\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"idIva\": 10,\r\n"
			+ "        \"descrizione\": \"IVA RIVENDITA 10%\",\r\n"
			+ "        \"aliquota\": 10\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"idIva\": 22,\r\n"
			+ "        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n"
			+ "        \"aliquota\": 22\r\n"
			+ "    }\r\n"
			+ "]";

	@Test
	void listAllItems() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/iva/cerca/tutti")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData)) 
				.andReturn();
	}
}
