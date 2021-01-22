package com.xantrix.webapp.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.entity.DettListini;
import com.xantrix.webapp.entity.Listini.Obsoleto;

@Repository
public interface PrezziRepository extends CrudRepository<DettListini, String> {

	@Query(value = "select o from DettListini o where o.codArt = :codArt and o.listino.id = :idList")
	public DettListini selByCodArtAndList(@Param(value = "codArt") String codArt, @Param(value = "idList") String idList);

	@Transactional
	@Modifying
	@Query(value = "delete from DettListini o where o.codArt = :codArt and o.listino.id = :idList")
	public void delRowDettList(@Param(value = "codArt") String codArt, @Param(value = "idList") String idList);

	@Query(value = "select o.prezzo from DettListini o where o.codArt = :codArt and o.listino.id = :idList and o.listino.obsoleto = :obsoleto")
	public Double findPrezzoArticoloByListinoIdAndCodArt(@Param(value = "idList") String idList, @Param(value = "codArt") String codArt, @Param(value = "obsoleto") Obsoleto obsoleto);

	public DettListini findByCodArtAndListinoId(String codArt, String listinoId);
}
