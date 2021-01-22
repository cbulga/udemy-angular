package com.xantrix.webapp.service;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xantrix.webapp.entity.Listini;
import com.xantrix.webapp.entity.dto.ListiniDto;
import com.xantrix.webapp.repository.ListinoRepository;

@Service
@Transactional(readOnly = true)
public class ListiniServiceImpl implements ListiniService {

	protected static final Function<Listini, ListiniDto> listini2ListiniDto = listini -> {
		ListiniDto listiniDto = null;
		if (listini != null) {
			listiniDto = ListiniDto.builder()
					.id(listini.getId())
					.descrizione(listini.getDescrizione())
					.obsoleto(Optional.ofNullable(listini.getObsoleto())
							.map(com.xantrix.webapp.entity.Listini.Obsoleto::getValue)
							.orElse(null))
					.dettListini(Optional.ofNullable(listini.getDettListini())
							.orElse(null)
							.stream()
							.map(PrezziServiceImpl.dettListini2DettListiniDto::apply)
							.collect(Collectors.toSet()))
					.build();
		}
		return listiniDto;
	};
	protected static final Function<ListiniDto, Listini> listiniDto2Listini = listiniDto -> {
		final Listini listini;
		if (listiniDto != null) {
			listini = Listini.builder()
					.id(listiniDto.getId())
					.descrizione(listiniDto.getDescrizione())
					.obsoleto(Optional.ofNullable(listiniDto.getObsoleto())
							.map(com.xantrix.webapp.entity.Listini.Obsoleto::fromValue)
							.orElse(null))
					.dettListini(Optional.ofNullable(listiniDto.getDettListini())
							.orElse(null)
							.stream()
							.map(PrezziServiceImpl.dettListiniDto2DettListini::apply)
							.collect(Collectors.toSet()))
					.build();
			Optional.ofNullable(listini.getDettListini())
					.orElse(null)
					.stream()
					.forEach(dl -> dl.setListino(listini));
		} else
			listini = null;
		return listini;
	};
	
	@Autowired
	private ListinoRepository listinoRepository;

	@Override
	public ListiniDto findById(String id) {
		return listini2ListiniDto.apply(listinoRepository.findById(id).orElse(null));
	}

	@Override
	@Transactional
	public void delListini(ListiniDto listiniDto) {
		Listini listini = listiniDto2Listini.apply(listiniDto);
		listinoRepository.delete(listini);
	}

	@Override
	@Transactional
	public void insListini(ListiniDto listiniDto) {
		Listini listini = listiniDto2Listini.apply(listiniDto);
		listinoRepository.save(listini);
	}
}
