package com.xantrix.webapp.service;

import java.util.List;

import com.xantrix.webapp.model.dtos.UtentiDto;

public interface UtentiService {

	public UtentiDto findByUserId(String userId);

	public void insUtente(UtentiDto utentiDto);

	public List<UtentiDto> getAllUsers();

	public void deleteUtentiByUserId(String userId);
}
