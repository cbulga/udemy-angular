package com.xantrix.webapp.service;

import java.util.List;

import com.xantrix.webapp.entities.Promo;

public interface PromoService {

	public List<Promo> selTutti();

	public Promo selByIdPromo(String idPromo);

	public Promo selByAnnoCodice(String anno, String codice);

	List<Promo> selPromoActive();

	public void insPromo(Promo promo);

	public void delPromo(Promo promo);
}
