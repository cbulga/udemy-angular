package com.xantrix.webapp.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.model.Utenti;
import com.xantrix.webapp.model.dtos.UtentiDto;
import com.xantrix.webapp.repository.UtentiRepository;

@Service
@Transactional(readOnly = true)
public class UtentiServiceImpl implements UtentiService {
	
	@Autowired
	private UtentiRepository utentiRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	private final Function<Utenti, UtentiDto> utenti2UtentiDto = utenti -> {
		UtentiDto utentiDto = null;
		if (utenti != null)
			utentiDto = modelMapper.map(utenti, UtentiDto.class);
		return utentiDto;
	};
	private final Function<UtentiDto, Utenti> utentiDto2Utenti = utentiDto -> {
		Utenti utenti = null;
		if (utentiDto != null) {
			utenti = modelMapper.map(utentiDto, Utenti.class);
			utenti.setPassword(passwordEncoder.encode(utentiDto.getPassword()));
		}
		return utenti;
	};
	
	@Override
	public UtentiDto findByUserId(String userId) {
		return utenti2UtentiDto.apply(utentiRepository.findByUserId(userId));
	}

	@Override
	@Transactional
	public void insUtente(UtentiDto utentiDto) {
		Utenti utenti = utentiDto2Utenti.apply(utentiDto);
		utentiRepository.save(utenti);
	}

	@Override
	public List<UtentiDto> getAllUsers() {
		List<Utenti> utenti = utentiRepository.findAll();
		List<UtentiDto> utentiDto = null;
		if (utenti != null && !utenti.isEmpty())
			utentiDto = utenti.stream()
					.map(utenti2UtentiDto::apply)
					.collect(Collectors.toList());
		return utentiDto;
	}

	@Override
	@Transactional
	public void deleteUtentiByUserId(String userId) {
		utentiRepository.deleteByUserId(userId);
	}
}
