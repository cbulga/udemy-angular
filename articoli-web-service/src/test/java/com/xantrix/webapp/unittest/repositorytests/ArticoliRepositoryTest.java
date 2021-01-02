package com.xantrix.webapp.unittest.repositorytests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;

import com.xantrix.webapp.Application;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

@SpringBootTest()
@ContextConfiguration(classes = Application.class)
class ArticoliRepositoryTest {

	@Autowired
	private ArticoliRepository articoliRepository;

	@Test
	void TestfindByDescrizioneLike() {
		List<Articoli> items = articoliRepository.selByDescrizioneLike("ACQUA ULIVETO%");
		assertEquals(2, items.size());
	}

	@Test
	void TestfindByDescrizioneLikePage() {
		List<Articoli> items = articoliRepository.findByDescrizioneLike("ACQUA%",PageRequest.of(0, 10));
		assertEquals(10, items.size());
	}

	@Test
	void TestfindByCodArt() throws Exception {
		assertThat(articoliRepository.findByCodArt("002000301"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
				
	}

	@Test
	void TestfindByEan() throws Exception {
		assertThat(articoliRepository.findByEan("8008490000021"))
				.extracting(Articoli::getDescrizione)
				.isEqualTo("ACQUA ULIVETO 15 LT");
				
	}
}
