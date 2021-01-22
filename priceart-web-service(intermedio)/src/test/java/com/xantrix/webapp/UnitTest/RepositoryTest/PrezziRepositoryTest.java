
package com.xantrix.webapp.UnitTest.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.entity.Listini;
import com.xantrix.webapp.repository.ListinoRepository;
import com.xantrix.webapp.repository.PrezziRepository;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-list1.properties")
//@ContextConfiguration(classes = Application.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class PrezziRepositoryTest {

	@Autowired
	private PrezziRepository prezziRepository;

	@Autowired
	private ListinoRepository listinoRepository;

	String IdList = "100";
	String codArt = "002000301";
	Double prezzo = 1.00;

	@Test
	@Order(1)
	void testInsListino() {
		Listini listinoTest = new Listini(IdList, "Listino Test 100", "No");

		Set<DettListini> dettListini = new HashSet<>();
		DettListini dettListTest = new DettListini(codArt, prezzo, listinoTest);
		dettListini.add(dettListTest);

		listinoTest.setDettListini(dettListini);

		listinoRepository.save(listinoTest);

		assertThat(listinoRepository.findById(IdList)).isNotEmpty();
	}

	@Test
	@Order(2)
	void testfindByCodArtAndIdList1() {
		assertThat(prezziRepository.selByCodArtAndList(codArt, IdList))
				.extracting(DettListini::getPrezzo)
				.isEqualTo(prezzo);
	}

	@Test
	@Transactional
	@Order(3)
	void testDeletePrezzo() {
		prezziRepository.delRowDettList(codArt, IdList);
		assertThat(prezziRepository.selByCodArtAndList(codArt, IdList)).isNull();
	}

	@Test
	@Order(4)
	void testDeleteListino() {
		Optional<Listini> listinoTest = listinoRepository.findById(IdList);
		listinoRepository.delete(listinoTest.get());
		assertThat(prezziRepository.selByCodArtAndList(codArt, IdList)).isNull();
	}
}
