package com.xantrix.webapp.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
			dto.setIdStatoArt(StringUtils.trim(dto.getIdStatoArt()));
			dto.setUm(StringUtils.trim(dto.getUm()));
			dto.setDescrizione(StringUtils.trim(dto.getDescrizione()));
		}
		return dto;
	};
	Function<ArticoliDto, Articoli> articoliDtoToArticoli = articoliDto -> {
		final Articoli articoli;
		if (articoliDto != null) {
			articoli = modelMapper.map(articoliDto, Articoli.class);
			articoli.setIdStatoArt(StringUtils.trim(articoli.getIdStatoArt()));
			articoli.setUm(StringUtils.trim(articoli.getUm()));
			articoli.setDescrizione(StringUtils.trim(articoli.getDescrizione()));
			if (articoli.getBarcode() != null && !articoli.getBarcode().isEmpty())
				articoliDto.getBarcode().stream().forEach(barcode -> barcode.setArticolo(articoli));
		} else
			articoli = null;
		return articoli;
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
	public void delArticolo(ArticoliDto articolo) {
		Articoli articoli = articoliDtoToArticoli.apply(articolo);
		articoliRepository.delete(articoli);
	}

	@Override
	@Transactional
	public void insArticolo(ArticoliDto articolo) {
		Articoli articoli = articoliDtoToArticoli.apply(articolo);
		articoliRepository.save(articoli);
	}

	@Override
	public ArticoliDto findByEan(String ean) {
		return articoliToArticoliDto.apply(articoliRepository.findByEan(ean));
	}
}
