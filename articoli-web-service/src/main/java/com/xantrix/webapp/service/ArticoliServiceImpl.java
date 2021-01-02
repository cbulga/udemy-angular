package com.xantrix.webapp.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.dtos.ArticoliDto;
import com.xantrix.webapp.entity.Articoli;
import com.xantrix.webapp.repository.ArticoliRepository;

@Service
@Transactional(readOnly = true)
public class ArticoliServiceImpl implements ArticoliService {
	@Autowired
	ArticoliRepository articoliRepository;

	@Autowired
	ModelMapper modelMapper;

	Function<Articoli, ArticoliDto> articoliToArticoliDto = articoli -> {
		ArticoliDto dto = null;
		if (articoli != null) {
			dto = modelMapper.map(articoli, ArticoliDto.class);
			dto.setIdStatoArt(dto.getIdStatoArt().trim());
			dto.setUm(dto.getUm().trim());
			dto.setDescrizione(dto.getDescrizione().trim());
		}
		return dto;
	};

	@Override
	public Iterable<Articoli> selTutti() {
		return articoliRepository.findAll();
	}

	@Override
	public List<Articoli> selByDescrizione(String descrizione, Pageable pageable) {
		return articoliRepository.findByDescrizioneLike(descrizione, pageable);
	}

	@Override
	public List<ArticoliDto> selByDescrizione(String descrizione) {
		List<Articoli> articoli = articoliRepository.selByDescrizioneLike(descrizione);
		List<ArticoliDto> articoliDto = null;
		if (articoli != null && !articoli.isEmpty())
			articoliDto = articoli.stream()
					.map(articoliToArticoliDto)
					.collect(Collectors.toList());
		return articoliDto;
	}

	@Override
	public ArticoliDto selByCodArt(String codArt) {
		return articoliToArticoliDto.apply(articoliRepository.findByCodArt(codArt));
	}

	@Override
	@Transactional
	public void delArticolo(Articoli articolo) {
		articoliRepository.delete(articolo);
	}

	@Override
	@Transactional
	public void insArticolo(Articoli articolo) {
		articoliRepository.save(articolo);
	}

	@Override
	public ArticoliDto findByEan(String ean) {
		return articoliToArticoliDto.apply(articoliRepository.findByEan(ean));
	}
}
