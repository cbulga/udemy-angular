package com.xantrix.webapp.service;

public interface PrezzoService {

	Double selPromoByCodArt(String codArt);

	Double selPromoByCodArtAndFid(String codArt);

	Double selByCodArtAndCodFid(String codArt, String codFid);

	void updOggettoPromo(String oggetto, Long id);
}
