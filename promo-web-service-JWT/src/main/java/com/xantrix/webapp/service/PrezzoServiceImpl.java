package com.xantrix.webapp.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xantrix.webapp.entities.DettPromo;
import com.xantrix.webapp.repository.PrezziPromoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PrezzoServiceImpl implements PrezzoService {

	@Autowired
	PrezziPromoRepository prezziPromoRep;

	@Override
	public Double selPromoByCodArt(String codArt) {
		double retVal = 0;

		DettPromo promo = prezziPromoRep.selByCodArt(codArt);

		if (promo != null) {
			if (promo.getTipoPromo().getIdTipoPromo() == 1) {
				try {
					retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
				} catch (NumberFormatException ex) {
					log.warn(ex.getMessage());
				}
			} else // TODO Gestire gli altri tipi di promozione
			{
				retVal = 0;
			}
		} else {
			log.warn("Promo Articolo Assente!!");
		}

		return retVal;
	}

	@Override
	public Double selPromoByCodArtAndFid(String CodArt) {
		double retVal = 0;

		DettPromo promo = prezziPromoRep.selByCodArtAndFid(CodArt);

		if (promo != null) {
			if (promo.getTipoPromo().getIdTipoPromo() == 1) {
				try {
					retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
				} catch (NumberFormatException ex) {
					log.warn(ex.getMessage());
				}
			} else // TODO Gestire gli altri tipi di promozione
			{
				retVal = 0;
			}
		} else {
			log.warn("Promo Articolo Fidelity Assente!!");
		}

		return retVal;
	}

	@Override
	public Double selByCodArtAndCodFid(String CodArt, String CodFid) {
		double retVal = 0;

		DettPromo promo = prezziPromoRep.selByCodArtAndCodFid(CodArt, CodFid);

		if (promo != null) {
			if (promo.getTipoPromo().getIdTipoPromo() == 1) {
				try {
					retVal = Double.parseDouble(promo.getOggetto().replace(",", "."));
				} catch (NumberFormatException ex) {
					log.warn(ex.getMessage());
				}
			}
		} else {
			log.warn(String.format("Promo Riservata Fidelity %s Assente!!", CodFid));
		}

		return retVal;
	}

	@Override
	public void updOggettoPromo(String Oggetto, Long Id) {
		// TODO Auto-generated method stub
	}
}
