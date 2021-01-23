package com.xantrix.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.xantrix.webapp.entities.DettPromo;

public interface PrezziPromoRepository extends JpaRepository<DettPromo, Long> {

	// Query JPQL - Selezione promo per codice articolo
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.isfid = 'No' AND CURRENT_DATE BETWEEN b.inizio AND b.fine")
	DettPromo selByCodArt(@Param("codart") String codArt);

	// Query JPQL - Selezione promo fidelity per codice articolo
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.isfid = 'Si' AND CURRENT_DATE BETWEEN b.inizio AND b.fine")
	DettPromo selByCodArtAndFid(@Param("codart") String codArt);

	// Query JPQL - Selezione promo per codice fidelity e codice
	@Query(value = "SELECT b FROM Promo a JOIN a.dettPromo b WHERE b.codart = :codart AND b.codfid = :codfid AND " + "CURRENT_DATE BETWEEN b.inizio AND b.fine")
	DettPromo selByCodArtAndCodFid(@Param("codart") String codArt, @Param("codfid") String codFid);

	/*
	 * @Query("SELECT DISTINCT b FROM Promo a JOIN a.dettPromo b WHERE CURRENT_DATE BETWEEN b.inizio AND b.fine") List<DettPromo> SelPromoActive();
	 * 
	 * //Query SQL - Modifica oggetto promozione (prezzo)
	 * 
	 * @Modifying
	 * 
	 * @Query(value = "UPDATE dettpromo SET OGGETTO = :oggetto WHERE ID = :id", nativeQuery = true) void UpdOggettoPromo(@Param("oggetto") String Oggetto, @Param("id") Long Id);
	 */
}
