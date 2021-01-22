package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.dto.DettListiniDto;

public interface PrezziService {

	Double findPrezzoArticoloByListinoIdAndCodArt(String listinoId, String codArt);

	DettListiniDto findByCodArtAndListinoId(String codArt, String listinoId);

	void delDettListini(DettListiniDto dettListiniDto);
}
